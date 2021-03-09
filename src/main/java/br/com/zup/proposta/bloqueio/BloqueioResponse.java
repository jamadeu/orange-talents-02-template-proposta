package br.com.zup.proposta.bloqueio;

import br.com.zup.proposta.cartao.Cartao;

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

    private final Cartao cartao;

    public BloqueioResponse(@NotBlank String idBloqueio, @NotNull LocalDateTime bloqueadoEm, @NotBlank String sistemaResponsavel, @NotNull Boolean ativo, Cartao cartao) {
        this.idBloqueio = idBloqueio;
        this.bloqueadoEm = bloqueadoEm;
        this.sistemaResponsavel = sistemaResponsavel;
        this.ativo = ativo;
        this.cartao = cartao;
    }

    public Bloqueio toModel() {
        return new Bloqueio(
                idBloqueio,
                bloqueadoEm,
                sistemaResponsavel,
                ativo,
                cartao
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
