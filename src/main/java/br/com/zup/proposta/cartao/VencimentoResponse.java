package br.com.zup.proposta.cartao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class VencimentoResponse {

    @NotBlank
    private String idVencimento;

    @NotNull
    @Positive
    private Integer dia;

    public VencimentoResponse(@NotBlank String idVencimento, @NotNull @Positive Integer dia) {
        this.idVencimento = idVencimento;
        this.dia = dia;
    }

    public Vencimento toModel() {
        return new Vencimento(
                idVencimento,
                dia
        );
    }

    public String getIdVencimento() {
        return idVencimento;
    }

    public Integer getDia() {
        return dia;
    }
}
