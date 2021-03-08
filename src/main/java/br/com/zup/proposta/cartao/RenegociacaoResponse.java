package br.com.zup.proposta.cartao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RenegociacaoResponse {


    private final String idRenegociacao;

    private final Integer quantidade;

    private final BigDecimal valor;

    private final LocalDateTime dataDeCriacao;

    public RenegociacaoResponse(String idRenegociacao, Integer quantidade, BigDecimal valor, LocalDateTime dataDeCriacao) {
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
