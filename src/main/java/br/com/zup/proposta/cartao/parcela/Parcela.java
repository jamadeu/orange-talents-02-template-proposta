package br.com.zup.proposta.cartao.parcela;

import br.com.zup.proposta.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String idParcela;

    @NotNull
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(nullable = false)
    private BigDecimal valor;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataDeCriacao;

    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Parcela() {
    }

    public Parcela(@NotBlank String idParcela, @NotNull Integer quantidade, @NotNull BigDecimal valor, @NotNull LocalDateTime dataDeCriacao, Cartao cartao) {
        this.idParcela = idParcela;
        this.quantidade = quantidade;
        this.valor = valor;
        this.dataDeCriacao = dataDeCriacao;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
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
