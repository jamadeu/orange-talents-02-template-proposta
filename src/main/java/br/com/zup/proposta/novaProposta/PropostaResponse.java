package br.com.zup.proposta.novaProposta;

public class PropostaResponse {

    private final String documento;
    private final String email;
    private final String nome;
    private final Endereco endereco;
    private final String salario;
    private final String status;

    public PropostaResponse(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.email = proposta.getEmail();
        this.nome = proposta.getNome();
        this.endereco = proposta.getEndereco();
        this.salario = proposta.getSalario().toString();
        this.status = String.valueOf(proposta.getStatusProposta());
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
}
