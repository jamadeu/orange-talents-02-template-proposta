package br.com.zup.proposta.novaProposta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    Optional<Proposta> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    @Query("select p from Proposta p where p.status_proposta = 'ELEGIVEL' and p.concluido = false")
    List<Proposta> propostasPendenteCartao();
}
