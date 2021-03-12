package br.com.zup.proposta.bloqueio;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.ClienteCartao;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/bloqueio")
public class BloqueioController {

    final Logger logger = LoggerFactory.getLogger(BloqueioController.class);

    @PersistenceContext
    private EntityManager em;

    private final ClienteCartao clienteCartao;

    public BloqueioController(ClienteCartao clienteCartao) {
        this.clienteCartao = clienteCartao;
    }

    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<?> bloqueiaCartao(@PathVariable("id") Long idCartao, @RequestBody BloqueioRequest request,
                                            HttpServletRequest requestDetails, @RequestHeader("user-agent") String agent) {
        logger.info("Busca cartao id={}", idCartao);
        Cartao cartao = em.find(Cartao.class, idCartao);
        if (cartao == null) {
            logger.error("Cartao id={} nao localizado", idCartao);
            return ResponseEntity.notFound().build();
        }

        try {
            logger.info("Solicitando bloqueio do cartao id={}, sistema responsavel {}", idCartao, request.getSistemaResponsavel());
            clienteCartao.bloquear(cartao.getNumero(), request);
            logger.info("Cartao id={} bloqueado", idCartao);
            List<Bloqueio> bloqueios = clienteCartao.buscaCartaoPorId(cartao.getNumero()).getBloqueios();
            Bloqueio bloqueio = bloqueios.get(bloqueios.size() - 1);
            logger.info("Bloqueio gerado {}", bloqueio);
            bloqueio.adicionaInfosSolicitante(requestDetails.getRemoteAddr(), agent);
            logger.info("Adicionado ip = {} e userAgent = {} ao bloqueio {}", requestDetails.getRemoteAddr(), agent, bloqueio);
            em.persist(bloqueio);
            logger.info("Bloqueio id = {} persistido", bloqueio.getId());
            cartao.adicionaBloqueio(bloqueio);
            logger.info("Bloqueio id = {} adicionado ao cartao id = {}", bloqueio.getId(), cartao.getId());
            em.merge(cartao);
            logger.info("Cartao id = {} atualizado no banco", cartao.getId());
        } catch (FeignException e) {
            logger.error("Erro ao gerar bloqueio para o cartao id = {}", cartao.getId());
            logger.error("Status {}", e.status());
            logger.error("Mensagem {}", e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok().build();
    }
}
