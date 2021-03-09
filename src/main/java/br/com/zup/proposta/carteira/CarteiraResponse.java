package br.com.zup.proposta.carteira;

import br.com.zup.proposta.cartao.Cartao;

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

    private final Cartao cartao;

    public CarteiraResponse(@NotBlank String idCarteira, @NotBlank @Email String email, @NotNull LocalDateTime associadaEm, @NotBlank String emissor, Cartao cartao) {
        this.idCarteira = idCarteira;
        this.email = email;
        this.associadaEm = associadaEm;
        this.emissor = emissor;
        this.cartao = cartao;
    }

    public Carteira toModel() {
        return new Carteira(
                idCarteira,
                email,
                associadaEm,
                emissor,
                cartao
        );
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
