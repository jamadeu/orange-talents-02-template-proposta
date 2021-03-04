package br.com.zup.proposta.analise;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "analise", url = "http://localhost:9999/api/solicitacao")
public interface AnaliseCliente {

    @PostMapping
    AnaliseResponse analise(AnaliseRequest analiseRequest);
}
