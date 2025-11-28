package mx.tecnm.backend.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import mx.tecnm.backend.api.models.Usuario.usuarios;
import mx.tecnm.backend.api.repository.UsuarioDAO;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioDAO repo;


    @GetMapping()
    public ResponseEntity<List<usuarios>> obtenerUsuario() {
        List<usuarios> usuarios = repo.consultarUsuario();
        return ResponseEntity.ok(usuarios);
    } 
}
