package br.com.zup.proposta.cartao;

import br.com.zup.proposta.aviso.AvisoRequest;
import br.com.zup.proposta.bloqueio.BloqueioRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "clienteCartao", url = "${API_CARTOES}")
public interface ClientCartao {

    @PostMapping
    CartaoResponse solicita(CartaoRequest cartaoRequest);

    @PostMapping("/{id}/bloqueios")
    void bloquear(@PathVariable String id, BloqueioRequest request);

    @GetMapping("/{id}")
    CartaoResponse buscaCartaoPorId(@PathVariable String id);

    @PostMapping("/{id}/avisos")
    void criaAviso(@PathVariable String id, AvisoRequest request);
}
