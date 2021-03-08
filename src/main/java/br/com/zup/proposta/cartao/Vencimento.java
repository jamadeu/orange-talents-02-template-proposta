package br.com.zup.proposta.cartao;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Embeddable
public class Vencimento {

    @NotBlank
    @Column(nullable = false)
    private String idVencimento;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer dia;

    @Deprecated
    public Vencimento() {
    }

    public Vencimento(@NotBlank String idVencimento, @NotNull @Positive Integer dia) {
        this.idVencimento = idVencimento;
        this.dia = dia;
    }

    public String getIdVencimento() {
        return idVencimento;
    }

    public Integer getDia() {
        return dia;
    }
}
