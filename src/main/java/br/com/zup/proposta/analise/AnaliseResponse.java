package br.com.zup.proposta.analise;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnaliseResponse {

    @JsonProperty
    private String documento;
    @JsonProperty
    private String nome;
    @JsonProperty
    private TipoStatus resultadoSolicitacao;
    @JsonProperty
    private String idProposta;

    public AnaliseResponse(String documento, String nome, TipoStatus resultadoSolicitacao, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }

    public AnaliseResponse(TipoStatus resultadoSolicitacao) {
        this.resultadoSolicitacao = resultadoSolicitacao;
    }

    public TipoStatus getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }
}
