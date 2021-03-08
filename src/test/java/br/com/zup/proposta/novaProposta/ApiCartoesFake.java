package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.cartao.CartaoRequest;
import br.com.zup.proposta.cartao.CartaoResponse;
import br.com.zup.proposta.cartao.VencimentoResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@Profile(value = "test")
public class ApiCartoesFake {

    @PostMapping("/api/cartoes-fake")
    public ResponseEntity<CartaoResponse> cria(@RequestBody @Valid CartaoRequest request) {
        CartaoResponse cartaoResponse = new CartaoResponse(
                "5312 9955 7436 6723",
                LocalDateTime.now(),
                request.getNome(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new BigDecimal(2000),
                null,
                new VencimentoResponse(
                        "1",
                        20,
                        LocalDateTime.now()
                ),
                String.valueOf(request.getIdProposta())
        );
        return ResponseEntity.ok(cartaoResponse);
    }
}
