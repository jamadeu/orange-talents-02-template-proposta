package br.com.zup.proposta.renegociacao;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Renegociacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String idRenegociacao;

    @NotNull
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(nullable = false)
    private BigDecimal valor;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataDeCriacao;

    @Deprecated
    public Renegociacao() {
    }

    public Renegociacao(@NotBlank String idRenegociacao, @NotNull Integer quantidade, @NotNull BigDecimal valor, @NotNull LocalDateTime dataDeCriacao) {
        this.idRenegociacao = idRenegociacao;
        this.quantidade = quantidade;
        this.valor = valor;
        this.dataDeCriacao = dataDeCriacao;
    }

    public Long getId() {
        return id;
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
