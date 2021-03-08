package br.com.zup.proposta.cartao;

import org.apache.tomcat.jni.Local;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class VencimentoResponse {

    @NotBlank
    private String idVencimento;

    @NotNull
    @Positive
    private Integer dia;

    @NotNull
    private LocalDateTime dataDeCriacao;

    public VencimentoResponse(@NotBlank String idVencimento, @NotNull @Positive Integer dia, @NotNull LocalDateTime dataDeCriacao) {
        this.idVencimento = idVencimento;
        this.dia = dia;
        this.dataDeCriacao = dataDeCriacao;
    }

    public Vencimento toModel() {
        return new Vencimento(
                idVencimento,
                dia,
                dataDeCriacao
        );
    }

    public String getIdVencimento() {
        return idVencimento;
    }

    public Integer getDia() {
        return dia;
    }

    public LocalDateTime getDataDeCriacao() {
        return dataDeCriacao;
    }
}
