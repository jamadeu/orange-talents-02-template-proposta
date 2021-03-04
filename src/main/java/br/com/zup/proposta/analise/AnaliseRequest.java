package br.com.zup.proposta.analise;

import br.com.zup.proposta.compartilhado.anotacoes.CpfOrCnpj;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class AnaliseRequest {

    @NotBlank
    @CpfOrCnpj
    @JsonProperty
    private String documento;
    @NotBlank
    @JsonProperty
    private String nome;
    @NotBlank
    @JsonProperty
    private String idProposta;

    public AnaliseRequest(@NotBlank String documento, @NotBlank String nome, @NotBlank String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }
}
