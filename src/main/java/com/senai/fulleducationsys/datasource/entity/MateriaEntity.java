package com.senai.fulleducationsys.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@Entity
@Data
@Table(name = "materia")
@AllArgsConstructor
@NoArgsConstructor
public class MateriaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia", nullable = false)
    private Long materiaId;

    @Column(name = "nome", length = 150, nullable = false)
    private String nomeMateria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_curso")
    private CursoEntity curso;


    public MateriaEntity(String nomeMateria, CursoEntity curso) {
        this.nomeMateria = nomeMateria;
        this.curso = curso;
    }
}
