package br.com.zup.proposta.jobs;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class SubmeteAnaliseTest {

    @Autowired
    private SubmeteAnalise submeteAnalise;

    @Autowired
    private PropostaRepository propostaRepository;

    @WithMockUser
    @Test
    @DisplayName("Quando a analise retornar 'SEM_RESTRICAO' o status da proposta deve ser 'ELEGIVEL'")
    void metodoCria_StatusPropostaElegivel_QuandoClienteNaoPossuirRestricao() throws Exception {
        Proposta novaProposta = propostaRepository.save(new Proposta(
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
        ));
        submeteAnalise.submetePropostasParaAnalise();
        Proposta proposta = propostaRepository.findById(novaProposta.getId()).orElseThrow();

        assertEquals(proposta.getEmail(), novaProposta.getEmail());
        assertEquals(StatusProposta.ELEGIVEL, proposta.getStatusProposta());
    }

    @WithMockUser
    @Test
    @DisplayName("Quando a analise retornar 'COM_RESTRICAO' o status da proposta deve ser 'NAO_ELEGIVEL'")
    void metodoCria_StatusPropostaNaoElegivel_QuandoClientePossuirRestricao() throws Exception {
        Proposta novaProposta = propostaRepository.save(new Proposta(
                "366.112.150-26",
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
        ));
        submeteAnalise.submetePropostasParaAnalise();
        Proposta proposta = propostaRepository.findById(novaProposta.getId()).orElseThrow();

        assertEquals(proposta.getEmail(), novaProposta.getEmail());
        assertEquals(StatusProposta.NAO_ELEGIVEL, proposta.getStatusProposta());
    }

}