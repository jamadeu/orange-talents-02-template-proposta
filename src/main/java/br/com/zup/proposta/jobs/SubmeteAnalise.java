package br.com.zup.proposta.jobs;

import br.com.zup.proposta.analise.AnaliseCliente;
import br.com.zup.proposta.analise.AnaliseRequest;
import br.com.zup.proposta.analise.AnaliseResponse;
import br.com.zup.proposta.novaProposta.Proposta;
import br.com.zup.proposta.novaProposta.PropostaRepository;
import br.com.zup.proposta.novaProposta.StatusProposta;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class SubmeteAnalise {

    private final Logger logger = LoggerFactory.getLogger(SubmeteAnalise.class);

    private final AnaliseCliente analiseCliente;
    private final PropostaRepository propostaRepository;
    private final List<Proposta> propostasPendentes = new ArrayList<>();

    public SubmeteAnalise(AnaliseCliente analiseCliente, PropostaRepository propostaRepository) {
        this.analiseCliente = analiseCliente;
        this.propostaRepository = propostaRepository;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void submetePropostasParaAnalise() {
        propostasPendentes.addAll(propostaRepository.findByStatusProposta(StatusProposta.PENDENTE));
        propostasPendentes.forEach(proposta -> {
            logger.info(proposta.toString());
            StatusProposta statusProposta = StatusProposta.NAO_ELEGIVEL;
            try {
                AnaliseResponse analiseResponse = analiseCliente.analise(new AnaliseRequest(
                        proposta.getDocumento(), proposta.getNome(), String.valueOf(proposta.getId())));
                statusProposta = analiseResponse.toModel();
                logger.info("Proposta elegivel");
            } catch (FeignException.UnprocessableEntity e) {
                logger.info("Proposta nao elegivel");
            } catch (Exception e) {
                logger.error("Erro na analise");
                logger.error(e.getMessage());
                logger.error(e.getCause().toString());
            }
            proposta.alteraStatus(statusProposta);
            propostaRepository.save(proposta);
            propostasPendentes.remove(proposta);
        });
    }
}
