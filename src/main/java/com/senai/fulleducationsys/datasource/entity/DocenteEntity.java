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
@Table(name = "docente")
@AllArgsConstructor
@NoArgsConstructor
public class DocenteEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente", nullable = false)
    private Long docenteId;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;


    @Column(name = "data_entrada")
    @ColumnDefault(value ="CURRENT_TIMESTAMP")
    private LocalDate dataEntrada;


    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", unique = true)
    private UsuarioEntity usuario;


    public DocenteEntity(String nome, UsuarioEntity usuario) {
        this.nome = nome;
        this.usuario = usuario;
    }

}
