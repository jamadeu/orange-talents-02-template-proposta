package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.analise.AnaliseRequest;
import br.com.zup.proposta.analise.AnaliseResponse;
import br.com.zup.proposta.analise.TipoStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile(value = "test")
public class ApiAnaliseFake {

    @PostMapping("/analise-fake")
    public ResponseEntity<AnaliseResponse> analise(@RequestBody AnaliseRequest request) {
        if (request.getDocumento().startsWith("3")) {
            AnaliseResponse analiseResponse = new AnaliseResponse(
                    request.getDocumento(),
                    request.getNome(),
                    TipoStatus.COM_RESTRICAO,
                    request.getIdProposta()
            );
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(analiseResponse);
        }
        AnaliseResponse analiseResponse = new AnaliseResponse(
                request.getDocumento(),
                request.getNome(),
                TipoStatus.SEM_RESTRICAO,
                request.getIdProposta()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(analiseResponse);
    }
}
