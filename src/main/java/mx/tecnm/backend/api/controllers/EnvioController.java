package mx.tecnm.backend.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import mx.tecnm.backend.api.models.Envio;
import mx.tecnm.backend.api.repository.EnvioDAO;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    @Autowired
    EnvioDAO repo;

    // 1. Obtener todos los envíos
    // 1. Obtener todos los envíos CON DEBUG
    @GetMapping()
    public ResponseEntity<?> obtenerEnvios() {
        try {
            System.out.println("DEBUG: Solicitud GET /envios recibida");
            System.out.println("DEBUG: Llamando al DAO para obtener todos los envíos...");
            
            List<Envio> envios = repo.consultarEnvios();
            
            System.out.println("DEBUG: Número de envíos obtenidos: " + envios.size());
            
            if (!envios.isEmpty()) {
                System.out.println("DEBUG: Primer envío en la lista:");
                System.out.println("  - ID: " + envios.get(0).id());
                System.out.println("  - Pedido ID: " + envios.get(0).pedidos_id());
                System.out.println("  - Domicilio ID: " + envios.get(0).domicilios_id());
                System.out.println("  - Estado: " + envios.get(0).estado_envio());
            }
            
            return ResponseEntity.ok(envios);
            
        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO en obtenerEnvios:");
            System.err.println("  - Mensaje: " + e.getMessage());
            System.err.println("  - Clase: " + e.getClass().getName());
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Error al obtener envíos\", \"detalle\": \"" + e.getMessage() + "\"}");
        }
    }

    // 2. Obtener envío por ID
    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerEnvioPorId(@PathVariable int id) {
        try {
            Envio envio = repo.buscarPorId(id);
            if (envio != null) {
                return ResponseEntity.ok(envio);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 3. Obtener envíos por pedido_id
    @GetMapping("/pedido/{pedido_id}")
    public ResponseEntity<List<Envio>> obtenerEnviosPorPedido(@PathVariable int pedido_id) {
        try {
            List<Envio> envios = repo.buscarPorPedidoId(pedido_id);
            if (envios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(envios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 4. Obtener envíos por domicilio_id
    @GetMapping("/domicilio/{domicilio_id}")
    public ResponseEntity<List<Envio>> obtenerEnviosPorDomicilio(@PathVariable int domicilio_id) {
        try {
            List<Envio> envios = repo.buscarPorDomicilioId(domicilio_id);
            if (envios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(envios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 5. Obtener envíos por estado
    // @GetMapping("/estado/{estado}")
    // public ResponseEntity<List<Envio>> obtenerEnviosPorEstado(@PathVariable String estado) {
    //     try {
    //         List<Envio> envios = repo.buscarPorEstado(estado);
    //         if (envios.isEmpty()) {
    //             return ResponseEntity.noContent().build();
    //         }
    //         return ResponseEntity.ok(envios);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).build();
    //     }
    // }

    // 6. Crear nuevo envío
    @PostMapping()
    public ResponseEntity<Envio> crearEnvio(@RequestBody Envio envio) {
        try {
            // Validaciones básicas
            if (envio.numero_seguimiento() == null || envio.numero_seguimiento().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Envio nuevoEnvio = repo.crearEnvio(envio);
            return ResponseEntity.ok(nuevoEnvio);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 7. Actualizar envío
    @PutMapping("/{id}")
    public ResponseEntity<Envio> actualizarEnvio(@PathVariable int id, @RequestBody Envio envio) {
        try {
            Envio envioActualizado = repo.actualizarEnvio(id, envio);
            if (envioActualizado != null) {
                return ResponseEntity.ok(envioActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 8. Actualizar estado de envío
    @PutMapping("/{id}/estado")
    public ResponseEntity<Envio> actualizarEstadoEnvio(@PathVariable int id, @RequestParam String estado) {
        try {
            Envio envioActualizado = repo.actualizarEstadoEnvio(id, estado);
            if (envioActualizado != null) {
                return ResponseEntity.ok(envioActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 9. Actualizar número de seguimiento
    @PutMapping("/{id}/seguimiento")
    public ResponseEntity<Envio> actualizarNumeroSeguimiento(@PathVariable int id, @RequestParam String numero_seguimiento) {
        try {
            Envio envioActualizado = repo.actualizarNumeroSeguimiento(id, numero_seguimiento);
            if (envioActualizado != null) {
                return ResponseEntity.ok(envioActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 10. Eliminar envío
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnvio(@PathVariable int id) {
        try {
            boolean eliminado = repo.eliminarEnvio(id);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 11. Buscar envíos por rango de fechas
    @GetMapping("/fecha")
    public ResponseEntity<List<Envio>> buscarPorRangoFechas(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            List<Envio> envios = repo.buscarPorRangoFechas(fechaInicio, fechaFin);
            if (envios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(envios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}