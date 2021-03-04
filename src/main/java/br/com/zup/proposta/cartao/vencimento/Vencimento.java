package br.com.zup.proposta.cartao.vencimento;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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

    @Deprecated
    public Vencimento() {
    }

    public Vencimento(@NotBlank String idVencimento, @NotNull @Positive Integer dia) {
        this.idVencimento = idVencimento;
        this.dia = dia;
    }
}
