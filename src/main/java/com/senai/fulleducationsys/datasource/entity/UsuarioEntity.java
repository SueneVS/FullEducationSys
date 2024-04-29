package com.senai.fulleducationsys.datasource.entity;

import com.senai.fulleducationsys.controller.dto.request.LoginRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;

@Entity
@Data
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Long usuarioId;

    @Column(name = "login", unique = true, length = 150, nullable = false)
    private String login;

    @Column(name = "senha", length = 150, nullable = false)
    private String senha;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_papel")
    private PapelEntity papel;

    public boolean senhaValida(
            LoginRequest loginRequest,
            BCryptPasswordEncoder bCryptEncoder
    ) {
        return bCryptEncoder.matches(
                loginRequest.senha(),
                this.senha
        );
    }


    public UsuarioEntity(String login, PapelEntity papel) {
        this.login = login;
        this.papel = papel;
    }

}

