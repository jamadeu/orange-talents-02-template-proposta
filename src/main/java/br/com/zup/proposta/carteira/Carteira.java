package br.com.zup.proposta.carteira;

import br.com.zup.proposta.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String idCarteira;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime associadaEm;

    @NotBlank
    @Column(nullable = false)
    private String emissor;

    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Carteira() {
    }

    public Carteira(@NotBlank String idCarteira, @NotBlank @Email String email, @NotNull LocalDateTime associadaEm, @NotBlank String emissor, Cartao cartao) {
        this.idCarteira = idCarteira;
        this.email = email;
        this.associadaEm = associadaEm;
        this.emissor = emissor;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public String getIdCarteira() {
        return idCarteira;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getAssociadaEm() {
        return associadaEm;
    }

    public String getEmissor() {
        return emissor;
    }
}
