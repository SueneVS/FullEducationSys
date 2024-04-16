package com.senai.fulleducationsys.controller.dto.request;



public record InserirUsuarioRequest(
        String login,
        String senha,
        String nomePapel ) {

}
