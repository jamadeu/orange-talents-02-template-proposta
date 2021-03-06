package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.analise.TipoStatus;
import br.com.zup.proposta.compartilhado.anotacoes.CpfOuCnpj;
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
    @CpfOuCnpj
    @Column(nullable = false, unique = true)
    @JsonProperty
    private String documento;

    @NotBlank
    @Email
    @Column(nullable = false)
    @JsonProperty
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false)
    @JsonProperty
    private String endereco;

    @NotNull
    @Positive
    @Column(nullable = false)
    @JsonProperty
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    @JsonProperty
    private StatusProposta statusProposta;

    @Deprecated
    public Proposta() {
    }

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

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public StatusProposta getStatusProposta() {
        return statusProposta;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void defineStatus(TipoStatus tipoStatus) {
        if (tipoStatus == TipoStatus.SEM_RESTRICAO) {
            this.statusProposta = StatusProposta.ELEGIVEL;
        } else {
            this.statusProposta = StatusProposta.NAO_ELEGIVEL;
        }
    }
}
