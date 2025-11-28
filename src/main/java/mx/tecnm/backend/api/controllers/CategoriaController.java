package mx.tecnm.backend.api.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;

import mx.tecnm.backend.api.models.Categoria.categoria;
import mx.tecnm.backend.api.repository.CategoriaDAO;

@RestController
@RequestMapping("/categorias")

public class CategoriaController {
    @Autowired
    CategoriaDAO repo;

    @GetMapping()
    public ResponseEntity<List<categoria>> obtenerCategorias() {
        List<categoria> categorias = repo.consultarCategorias();
        return ResponseEntity.ok(categorias);
    } 

}
