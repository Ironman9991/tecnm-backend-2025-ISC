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

import mx.tecnm.backend.api.models.Domicilio;
import mx.tecnm.backend.api.repository.DomicilioDAO;

@RestController
@RequestMapping("/domicilios")
public class DomicilioController {

    @Autowired
    DomicilioDAO repo;

    // 1. Obtener todos los domicilios
    @GetMapping()
    public ResponseEntity<List<Domicilio>> obtenerDomicilios() {
        try {
            List<Domicilio> domicilios = repo.consultarDomicilios();
            return ResponseEntity.ok(domicilios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 2. Obtener domicilio por ID
     @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDomicilioPorId(@PathVariable int id) {
        try {
            System.out.println("DEBUG: Buscando domicilio con ID: " + id);
            Domicilio domicilio = repo.buscarPorId(id);
            if (domicilio != null) {
                System.out.println("DEBUG: Domicilio encontrado: " + domicilio.calle());
                return ResponseEntity.ok(domicilio);
            } else {
                System.out.println("DEBUG: Domicilio no encontrado para ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Domicilio no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("ERROR en obtenerDomicilioPorId: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
        }
    }

    // 3. Obtener domicilios por usuario_id
    @GetMapping("/usuario/{usuario_id}")
    public ResponseEntity<List<Domicilio>> obtenerDomiciliosPorUsuario(@PathVariable int usuario_id) {
        try {
            List<Domicilio> domicilios = repo.buscarPorUsuarioId(usuario_id);
            if (domicilios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(domicilios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 4. Crear nuevo domicilio
  @PostMapping()
public ResponseEntity<?> crearDomicilio(@RequestBody Domicilio domicilio) {
    try {
        // Debug: Mostrar datos recibidos
        System.out.println("DEBUG: Solicitud POST /domicilios recibida");
        System.out.println("DEBUG: Datos recibidos en JSON:");
        System.out.println("  - ID: " + domicilio.id());
        System.out.println("  - Calle: " + domicilio.calle());
        System.out.println("  - Número: " + domicilio.numero());
        System.out.println("  - Colonia: " + domicilio.colonia());
        System.out.println("  - CP: " + domicilio.cp());
        System.out.println("  - Estado: " + domicilio.estado());
        System.out.println("  - Ciudad: " + domicilio.ciudad());
        System.out.println("  - Usuario ID: " + domicilio.usuarios_id());
        
        // Validaciones detalladas
        if (domicilio.calle() == null || domicilio.calle().trim().isEmpty()) {
            System.err.println("ERROR: El campo 'calle' es requerido y no puede estar vacío");
            return ResponseEntity.badRequest()
                .body("{\"error\": \"El campo 'calle' es requerido\"}");
        }
        
        if (domicilio.numero() == null || domicilio.numero().trim().isEmpty()) {
            System.err.println("ERROR: El campo 'numero' es requerido y no puede estar vacío");
            return ResponseEntity.badRequest()
                .body("{\"error\": \"El campo 'numero' es requerido\"}");
        }
        
        if (domicilio.colonia() == null || domicilio.colonia().trim().isEmpty()) {
            System.err.println("ERROR: El campo 'colonia' es requerido y no puede estar vacío");
            return ResponseEntity.badRequest()
                .body("{\"error\": \"El campo 'colonia' es requerido\"}");
        }
        
        if (domicilio.cp() == null || domicilio.cp().trim().isEmpty()) {
            System.err.println("ERROR: El campo 'cp' (código postal) es requerido");
            return ResponseEntity.badRequest()
                .body("{\"error\": \"El campo 'cp' (código postal) es requerido\"}");
        }
        
        if (domicilio.estado() == null || domicilio.estado().trim().isEmpty()) {
            System.err.println("ERROR: El campo 'estado' es requerido");
            return ResponseEntity.badRequest()
                .body("{\"error\": \"El campo 'estado' es requerido\"}");
        }
        
        if (domicilio.ciudad() == null || domicilio.ciudad().trim().isEmpty()) {
            System.err.println("ERROR: El campo 'ciudad' es requerido");
            return ResponseEntity.badRequest()
                .body("{\"error\": \"El campo 'ciudad' es requerido\"}");
        }
        
        if (domicilio.usuarios_id() <= 0) {
            System.err.println("ERROR: El campo 'usuarios_id' debe ser un número positivo");
            return ResponseEntity.badRequest()
                .body("{\"error\": \"El campo 'usuarios_id' debe ser un número positivo\"}");
        }
        
        System.out.println("DEBUG: Validaciones pasadas correctamente");
        System.out.println("DEBUG: Llamando al DAO para crear domicilio...");
        
        // Crear el domicilio
        Domicilio nuevoDomicilio = repo.crearDomicilio(domicilio);
        
        // Debug: Mostrar resultado
        System.out.println("DEBUG: Domicilio creado exitosamente");
        System.out.println("DEBUG: Nuevo domicilio creado con ID: " + nuevoDomicilio.id());
        System.out.println("DEBUG: Detalles del domicilio creado:");
        System.out.println("  - Nuevo ID: " + nuevoDomicilio.id());
        System.out.println("  - Calle: " + nuevoDomicilio.calle());
        System.out.println("  - Número: " + nuevoDomicilio.numero());
        System.out.println("  - Colonia: " + nuevoDomicilio.colonia());
        System.out.println("  - CP: " + nuevoDomicilio.cp());
        System.out.println("  - Estado: " + nuevoDomicilio.estado());
        System.out.println("  - Ciudad: " + nuevoDomicilio.ciudad());
        System.out.println("  - Usuario ID: " + nuevoDomicilio.usuarios_id());
        
        // Retornar respuesta exitosa
        return ResponseEntity.ok(nuevoDomicilio);
        
    } catch (Exception e) {
        // Debug detallado del error
        System.err.println("ERROR CRÍTICO en crearDomicilio:");
        System.err.println("  - Mensaje: " + e.getMessage());
        System.err.println("  - Clase: " + e.getClass().getName());
        
        // Imprimir stack trace completo
        e.printStackTrace();
        
        // Verificar si es un error de SQL
        if (e.getMessage() != null && e.getMessage().contains("SQL")) {
            System.err.println("ERROR DE SQL DETECTADO");
            System.err.println("Posible problema con la consulta o estructura de la base de datos");
        }
        
        // Verificar si es un error de conexión
        if (e.getMessage() != null && (
            e.getMessage().contains("connection") || 
            e.getMessage().contains("timeout") ||
            e.getMessage().contains("JDBC"))) {
            System.err.println("ERROR DE CONEXIÓN A LA BASE DE DATOS");
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("{\"error\": \"Error al crear domicilio\", \"detalle\": \"" + e.getMessage() + "\"}");
    }
}

    // 5. Actualizar domicilio
    @PutMapping("/{id}")
    public ResponseEntity<Domicilio> actualizarDomicilio(@PathVariable int id, @RequestBody Domicilio domicilio) {
        try {
            Domicilio domicilioActualizado = repo.actualizarDomicilio(id, domicilio);
            if (domicilioActualizado != null) {
                return ResponseEntity.ok(domicilioActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 6. Eliminar domicilio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDomicilio(@PathVariable int id) {
        try {
            boolean eliminado = repo.eliminarDomicilio(id);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 7. Buscar domicilios por ciudad
    @GetMapping("/ciudad")
    public ResponseEntity<List<Domicilio>> buscarPorCiudad(@RequestParam String ciudad) {
        try {
            List<Domicilio> domicilios = repo.buscarPorCiudad(ciudad);
            if (domicilios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(domicilios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 8. Buscar domicilios por estado
    @GetMapping("/estado")
    public ResponseEntity<List<Domicilio>> buscarPorEstado(@RequestParam String estado) {
        try {
            List<Domicilio> domicilios = repo.buscarPorEstado(estado);
            if (domicilios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(domicilios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}