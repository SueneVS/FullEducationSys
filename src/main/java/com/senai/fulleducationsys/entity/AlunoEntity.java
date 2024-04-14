package com.senai.fulleducationsys.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "aluno")
@AllArgsConstructor
@NoArgsConstructor
public class AlunoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno", nullable = false)
    private int alunoId;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_nascimento", nullable = false)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dataNascimento;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_usuario", unique = true)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name="id_turma")
    private TurmaEntity turma;


    public AlunoEntity(String nome, LocalDate dataNascimento, UsuarioEntity usuario, TurmaEntity turma) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.usuario = usuario;
        this.turma = turma;
    }
}
