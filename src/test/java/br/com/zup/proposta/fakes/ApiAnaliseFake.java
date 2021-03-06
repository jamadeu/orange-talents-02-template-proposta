package br.com.zup.proposta.fakes;

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

    @PostMapping("/api/fake/analise")
    public ResponseEntity<AnaliseResponse> analise(@RequestBody AnaliseRequest request) {
        if (request.getDocumento().startsWith("3")) {
            AnaliseResponse analiseResponse = new AnaliseResponse(
                    request.getIdProposta(),
                    TipoStatus.COM_RESTRICAO
            );
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(analiseResponse);
        }
        AnaliseResponse analiseResponse = new AnaliseResponse(
                request.getIdProposta(),
                TipoStatus.SEM_RESTRICAO
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(analiseResponse);
    }
}
