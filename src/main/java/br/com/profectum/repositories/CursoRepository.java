package br.com.profectum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.profectum.model.Curso;
import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long>{

}