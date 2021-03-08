package br.com.zup.proposta.cartao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParcelaResponse {

    @NotBlank
    private final String idParcela;

    @NotNull
    private final Integer quantidade;

    @NotNull
    private final BigDecimal valor;

    @NotNull
    private final LocalDateTime dataDeCriacao;

    public ParcelaResponse(@NotBlank String idParcela, @NotNull Integer quantidade, @NotNull BigDecimal valor, @NotNull LocalDateTime dataDeCriacao) {
        this.idParcela = idParcela;
        this.quantidade = quantidade;
        this.valor = valor;
        this.dataDeCriacao = dataDeCriacao;
    }

    public Parcela toModel() {
        return new Parcela(
                idParcela,
                quantidade,
                valor,
                dataDeCriacao
        );
    }

    public String getIdParcela() {
        return idParcela;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataDeCriacao() {
        return dataDeCriacao;
    }
}
