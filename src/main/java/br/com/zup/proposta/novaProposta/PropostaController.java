package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.analise.AnaliseCliente;
import br.com.zup.proposta.analise.AnaliseRequest;
import br.com.zup.proposta.analise.AnaliseResponse;
import br.com.zup.proposta.analise.TipoStatus;
import feign.FeignException.UnprocessableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/proposta")
class PropostaController {

    final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    final PropostaRepository propostaRepository;
    final AnaliseCliente analiseCliente;

    public PropostaController(PropostaRepository propostaRepository, AnaliseCliente analiseCliente) {
        this.propostaRepository = propostaRepository;
        this.analiseCliente = analiseCliente;
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
        } catch (UnprocessableEntity e) {
            logger.info("Status code da analise {}", e.status());
            logger.info(e.contentUTF8());
            analiseResponse = new AnaliseResponse(TipoStatus.COM_RESTRICAO);
        }
        logger.info("Resultado da analise, cliente {}.", analiseResponse.getResultadoSolicitacao());
        proposta.alteraStatus(analiseResponse.getResultadoSolicitacao());
        propostaRepository.save(proposta);
        logger.info("Proposta {}.", proposta.getStatusProposta());
        URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
