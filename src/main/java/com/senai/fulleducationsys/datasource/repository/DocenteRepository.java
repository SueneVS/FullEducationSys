package com.senai.fulleducationsys.datasource.repository;


import com.senai.fulleducationsys.datasource.entity.DocenteEntity;
import com.senai.fulleducationsys.datasource.entity.MateriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteRepository extends JpaRepository<DocenteEntity, Long> {
}
