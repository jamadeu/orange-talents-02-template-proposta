package br.com.zup.proposta.novaProposta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "API_ANALISE=http://localhost:8080/api/analise-fake",
                "API_CARTOES=http://localhost:8080/api/cartoes-fake"
        })
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

        MvcResult mvcResult = performPost(novaPropostaRequest, 201);

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

        MvcResult mvcResult = performPost(novaPropostaRequest, 201);

        Proposta proposta = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento()).orElseThrow();
        String locationEsperado = "http://localhost:8080" + URL_API_PROPOSTA + "/" + proposta.getId();
        String location = mvcResult.getResponse().getHeader("Location");

        assertEquals(locationEsperado, location);
        assertEquals(proposta.getEmail(), novaPropostaRequest.getEmail());
    }

    @DisplayName("Retorna status 400 quando o documento é invalido, nulo ou vazio.")
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"111.111.111-11", "11.111.111/0001-11"})
    void metodoCria_Retorna400_QuandoDocumentoInvalidoOuNuloOuVazio(String documento) throws Exception {
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

        performPost(novaPropostaRequest, 400);

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"email invalido"})
    @DisplayName("Retorna status 400 quando a email for invalido, nulo ou vazio.")
    void metodoCria_Retorna400_QuandoEmailInvalidoOuNuloOuVazio(String email) throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                email,
                "Nome",
                new EnderecoRequest(
                        "Logradouro",
                        "Numero",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        performPost(novaPropostaRequest, 400);

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Retorna status 400 quando a nome for nulo ou vazio.")
    void metodoCria_Retorna400_QuandoNomeNuloOuVazio(String nome) throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                nome,
                new EnderecoRequest(
                        "Logradouro",
                        "Numero",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        performPost(novaPropostaRequest, 400);

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

        performPost(novaPropostaRequest, 400);

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

        performPost(novaPropostaRequest, 400);

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

        performPost(novaPropostaRequest, 400);

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

        MvcResult mvcResult = performPost(novaPropostaRequest, 422);

        String errorMessage = mvcResult.getResponse().getContentAsString();
        assertEquals(errorMessage, "{\"mensagens\":[\"Ja existe uma proposta para este cliente\"]}");
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

        performPost(novaPropostaRequest, 201);
        ;

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

        performPost(novaPropostaRequest, 201);

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
                "\"status\":\"null\"," +
                "\"cartao\":\"Nao possui cartao\"}";

        MvcResult mvcResult = performGet(200, URL_API_PROPOSTA + "/" + proposta.getId());

        String body = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedBody, body);
    }

    @Test
    @DisplayName("Retorna status 400 quando a proposta não existe")
    void metodoBuscaPorId_Retorna400() throws Exception {
        performGet(404, URL_API_PROPOSTA + new Random().nextLong());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Retorna status 400 quando a logradouro for nulo ou vazio.")
    void metodoCria_Retorna400_QuandoLogradouroNuloOuVazio(String logradouro) throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        logradouro,
                        "Numero",
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        performPost(novaPropostaRequest, 400);

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Retorna status 400 quando a numero for nulo ou vazio.")
    void metodoCria_Retorna400_QuandoNumeroNuloOuVazio(String numero) throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        numero,
                        "Bairro",
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        performPost(novaPropostaRequest, 400);

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Retorna status 400 quando a bairro for nulo ou vazio.")
    void metodoCria_Retorna400_QuandoBairroNuloOuVazio(String bairro) throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        bairro,
                        "Cidade",
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        performPost(novaPropostaRequest, 400);

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Retorna status 400 quando a cidade for nulo ou vazio.")
    void metodoCria_Retorna400_QuandoCidadeNuloOuVazio(String cidade) throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        cidade,
                        "Estado",
                        "cep"),
                new BigDecimal(2000)
        );

        performPost(novaPropostaRequest, 400);

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Retorna status 400 quando a estado for nulo ou vazio.")
    void metodoCria_Retorna400_QuandoEstadoNuloOuVazio(String estado) throws Exception {
        NovaPropostaRequest novaPropostaRequest = new NovaPropostaRequest(
                "041.112.040-90",
                "email@test.com",
                "Nome",
                new EnderecoRequest(
                        "Rua",
                        "100",
                        "Bairro",
                        "Cidade",
                        estado,
                        "cep"),
                new BigDecimal(2000)
        );

        performPost(novaPropostaRequest, 400);

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Retorna status 400 quando a cep for nulo ou vazio.")
    void metodoCria_Retorna400_QuandoCepNuloOuVazio(String cep) throws Exception {
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
                        cep),
                new BigDecimal(2000)
        );

        performPost(novaPropostaRequest, 400);

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento());

        assertTrue(propostaOptional.isEmpty());
    }

    @Test
    @DisplayName("Cartao gerado e adicionado a proposta")
    void cartaoGerado() throws Exception {
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

        MvcResult mvcResult = performPost(novaPropostaRequest, 201);
        Thread.sleep(2000);
        Proposta proposta = propostaRepository.findByDocumento(novaPropostaRequest.getDocumento()).orElseThrow();

        assertEquals(proposta.getEmail(), novaPropostaRequest.getEmail());
        assertNotNull(proposta.getCartao());
    }

    private MvcResult performPost(NovaPropostaRequest request, int status) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(URL_API_PROPOSTA)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(status)
        ).andReturn();
    }

    private MvcResult performGet(int status, String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(url)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(status)
        ).andReturn();
    }
}
