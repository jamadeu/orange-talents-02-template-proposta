package br.com.zup.proposta.jobs;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoRepository;
import br.com.zup.proposta.novaProposta.Endereco;
import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.novaProposta.PropostaRepository;
import br.com.zup.proposta.novaProposta.StatusProposta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class SolicitaCartaoTest {

    @Autowired
    private SolicitaCartao solicitaCartao;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @WithMockUser
    @Test
    @DisplayName("adiciona cartao a proposta'")
    void adicionaCartao() {
        Proposta novaProposta = new Proposta(
                "971.706.120-38",
                "email@teste.com",
                "Nome",
                new Endereco(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"
                ),
                new BigDecimal(2000)
        );
        novaProposta.alteraStatus(StatusProposta.ELEGIVEL);
        propostaRepository.save(novaProposta);
        solicitaCartao.solicitaCartao();
        Proposta proposta = propostaRepository.findById(novaProposta.getId()).orElseThrow();
        Cartao cartao = cartaoRepository.findById(proposta.getCartao().getId()).orElseThrow();

        assertEquals(cartao, proposta.getCartao());
        assertEquals(proposta.getEmail(), novaProposta.getEmail());
        assertEquals(StatusProposta.CONCLUIDA, proposta.getStatusProposta());
    }

}