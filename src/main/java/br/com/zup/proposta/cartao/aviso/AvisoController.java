package br.com.zup.proposta.cartao.aviso;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoRepository;
import br.com.zup.proposta.cartao.ClienteCartao;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/aviso")
public class AvisoController {

    final Logger logger = LoggerFactory.getLogger(AvisoController.class);

    private final CartaoRepository cartaoRepository;
    private final ClienteCartao clienteCartao;

    public AvisoController(ClienteCartao clienteCartao, CartaoRepository cartaoRepository) {
        this.clienteCartao = clienteCartao;
        this.cartaoRepository = cartaoRepository;
    }

    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<?> adicionaAviso(@PathVariable("id") Long idCartao, @RequestBody @Valid AvisoRequest request,
                                           HttpServletRequest requestDetails, @RequestHeader("user-agent") String agent) {
        Cartao cartao = cartaoRepository.findById(idCartao)
                .orElseThrow(() -> {
                    logger.error("Cartao id={} nao localizado", idCartao);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        try {
            logger.info("Gerando aviso de viagem para o cartao id={}", cartao.getId());
            clienteCartao.criaAviso(cartao.getNumero(), request);
            List<Aviso> avisos = clienteCartao.buscaCartaoPorId(cartao.getNumero()).getAvisos();
            Aviso aviso = avisos.get(avisos.size() - 1);
            aviso.adicionaInfosSolicitante(requestDetails.getRemoteAddr(), agent);
            cartao.adicionaAviso(aviso);
            cartaoRepository.save(cartao);
            logger.info("Cartao id = {} atualizado", cartao.getId());
        } catch (FeignException e) {
            logger.error("Erro ao gerar aviso de viagem para o cartao id = {}", cartao.getId());
            logger.error("Status {}", e.status());
            logger.error("Mensagem {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return ResponseEntity.ok().build();
    }
}
