package br.com.zup.proposta.analise;

public class AnaliseResponse {

    private final String documento;
    private final String nome;
    private final TipoStatus resultadoSolicitacao;
    private final String idProposta;

    public AnaliseResponse(String documento, String nome, TipoStatus resultadoSolicitacao, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }

    public TipoStatus getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }
}
