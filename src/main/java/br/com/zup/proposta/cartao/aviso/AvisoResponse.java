package br.com.zup.proposta.cartao.aviso;

import br.com.zup.proposta.cartao.Cartao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


public class AvisoResponse {

    @NotNull
    private final LocalDate validoAte;

    @NotBlank
    private final String destino;

    private final Cartao cartao;

    public AvisoResponse(@NotNull LocalDate validoAte, @NotBlank String destino, Cartao cartao) {
        this.validoAte = validoAte;
        this.destino = destino;
        this.cartao = cartao;
    }

    public Aviso toModel() {
        return new Aviso(validoAte, destino, cartao);
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
