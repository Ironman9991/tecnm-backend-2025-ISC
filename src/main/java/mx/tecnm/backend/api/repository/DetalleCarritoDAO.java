package mx.tecnm.backend.api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.tecnm.backend.api.models.DetalleCarrito;
import mx.tecnm.backend.api.models.Pedido;

@Repository
public class DetalleCarritoDAO {
    
    @Autowired
    private JdbcClient conexion;
    
    @Autowired
    private ProductoDAO productorepo;

    @Autowired
    private PedidoDAO pedidorepo;

    // ✅ MÉTODO NUEVO: OBTENER TODOS LOS DETALLES DEL CARRITO (sin filtro)
    public List<DetalleCarrito> consultarTodosDetallesCarrito() {
        String sql = """
            SELECT id, cantidad, precio, productos_id, usuarios_id 
            FROM detalles_carrito 
            ORDER BY usuarios_id, productos_id
            """;
        
        return conexion.sql(sql)
            .query((rs, rowNum) -> {
                Long id = rs.getLong("id");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");
                Long productosId = rs.getLong("productos_id");
                Long usuariosId = rs.getLong("usuarios_id");
                
                return new DetalleCarrito(id, cantidad, precio, productosId, usuariosId);
            })
            .list();
    }

    // ✅ MÉTODO NUEVO: OBTENER DETALLES CON INFORMACIÓN DE PRODUCTO Y USUARIO
    public List<Object[]> consultarDetallesCarritoCompletos() {
        String sql = """
            SELECT 
                dc.id,
                dc.cantidad,
                dc.precio,
                dc.productos_id,
                p.nombre as producto_nombre,
                p.sku as producto_sku,
                dc.usuarios_id,
                u.nombre as usuario_nombre,
                u.email as usuario_email,
                dc.fecha_agregado
            FROM detalles_carrito dc
            JOIN productos p ON dc.productos_id = p.id
            JOIN usuarios u ON dc.usuarios_id = u.id
            ORDER BY dc.fecha_agregado DESC
            """;
        
        return conexion.sql(sql)
            .query((rs, rowNum) -> new Object[] {
                rs.getLong("id"),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getLong("productos_id"),
                rs.getString("producto_nombre"),
                rs.getString("producto_sku"),
                rs.getLong("usuarios_id"),
                rs.getString("usuario_nombre"),
                rs.getString("usuario_email"),
                rs.getTimestamp("fecha_agregado")
            })
            .list();
    }

    // ✅ MÉTODO NUEVO: CONTAR TOTAL DE ITEMS EN TODOS LOS CARRITOS
    public int contarTotalItemsCarritos() {
        String sql = "SELECT COUNT(*) FROM detalles_carrito";
        return conexion.sql(sql)
            .query(Integer.class)
            .single();
    }

    // ✅ MÉTODO NUEVO: OBTENER RESUMEN POR USUARIO
    public List<Object[]> obtenerResumenPorUsuario() {
        String sql = """
            SELECT 
                usuarios_id,
                u.nombre as usuario_nombre,
                COUNT(*) as total_items,
                SUM(cantidad) as total_cantidad,
                SUM(precio) as total_precio
            FROM detalles_carrito dc
            JOIN usuarios u ON dc.usuarios_id = u.id
            GROUP BY usuarios_id, u.nombre
            ORDER BY total_precio DESC
            """;
        
        return conexion.sql(sql)
            .query((rs, rowNum) -> new Object[] {
                rs.getLong("usuarios_id"),
                rs.getString("usuario_nombre"),
                rs.getInt("total_items"),
                rs.getInt("total_cantidad"),
                rs.getDouble("total_precio")
            })
            .list();
    }

    // ✅ MÉTODO EXISTENTE: Consultar por usuario (ya lo tienes)
    public List<DetalleCarrito> consultarDetalleCarrito(int usuario_id) {
        String sql = """
            SELECT id, cantidad, precio, productos_id, usuarios_id 
            FROM detalles_carrito 
            WHERE usuarios_id = ?
            ORDER BY id
            """;
        
        return conexion.sql(sql)
            .param(usuario_id)
            .query((rs, rowNum) -> {
                Long id = rs.getLong("id");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");
                Long productosId = rs.getLong("productos_id");
                Long usuariosId = rs.getLong("usuarios_id");
                
                return new DetalleCarrito(id, cantidad, precio, productosId, usuariosId);
            })
            .list();
    }

    // BUSCAR PRODUCTO EN EL CARRITO - CORREGIDO
    public DetalleCarrito BuscarProductoCarrito(int usuario_id, int producto_id) {
        String sql = """
            SELECT id, cantidad, precio, productos_id, usuarios_id 
            FROM detalles_carrito 
            WHERE usuarios_id = ? AND productos_id = ?
            """;
        
        return conexion.sql(sql)
            .param(usuario_id)
            .param(producto_id)
            .query((rs, rowNum) -> {
                Long id = rs.getLong("id");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");
                Long productosId = rs.getLong("productos_id");
                Long usuariosId = rs.getLong("usuarios_id");
                
                return new DetalleCarrito(id, cantidad, precio, productosId, usuariosId);
            })
            .optional()
            .orElse(null);
    }

    // AGREGAR AL CARRITO - CORREGIDO
    @Transactional
    public DetalleCarrito agregarAlCarrito(int usuario_id, int producto_id, int cantidad) {
        // Obtener precio del producto
        var producto = productorepo.busquedaID(producto_id);
        if (producto == null || producto.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }
        
        double precioUnitario = producto.get(0).precio();
        double precioTotal = precioUnitario * cantidad;
        
        String sql = """
            INSERT INTO detalles_carrito (cantidad, precio, productos_id, usuarios_id) 
            VALUES (?, ?, ?, ?) 
            RETURNING id, cantidad, precio, productos_id, usuarios_id
            """;
        
        return conexion.sql(sql)
            .param(cantidad)
            .param(precioTotal)
            .param(producto_id)
            .param(usuario_id)
            .query((rs, rowNum) -> {
                Long id = rs.getLong("id");
                int cant = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");
                Long productosId = rs.getLong("productos_id");
                Long usuariosId = rs.getLong("usuarios_id");
                
                return new DetalleCarrito(id, cant, precio, productosId, usuariosId);
            })
            .single();
    }

    // ACTUALIZAR CANTIDAD EN CARRITO - CORREGIDO
    @Transactional
    public DetalleCarrito actualizarCantidadEnCarrito(Long detalle_carrito_id, int nueva_cantidad) {
        // Primero obtener el detalle actual
        String selectSql = "SELECT productos_id FROM detalles_carrito WHERE id = ?";
        
        Long productoId = conexion.sql(selectSql)
            .param(detalle_carrito_id)
            .query(Long.class)
            .single();
        
        // Obtener precio unitario
        var producto = productorepo.busquedaID(productoId.intValue());
        if (producto == null || producto.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }
        
        double precioUnitario = producto.get(0).precio();
        double nuevoPrecioTotal = precioUnitario * nueva_cantidad;
        
        // Actualizar
        String updateSql = """
            UPDATE detalles_carrito 
            SET cantidad = ?, precio = ? 
            WHERE id = ? 
            RETURNING id, cantidad, precio, productos_id, usuarios_id
            """;
        
        return conexion.sql(updateSql)
            .param(nueva_cantidad)
            .param(nuevoPrecioTotal)
            .param(detalle_carrito_id)
            .query((rs, rowNum) -> {
                Long id = rs.getLong("id");
                int cant = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");
                Long productosId = rs.getLong("productos_id");
                Long usuariosId = rs.getLong("usuarios_id");
                
                return new DetalleCarrito(id, cant, precio, productosId, usuariosId);
            })
            .single();
    }

    // BORRAR DEL CARRITO - CORREGIDO
    @Transactional
    public boolean borrarDelCarrito(int usuario_id, int producto_id) {
        DetalleCarrito detalle = BuscarProductoCarrito(usuario_id, producto_id);
        if (detalle == null) {
            return false;
        }
        
        String sql = "DELETE FROM detalles_carrito WHERE id = ?";
        int filasEliminadas = conexion.sql(sql)
            .param(detalle.id())
            .update();
        
        return filasEliminadas > 0;
    }

   @Transactional
public boolean realizarpedido(int usuario_id, int metodo_pago_id) {
    List<DetalleCarrito> lista = consultarDetalleCarrito(usuario_id);
    
    if (lista.isEmpty()) {
        return false;
    }
    
    // Calcular total
    double totalDouble = lista.stream()
        .mapToDouble(DetalleCarrito::precio)
        .sum();
    BigDecimal total = BigDecimal.valueOf(totalDouble);
    
    // Crear pedido
    Pedido pedidoNuevo = pedidorepo.crearPedido(
        usuario_id,
        metodo_pago_id,
        total
    );
    
    // Crear detalles del pedido
    for (DetalleCarrito detalle : lista) {
        // El precio ya viene calculado desde el carrito (cantidad * precio_unitario)
        String sqldetalle = """
            INSERT INTO detalles_pedidos (cantidad, precio, productos_id, pedidos_id) 
            VALUES (?, ?, ?, ?)
            """;
        
        conexion.sql(sqldetalle)
            .param(detalle.cantidad())
            .param(detalle.precio())
            .param(detalle.productos_id().intValue())
            .param(pedidoNuevo.id())
            .update();
    }
    
    // Vaciar carrito
    String sql = "DELETE FROM detalles_carrito WHERE usuarios_id = ?";
    conexion.sql(sql)
        .param(usuario_id)
        .update();
    
    return true;
}
}