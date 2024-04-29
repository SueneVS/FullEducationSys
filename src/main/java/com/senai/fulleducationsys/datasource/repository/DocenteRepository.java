package com.senai.fulleducationsys.datasource.repository;


import com.senai.fulleducationsys.datasource.entity.DocenteEntity;
import com.senai.fulleducationsys.datasource.entity.PapelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<DocenteEntity, Long> {

    List<DocenteEntity> findByUsuarioPapel(Optional<PapelEntity> papelEntity);

}
