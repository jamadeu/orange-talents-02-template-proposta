package br.com.zup.proposta.cartao;

import br.com.zup.proposta.aviso.AvisoRequest;
import br.com.zup.proposta.cartao.bloqueio.BloqueioRequest;
import br.com.zup.proposta.carteira.CarteiraRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "clienteCartao", url = "${api.cartoes}")
public interface ClienteCartao {

    @PostMapping
    CriaCartaoResponse solicita(CartaoRequest cartaoRequest);

    @PostMapping("/{id}/bloqueios")
    void bloquear(@PathVariable String id, BloqueioRequest request);

    @GetMapping("/{id}")
    BuscaCartaoResponse buscaCartaoPorId(@PathVariable String id);

    @PostMapping("/{id}/avisos")
    void criaAviso(@PathVariable String id, AvisoRequest request);

    @PostMapping("/{id}/carteiras")
    void associarCarteira(@PathVariable String id, CarteiraRequest request);
}
