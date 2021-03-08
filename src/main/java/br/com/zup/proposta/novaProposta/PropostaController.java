package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.analise.AnaliseCliente;
import br.com.zup.proposta.analise.AnaliseRequest;
import br.com.zup.proposta.analise.AnaliseResponse;
import br.com.zup.proposta.analise.TipoStatus;
import br.com.zup.proposta.cartao.*;
import feign.FeignException;
import feign.FeignException.UnprocessableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proposta")
class PropostaController {

    final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    private final List<Proposta> propostasPendenteCartao = new ArrayList<>();

    private final PropostaRepository propostaRepository;
    private final CartaoRepository cartaoRepository;
    private final AnaliseCliente analiseCliente;
    private final SolicitaCartao solicitaCartao;

    public PropostaController(PropostaRepository propostaRepository, AnaliseCliente analiseCliente, SolicitaCartao solicitaCartao, CartaoRepository cartaoRepository) {
        this.propostaRepository = propostaRepository;
        this.analiseCliente = analiseCliente;
        this.solicitaCartao = solicitaCartao;
        this.cartaoRepository = cartaoRepository;
        propostasPendenteCartao.addAll(propostaRepository.findByStatusPropostaAndConcluido(StatusProposta.ELEGIVEL, false));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropostaResponse> buscaPorId(@PathVariable Long id) {
        Optional<Proposta> optionalProposta = propostaRepository.findById(id);
        if (optionalProposta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new PropostaResponse(optionalProposta.get()));
    }

    @PostMapping
    public ResponseEntity<?> cria(@RequestBody @Valid NovaPropostaRequest novaPropostaRequest, UriComponentsBuilder uriBuilder) {
        if (propostaRepository.existsByDocumento(novaPropostaRequest.getDocumento())) {
            logger.error("Proposta invalida, ja existe uma proposta para o documento={}", novaPropostaRequest.getDocumento());
            return ResponseEntity.unprocessableEntity().body("Proposta invalida, ja existe uma proposta para este cliente");
        }
        Proposta proposta = novaPropostaRequest.toModel();
        logger.info("NovaPropostaRequest para Proposta ok");
        propostaRepository.save(proposta);
        logger.info("Proposta documento={} criada com sucesso.", proposta.getDocumento());
        AnaliseResponse analiseResponse;
        try {
            analiseResponse = analiseCliente.analise(
                    new AnaliseRequest(proposta.getDocumento(), proposta.getNome(), String.valueOf(proposta.getId())));
            propostasPendenteCartao.add(proposta);
            logger.info("Proposta id = {}, pendente cartao", proposta.getId());
        } catch (UnprocessableEntity e) {
            logger.info("Status code da analise {}", e.status());
            logger.info(e.contentUTF8());
            analiseResponse = new AnaliseResponse(TipoStatus.COM_RESTRICAO);
        }
        logger.info("Resultado da analise, cliente {}.", analiseResponse.getResultadoSolicitacao());
        proposta.alteraStatus(analiseResponse.getResultadoSolicitacao());
        propostaRepository.save(proposta);
        logger.info("Proposta {}.", proposta.getStatusProposta());
        URI uri = uriBuilder.path("/api/proposta/{id}").port(8080).buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Scheduled(fixedDelay = 1000)
    public void solicitaCartao() {
        while (!propostasPendenteCartao.isEmpty()) {
            Proposta proposta = propostasPendenteCartao.get(0);
            logger.info("Solicitando cartao para a proposta {}", proposta.getId());
            CartaoRequest cartaoRequest = new CartaoRequest(proposta.getDocumento(), proposta.getNome(), proposta.getId());
            try {
                CartaoResponse cartaoResponse = solicitaCartao.solicita(cartaoRequest);
                logger.info("Cartao para a proposta {} gerado", proposta.getId());
                Cartao cartao = cartaoResponse.toModel(propostaRepository);
                cartaoRepository.save(cartao);
                proposta.adicionaCartao(cartao);
                propostaRepository.save(proposta);
                logger.info("Cartao {} adicionado a proposta {}", cartao.getNumero(), proposta.getId());
                propostasPendenteCartao.remove(0);
            } catch (FeignException e) {
                logger.warn("Falha ao gerar cartao para a proposta {}", proposta.getId());
                logger.warn("Status {}", e.status());
                logger.warn("Mensagem {}", e.getMessage());
                logger.info("Aguardando nova iteração");
            }
        }
    }
}
