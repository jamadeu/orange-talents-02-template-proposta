package br.com.zup.proposta.fakes;

import br.com.zup.proposta.cartao.CartaoRequest;
import br.com.zup.proposta.cartao.CriaCartaoResponse;
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

    @PostMapping("/api/fake/cartoes")
    public ResponseEntity<CriaCartaoResponse> cria(@RequestBody @Valid CartaoRequest request) {
        CriaCartaoResponse criaCartaoResponse = new CriaCartaoResponse(
                "5312995574366723",
                LocalDateTime.now(),
                new BigDecimal(2000),
                new VencimentoResponse(
                        "1",
                        20,
                        LocalDateTime.now()
                ),
                String.valueOf(request.getIdProposta())
        );
        return ResponseEntity.ok(criaCartaoResponse);
    }
}
