package br.com.zup.proposta.novaProposta;

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
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"API_ANALISE=http://localhost:8080/analise-fake"})
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class PropostaControllerTest {

    private final String URL_API_PROPOSTA = "/api/proposta";

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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(201)
        ).andReturn();

        Proposta proposta = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento()).orElseThrow();
        String locationEsperado = "http://localhost:8080" + URL_API_PROPOSTA + "/" + proposta.getId();
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(201)
        ).andReturn();

        Proposta proposta = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento()).orElseThrow();
        String locationEsperado = "http://localhost:8080" + URL_API_PROPOSTA + "/" + proposta.getId();
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
                .post(URL_API_PROPOSTA)
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                null
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }


    @DisplayName("Retorna status 400 quando o salario é negativo.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -10})
    void metodoCria_Retorna400_QuandoSalarioZeroOuNegativo(int salario) throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(salario)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );
        propostaRepository.save(novaPropostaRequest.toModel());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(422)
        ).andReturn();

        String errorMessage = mvcResult.getResponse().getContentAsString();
        assertEquals(errorMessage, "Proposta invalida, ja existe uma proposta para este cliente");
    }

    @Test
    @DisplayName("Quando a analise retornar 'SEM_RESTRICAO' o status da proposta deve ser 'ELEGIVEL'")
    void metodoCria_StatusPropostaElegivel_QuandoClienteNaoPossuirRestricao() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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

    @Test
    @DisplayName("Quando a analise retornar 'COM_RESTRICAO' o status da proposta deve ser 'NAO_ELEGIVEL'")
    void metodoCria_StatusPropostaNaoElegivel_QuandoClientePossuirRestricao() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "366.112.150-26",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(201)
        ).andReturn();

        Proposta proposta = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento()).orElseThrow();

        assertEquals(proposta.getEmail(), novaPropostaRequest.getEmail());
        assertEquals(proposta.getStatusProposta(), StatusProposta.NAO_ELEGIVEL);
    }

    @Test
    @DisplayName("Retorna status 200 e um Json com os dados da proposta")
    void metodoBuscaPorId_Retorna200() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );
        Proposta proposta = propostaRepository.save(novaPropostaRequest.toModel());

        String expectedBody = "{\"documento\":\"041.112.040-90\"," +
                "\"email\":\"email@test.com\"," +
                "\"nome\":\"Nome\"," +
                "\"endereco\":{\"logradouro\":\"Rua\"," +
                "\"numero\":\"100\"," +
                "\"bairro\":\"Bairro\"," +
                "\"cidade\":\"Cidade\"," +
                "\"estado\":\"Estado\"," +
                "\"cep\":\"cep\"}," +
                "\"salario\":\"2000.00\"," +
                "\"status\":\"null\"}";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(URL_API_PROPOSTA + "/" + proposta.getId())
        ).andExpect(MockMvcResultMatchers
                .status()
                .isOk()
        ).andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedBody, body);
    }

    @Test
    @DisplayName("Retorna status 400 quando a proposta não existe")
    void metodoBuscaPorId_Retorna400() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_API_PROPOSTA + new Random().nextLong())
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(404)
        );
    }

    @Test
    @DisplayName("Retorna status 400 quando a logradouro for nulo.")
    void metodoCria_Retorna400_QuandoLogradouroNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        null,
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a logradouro for vazio.")
    void metodoCria_Retorna400_QuandoLogradouroVazio() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a numero for nulo.")
    void metodoCria_Retorna400_QuandoNumeroNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        null,
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a numero for vazio.")
    void metodoCria_Retorna400_QuandoNumeroVazio() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a bairro for nulo.")
    void metodoCria_Retorna400_QuandoBairroNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        null,
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a bairro for vazio.")
    void metodoCria_Retorna400_QuandoBairroVazio() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a cidade for nulo.")
    void metodoCria_Retorna400_QuandoCidadeNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        null,
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a cidade for vazio.")
    void metodoCria_Retorna400_QuandoCidadeVazio() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a estado for nulo.")
    void metodoCria_Retorna400_QuandoEstadoNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        null,
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a estado for vazio.")
    void metodoCria_Retorna400_QuandoEstadoVazio() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "",
                        "cep"),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a cep for nulo.")
    void metodoCria_Retorna400_QuandoCepNulo() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        null),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
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
    @DisplayName("Retorna status 400 quando a cep for vazio.")
    void metodoCria_Retorna400_QuandoCepVazio() throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        ""),
                new BigDecimal(2000)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
                .content(objectMapper.writeValueAsString(novaPropostaRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .isBadRequest()
        );

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }
}
