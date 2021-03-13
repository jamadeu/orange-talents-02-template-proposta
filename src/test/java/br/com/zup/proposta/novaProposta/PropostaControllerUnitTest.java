package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.analise.AnaliseCliente;
import br.com.zup.proposta.analise.AnaliseRequest;
import br.com.zup.proposta.analise.AnaliseResponse;
import br.com.zup.proposta.analise.TipoStatus;
import br.com.zup.proposta.cartao.ClienteCartao;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PropostaControllerUnitTest {

    @InjectMocks
    private PropostaController propostaController;

    @Mock
    private AnaliseCliente analiseCliente;

    @Mock
    private ClienteCartao clienteCartao;

    @Mock
    private Logger logger;

    @Mock
    private Tracer tracer;

    @Mock
    private Span span;

    @Mock
    private PropostaRepository propostaRepository;

    @BeforeEach
    void setup() {
        MockHttpServletRequest servlet = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servlet));
        when(propostaRepository.findByStatusPropostaAndConcluido(StatusProposta.ELEGIVEL, false))
                .thenReturn(new ArrayList<Proposta>());
        when(analiseCliente.analise(any(AnaliseRequest.class)))
                .thenReturn(new AnaliseResponse(TipoStatus.SEM_RESTRICAO));
    }

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
        Proposta proposta = novaPropostaRequest.toModel();
        when(propostaRepository.save(any(Proposta.class))).thenReturn(proposta);

        ResponseEntity<?> response = propostaController.cria(novaPropostaRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().get("Location"));
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

        Proposta proposta = novaPropostaRequest.toModel();
        when(propostaRepository.save(any(Proposta.class))).thenReturn(proposta);

        ResponseEntity<?> response = propostaController.cria(novaPropostaRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().get("Location"));
    }
}
