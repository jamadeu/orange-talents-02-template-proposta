package br.com.zup.proposta.cartao;

import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.novaProposta.PropostaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartaoResponse {

    final Logger logger = LoggerFactory.getLogger(CartaoResponse.class);

    private final String id;
    private final LocalDateTime emitidoEm;
    private final String titular;
    private final List<BloqueioResponse> bloqueiosResponse;
    private final List<AvisoResponse> avisosResponse;
    private final List<CarteiraResponse> carteirasResponse;
    private final List<ParcelaResponse> parcelasResponse;
    private final BigDecimal limite;
    private final RenegociacaoResponse renegociacaoResponse;
    private final VencimentoResponse vencimentoResponse;
    private final String idProposta;

    public CartaoResponse(String id, LocalDateTime emitidoEm, String titular, List<BloqueioResponse> bloqueiosResponse, List<AvisoResponse> avisosResponse, List<CarteiraResponse> carteirasResponse, List<ParcelaResponse> parcelasResponse, BigDecimal limite, RenegociacaoResponse renegociacaoResponse, VencimentoResponse vencimentoResponse, String idProposta) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.bloqueiosResponse = bloqueiosResponse;
        this.avisosResponse = avisosResponse;
        this.carteirasResponse = carteirasResponse;
        this.parcelasResponse = parcelasResponse;
        this.limite = limite;
        this.renegociacaoResponse = renegociacaoResponse;
        this.vencimentoResponse = vencimentoResponse;
        this.idProposta = idProposta;
    }

    public Cartao toModel(PropostaRepository propostaRepository) {
        Optional<Proposta> optionalProposta = propostaRepository.findById(Long.getLong(idProposta));
        if (optionalProposta.isEmpty()) {
            logger.error("Proposta não localizada, id = {}", idProposta);
            throw new RuntimeException();
        }
        Proposta proposta = optionalProposta.get();
        List<Bloqueio> bloqueios = bloqueiosResponse.stream().map(BloqueioResponse::toModel).collect(Collectors.toList());
        List<Aviso> avisos = avisosResponse.stream().map(AvisoResponse::toModel).collect(Collectors.toList());
        List<Carteira> carteiras = carteirasResponse.stream().map(CarteiraResponse::toModel).collect(Collectors.toList());
        List<Parcela> parcelas = parcelasResponse.stream().map(ParcelaResponse::toModel).collect(Collectors.toList());

        return new Cartao(
                id,
                emitidoEm,
                titular,
                bloqueios,
                avisos,
                carteiras,
                parcelas,
                limite,
                renegociacaoResponse.toModel(),
                vencimentoResponse.toModel(),
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

    public List<BloqueioResponse> getBloqueios() {
        return bloqueiosResponse;
    }

    public List<AvisoResponse> getAvisos() {
        return avisosResponse;
    }

    public List<CarteiraResponse> getCarteiras() {
        return carteirasResponse;
    }

    public List<ParcelaResponse> getParcelas() {
        return parcelasResponse;
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
