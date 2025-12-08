package mx.tecnm.backend.api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.tecnm.backend.api.models.DetalleCarrito;
import mx.tecnm.backend.api.repository.DetalleCarritoDAO;

@RestController
@RequestMapping("/detallecarrito")
public class DetalleCarritoController {

    @Autowired
    DetalleCarritoDAO repo;

    // ✅ ENDPOINT NUEVO: Obtener todos los detalles del carrito (sin filtro)
    @GetMapping("/todos")
    public ResponseEntity<?> consultarTodosLosCarritos() {
        try {
            System.out.println("Consultando TODOS los detalles del carrito");
            List<DetalleCarrito> detalles = repo.consultarTodosDetallesCarrito();
            System.out.println("Total de registros encontrados: " + detalles.size());
            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            System.err.println("ERROR en consultarTodosLosCarritos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al consultar todos los carritos: " + e.getMessage());
        }
    }

    // ✅ ENDPOINT NUEVO: Obtener detalles completos con información de productos y usuarios
    @GetMapping("/completos")
    public ResponseEntity<?> consultarCarritosCompletos() {
        try {
            System.out.println("Consultando carritos con información completa");
            List<Object[]> detallesCompletos = repo.consultarDetallesCarritoCompletos();
            
            // Convertir a una estructura más legible
            List<Map<String, Object>> resultado = detallesCompletos.stream()
                .map(detalle -> Map.of(
                    "id", detalle[0],
                    "cantidad", detalle[1],
                    "precio", detalle[2],
                    "producto_id", detalle[3],
                    "producto_nombre", detalle[4],
                    "producto_sku", detalle[5],
                    "usuario_id", detalle[6],
                    "usuario_nombre", detalle[7],
                    "usuario_email", detalle[8],
                    "fecha_agregado", detalle[9]
                ))
                .toList();
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            System.err.println("ERROR en consultarCarritosCompletos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al consultar carritos completos: " + e.getMessage());
        }
    }

    // ✅ ENDPOINT NUEVO: Obtener resumen por usuario
    @GetMapping("/resumen")
    public ResponseEntity<?> obtenerResumenCarritos() {
        try {
            System.out.println("Obteniendo resumen de carritos por usuario");
            List<Object[]> resumen = repo.obtenerResumenPorUsuario();
            
            List<Map<String, Object>> resultado = resumen.stream()
                .map(item -> Map.of(
                    "usuario_id", item[0],
                    "usuario_nombre", item[1],
                    "total_items", item[2],
                    "total_cantidad", item[3],
                    "total_precio", item[4]
                ))
                .toList();
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            System.err.println("ERROR en obtenerResumenCarritos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener resumen: " + e.getMessage());
        }
    }

    // ✅ ENDPOINT NUEVO: Contar total de items en todos los carritos
    @GetMapping("/contar")
    public ResponseEntity<?> contarItemsCarritos() {
        try {
            int total = repo.contarTotalItemsCarritos();
            return ResponseEntity.ok(Map.of(
                "total_items", total,
                "mensaje", "Total de items en todos los carritos: " + total
            ));
        } catch (Exception e) {
            System.err.println("ERROR en contarItemsCarritos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al contar items: " + e.getMessage());
        }
    }

    // ✅ ENDPOINT EXISTENTE: Consultar carrito de un usuario específico
    @GetMapping
    public ResponseEntity<?> consultarCarrito(@RequestParam int usuario_id) {
        try {
            System.out.println("Consultando carrito para usuario_id: " + usuario_id);
            List<DetalleCarrito> detalleCarritos = repo.consultarDetalleCarrito(usuario_id);
            System.out.println("Encontrados " + detalleCarritos.size() + " items en el carrito");
            return ResponseEntity.ok(detalleCarritos);
        } catch (Exception e) {
            System.err.println("ERROR en consultarCarrito: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al consultar carrito: " + e.getMessage());
        }
    }
    @PostMapping()
    public ResponseEntity<?> agregarAlCarrito(
            @RequestParam int usuario_id, 
            @RequestParam int producto_id) {
        try {
            System.out.println("Agregando producto al carrito - usuario_id: " + usuario_id + ", producto_id: " + producto_id);
            
            DetalleCarrito busqueda = repo.BuscarProductoCarrito(usuario_id, producto_id);
            DetalleCarrito resultado = null;
            
            if (busqueda == null) {
                System.out.println("Producto no encontrado en carrito, agregando nuevo");
                resultado = repo.agregarAlCarrito(usuario_id, producto_id, 1);
            } else {
                System.out.println("Producto encontrado, actualizando cantidad");
                resultado = repo.actualizarCantidadEnCarrito(busqueda.id(), busqueda.cantidad() + 1);
            }

            System.out.println("Operación exitosa: " + resultado);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            System.err.println("ERROR en agregarAlCarrito: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al agregar al carrito: " + e.getMessage());
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> borrarDelCarrito(
            @RequestParam int usuario_id, 
            @RequestParam int producto_id) {
        try {
            System.out.println("Borrando producto del carrito - usuario_id: " + usuario_id + ", producto_id: " + producto_id);
            
            boolean exito = repo.borrarDelCarrito(usuario_id, producto_id);
            if (exito) {
                System.out.println("Producto borrado exitosamente");
                return ResponseEntity.noContent().build();
            } else {
                System.out.println("Producto no encontrado en carrito");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("ERROR en borrarDelCarrito: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al borrar del carrito: " + e.getMessage());
        }
    }

    @PostMapping("/realizarpedido")
    public ResponseEntity<?> realizarPedido(
            @RequestParam int usuario_id, 
            @RequestParam int metodo_pago_id) {
        try {
            System.out.println("Realizando pedido - usuario_id: " + usuario_id + ", metodo_pago_id: " + metodo_pago_id);
            
            boolean exito = repo.realizarpedido(usuario_id, metodo_pago_id);
            if (exito) {
                System.out.println("Pedido realizado exitosamente");
                return ResponseEntity.noContent().build();
            } else {
                System.out.println("Carrito vacío, no se puede realizar pedido");
                return ResponseEntity.badRequest().body("El carrito está vacío");
            }
        } catch (Exception e) {
            System.err.println("ERROR en realizarPedido: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al realizar pedido: " + e.getMessage());
        }
    }
}