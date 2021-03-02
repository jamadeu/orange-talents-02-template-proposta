package br.com.zup.proposta.proposta;

import br.com.zup.proposta.compartilhado.anotacoes.CpfOrCnpj;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class NovaPropostaRequest {

    @NotBlank
    @CpfOrCnpj
    @JsonProperty
    final String documento;
    @NotBlank
    @Email
    @JsonProperty
    final String email;
    @NotBlank
    @JsonProperty
    final String nome;
    @NotBlank
    @JsonProperty
    final String endereco;
    @NotNull
    @Positive
    @JsonProperty
    final Integer salario;

    @JsonCreator
    public NovaPropostaRequest(@NotBlank String documento, @NotBlank @Email String email, @NotBlank String nome, @NotBlank String endereco, @NotNull @Positive Integer salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Proposta toModel() {
        return new Proposta(this.documento, this.email, this.nome, this.endereco, new BigDecimal(salario));
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }
}
