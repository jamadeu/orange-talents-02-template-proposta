package br.com.zup.proposta.novaProposta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    Optional<Proposta> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    List<Proposta> findByStatusPropostaAndConcluido(StatusProposta statusProposta, Boolean concluido);
}
