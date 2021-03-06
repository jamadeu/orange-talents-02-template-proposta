package br.com.zup.proposta.novaProposta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    Optional<Proposta> findByEmail(String email);

    boolean existsByDocumento(String documento);

    List<Proposta> findByStatusProposta(StatusProposta status);
}
