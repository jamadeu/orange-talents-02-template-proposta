package br.com.zup.proposta.cartao.aviso;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


public class AvisoResponse {

    @NotNull
    private final LocalDate validoAte;

    @NotBlank
    private final String destino;

    public AvisoResponse(@NotNull LocalDate validoAte, @NotBlank String destino) {
        this.validoAte = validoAte;
        this.destino = destino;
    }

    public Aviso toModel() {
        return new Aviso(validoAte, destino);
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

    public String getDestino() {
        return destino;
    }
}
