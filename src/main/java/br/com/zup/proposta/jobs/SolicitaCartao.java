package br.com.zup.proposta.jobs;

import br.com.zup.proposta.cartao.*;
import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.novaProposta.PropostaRepository;
import br.com.zup.proposta.novaProposta.StatusProposta;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class SolicitaCartao {

    //TODO refatorar


    private final Span span;
    private final List<Proposta> propostasPendenteCartao = new ArrayList<>();
    private final PropostaRepository propostaRepository;
    private final CartaoRepository cartaoRepository;
    private final ClienteCartao clienteCartao;

    public SolicitaCartao(PropostaRepository propostaRepository, CartaoRepository cartaoRepository, ClienteCartao clienteCartao, Tracer tracer) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.clienteCartao = clienteCartao;
        this.span = tracer.activeSpan();
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void solicitaCartao() {
        propostasPendenteCartao.addAll(propostaRepository.findByStatusPropostaAndConcluido(StatusProposta.ELEGIVEL, false));
        propostasPendenteCartao.forEach(proposta -> {
            span.setBaggageItem("Proposta", proposta.toString());
            CartaoRequest cartaoRequest = new CartaoRequest(proposta.getDocumento(), proposta.getNome(), proposta.getId());
            try {
                CriaCartaoResponse response = clienteCartao.solicita(cartaoRequest);
                Cartao cartao = response.toModel(proposta);
                span.setBaggageItem("Cartao", cartao.toString());
//                cartaoRepository.save(cartao);
                proposta.adicionaCartao(cartao);
                propostaRepository.save(proposta);
                span.log("Cartao adicionado a proposta");
                span.setBaggageItem("Proposta", proposta.toString());
                span.setBaggageItem("Cartao", cartao.toString());
                propostasPendenteCartao.remove(proposta);
            } catch (Exception e) {
                span.setBaggageItem("Proposta", proposta.toString());
                span.log(e.getMessage());
                span.log(e.getCause().toString());
            }

        });
    }
}
