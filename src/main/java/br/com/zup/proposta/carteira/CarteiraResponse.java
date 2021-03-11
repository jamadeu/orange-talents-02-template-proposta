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
    private final TipoCarteira emissor;

    private final Cartao cartao;

    public CarteiraResponse(Carteira carteira) {
        this.idCarteira = carteira.getIdCarteira();
        this.email = carteira.getEmail();
        this.associadaEm = carteira.getAssociadaEm();
        this.emissor = carteira.getEmissor();
        this.cartao = carteira.getCartao();
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

    public TipoCarteira getEmissor() {
        return emissor;
    }
}
