package com.senai.fulleducationsys.datasource.repository;

import com.senai.fulleducationsys.datasource.entity.NotasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotasRepository extends JpaRepository<NotasEntity, Long> {
    @Query(
            " select t from NotasEntity t " +
                    " where t.aluno.alunoId = :id"
    )
    List<NotasEntity> findAllByAlunoId(@Param("id") Long notasId);
}
