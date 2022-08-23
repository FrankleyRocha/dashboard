package br.eti.frankley.dashboard.repository;

import br.eti.frankley.dashboard.domain.Conexao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Conexao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConexaoRepository extends JpaRepository<Conexao, Long> {}
