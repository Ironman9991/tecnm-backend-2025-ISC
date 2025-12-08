package mx.tecnm.backend.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.tecnm.backend.api.models.DetallePedido;
import mx.tecnm.backend.api.repository.DetallePedidoDAO;

@RestController
@RequestMapping("/detallepedidos")
public class DetallePedidoController {

    @Autowired
    DetallePedidoDAO repo;

    // 1. Obtener todos los detalles de pedidos
    @GetMapping()
    public ResponseEntity<List<DetallePedido>> consultarDetallePedidos() {
        try {
            List<DetallePedido> detallePedidos = repo.consultarDetallePedidos();
            return ResponseEntity.ok(detallePedidos);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    // 2. Buscar detalles por ID de pedido
@GetMapping("/por-pedido")
    public ResponseEntity<?> buscarPorPedidoId(@RequestParam int pedido_id) {
        try {
            System.out.println("DEBUG: Buscando detalles para pedido_id: " + pedido_id);
            
            List<DetallePedido> detallePedidos = repo.buscarPorPedidoId(pedido_id);
            System.out.println("DEBUG: Resultados encontrados: " + detallePedidos.size());
            
            if (detallePedidos.isEmpty()) {
                System.out.println("DEBUG: No se encontraron detalles para pedido_id: " + pedido_id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron detalles para el pedido con ID: " + pedido_id);
            }
            
            return ResponseEntity.ok(detallePedidos);
        } catch (Exception e) {
            System.err.println("ERROR en buscarPorPedidoId para pedido_id=" + pedido_id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al buscar detalles del pedido: " + e.getMessage());
        }
    }
    
       // 3. Buscar detalles por ID de producto
    @GetMapping("/por-producto")
    public ResponseEntity<?> buscarPorProductoId(@RequestParam int producto_id) {
        try {
            System.out.println("DEBUG: Buscando detalles para producto_id: " + producto_id);
            
            List<DetallePedido> detallePedidos = repo.buscarPorProductoId(producto_id);
            System.out.println("DEBUG: Resultados encontrados: " + detallePedidos.size());
            
            if (detallePedidos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(detallePedidos);
        } catch (Exception e) {
            System.err.println("ERROR en buscarPorProductoId: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
        }
    }
    
    // 4. Crear un nuevo detalle de pedido (MODIFICADO)
// 4. Crear un nuevo detalle de pedido (MODIFICADO)
@PostMapping()
public ResponseEntity<?> crearDetallePedido(
        @RequestParam int cantidad,
        @RequestParam int productos_id,
        @RequestParam int pedidos_id) {
    try {
        System.out.println("DEBUG: Creando detalle pedido - cantidad: " + cantidad + 
                         ", producto_id: " + productos_id + 
                         ", pedido_id: " + pedidos_id);
        
        // Validaciones básicas
        if (cantidad <= 0) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor a 0");
        }
        
        // Ya no validamos el precio aquí, se obtiene del producto
        
        DetallePedido nuevoDetalle = repo.crearDetallePedido(cantidad, productos_id, pedidos_id);
        System.out.println("DEBUG: Detalle creado con ID: " + nuevoDetalle.id());
        return ResponseEntity.ok(nuevoDetalle);
    } catch (Exception e) {
        System.err.println("ERROR en crearDetallePedido: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error al crear detalle de pedido: " + e.getMessage());
    }
}
    // 5. Método adicional: Buscar por rango de precios
    @GetMapping("/por-precio")
    public ResponseEntity<?> buscarPorRangoPrecio(
            @RequestParam double precioMin,
            @RequestParam double precioMax) {
        try {
            System.out.println("DEBUG: Buscando por rango de precio: " + precioMin + " - " + precioMax);
            
            if (precioMin > precioMax) {
                return ResponseEntity.badRequest().body("El precio mínimo no puede ser mayor al máximo");
            }
            
            List<DetallePedido> detallePedidos = repo.buscarPorRangoPrecio(precioMin, precioMax);
            System.out.println("DEBUG: Resultados encontrados: " + detallePedidos.size());
            
            return ResponseEntity.ok(detallePedidos);
        } catch (Exception e) {
            System.err.println("ERROR en buscarPorRangoPrecio: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al buscar por rango de precio: " + e.getMessage());
        }
    }
}