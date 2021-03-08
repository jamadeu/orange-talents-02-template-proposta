package br.com.zup.proposta.novaProposta;

public class PropostaResponse {

    private final String documento;
    private final String email;
    private final String nome;
    private final Endereco endereco;
    private final String salario;
    private final String status;
    private final String cartao;

    public PropostaResponse(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.email = proposta.getEmail();
        this.nome = proposta.getNome();
        this.endereco = proposta.getEndereco();
        this.salario = proposta.getSalario().toString();
        StatusProposta statusProposta = proposta.getStatusProposta();
        this.status = String.valueOf(statusProposta);
        if (statusProposta == StatusProposta.NAO_ELEGIVEL) {
            this.cartao = "Não possui cartao";
        } else {
            this.cartao = proposta.getCartao().getNumero();
        }
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getSalario() {
        return salario;
    }

    public String getStatus() {
        return status;
    }

    public String getCartao() {
        return cartao;
    }
}
