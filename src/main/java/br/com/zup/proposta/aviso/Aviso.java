package br.com.zup.proposta.aviso;

import br.com.zup.proposta.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate validoAte;

    @NotBlank
    @Column(nullable = false)
    private String destino;

    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Aviso() {
    }

    public Aviso(@NotNull LocalDate validoAte, @NotBlank String destino, Cartao cartao) {
        this.validoAte = validoAte;
        this.destino = destino;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

    public String getDestino() {
        return destino;
    }
}
