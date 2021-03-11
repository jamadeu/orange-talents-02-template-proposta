package br.com.zup.proposta.carteira;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CarteiraRequest {

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final TipoCarteira carteira;

    public CarteiraRequest(@NotBlank @Email String email, @NotBlank TipoCarteira carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public TipoCarteira getCarteira() {
        return carteira;
    }
}
