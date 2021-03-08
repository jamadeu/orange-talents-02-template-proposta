package br.com.zup.proposta.cartao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


public class BloqueioResponse {

    @NotBlank
    private final String idBloqueio;

    @NotNull
    private final LocalDateTime bloqueadoEm;

    @NotBlank
    private final String sistemaResponsavel;

    @NotNull
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
