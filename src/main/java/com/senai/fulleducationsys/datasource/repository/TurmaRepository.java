package com.senai.fulleducationsys.datasource.repository;


import com.senai.fulleducationsys.datasource.entity.TurmaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<TurmaEntity, Long> {
    @Query(
            "SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
                    "FROM TurmaEntity t " +
                    "WHERE t.nome = :nome"
    )
    boolean existsByNomeTurma(@Param("nome") String nome);
}
