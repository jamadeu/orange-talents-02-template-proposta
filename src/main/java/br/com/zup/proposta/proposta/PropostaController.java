package br.com.zup.proposta.proposta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/proposta")
class PropostaController {

    final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    final PropostaRepository propostaRepository;

    public PropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @PostMapping
    public ResponseEntity<Void> cria(@RequestBody @Valid NovaPropostaRequest novaPropostaRequest, UriComponentsBuilder uriBuilder) {
        if (propostaRepository.findByDocumento(novaPropostaRequest.getDocumento()).isPresent()) {
            logger.error("Proposta invalida, ja existe uma proposta para o documento={}", novaPropostaRequest.getDocumento());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Proposta invalida");
        }
        Proposta proposta = novaPropostaRequest.toModel();
        logger.info("NovaPropostaRequest para Proposta ok");
        propostaRepository.save(proposta);
        logger.info("Proposta documento={} criada com sucesso!", proposta.getDocumento());
        URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
