package br.com.zup.proposta.proposta;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class PropostaResponse {

    @JsonProperty
    private String documento;
    @JsonProperty
    private String email;
    @JsonProperty
    private String nome;
    @JsonProperty
    private String endereco;
    @JsonProperty
    private String salario;
    @JsonProperty
    private String status;

    public PropostaResponse(@NotNull Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.email = proposta.getEmail();
        this.nome = proposta.getNome();
        this.endereco = proposta.getEndereco();
        this.salario = String.valueOf(proposta.getSalario());
        this.status = String.valueOf(proposta.getStatusProposta());
    }
}
