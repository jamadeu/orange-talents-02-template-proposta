package br.com.zup.proposta.aviso;

import br.com.zup.proposta.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate validoAte;

    @NotBlank
    @Column(nullable = false)
    private String destino;

    @ManyToOne
    private Cartao cartao;

    private String ip;

    private String userAgent;

    private final LocalDateTime criadoEm = LocalDateTime.now();

    @Deprecated
    public Aviso() {
    }

    public Aviso(@NotNull LocalDate validoAte, @NotBlank String destino, Cartao cartao) {
        this.validoAte = validoAte;
        this.destino = destino;
        this.cartao = cartao;
    }

    public void adicionaInfosSolicitante(String ip, String userAgent) {
        this.ip = ip;
        this.userAgent = userAgent;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

    public String getDestino() {
        return destino;
    }
}
