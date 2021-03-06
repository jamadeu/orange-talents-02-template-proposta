package br.com.zup.proposta.analise;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "analise", url = "${api.analise}")
public interface AnaliseCliente {

    @PostMapping
    AnaliseResponse analise(AnaliseRequest analiseRequest);
}
