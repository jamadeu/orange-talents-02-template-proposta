package br.com.zup.proposta.novaProposta;

import org.springframework.security.crypto.encrypt.Encryptors;

public class PropostaResponse {

    private final String documento;
    private final String email;
    private final String nome;
    private final Endereco endereco;
    private final String salario;
    private final String status;
    private final String cartao;

    public PropostaResponse(Proposta proposta) {
        this.documento = Encryptors.text("123123", "123123").decrypt(proposta.getDocumento());
        this.email = proposta.getEmail();
        this.nome = proposta.getNome();
        this.endereco = proposta.getEndereco();
        this.salario = proposta.getSalario().toString();
        StatusProposta statusProposta = proposta.getStatusProposta();
        this.status = String.valueOf(statusProposta);
        if (statusProposta == StatusProposta.NAO_ELEGIVEL || proposta.getCartao() == null) {
            this.cartao = "Nao possui cartao";
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
