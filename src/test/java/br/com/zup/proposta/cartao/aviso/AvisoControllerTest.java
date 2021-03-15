package br.com.zup.proposta.cartao.aviso;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.vencimento.Vencimento;
import br.com.zup.proposta.novaProposta.Endereco;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AvisoControllerTest {

    private final String URI_API_AVISO = "/api/aviso";

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
    @DisplayName("Retorna 200 quando aviso adicionado ao cartao com sucesso")
    void retorna200() throws Exception {
        String uri = URI_API_AVISO + "/" + cartao.getId();
        AvisoRequest request = new AvisoRequest("Destino", LocalDate.of(2050, 3, 1));
        MvcResult mvcResult = performPost(request, 200, uri);

        List<Aviso> avisos = cartao.getAvisos();
        assertEquals(1, avisos.size());

        Aviso aviso = avisos.get(0);
        assertAll(
                () -> assertEquals("Destino", aviso.getDestino()),
                () -> assertEquals(LocalDate.of(2050, 3, 1), aviso.getValidoAte())
        );
    }

    @WithMockUser
    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Retorna 400 quando destino vazio ou nulo")
    void retorna400DestinoVazioOuNulo(String destino) throws Exception {
        String uri = URI_API_AVISO + "/" + cartao.getId();
        AvisoRequest request = new AvisoRequest(destino, LocalDate.of(2050, 3, 1));
        MvcResult mvcResult = performPost(request, 400, uri);

        List<Aviso> avisos = cartao.getAvisos();
        assertEquals(0, avisos.size());
    }

    @WithMockUser
    @ParameterizedTest
    @NullSource
    @DisplayName("Retorna 400 quando validoAte nulo")
    void retorna400validoAteNulo(LocalDate validoAte) throws Exception {
        String uri = URI_API_AVISO + "/" + cartao.getId();
        AvisoRequest request = new AvisoRequest("Destino", validoAte);
        MvcResult mvcResult = performPost(request, 400, uri);

        List<Aviso> avisos = cartao.getAvisos();
        assertEquals(0, avisos.size());
    }

    @WithMockUser
    @Test
    @NullSource
    @EmptySource
    @DisplayName("Retorna 400 quando validoAte é passado")
    void retorna400ValidoAtePassado() throws Exception {
        String uri = URI_API_AVISO + "/" + cartao.getId();
        AvisoRequest request = new AvisoRequest("Destino", LocalDate.of(2020, 12, 12));
        MvcResult mvcResult = performPost(request, 400, uri);

        List<Aviso> avisos = cartao.getAvisos();
        assertEquals(0, avisos.size());
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