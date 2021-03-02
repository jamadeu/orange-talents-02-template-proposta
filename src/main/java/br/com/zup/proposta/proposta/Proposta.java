package br.com.zup.proposta.proposta;

import br.com.zup.proposta.compartilhado.anotacoes.CpfOrCnpj;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @CpfOrCnpj
    @JsonProperty
    String documento;

    @NotBlank
    @Email
    @JsonProperty
    String email;

    @NotBlank
    @JsonProperty
    String nome;

    @NotBlank
    @JsonProperty
    String endereco;

    @NotNull
    @Positive
    @JsonProperty
    BigDecimal salario;

    @Deprecated
    public Proposta() {
    }

    @JsonCreator
    public Proposta(@NotBlank String documento, @NotBlank @Email String email, @NotBlank String nome, @NotBlank String endereco, @NotNull @Positive BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
