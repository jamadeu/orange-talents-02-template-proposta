package br.com.zup.proposta.analise;

import br.com.zup.proposta.novaProposta.StatusProposta;

public class AnaliseResponse {

    private final String idProposta;
    private final TipoStatus status;

    public AnaliseResponse(String idProposta, TipoStatus status) {
        this.idProposta = idProposta;
        this.status = status;
    }

    public StatusProposta toModel() {
        if (TipoStatus.COM_RESTRICAO.equals(status)) {
            return StatusProposta.NAO_ELEGIVEL;
        }
        return StatusProposta.ELEGIVEL;
    }

    public String getIdProposta() {
        return idProposta;
    }

    public TipoStatus getStatus() {
        return status;
    }
}
