package br.com.zup.proposta.proposta;

import br.com.zup.proposta.compartilhado.anotacoes.CpfOrCnpj;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @CpfOrCnpj
    @JsonProperty
    @Column(nullable = false, unique = true)
    private String documento;

    @NotBlank
    @Email
    @JsonProperty
    @Column(nullable = false)
    private String email;

    @NotBlank
    @JsonProperty
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @JsonProperty
    @Column(nullable = false)
    private String endereco;

    @NotNull
    @Positive
    @JsonProperty
    @Column(nullable = false)
    private BigDecimal salario;

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
