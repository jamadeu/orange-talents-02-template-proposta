package br.com.zup.proposta.cartao;

import br.com.zup.proposta.aviso.Aviso;
import br.com.zup.proposta.bloqueio.Bloqueio;
import br.com.zup.proposta.carteira.Carteira;
import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.parcela.Parcela;
import br.com.zup.proposta.renegociacao.Renegociacao;
import br.com.zup.proposta.renegociacao.RenegociacaoResponse;
import br.com.zup.proposta.vencimento.Vencimento;
import br.com.zup.proposta.vencimento.VencimentoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartaoResponse {

    final Logger logger = LoggerFactory.getLogger(CartaoResponse.class);

    private final String id;
    private final LocalDateTime emitidoEm;
    private final String titular;
    private final List<Bloqueio> bloqueios;
    private final List<Aviso> avisos;
    private final List<Carteira> carteiras;
    private final List<Parcela> parcelas;
    private final BigDecimal limite;
    private final RenegociacaoResponse renegociacaoResponse;
    private final VencimentoResponse vencimentoResponse;
    private final String idProposta;

    public CartaoResponse(String id, LocalDateTime emitidoEm, String titular, List<Bloqueio> bloqueios, List<Aviso> avisos, List<Carteira> carteiras, List<Parcela> parcelas, BigDecimal limite, RenegociacaoResponse renegociacaoResponse, VencimentoResponse vencimentoResponse, String idProposta) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.bloqueios = bloqueios;
        this.avisos = avisos;
        this.carteiras = carteiras;
        this.parcelas = parcelas;
        this.limite = limite;
        this.renegociacaoResponse = renegociacaoResponse;
        this.vencimentoResponse = vencimentoResponse;
        this.idProposta = idProposta;
    }

    public Cartao toModel(EntityManager em) {
        Proposta proposta = em.find(Proposta.class, Long.parseLong(idProposta));
        if (proposta == null) {
            logger.error("Proposta não localizada, id = {}", idProposta);
            throw new RuntimeException();
        }
        Renegociacao renegociacao = null;
        if (renegociacaoResponse != null) {
            renegociacao = renegociacaoResponse.toModel();
            em.persist(renegociacao);
        }
        Vencimento vencimento = null;
        if (vencimentoResponse != null) {
            vencimento = vencimentoResponse.toModel();
            em.persist(vencimento);
        }
        return new Cartao(
                this.id,
                emitidoEm,
                titular,
                bloqueios,
                avisos,
                carteiras,
                parcelas,
                limite,
                renegociacao,
                vencimento,
                proposta
        );
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public String getTitular() {
        return titular;
    }

    public List<Bloqueio> getBloqueios() {
        return bloqueios;
    }

    public List<Aviso> getAvisos() {
        return avisos;
    }

    public List<Carteira> getCarteiras() {
        return carteiras;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public RenegociacaoResponse getRenegociacaoResponse() {
        return renegociacaoResponse;
    }

    public VencimentoResponse getVencimentoResponse() {
        return vencimentoResponse;
    }

    public String getIdProposta() {
        return idProposta;
    }

    @Override
    public String toString() {
        return "CartaoResponse{" +
                "id='" + id + '\'' +
                ", emitidoEm=" + emitidoEm +
                ", titular='" + titular + '\'' +
                ", bloqueios=" + bloqueios +
                ", avisos=" + avisos +
                ", carteiras=" + carteiras +
                ", parcelas=" + parcelas +
                ", limite=" + limite +
                ", renegociacaoResponse=" + renegociacaoResponse +
                ", vencimentoResponse=" + vencimentoResponse +
                ", idProposta='" + idProposta + '\'' +
                '}';
    }
}
