package br.com.zup.proposta.novaProposta;

import br.com.zup.proposta.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class PropostaPendenteCartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "propostaId", unique = true, nullable = false, referencedColumnName = "id")
    private Proposta proposta;

    @OneToOne
    @JoinColumn(name = "cartaoId", referencedColumnName = "id")
    private Cartao cartao;

    private Boolean concluido;

    private LocalDateTime concluidoEm;

    @Deprecated
    public PropostaPendenteCartao() {
    }

    public PropostaPendenteCartao(@NotNull Proposta proposta) {
        this.proposta = proposta;
    }

    public void adicionaCartao(@NotNull Cartao cartao) {
        proposta.adicionaCartao(cartao);
        this.cartao = cartao;
        concluido = true;
        concluidoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public Boolean getConcluido() {
        return concluido;
    }

    public LocalDateTime getConcluidoEm() {
        return concluidoEm;
    }
}
