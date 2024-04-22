package com.senai.fulleducationsys.datasource.repository;


import com.senai.fulleducationsys.datasource.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<CursoEntity, Long> {
    boolean existsByNomeCurso(String nome);
}
