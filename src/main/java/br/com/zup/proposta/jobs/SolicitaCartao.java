package br.com.zup.proposta.jobs;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoRequest;
import br.com.zup.proposta.cartao.ClienteCartao;
import br.com.zup.proposta.cartao.CriaCartaoResponse;
import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.novaProposta.PropostaRepository;
import br.com.zup.proposta.novaProposta.StatusProposta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class SolicitaCartao {

    private final Logger logger = LoggerFactory.getLogger(SubmeteAnalise.class);

    private final List<Proposta> propostasPendenteCartao = new ArrayList<>();
    private final PropostaRepository propostaRepository;
    private final ClienteCartao clienteCartao;

    public SolicitaCartao(PropostaRepository propostaRepository, ClienteCartao clienteCartao) {
        this.propostaRepository = propostaRepository;
        this.clienteCartao = clienteCartao;
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void solicitaCartao() {
        propostasPendenteCartao.addAll(propostaRepository.findByStatusProposta(StatusProposta.ELEGIVEL));
        propostasPendenteCartao.forEach(proposta -> {
            logger.info("Proposta - {}", proposta.toString());
            CartaoRequest cartaoRequest = new CartaoRequest(proposta.getDocumento(), proposta.getNome(), proposta.getId());
            try {
                CriaCartaoResponse response = clienteCartao.solicita(cartaoRequest);
                Cartao cartao = response.toModel(proposta);
                logger.info("Cartao - {}", cartao.toString());
                proposta.adicionaCartao(cartao);
                propostaRepository.save(proposta);
                logger.info("Cartao adicionado a proposta");
            } catch (Exception e) {
                logger.error("Proposta - {}", proposta.toString());
                logger.error(e.getMessage());
                logger.error(e.getCause().toString());
            }
        });
    }
}
