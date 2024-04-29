package com.senai.fulleducationsys.datasource.repository;

import com.senai.fulleducationsys.datasource.entity.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoEntity, Long> {
}
