package br.com.zup.proposta.cartao.biometria;

import br.com.zup.proposta.cartao.Cartao;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {

    @NotBlank
    private String biometria;

    @Deprecated
    public BiometriaRequest() {
    }

    public BiometriaRequest(@NotBlank String biometria) {
        this.biometria = biometria;
    }

    public String getBiometria() {
        return biometria;
    }

    public Biometria toModel(Cartao cartao) {
        return new Biometria(biometria, cartao);
    }
}
