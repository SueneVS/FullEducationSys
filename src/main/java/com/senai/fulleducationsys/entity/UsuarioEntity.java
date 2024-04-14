package com.senai.fulleducationsys.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private int usuarioId;

    @Column(name = "login", unique = true, length = 150, nullable = false)
    private String login;

    @Column(name = "senha", length = 150, nullable = false)
    private String senha;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_papel")
    private PapelEntity papel;

    @OneToOne(mappedBy = "usuario")
    private DocenteEntity docente;

    @OneToOne(mappedBy = "usuario")
    private AlunoEntity aluno;


    public UsuarioEntity(String login, PapelEntity papel, DocenteEntity docente, AlunoEntity aluno) {
        this.login = login;
        this.papel = papel;
        this.docente = docente;
        this.aluno = aluno;
    }
}

