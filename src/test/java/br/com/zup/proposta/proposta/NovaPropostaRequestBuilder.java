package br.com.zup.proposta.proposta;

import java.math.BigDecimal;

public class NovaPropostaRequestBuilder {

    public static NovaPropostaRequest criaNovaPropostaRequest() {
        return new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                "Endereço",
                new BigDecimal(2000)
        );
    }

    public static NovaPropostaRequest criaNovaPropostaRequest(String documento, String email, String nome, String endereco, BigDecimal salario) {
        return new NovaPropostaRequest(
                documento,
                email,
                nome,
                endereco,
                salario
        );
    }
}
