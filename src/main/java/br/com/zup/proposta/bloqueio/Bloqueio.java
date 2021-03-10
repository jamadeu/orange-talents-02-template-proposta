package br.com.zup.proposta.bloqueio;

import br.com.zup.proposta.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String idBloqueio;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime bloqueadoEm;

    @NotBlank
    @Column(nullable = false)
    private String sistemaResponsavel;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne
    private Cartao cartao;

    private String ip;

    private String userAgent;

    @Deprecated
    public Bloqueio() {
    }

    public Bloqueio(@NotBlank String idBloqueio, @NotNull LocalDateTime bloqueadoEm, @NotBlank String sistemaResponsavel, @NotNull Boolean ativo, Cartao cartao) {
        this.idBloqueio = idBloqueio;
        this.bloqueadoEm = bloqueadoEm;
        this.sistemaResponsavel = sistemaResponsavel;
        this.ativo = ativo;
        this.cartao = cartao;
    }

    public void adicionaInfosSolicitante(String ip, String userAgent) {
        this.ip = ip;
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public String getIdBloqueio() {
        return idBloqueio;
    }

    public LocalDateTime getBloqueadoEm() {
        return bloqueadoEm;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
