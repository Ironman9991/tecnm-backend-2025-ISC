package mx.tecnm.backend.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.tecnm.backend.api.models.MetodoPago;
import mx.tecnm.backend.api.repository.MetodoPagoDAO;

@RestController
@RequestMapping("/metodospago")
public class MetodoPagoController {

    @Autowired
    MetodoPagoDAO repo;

    // 1. Obtener todos los métodos de pago
    @GetMapping()
    public ResponseEntity<List<MetodoPago>> obtenerMetodosPago() {
        try {
            List<MetodoPago> metodospago = repo.consultarMetodosPago();
            return ResponseEntity.ok(metodospago);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 2. Obtener método de pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<MetodoPago> obtenerMetodoPagoPorId(@PathVariable int id) {
        try {
            MetodoPago metodoPago = repo.buscarPorId(id);
            if (metodoPago != null) {
                return ResponseEntity.ok(metodoPago);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 3. Buscar métodos de pago por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<MetodoPago>> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<MetodoPago> metodospago = repo.buscarPorNombre(nombre);
            if (metodospago.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(metodospago);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 4. Crear nuevo método de pago
    @PostMapping()
    public ResponseEntity<MetodoPago> crearMetodoPago(@RequestBody MetodoPago metodoPago) {
        try {
            // Validaciones básicas
            if (metodoPago.nombre() == null || metodoPago.nombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            MetodoPago nuevoMetodoPago = repo.crearMetodoPago(metodoPago);
            return ResponseEntity.ok(nuevoMetodoPago);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 5. Crear nuevo método de pago (versión con parámetros)
    @PostMapping("/nuevo")
    public ResponseEntity<MetodoPago> crearMetodoPagoParam(
            @RequestParam String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            MetodoPago metodoPago = new MetodoPago(0, nombre);
            MetodoPago nuevoMetodoPago = repo.crearMetodoPago(metodoPago);
            return ResponseEntity.ok(nuevoMetodoPago);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 6. Actualizar método de pago
    @PutMapping("/{id}")
    public ResponseEntity<MetodoPago> actualizarMetodoPago(
            @PathVariable int id, 
            @RequestBody MetodoPago metodoPago) {
        try {
            if (metodoPago.nombre() == null || metodoPago.nombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            MetodoPago metodoPagoActualizado = repo.actualizarMetodoPago(id, metodoPago);
            if (metodoPagoActualizado != null) {
                return ResponseEntity.ok(metodoPagoActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 7. Actualizar solo el nombre
    @PutMapping("/{id}/nombre")
    public ResponseEntity<MetodoPago> actualizarNombreMetodoPago(
            @PathVariable int id, 
            @RequestParam String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            MetodoPago metodoPagoActualizado = repo.actualizarNombreMetodoPago(id, nombre);
            if (metodoPagoActualizado != null) {
                return ResponseEntity.ok(metodoPagoActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 8. Eliminar método de pago (borrado físico)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMetodoPago(@PathVariable int id) {
        try {
            boolean eliminado = repo.eliminarMetodoPago(id);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 9. Contar métodos de pago
    @GetMapping("/contar")
    public ResponseEntity<Integer> contarMetodosPago() {
        try {
            int cantidad = repo.contarMetodosPago();
            return ResponseEntity.ok(cantidad);
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); 
            }
        }
}