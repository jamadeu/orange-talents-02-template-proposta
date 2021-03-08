package br.com.zup.proposta.cartao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


public class CarteiraResponse {

    @NotBlank
    private final String idCarteira;

    @NotBlank
    @Email
    private final String email;

    @NotNull
    private final LocalDateTime associadaEm;

    @NotBlank
    private final String emissor;


    public CarteiraResponse(@NotBlank String idCarteira, @NotBlank @Email String email, @NotNull LocalDateTime associadaEm, @NotBlank String emissor) {
        this.idCarteira = idCarteira;
        this.email = email;
        this.associadaEm = associadaEm;
        this.emissor = emissor;
    }

    public Carteira toModel() {
        return new Carteira(
                idCarteira,
                email,
                associadaEm,
                emissor
        );
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
