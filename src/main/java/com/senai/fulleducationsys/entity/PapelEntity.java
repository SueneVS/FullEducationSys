package com.senai.fulleducationsys.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Data
@Table(name = "papel")
@AllArgsConstructor
@NoArgsConstructor
public class PapelEntity implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_papel", nullable = false)
        private int papelId;

        @Column(name = "nome", unique = true, length = 150, nullable = false)
        private String nome;

        public PapelEntity(String nome) {
                this.nome = nome;
        }

}
