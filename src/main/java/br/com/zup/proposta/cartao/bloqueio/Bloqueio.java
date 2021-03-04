package br.com.zup.proposta.cartao.bloqueio;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String idBloqueio;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime bloqueadoEm;

    @NotBlank
    @Column(nullable = false)
    private String sistemaResponsavel;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo;

    @Deprecated
    public Bloqueio() {
    }

    public Bloqueio(@NotBlank String idBloqueio, @NotNull LocalDateTime bloqueadoEm, @NotBlank String sistemaResponsavel, @NotNull Boolean ativo) {
        this.idBloqueio = idBloqueio;
        this.bloqueadoEm = bloqueadoEm;
        this.sistemaResponsavel = sistemaResponsavel;
        this.ativo = ativo;
    }
}
