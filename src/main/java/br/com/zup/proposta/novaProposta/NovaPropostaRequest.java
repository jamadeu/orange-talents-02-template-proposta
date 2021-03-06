package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.compartilhado.anotacoes.CpfOuCnpj;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class NovaPropostaRequest {

    @NotBlank
    @CpfOuCnpj
    @JsonProperty
    private final String documento;
    @NotBlank
    @Email
    @JsonProperty
    private final String email;
    @NotBlank
    @JsonProperty
    private final String nome;
    @NotBlank
    @JsonProperty
    private final String endereco;
    @NotNull
    @Positive
    @JsonProperty
    private final BigDecimal salario;

    @JsonCreator
    public NovaPropostaRequest(@NotBlank String documento, @NotBlank @Email String email, @NotBlank String nome, @NotBlank String endereco, @NotNull @Positive BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Proposta toModel() {
        return new Proposta(this.documento, this.email, this.nome, this.endereco, salario);
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }
}
