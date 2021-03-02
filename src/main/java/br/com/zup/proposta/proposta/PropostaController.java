package br.com.zup.proposta.proposta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/proposta")
class PropostaController {

    final PropostaRepository propostaRepository;

    public PropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @PostMapping
    public ResponseEntity<Void> cria(@RequestBody @Valid NovaPropostaRequest novaPropostaRequest, UriComponentsBuilder uriBuilder) {
        Proposta proposta = novaPropostaRequest.toModel();
        propostaRepository.save(proposta);
        URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
