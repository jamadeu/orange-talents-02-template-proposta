package br.com.zup.proposta.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "solicitaCartao", url = "${API_CARTOES}")
public interface SolicitaCartao {

    @PostMapping
    CartaoResponse solicita(CartaoRequest cartaoRequest);
}
