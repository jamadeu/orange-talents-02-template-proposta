package br.com.zup.proposta.cartao;

import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.cartao.vencimento.VencimentoResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CriaCartaoResponse {

    private final String id;
    private final LocalDateTime emitidoEm;
    private final BigDecimal limite;
    private final VencimentoResponse vencimentoResponse;
    private final String idProposta;

    public CriaCartaoResponse(String id, LocalDateTime emitidoEm, BigDecimal limite, VencimentoResponse vencimentoResponse, String idProposta) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.limite = limite;
        this.vencimentoResponse = vencimentoResponse;
        this.idProposta = idProposta;
    }

    public Cartao toModel(Proposta proposta) {
        if (!String.valueOf(proposta.getId()).equals(idProposta)) {
            throw new IllegalArgumentException("Proposta invalida");
        }
        return new Cartao(
                this.id,
                emitidoEm,
                proposta.getNome(),
                limite,
                vencimentoResponse.toModel(),
                proposta
        );
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public VencimentoResponse getVencimentoResponse() {
        return vencimentoResponse;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
