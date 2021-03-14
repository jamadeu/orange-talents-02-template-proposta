package br.com.zup.proposta.novaProposta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/proposta")
class PropostaController {

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);
    private final PropostaRepository propostaRepository;

    @Autowired
    public PropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<PropostaResponse> buscaPorId(@PathVariable Long id) {
        Optional<Proposta> optionalProposta = propostaRepository.findById(id);
        if (optionalProposta.isEmpty()) {
            logger.error("Proposta nao {} encontrada", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Proposta proposta = optionalProposta.get();
        logger.info(proposta.toString());
        return ResponseEntity.ok(new PropostaResponse(proposta));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cria(@RequestBody @Valid NovaPropostaRequest novaPropostaRequest) {
        if (propostaRepository.existsByDocumento(novaPropostaRequest.getDocumento())) {
            logger.error("Ja existe uma proposta para este cliente documento = " + novaPropostaRequest.getDocumento());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Ja existe uma proposta para este cliente");
        }
        Proposta proposta = novaPropostaRequest.toModel();
        propostaRepository.save(proposta);
        logger.info(proposta.toString());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
