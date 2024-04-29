package com.senai.fulleducationsys.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "nota")
@AllArgsConstructor
@NoArgsConstructor
public class NotasEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notas", nullable = false)
    private Long notasId;

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private AlunoEntity aluno;

    @ManyToOne
    @JoinColumn(name = "id_professor")
    private DocenteEntity professor;

    @ManyToOne
    @JoinColumn(name = "id_materia")
    private MateriaEntity materia;


    @Column(name = "valor",  nullable = false)
    @ColumnDefault(value = "0.00")
    private Double nota;

    @Column(name = "data_nota", nullable = false)
    @ColumnDefault(value ="CURRENT_TIMESTAMP")
    private LocalDate dataNota;


    public NotasEntity(AlunoEntity aluno, DocenteEntity professor, MateriaEntity materia, Double nota, LocalDate dataNota) {
        this.aluno = aluno;
        this.professor = professor;
        this.materia = materia;
        this.nota = nota;
        this.dataNota = dataNota;
    }
}
