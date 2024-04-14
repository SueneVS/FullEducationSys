package com.senai.fulleducationsys.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.LocalDate;
@Entity
@Data
@Table(name = "docente")
@AllArgsConstructor
@NoArgsConstructor
public class DocenteEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente", nullable = false)
    private int docenteId;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;


    @Column(name = "data_entrada")
    @ColumnDefault(value ="CURRENT_TIMESTAMP")
    private LocalDate dataEntrada;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_usuario", unique = true)
    private UsuarioEntity usuario;


    public DocenteEntity(String nome, UsuarioEntity usuario) {
        this.nome = nome;
        this.usuario = usuario;
    }
}
