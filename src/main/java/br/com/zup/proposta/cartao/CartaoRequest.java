package br.com.zup.proposta.cartao;

import br.com.zup.proposta.compartilhado.anotacoes.CpfOuCnpj;

import javax.validation.constraints.NotBlank;

public class CartaoRequest {

    @NotBlank
    @CpfOuCnpj
    private final String documento;

    @NotBlank
    private final String nome;

    @NotBlank
    private final String idProposta;

    public CartaoRequest(@NotBlank String documento, @NotBlank String nome, @NotBlank String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }
}
