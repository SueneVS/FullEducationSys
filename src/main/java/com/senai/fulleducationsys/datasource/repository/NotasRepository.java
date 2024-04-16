package com.senai.fulleducationsys.datasource.repository;

import com.senai.fulleducationsys.datasource.entity.NotasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotasRepository extends JpaRepository<NotasEntity, Long> {
}
