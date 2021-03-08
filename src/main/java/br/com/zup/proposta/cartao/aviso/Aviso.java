package br.com.zup.proposta.cartao.aviso;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Embeddable
public class Aviso {

    @NotNull
    @Column(nullable = false)
    private LocalDate validoAte;

    @NotBlank
    @Column(nullable = false)
    private String destino;

    @Deprecated
    public Aviso() {
    }

    public Aviso(@NotNull LocalDate validoAte, @NotBlank String destino) {
        this.validoAte = validoAte;
        this.destino = destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

    public String getDestino() {
        return destino;
    }
}
