package br.com.zup.proposta.biometria;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.novaProposta.EnderecoRequest;
import br.com.zup.proposta.novaProposta.NovaPropostaRequest;
import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.novaProposta.PropostaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "API_ANALISE=http://localhost:8080/api/analise-fake",
                "API_CARTOES=http://localhost:8080/api/cartoes-fake"
        })
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BiometriaControllerTest {

    private final String URI_API_BIOMETRIA = "/api/biometria";
    private final String URI_API_PROPOSTA = "/api/proposta";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropostaRepository propostaRepository;

    @PersistenceContext
    private EntityManager em;

    private Cartao cartao;

    @BeforeEach
    void setup() throws Exception {
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

        MvcResult mvcResult = performPost(novaPropostaRequest, 201, URI_API_PROPOSTA);
        Thread.sleep(1500);
        String location = mvcResult.getResponse().getHeader("Location");
        String idProposta = location.substring(location.length() - 1);
        Proposta proposta = em.find(Proposta.class, Long.parseLong(idProposta));
        cartao = proposta.getCartao();
    }

    @Test
    @DisplayName("Retorna 201 quando biometria adicionada ao cartao com sucesso")
    void retorna201_QuandoSucesso() throws Exception {
        byte[] biometriaString = new String("Biometria").getBytes(StandardCharsets.UTF_8);
        String uri = URI_API_BIOMETRIA + "/" + cartao.getId();
        BiometriaRequest biometriaRequest = new BiometriaRequest(Base64.getEncoder().encodeToString(biometriaString));
        MvcResult mvcResult = performPost(biometriaRequest, 201, uri);

        String location = mvcResult.getResponse().getHeader("Location");
        String idBiometria = location.substring(location.length() - 1);
        Biometria biometria = em.find(Biometria.class, Long.parseLong(idBiometria));
        Cartao cartaoBio = biometria.getCartao();

        assertEquals(cartao.getNumero(), cartaoBio.getNumero());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Retorna 400 quando biometria vazia ou nula")
    void retorna400_QuandoBiometriaVaziaOuNula(String biometria) throws Exception {
        String uri = URI_API_BIOMETRIA + "/" + cartao.getId();
        BiometriaRequest biometriaRequest = new BiometriaRequest(biometria);
        MvcResult mvcResult = performPost(biometriaRequest, 400, uri);
    }

    private MvcResult performPost(Object request, int status, String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(status)
        ).andReturn();
    }
}