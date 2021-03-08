package br.com.zup.proposta.cartao;

import br.com.zup.proposta.novaProposta.Proposta;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String numero;

    @NotNull
    private LocalDateTime emitidoEm;

    @NotBlank
    private String titular;

    @NotNull
    @Embedded
    private List<Bloqueio> bloqueios;

    @NotNull
    @Embedded
    private List<Aviso> avisos;

    @NotNull
    @Embedded
    private List<Carteira> carteiras;

    @NotNull
    @Embedded
    private List<Parcela> parcelas;

    @NotNull
    private BigDecimal limite;

    @NotNull
    @Embedded
    private Renegociacao renegociacao;

    @NotNull
    @Embedded
    private Vencimento vencimento;

    @NotNull
    @OneToOne
    @JoinColumn(name = "idProposta", referencedColumnName = "id")
    private Proposta proposta;

    @Deprecated
    public Cartao() {
    }

    public Cartao(@NotBlank String numero, @NotNull LocalDateTime emitidoEm, @NotBlank String titular, @NotNull List<Bloqueio> bloqueios, @NotNull List<Aviso> avisos, @NotNull List<Carteira> carteiras, @NotNull List<Parcela> parcelas, @NotNull BigDecimal limite, @NotNull Renegociacao renegociacao, @NotNull Vencimento vencimento, @NotNull Proposta proposta) {
        this.numero = numero;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.bloqueios = bloqueios;
        this.avisos = avisos;
        this.carteiras = carteiras;
        this.parcelas = parcelas;
        this.limite = limite;
        this.renegociacao = renegociacao;
        this.vencimento = vencimento;
        this.proposta = proposta;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public String getTitular() {
        return titular;
    }

    public List<Bloqueio> getBloqueios() {
        return bloqueios;
    }

    public List<Aviso> getAvisos() {
        return avisos;
    }

    public List<Carteira> getCarteiras() {
        return carteiras;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public Renegociacao getRenegociacao() {
        return renegociacao;
    }

    public Vencimento getVencimento() {
        return vencimento;
    }

    public Proposta getProposta() {
        return proposta;
    }
}
