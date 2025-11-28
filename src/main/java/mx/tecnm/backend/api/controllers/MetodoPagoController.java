

package mx.tecnm.backend.api.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import mx.tecnm.backend.api.models.MetodoPago.metodopago;
import mx.tecnm.backend.api.repository.MetodoPagoDAO;

@RestController
@RequestMapping("/metodospago")
public class MetodoPagoController {

    @Autowired
    MetodoPagoDAO repo;

    @GetMapping()
    public ResponseEntity<List<metodopago>> obtenerMetodosPago() {
        List<metodopago> metodospago = repo.consultarMetodosPago();
       return ResponseEntity.ok(metodospago);

}
}
