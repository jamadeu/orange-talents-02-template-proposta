package br.com.zup.proposta.cartao.bloqueio;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.novaProposta.Endereco;
import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.novaProposta.PropostaRepository;
import br.com.zup.proposta.vencimento.Vencimento;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BloqueioControllerTest {

    private final String URI_API_BLOQUEIO = "/api/bloqueio";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropostaRepository propostaRepository;

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
        proposta.adicionaCartao(new Cartao(
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
    @DisplayName("Retorna 200 quando bloqueio adicionado ao cartao com sucesso")
    void retorna200() throws Exception {
        String uri = URI_API_BLOQUEIO + "/" + cartao.getId();
        BloqueioRequest request = new BloqueioRequest("Proposta");
        MvcResult mvcResult = performPost(request, 200, uri);

        List<Bloqueio> bloqueios = cartao.getBloqueios();
        assertEquals(1, bloqueios.size());

        Bloqueio bloqueio = bloqueios.get(0);
        assertEquals("Proposta", bloqueio.getSistemaResponsavel());
    }

    @WithMockUser
    @Test
    @DisplayName("Retorna 404 quando cartao nao e localizado")
    void retorna404() throws Exception {
        String uri = URI_API_BLOQUEIO + "/" + new Random().nextLong();
        BloqueioRequest request = new BloqueioRequest("Proposta");
        MvcResult mvcResult = performPost(request, 404, uri);

        List<Bloqueio> bloqueios = cartao.getBloqueios();
        assertEquals(0, bloqueios.size());
    }


    private MvcResult performPost(Object request, int status, String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .header("user-agent", "username")
        ).andExpect(MockMvcResultMatchers
                .status()
                .is(status)
        ).andReturn();
    }

}