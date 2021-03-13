package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.analise.AnaliseCliente;
import br.com.zup.proposta.analise.AnaliseRequest;
import br.com.zup.proposta.analise.AnaliseResponse;
import br.com.zup.proposta.analise.TipoStatus;
import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoRequest;
import br.com.zup.proposta.cartao.CartaoResponse;
import br.com.zup.proposta.cartao.ClienteCartao;
import feign.FeignException;
import feign.FeignException.UnprocessableEntity;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proposta")
class PropostaController {

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);
    private final Tracer tracer;
    private final Span span;

    private final List<Proposta> propostasPendenteCartao = new ArrayList<>();

    @PersistenceContext
    private EntityManager em;

    private final AnaliseCliente analiseCliente;
    private final ClienteCartao clienteCartao;
    private final PropostaRepository propostaRepository;

    public PropostaController(PropostaRepository propostaRepository, AnaliseCliente analiseCliente, ClienteCartao clienteCartao, Tracer tracer) {
        this.analiseCliente = analiseCliente;
        this.clienteCartao = clienteCartao;
        this.tracer = tracer;
        this.span = tracer.activeSpan();
        this.propostaRepository = propostaRepository;
        propostasPendenteCartao.addAll(propostaRepository.findByStatusPropostaAndConcluido(StatusProposta.ELEGIVEL, false));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<PropostaResponse> buscaPorId(@PathVariable Long id) {
        Optional<Proposta> optionalProposta = propostaRepository.findById(id);
        if (optionalProposta.isEmpty()) {
            span.log("Cartao não localizado");
            return ResponseEntity.notFound().build();
        }
        Proposta proposta = optionalProposta.get();
        return ResponseEntity.ok(new PropostaResponse(proposta));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cria(@RequestBody @Valid NovaPropostaRequest novaPropostaRequest) {
        Proposta proposta = novaPropostaRequest.toModel();
        logger.info("NovaPropostaRequest para Proposta ok {}", proposta.getDocumento());
        propostaRepository.save(proposta);
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
        logger.info("Proposta id= {} - {}.", proposta.getId(), proposta.getStatusProposta());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void solicitaCartao() {
        while (!propostasPendenteCartao.isEmpty()) {
            Proposta proposta = propostasPendenteCartao.get(0);
            logger.info("Solicitando cartao para a proposta {}", proposta.getId());
            CartaoRequest cartaoRequest = new CartaoRequest(proposta.getDocumento(), proposta.getNome(), proposta.getId());
            try {
                CartaoResponse cartaoResponse = clienteCartao.solicita(cartaoRequest);
                logger.info("Cartao para a proposta {} gerado", proposta.getId());
                logger.info("CartaoResponse {}", cartaoResponse.toString());
                Cartao cartao = cartaoResponse.toModel(em);
                logger.info("Cartao gerado {}", cartao);
                em.persist(cartao);
                proposta.adicionaCartao(cartao);
                em.merge(proposta);
                logger.info("Cartao {} adicionado a proposta {}", cartao.getNumero(), proposta.getId());
                propostasPendenteCartao.remove(proposta);
            } catch (FeignException e) {
                logger.warn("Falha ao gerar cartao para a proposta {}", proposta.getId());
                logger.warn("Status {}", e.status());
                logger.warn("Mensagem {}", e.getMessage());
                logger.info("Aguardando nova iteração");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
