package br.com.zup.proposta.cartao.biometria;

import br.com.zup.proposta.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String biometria;

    private final LocalDateTime associadaEm = LocalDateTime.now();

    @ManyToOne
    @JsonIgnore
    private Cartao cartao;

    @Deprecated
    public Biometria() {
    }

    public Biometria(@NotBlank String biometria, Cartao cartao) {
        this.biometria = biometria;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public String getBiometria() {
        return biometria;
    }

    public LocalDateTime getAssociadaEm() {
        return associadaEm;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
