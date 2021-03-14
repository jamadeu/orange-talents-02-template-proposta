package br.com.zup.proposta.cartao;

import br.com.zup.proposta.aviso.Aviso;
import br.com.zup.proposta.bloqueio.Bloqueio;
import br.com.zup.proposta.carteira.Carteira;
import br.com.zup.proposta.parcela.Parcela;
import br.com.zup.proposta.renegociacao.RenegociacaoResponse;
import br.com.zup.proposta.vencimento.VencimentoResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BuscaCartaoResponse {

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

    public BuscaCartaoResponse(String id, LocalDateTime emitidoEm, String titular, List<Bloqueio> bloqueios, List<Aviso> avisos, List<Carteira> carteiras, List<Parcela> parcelas, BigDecimal limite, RenegociacaoResponse renegociacaoResponse, VencimentoResponse vencimentoResponse, String idProposta) {
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
}
