package br.com.zup.proposta.fakes;

import br.com.zup.proposta.cartao.BuscaCartaoResponse;
import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoRequest;
import br.com.zup.proposta.cartao.CriaCartaoResponse;
import br.com.zup.proposta.cartao.aviso.Aviso;
import br.com.zup.proposta.cartao.aviso.AvisoRequest;
import br.com.zup.proposta.cartao.bloqueio.Bloqueio;
import br.com.zup.proposta.cartao.bloqueio.BloqueioRequest;
import br.com.zup.proposta.cartao.vencimento.Vencimento;
import br.com.zup.proposta.cartao.vencimento.VencimentoResponse;
import br.com.zup.proposta.novaProposta.Endereco;
import br.com.zup.proposta.novaProposta.Proposta;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@Profile(value = "test")
@RequestMapping("/api/fake/cartoes")
public class ApiCartoesFake {

    private final Proposta proposta = new Proposta(
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

    private final Cartao cartao = new Cartao(
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
    );

    @PostMapping
    public ResponseEntity<CriaCartaoResponse> cria(@RequestBody @Valid CartaoRequest request) {
        CriaCartaoResponse criaCartaoResponse = new CriaCartaoResponse(
                "5312995574366723",
                LocalDateTime.now(),
                new BigDecimal(2000),
                new VencimentoResponse(
                        "1",
                        20,
                        LocalDateTime.now()
                ),
                String.valueOf(request.getIdProposta())
        );
        return ResponseEntity.ok(criaCartaoResponse);
    }

    @PostMapping("/{id}/bloqueios")
    public ResponseEntity<Void> bloqueio(@RequestBody @Valid BloqueioRequest request) {
        Bloqueio bloqueio = new Bloqueio(
                "123",
                LocalDateTime.now(),
                request.getSistemaResponsavel(),
                true,
                cartao
        );
        this.cartao.adicionaBloqueio(bloqueio);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/avisos")
    public ResponseEntity<Void> aviso(@RequestBody @Valid AvisoRequest request) {
        Aviso aviso = new Aviso(
                request.getValidoAte(),
                request.getDestino(),
                this.cartao
        );
        cartao.adicionaAviso(aviso);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<BuscaCartaoResponse> buscaCartaoPorId(@PathVariable("id") Long id) {
        Cartao cartao = geraCartao();
        BuscaCartaoResponse buscaCartaoResponse = new BuscaCartaoResponse(
                "5180 2059 9038 4287",
                LocalDateTime.now(),
                "Nome",
                cartao.getBloqueios(),
                cartao.getAvisos(),
                new ArrayList<>(),
                new ArrayList<>(),
                new BigDecimal(1000),
                null,
                new VencimentoResponse(
                        "id",
                        14,
                        LocalDateTime.now()
                ),
                "1"
        );
        return ResponseEntity.ok(buscaCartaoResponse);
    }

    private Cartao geraCartao() {
        return this.cartao;
    }
}
