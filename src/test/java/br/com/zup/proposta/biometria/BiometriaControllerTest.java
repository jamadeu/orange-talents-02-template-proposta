package br.com.zup.proposta.biometria;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.novaProposta.*;
import br.com.zup.proposta.vencimento.Vencimento;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BiometriaControllerTest {

    private final String URI_API_BIOMETRIA = "/api/biometria";

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
        Proposta proposta = new Proposta(
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
        proposta.adicionaCartao( new Cartao(
                "5180 2059 9038 4287",
                LocalDateTime.now(),
                proposta.getNome(),
                new BigDecimal(1000),
                new Vencimento(
                        "id",
                        14,
                        LocalDateTime.now()
                ),
                proposta
        ));
        propostaRepository.save(proposta);
        this.cartao = proposta.getCartao();
    }

    @WithMockUser
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

    @WithMockUser
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