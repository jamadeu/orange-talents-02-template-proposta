package br.com.zup.proposta.cartao.renegociacao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RenegociacaoResponse {

    @NotBlank
    private final String idRenegociacao;

    @NotNull
    private final Integer quantidade;

    @NotNull
    private final BigDecimal valor;

    @NotNull
    private final LocalDateTime dataDeCriacao;

    public RenegociacaoResponse(@NotBlank String idRenegociacao, @NotNull Integer quantidade, @NotNull BigDecimal valor, @NotNull LocalDateTime dataDeCriacao) {
        this.idRenegociacao = idRenegociacao;
        this.quantidade = quantidade;
        this.valor = valor;
        this.dataDeCriacao = dataDeCriacao;
    }

    public Renegociacao toModel() {
        return new Renegociacao(
                idRenegociacao,
                quantidade,
                valor,
                dataDeCriacao
        );
    }

    public String getIdRenegociacao() {
        return idRenegociacao;
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
