package mx.tecnm.backend.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import mx.tecnm.backend.api.models.Pedido.pedido;
import mx.tecnm.backend.api.repository.PedidoDAO;

@RestController
@RequestMapping("/pedidos")

public class PedidoController {

    @Autowired
    PedidoDAO repo;

    @GetMapping()
    public ResponseEntity<List<pedido>> obtenerPedidos() {
        List<pedido> pedidos = repo.consultarPedidos();
        return ResponseEntity.ok(pedidos);
    }
}
