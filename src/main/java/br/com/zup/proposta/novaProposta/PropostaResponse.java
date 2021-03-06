package br.com.zup.proposta.novaProposta;

public class PropostaResponse {


    private String documento;
    private String email;
    private String nome;
    private Endereco endereco;
    private String salario;
    private String status;

    public PropostaResponse(String documento, String email, String nome, Endereco endereco, String salario, String status) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
        this.status = status;
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
