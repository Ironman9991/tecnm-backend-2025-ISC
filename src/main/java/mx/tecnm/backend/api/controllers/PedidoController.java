package mx.tecnm.backend.api.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.tecnm.backend.api.models.Pedido;
import mx.tecnm.backend.api.repository.PedidoDAO;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    PedidoDAO repo;

    // GET - Consultar todos los pedidos
    @GetMapping()
    public ResponseEntity<List<Pedido>> Consultarpedido() {
        List<Pedido> pedidos = repo.Consultarpedido();
        return ResponseEntity.ok(pedidos);
    }

    // GET - Buscar pedido por ID
    @GetMapping("/buscarid")
    public ResponseEntity<Pedido> buscarPorId(@RequestParam int id) {
        Pedido pedido = repo.ConsultarpedidoPorID(id);
        return ResponseEntity.ok(pedido);
    }

    // GET - Buscar pedidos por usuario
    @GetMapping("/buscarusuario")
    public ResponseEntity<List<Pedido>> buscarPorUsuario(@RequestParam int usuarios_id) {
        List<Pedido> pedidos = repo.buscarPorUsuario(usuarios_id);
        return ResponseEntity.ok(pedidos);
    }

    // POST - Crear pedido básico
    @PostMapping()
    public ResponseEntity<Pedido> crearPedido(
            @RequestParam int usuarios_id,
            @RequestParam int metodos_pago_id,
            @RequestParam BigDecimal total) {
        
        Pedido pedidoCreado = repo.crearPedido(usuarios_id, metodos_pago_id, total);
        return ResponseEntity.ok(pedidoCreado);
    }

    // POST - Crear pedido completo (versión con double para compatibilidad)
    @PostMapping("/completo")
    public ResponseEntity<Pedido> crearPedidoCompleto(
            @RequestParam int usuarios_id,
            @RequestParam int metodos_pago_id,
            @RequestParam double total,
            @RequestParam(required = false) Double importe_envio,
            @RequestParam(required = false) Double importe_iva,
            @RequestParam(required = false) Double importe_productos) {
        
        // Convertir double a BigDecimal
        BigDecimal totalBD = BigDecimal.valueOf(total);
        BigDecimal importe_envioBD = importe_envio != null ? BigDecimal.valueOf(importe_envio) : null;
        BigDecimal importe_ivaBD = importe_iva != null ? BigDecimal.valueOf(importe_iva) : null;
        BigDecimal importe_productosBD = importe_productos != null ? BigDecimal.valueOf(importe_productos) : null;
        
        Pedido pedidoCreado = repo.crearPedidoCompleto(
            usuarios_id, metodos_pago_id, totalBD, 
            importe_envioBD, importe_ivaBD, importe_productosBD
        );
        return ResponseEntity.ok(pedidoCreado);
    }

    // PUT - Actualizar pedido (versión con double)
    @PutMapping()
    public ResponseEntity<Pedido> actualizarPedido(
            @RequestParam int id,
            @RequestParam(required = false) Double total,
            @RequestParam(required = false) Double importe_envio,
            @RequestParam(required = false) Double importe_iva,
            @RequestParam(required = false) Double importe_productos) {
        
        // Convertir double a BigDecimal
        BigDecimal totalBD = total != null ? BigDecimal.valueOf(total) : null;
        BigDecimal importe_envioBD = importe_envio != null ? BigDecimal.valueOf(importe_envio) : null;
        BigDecimal importe_ivaBD = importe_iva != null ? BigDecimal.valueOf(importe_iva) : null;
        BigDecimal importe_productosBD = importe_productos != null ? BigDecimal.valueOf(importe_productos) : null;
        
        Pedido pedidoActualizado = repo.actualizarPedido(id, totalBD, importe_envioBD, importe_ivaBD, importe_productosBD);
        return ResponseEntity.ok(pedidoActualizado);
    }

    // PUT - Actualizar método de pago
    @PutMapping("/actualizarpago")
    public ResponseEntity<Pedido> actualizarMetodoPago(
            @RequestParam int id,
            @RequestParam int metodos_pago_id) {
        
        Pedido pedidoActualizado = repo.actualizarMetodoPago(id, metodos_pago_id);
        return ResponseEntity.ok(pedidoActualizado);
    }

    // DELETE - Eliminar pedido
    @DeleteMapping()
    public ResponseEntity<List<Pedido>> eliminarPedido(@RequestParam int id) {
        List<Pedido> pedidoEliminado = repo.eliminarPedido(id);
        return ResponseEntity.ok(pedidoEliminado);
    }

    // GET - Pedidos recientes
    @GetMapping("/recientes")
    public ResponseEntity<List<Pedido>> obtenerRecientes() {
        List<Pedido> pedidos = repo.obtenerPedidosRecientes();
        return ResponseEntity.ok(pedidos);
    }

    // GET - Total de ventas
    @GetMapping("/totalventas")
    public ResponseEntity<BigDecimal> obtenerTotalVentas() {
        BigDecimal totalVentas = repo.obtenerTotalVentas();
        return ResponseEntity.ok(totalVentas);
    }

    // GET - Pedidos por rango de fechas
@GetMapping("/porfecha")
public ResponseEntity<?> obtenerPorFecha(
        @RequestParam String fecha_inicio,
        @RequestParam String fecha_fin) {
    
    try {
        // Validar formato de fechas (opcional, pero recomendado)
        if (!fecha_inicio.matches("\\d{4}-\\d{2}-\\d{2}") || 
            !fecha_fin.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.badRequest()
                .body("Formato de fecha inválido. Use YYYY-MM-DD");
        }
        
        List<Pedido> pedidos = repo.obtenerPorRangoFechas(fecha_inicio, fecha_fin);
        return ResponseEntity.ok(pedidos);
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body("Error al filtrar por fechas: " + e.getMessage());
    }
}
}