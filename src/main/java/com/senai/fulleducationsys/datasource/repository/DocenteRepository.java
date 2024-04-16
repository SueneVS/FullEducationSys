package com.senai.fulleducationsys.datasource.repository;


import com.senai.fulleducationsys.datasource.entity.DocenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocenteRepository extends JpaRepository<DocenteEntity, Long> {
}
