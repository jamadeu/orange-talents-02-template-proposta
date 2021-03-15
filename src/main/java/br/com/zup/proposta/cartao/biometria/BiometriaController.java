package br.com.zup.proposta.cartao.biometria;

import br.com.zup.proposta.cartao.Cartao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/biometria")
public class BiometriaController {

    final Logger logger = LoggerFactory.getLogger(BiometriaController.class);

    @PersistenceContext
    private EntityManager em;

    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<?> adicionaBiometria(@PathVariable("id") Long cartaoId, @RequestBody @Valid BiometriaRequest request, UriComponentsBuilder uriBuilder) {
        Cartao cartao = em.find(Cartao.class, cartaoId);
        if (cartao == null) {
            logger.error("Cartao id={} nao localizado", cartaoId);
            return ResponseEntity.notFound().build();
        }
        Biometria biometria = request.toModel(cartao);
        em.persist(biometria);
        logger.info("Biometria persistida, {}", biometria);
        cartao.adicionaBiometria(biometria);
        em.merge(cartao);
        logger.info("Biometria id={} adicionada ao cartao Cartao id={}", biometria.getId(), cartaoId);
        URI uri = uriBuilder.path("/api/biometria/{id}").buildAndExpand(biometria.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
