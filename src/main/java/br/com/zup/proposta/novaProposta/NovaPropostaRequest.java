package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.compartilhado.anotacoes.CpfOuCnpj;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class NovaPropostaRequest {

    @NotBlank
    @CpfOuCnpj
    private final String documento;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    private final String nome;
    @NotBlank
    private final EnderecoRequest enderecoRequest;
    @NotNull
    @Positive
    private final BigDecimal salario;

    public NovaPropostaRequest(@NotBlank String documento, @NotBlank @Email String email, @NotBlank String nome, EnderecoRequest enderecoRequest, @NotNull @Positive BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.enderecoRequest = enderecoRequest;
        this.salario = salario;
    }

    public Proposta toModel() {
        Endereco endereco = enderecoRequest.toModel();
        return new Proposta(this.documento, this.email, this.nome, endereco, salario);
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public EnderecoRequest getEnderecoRequest() {
        return enderecoRequest;
    }

    public BigDecimal getSalario() {
        return salario;
    }
}
