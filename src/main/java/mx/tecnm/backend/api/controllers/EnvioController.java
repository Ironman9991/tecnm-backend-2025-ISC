package mx.tecnm.backend.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.tecnm.backend.api.models.Envio.envio;
import mx.tecnm.backend.api.repository.EnvioDAO;

@RestController
@RequestMapping("/envios")

public class EnvioController {
    @Autowired
    EnvioDAO repo;

    @GetMapping()
    public ResponseEntity<List<envio>> obtenerEnvios() {
        List<envio> envios = repo.consultarEnvios();
        return ResponseEntity.ok(envios);
    } 
}
