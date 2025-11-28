package mx.tecnm.backend.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.tecnm.backend.api.models.DetalleCarrito.detallecarrito;
import mx.tecnm.backend.api.repository.DetalleCarritoDAO;

@RestController
@RequestMapping("/detallecarritos")
public class DetalleCarritoController {

    @Autowired
    DetalleCarritoDAO repo;

    @GetMapping()
    public ResponseEntity<List<detallecarrito>> obtenerDetalleCarritos() {
        List<detallecarrito> detalleCarritos = repo.consultarDetalleCarritos();
        return ResponseEntity.ok(detalleCarritos);
    }

    
}
