package com.senai.fulleducationsys.datasource.repository;

import com.senai.fulleducationsys.datasource.entity.PapelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PapelRepository extends JpaRepository<PapelEntity, Long> {
    Optional<PapelEntity> findByNome(String nome);
}
