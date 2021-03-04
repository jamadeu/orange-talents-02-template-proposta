package br.com.zup.proposta.proposta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class PropostaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropostaRepository propostaRepository;

    @Test
    @DisplayName("retorna status 201, Header Location preenchido com a URL da nova proposta e a nova proposta com cpf persistida no banco em caso de sucesso.")
    void metodoCria_retorna201_EmCasoDeSucessoComCPF() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                "Endereço",
                new BigDecimal(2000)
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(201)
        ).andReturn();

        Proposta proposta = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento()).orElseThrow();
        String locationEsperado = "http://localhost/proposta/" + proposta.getId();
        String location = mvcResult.getResponse().getHeader("Location");

        assertEquals(locationEsperado, location);
        assertEquals(proposta.getEmail(), novaPropostaRequest.getEmail());
    }

    @Test
    @DisplayName("Retorna status 201, Header Location preenchido com a URL da nova proposta e a nova proposta com cnpj persistida no banco em caso de sucesso.")
    void metodoCria_Retorna201_EmCasoDeSucessoComCNPJ() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "28.565.312/0001-61",
                "email@test.com",
                "Nome",
                "Endereço",
                new BigDecimal(2000)
        );
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(201)
        ).andReturn();

        Proposta proposta = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento()).orElseThrow();
        String locationEsperado = "http://localhost/proposta/" + proposta.getId();
        String location = mvcResult.getResponse().getHeader("Location");

        assertEquals(locationEsperado, location);
        assertEquals(proposta.getEmail(), novaPropostaRequest.getEmail());
    }

    @Test
    @DisplayName("Retorna status 400 quando o documento é nulo.")
    void metodoCria_Retorna400_QuandoDocumentoNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                null,
                "email@test.com",
                "Nome",
                "Endereço",
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }


    @DisplayName("Retorna status 400 quando o documento não é válido.")
    @ParameterizedTest
    @ValueSource(strings = {"111.111.111-11", "11.111.111/0001-11"})
    void metodoCria_Retorna400_QuandoDocumentoNaoValido(String documento) throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                documento,
                "email@test.com",
                "Nome",
                "Endereço",
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Retorna status 400 quando o documento vazio.")
    void metodoCria_Retorna400_QuandoDocumentoVazio() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "",
                "email@test.com",
                "Nome",
                "Endereço",
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Retorna status 400 quando o email é nulo.")
    void metodoCria_Retorna400_QuandoEmailNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                null,
                "Nome",
                "Endereço",
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Retorna status 400 quando o email é vazio.")
    void metodoCria_Retorna400_QuandoEmailVazio() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "",
                "Nome",
                "Endereço",
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Retorna status 400 quando o email é invalido.")
    void metodoCria_Retorna400_QuandoEmailInvalido() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email invalido",
                "Nome",
                "Endereço",
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Retorna status 400 quando o nome é vazio.")
    void metodoCria_Retorna400_QuandoNomeVazio() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "",
                "Endereço",
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Retorna status 400 quando o nome é nulo.")
    void metodoCria_Retorna400_QuandoNomeNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                null,
                "Endereço",
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Retorna status 400 quando o endereco é vazio.")
    void metodoCria_Retorna400_QuandoEnderecoVazio() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                "",
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Retorna status 400 quando o endereco é nulo.")
    void metodoCria_Retorna400_QuandoEnderecoNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                null,
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Retorna status 400 quando o salario é nulo.")
    void metodoCria_Retorna400_QuandoSalarioNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                "Endereço",
                null
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }


    @DisplayName("Retorna status 400 quando o salario é zero ou negativo.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void metodoCria_Retorna400_QuandoSalarioZeroOuNegativo(int salario) throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                "Endereço",
                new BigDecimal(salario)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Retorna status 422 quando o solicitante ja possui uma proposta.")
    void metodoCria_Retorna422_QuandoSolicitanteJaPossuiProposta() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                "Endereço",
                new BigDecimal(2000));
        propostaRepository.save(novaPropostaRequest.toModel());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(422)
        ).andExpect(MockMvcResultMatchers
                .content()
                .string("{\"mensagens\":[\"Proposta invalida\"]}")
        );
    }

    @Test
    @DisplayName("Quando a analise retornar 'SEM_RESTRICAO' o status da proposta deve ser 'ELEGIVEL'")
    void metodoCria_StatusPropostaElegivel_QuandoClienteNaoPossuirRestricao() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                "Endereço",
                new BigDecimal(2000));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/proposta")
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(201)
        ).andReturn();

        Proposta proposta = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento()).orElseThrow();

        assertEquals(proposta.getEmail(), novaPropostaRequest.getEmail());
        assertEquals(proposta.getStatusProposta(), StatusProposta.ELEGIVEL);
    }
}
