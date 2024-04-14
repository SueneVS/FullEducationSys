package com.senai.fulleducationsys.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "turma")
@AllArgsConstructor
@NoArgsConstructor
public class TurmaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turma", nullable = false)
    private int turmaId;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @OneToMany(mappedBy="turma", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<AlunoEntity> alunos;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_professor")
    private DocenteEntity professor;

    @ManyToOne
    @JoinColumn(name="id_curso")
    private CursoEntity curso;


    public TurmaEntity(String nome, List<AlunoEntity> alunos, DocenteEntity professor, CursoEntity curso) {
        this.nome = nome;
        this.alunos = alunos;
        this.professor = professor;
        this.curso = curso;
    }
}
