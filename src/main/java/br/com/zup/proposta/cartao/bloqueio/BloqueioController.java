package br.com.zup.proposta.cartao.bloqueio;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.ClienteCartao;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
    public ResponseEntity<?> bloqueiaCartao(@PathVariable("id") Long idCartao, @RequestBody @Valid BloqueioRequest request,
                                            HttpServletRequest requestDetails, @RequestHeader("user-agent") String agent) {
        Cartao cartao = em.find(Cartao.class, idCartao);
        if (cartao == null) {
            logger.error("Cartao id={} nao localizado", idCartao);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        try {
            clienteCartao.bloquear(cartao.getNumero(), request);
            List<Bloqueio> bloqueios = clienteCartao.buscaCartaoPorId(cartao.getNumero()).getBloqueios();
            Bloqueio bloqueio = bloqueios.get(bloqueios.size() - 1);
            logger.info("Bloqueio gerado {}", bloqueio);
            bloqueio.adicionaInfosSolicitante(requestDetails.getRemoteAddr(), agent);
            cartao.adicionaBloqueio(bloqueio);
            logger.info("Bloqueio id = {} adicionado ao cartao id = {}", bloqueio.getId(), cartao.getId());
            em.merge(cartao);
        } catch (FeignException e) {
            logger.error("Erro ao gerar bloqueio para o cartao id = {}", cartao.getId());
            logger.error("Status {}", e.status());
            logger.error("Mensagem {}", e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok().build();
    }
}
