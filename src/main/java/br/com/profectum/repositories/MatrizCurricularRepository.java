package br.com.profectum.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.profectum.model.MatrizCurricular;
import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface MatrizCurricularRepository extends JpaRepository<MatrizCurricular, Long>{
	Optional<MatrizCurricular> findByNomeMatrizCurricular(String nomeMatrizCurricular);
}
