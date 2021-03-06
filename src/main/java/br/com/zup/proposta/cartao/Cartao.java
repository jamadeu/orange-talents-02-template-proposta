package br.com.zup.proposta.cartao;

import br.com.zup.proposta.cartao.aviso.Aviso;
import br.com.zup.proposta.cartao.biometria.Biometria;
import br.com.zup.proposta.cartao.bloqueio.Bloqueio;
import br.com.zup.proposta.carteira.Carteira;
import br.com.zup.proposta.carteira.TipoCarteira;
import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.cartao.parcela.Parcela;
import br.com.zup.proposta.cartao.renegociacao.Renegociacao;
import br.com.zup.proposta.cartao.vencimento.Vencimento;

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
    private final List<Bloqueio> bloqueios = new ArrayList<>();

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private final List<Aviso> avisos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE)
    private final List<Carteira> carteiras = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE)
    private final List<Parcela> parcelas = new ArrayList<>();

    @NotNull
    private BigDecimal limite;

    @OneToOne(cascade = CascadeType.MERGE)
    private Renegociacao renegociacao;

    @OneToOne(cascade = CascadeType.MERGE)
    private Vencimento vencimento;

    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "idProposta", referencedColumnName = "id")
    private Proposta proposta;

    @OneToMany(cascade = CascadeType.MERGE)
    private final List<Biometria> biometrias = new ArrayList<>();

    @Enumerated
    private StatusCartao status = StatusCartao.ATIVO;

    @Deprecated
    public Cartao() {
    }

    public Cartao(@NotBlank String numero, @NotNull LocalDateTime emitidoEm, @NotBlank String titular, @NotNull BigDecimal limite, Vencimento vencimento, @NotNull Proposta proposta) {
        this.numero = numero;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
        this.vencimento = vencimento;
        this.proposta = proposta;
    }

    public void adicionaBiometria(Biometria biometria) {
        this.biometrias.add(biometria);
    }

    public void adicionaBloqueio(Bloqueio bloqueio) {
        this.bloqueios.add(bloqueio);
        this.alteraStatus(StatusCartao.BLOQUEADO);
    }

    public void adicionaAviso(Aviso aviso) {
        this.avisos.add(aviso);
    }

    public void adicionaCarteira(Carteira carteira) {
        this.carteiras.add(carteira);
    }

    public boolean possuiCarteira(TipoCarteira emissor) {
        for (Carteira c : carteiras) {
            if (c.getEmissor().equals(emissor)) {
                return true;
            }
        }
        return false;
    }

    public void alteraStatus(StatusCartao status) {
        this.status = status;
    }

    public StatusCartao getStatus() {
        return status;
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

    @Override
    public String toString() {
        return "Cartao{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", emitidoEm=" + emitidoEm +
                ", titular='" + titular + '\'' +
                ", bloqueios=" + bloqueios +
                ", avisos=" + avisos +
                ", carteiras=" + carteiras +
                ", parcelas=" + parcelas +
                ", limite=" + limite +
                ", renegociacao=" + renegociacao +
                ", vencimento=" + vencimento +
                ", proposta=" + proposta +
                ", biometrias=" + biometrias +
                ", status=" + status +
                '}';
    }
}
