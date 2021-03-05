package br.com.zup.proposta.analise;

import br.com.zup.proposta.compartilhado.anotacoes.CpfOuCnpj;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class AnaliseRequest {

    @NotBlank
    @CpfOuCnpj
    @JsonProperty
    private final String documento;
    @NotBlank
    @JsonProperty
    private final String nome;
    @NotBlank
    @JsonProperty
    private final String idProposta;

    public AnaliseRequest(@NotBlank String documento, @NotBlank String nome, @NotBlank String idProposta) {
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

    public String getIdProposta() {
        return idProposta;
    }
}
