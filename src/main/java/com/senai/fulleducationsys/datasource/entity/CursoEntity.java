package com.senai.fulleducationsys.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "curso")
@AllArgsConstructor
@NoArgsConstructor
public class CursoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso", nullable = false)
    private Long cursoId;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    //@OneToMany(mappedBy = "curso", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    //private List<TurmaEntity> turmas;

    //@OneToMany(mappedBy = "curso", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    //private List<MateriaEntity> materias;


    public CursoEntity(String nome) {
        this.nome = nome;
    }
}
