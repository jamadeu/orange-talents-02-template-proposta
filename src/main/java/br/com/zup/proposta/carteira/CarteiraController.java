package br.com.zup.proposta.carteira;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.ClientCartao;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/carteira")
public class CarteiraController {

    final Logger logger = LoggerFactory.getLogger(CarteiraController.class);

    @PersistenceContext
    private EntityManager em;

    private final ClientCartao clientCartao;

    public CarteiraController(ClientCartao clientCartao) {
        this.clientCartao = clientCartao;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscaCarteiraPorId(@PathVariable("id") Long id) {
        Carteira carteira = em.find(Carteira.class, id);
        if (carteira == null) {
            logger.error("Carteira id = {} nao localizada", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CarteiraResponse(carteira));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> adicionaCarteira(@PathVariable("id") Long idCartao, @RequestBody @Valid CarteiraRequest request, UriComponentsBuilder uriBuilder) {
        logger.info("Busca cartao id={}", idCartao);
        Cartao cartao = em.find(Cartao.class, idCartao);
        if (cartao == null) {
            logger.error("Cartao id={} nao localizado", idCartao);
            return ResponseEntity.notFound().build();
        }
        if (cartao.possuiCarteira(request.getCarteira())) {
            logger.error("Cartao id = {} ja possui carteira {}", cartao.getId(), request.getCarteira());
            return ResponseEntity.unprocessableEntity().body("Cartao ja possui esta carteira");
        }
        Carteira carteira;
        try {
            logger.info("Associando carteira {} para o cartao id={}", request.getCarteira(), cartao.getId());
            clientCartao.associarCarteira(cartao.getNumero(), request);
            List<Carteira> carteiras = clientCartao.buscaCartaoPorId(cartao.getNumero()).getCarteiras();
            carteira = carteiras.get(carteiras.size() - 1);
            em.persist(carteira);
            logger.info("Carteira id = {} persistida", carteira.getId());
            cartao.adicionaCarteira(carteira);
            logger.info("Carteira id = {} associada ao cartao id = {}", carteira.getId(), cartao.getId());
            em.merge(cartao);
        } catch (FeignException e) {
            logger.error("Erro ao associar carteira {} para o cartao id = {}", request.getCarteira(), cartao.getId());
            logger.error("Status {}", e.status());
            logger.error("Mensagem {}", e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
        URI uri = uriBuilder.path("/api/carteira/{id}").buildAndExpand(carteira.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
