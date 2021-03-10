package br.com.zup.proposta.aviso;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.ClientCartao;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/avisos")
public class AvisoController {

    final Logger logger = LoggerFactory.getLogger(AvisoController.class);

    @PersistenceContext
    private EntityManager em;

    private final ClientCartao clientCartao;

    public AvisoController(ClientCartao clientCartao) {
        this.clientCartao = clientCartao;
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> adicionaAviso(@PathVariable("id") Long idCartao, @RequestBody AvisoRequest request,
                                           HttpServletRequest requestDetails, @RequestHeader("user-agent") String agent) {
        logger.info("Busca cartao id={}", idCartao);
        Cartao cartao = em.find(Cartao.class, idCartao);
        if (cartao == null) {
            logger.error("Cartao id={} nao localizado", idCartao);
            return ResponseEntity.notFound().build();
        }

        try {
            logger.info("Gerando aviso de viagem para o cartao id={}", cartao.getId());
            clientCartao.criaAviso(cartao.getNumero(), request);
            List<Aviso> avisos = clientCartao.buscaCartaoPorId(cartao.getNumero()).getAvisos();
            Aviso aviso = avisos.get(avisos.size() - 1);
            logger.info("Aviso criado {}", aviso);
            aviso.adicionaInfosSolicitante(requestDetails.getRemoteAddr(), agent);
            logger.info("Adicionado ip = {} e userAgent = {} ao aviso {}", requestDetails.getRemoteAddr(), agent, aviso);
            em.persist(aviso);
            logger.info("Aviso id = {} persistido", aviso.getId());
            cartao.adicionaAviso(aviso);
            logger.info("Aviso id = {} adicionado ao cartao id = {}", aviso.getId(), cartao.getId());
            em.merge(cartao);
            logger.info("Cartao id = {} atualizado no banco", cartao.getId());
        } catch (FeignException e) {
            logger.error("Erro ao gerar aviso de viagem para o cartao id = {}", cartao.getId());
            logger.error("Status {}", e.status());
            logger.error("Mensagem {}", e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok().build();
    }
}
