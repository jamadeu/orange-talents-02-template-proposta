package br.com.zup.proposta.cartao;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Embeddable
public class Carteira {

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

    @Deprecated
    public Carteira() {
    }

    public Carteira(@NotBlank String idCarteira, @NotBlank @Email String email, @NotNull LocalDateTime associadaEm, @NotBlank String emissor) {
        this.idCarteira = idCarteira;
        this.email = email;
        this.associadaEm = associadaEm;
        this.emissor = emissor;
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
