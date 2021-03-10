package br.com.zup.proposta.carteira;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CarteiraRequest {

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String carteira;

    public CarteiraRequest(@NotBlank @Email String email, @NotBlank String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }
}
