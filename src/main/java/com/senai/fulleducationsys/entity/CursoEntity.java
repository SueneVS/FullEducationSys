package com.senai.fulleducationsys.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.LocalDate;
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
    private int cursoId;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<TurmaEntity> turmas;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<MateriaEntity> materias;


    public CursoEntity(String nome, List<TurmaEntity> turmas, List<MateriaEntity> materias) {
        this.nome = nome;
        this.turmas = turmas;
        this.materias = materias;
    }
}
