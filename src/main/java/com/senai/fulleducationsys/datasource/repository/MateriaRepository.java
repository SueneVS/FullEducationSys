package com.senai.fulleducationsys.datasource.repository;

import com.senai.fulleducationsys.datasource.entity.MateriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaRepository extends JpaRepository<MateriaEntity, Long> {
    @Query(
            " select t from MateriaEntity t " +
                    " where t.curso.cursoId = :id"
    )
    List<MateriaEntity> findAllByCursoId(@Param("id")Long cursoId);
}
