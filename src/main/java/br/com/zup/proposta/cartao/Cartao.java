package br.com.zup.proposta.cartao;

import br.com.zup.proposta.aviso.Aviso;
import br.com.zup.proposta.biometria.Biometria;
import br.com.zup.proposta.bloqueio.Bloqueio;
import br.com.zup.proposta.carteira.Carteira;
import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.parcela.Parcela;
import br.com.zup.proposta.renegociacao.Renegociacao;
import br.com.zup.proposta.vencimento.Vencimento;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private List<Bloqueio> bloqueios = new ArrayList<>();

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private List<Aviso> avisos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Carteira> carteiras = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Parcela> parcelas = new ArrayList<>();

    @NotNull
    private BigDecimal limite;

    @OneToOne(cascade = CascadeType.MERGE)
    private Renegociacao renegociacao;

    @OneToOne(cascade = CascadeType.MERGE)
    private Vencimento vencimento;

    @NotNull
    @OneToOne
    @JoinColumn(name = "idProposta", referencedColumnName = "id")
    private Proposta proposta;

    @OneToMany(cascade = CascadeType.MERGE)
    private final List<Biometria> biometrias = new ArrayList<>();

    @Deprecated
    public Cartao() {
    }

    public Cartao(@NotBlank String numero, @NotNull LocalDateTime emitidoEm, @NotBlank String titular, List<Bloqueio> bloqueios, List<Aviso> avisos, List<Carteira> carteiras, List<Parcela> parcelas, @NotNull BigDecimal limite, Renegociacao renegociacao, Vencimento vencimento, @NotNull Proposta proposta) {
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

    public void adicionaBiometria(Biometria biometria) {
        this.biometrias.add(biometria);
    }

    public void adicionaBloqueio(Bloqueio bloqueio) {
        this.bloqueios.add(bloqueio);
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

    public List<Biometria> getBiometrias() {
        return biometrias;
    }

}
