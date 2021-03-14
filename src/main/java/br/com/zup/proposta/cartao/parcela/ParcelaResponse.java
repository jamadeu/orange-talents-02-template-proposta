package br.com.zup.proposta.cartao.parcela;

import br.com.zup.proposta.cartao.Cartao;

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

    private final Cartao cartao;

    public ParcelaResponse(@NotBlank String idParcela, @NotNull Integer quantidade, @NotNull BigDecimal valor, @NotNull LocalDateTime dataDeCriacao, Cartao cartao) {
        this.idParcela = idParcela;
        this.quantidade = quantidade;
        this.valor = valor;
        this.dataDeCriacao = dataDeCriacao;
        this.cartao = cartao;
    }

    public Parcela toModel() {
        return new Parcela(
                idParcela,
                quantidade,
                valor,
                dataDeCriacao,
                cartao
        );
    }

    public Cartao getCartao() {
        return cartao;
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
