package br.com.zup.proposta.novaProposta;

import javax.validation.constraints.NotBlank;

public class EnderecoRequest {

    @NotBlank
    private final String logradouro;
    @NotBlank
    private final String numero;
    @NotBlank
    private final String bairro;
    @NotBlank
    private final String cidade;
    @NotBlank
    private final String estado;
    @NotBlank
    private final String cep;

    public EnderecoRequest(@NotBlank String logradouro, @NotBlank String numero, @NotBlank String bairro, @NotBlank String cidade, @NotBlank String estado, @NotBlank String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public Endereco toModel() {
        return new Endereco(logradouro, numero, bairro, cidade, estado, cep);
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }
}
