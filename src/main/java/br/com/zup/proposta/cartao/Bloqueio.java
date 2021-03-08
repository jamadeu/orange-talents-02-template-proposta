package br.com.zup.proposta.cartao;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Embeddable
public class Bloqueio {

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

    public String getIdBloqueio() {
        return idBloqueio;
    }

    public LocalDateTime getBloqueadoEm() {
        return bloqueadoEm;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}
