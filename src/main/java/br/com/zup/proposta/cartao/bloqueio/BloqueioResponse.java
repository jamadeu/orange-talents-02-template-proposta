package br.com.zup.proposta.cartao.bloqueio;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


public class BloqueioResponse {

    @NotBlank
    @Column(nullable = false)
    private final String idBloqueio;

    @NotNull
    @Column(nullable = false)
    private final LocalDateTime bloqueadoEm;

    @NotBlank
    @Column(nullable = false)
    private final String sistemaResponsavel;

    @NotNull
    @Column(nullable = false)
    private final Boolean ativo;

    public BloqueioResponse(@NotBlank String idBloqueio, @NotNull LocalDateTime bloqueadoEm, @NotBlank String sistemaResponsavel, @NotNull Boolean ativo) {
        this.idBloqueio = idBloqueio;
        this.bloqueadoEm = bloqueadoEm;
        this.sistemaResponsavel = sistemaResponsavel;
        this.ativo = ativo;
    }

    public Bloqueio toModel() {
        return new Bloqueio(
                idBloqueio,
                bloqueadoEm,
                sistemaResponsavel,
                ativo
        );
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
