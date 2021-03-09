package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.cartao.CartaoRequest;
import br.com.zup.proposta.cartao.CartaoResponse;
import br.com.zup.proposta.vencimento.VencimentoResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@Profile(value = "test")
public class ApiCartoesFake {

    @PostMapping("/api/cartoes-fake")
    public ResponseEntity<CartaoResponse> cria(@RequestBody @Valid CartaoRequest request) {
        CartaoResponse cartaoResponse = new CartaoResponse(
                "5312995574366723",
                LocalDateTime.now(),
                request.getNome(),
                null,
                null,
                null,
                null,
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
