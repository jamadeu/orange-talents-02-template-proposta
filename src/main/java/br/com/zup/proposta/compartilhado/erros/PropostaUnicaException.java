package br.com.zup.proposta.compartilhado.erros;

public class PropostaUnicaException extends RuntimeException {

    private final String mensagem;

    public PropostaUnicaException(String mensagem) {
        super(mensagem);
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
