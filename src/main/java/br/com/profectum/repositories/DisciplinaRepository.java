package br.com.profectum.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.profectum.model.Disciplina;
import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long>{
	Optional<Disciplina> findByNomeDisciplina(String nomeDisciplina);
}