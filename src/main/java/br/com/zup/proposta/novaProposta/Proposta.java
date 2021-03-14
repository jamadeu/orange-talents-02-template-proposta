package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.analise.TipoStatus;
import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.compartilhado.anotacoes.CpfOuCnpj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.encrypt.Encryptors;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @CpfOuCnpj
    @Column(nullable = false, unique = true)
    private String documento;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotNull
    @Embedded
    @Column(nullable = false)
    private Endereco endereco;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    private StatusProposta statusProposta = StatusProposta.PENDENTE;

    @OneToOne(mappedBy = "proposta", cascade = CascadeType.ALL)
    private Cartao cartao;

    private Boolean concluido;

    private LocalDateTime concluidoEm;
    private LocalDateTime updatedAt;

    @Deprecated
    public Proposta() {
    }

    public Proposta(@NotBlank String documento, @NotBlank @Email String email, @NotBlank String nome, @NotNull Endereco endereco, @NotNull @Positive BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Cartao getCartao() {
        return cartao;
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

    public Endereco getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public Boolean getConcluido() {
        return concluido;
    }

    public LocalDateTime getConcluidoEm() {
        return concluidoEm;
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void alteraStatus(StatusProposta status) {
        if (status == null){
            throw new IllegalArgumentException("Status nao pode ser nulo");
        }
        this.statusProposta = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void adicionaCartao(@NotNull Cartao cartao) {
        this.cartao = cartao;
        this.concluiProposta();
    }

    public void concluiProposta() {
        this.concluidoEm = LocalDateTime.now();
        this.concluido = true;
    }

    @Override
    public String toString() {
        return "Proposta{" +
                "id=" + id +
                ", documento='" + documento + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", endereco=" + endereco +
                ", salario=" + salario +
                ", statusProposta=" + statusProposta +
                ", cartao=" + cartao +
                ", concluido=" + concluido +
                ", concluidoEm=" + concluidoEm +
                '}';
    }
}
