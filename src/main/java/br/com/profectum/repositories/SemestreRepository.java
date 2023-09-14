package br.com.profectum.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.profectum.model.Semestre;
import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface SemestreRepository extends JpaRepository<Semestre, Long>{
	Optional<Semestre> findByNomeSemestre(String nomeSemestre);
}