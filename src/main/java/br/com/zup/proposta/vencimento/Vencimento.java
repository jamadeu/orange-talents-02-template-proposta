package br.com.zup.proposta.vencimento;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
public class Vencimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String idVencimento;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer dia;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataDeCriacao = LocalDateTime.now();

    @Deprecated
    public Vencimento() {
    }

    public Vencimento(@NotBlank String idVencimento, @NotNull @Positive Integer dia, @NotNull LocalDateTime dataDeCriacao) {
        this.idVencimento = idVencimento;
        this.dia = dia;
        this.dataDeCriacao = dataDeCriacao;
    }

    public String getIdVencimento() {
        return idVencimento;
    }

    public Integer getDia() {
        return dia;
    }

    public LocalDateTime getDataDeCriacao() {
        return dataDeCriacao;
    }
}
