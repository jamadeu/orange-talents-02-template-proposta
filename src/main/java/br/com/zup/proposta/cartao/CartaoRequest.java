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
    private final Long idProposta;

    public CartaoRequest(@NotBlank String documento, @NotBlank String nome, @NotBlank Long idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdProposta() {
        return idProposta;
    }
}
