package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.DetallePedido;

@Repository
public class DetallePedidoDAO {

    @Autowired
    private JdbcClient conexion;
    
    @Autowired
    private ProductoDAO productorepo; // Asegúrate de tener este DAO inyectado

    // 1. Obtener todos los detalles de pedidos
    public List<DetallePedido> consultarDetallePedidos() {
        String sql = "SELECT id, cantidad, precio, productos_id, pedidos_id FROM detalles_pedidos";
        
        return conexion.sql(sql)
            .query((rs, rowNum) -> new DetallePedido(
                rs.getInt("id"),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getInt("productos_id"),
                rs.getInt("pedidos_id")))
            .list();
    }
    
    // 2. Buscar detalles por ID de pedido
    public List<DetallePedido> buscarPorPedidoId(int pedido_id) {
        System.out.println("DAO DEBUG: Buscando detalles para pedido_id: " + pedido_id);
        
        String sql = "SELECT id, cantidad, precio, productos_id, pedidos_id FROM detalles_pedidos WHERE pedidos_id = ?";
        
        try {
            List<DetallePedido> resultados = conexion.sql(sql)
                .param(pedido_id)
                .query((rs, rowNum) -> new DetallePedido(
                    rs.getInt("id"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio"),
                    rs.getInt("productos_id"),
                    rs.getInt("pedidos_id")))
                .list();
            
            System.out.println("DAO DEBUG: Resultados encontrados: " + resultados.size());
            return resultados;
        } catch (Exception e) {
            System.err.println("DAO ERROR en buscarPorPedidoId: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // 3. Buscar detalles por ID de producto
    public List<DetallePedido> buscarPorProductoId(int producto_id) {
        String sql = "SELECT id, cantidad, precio, productos_id, pedidos_id FROM detalles_pedidos WHERE productos_id = ?";
        
        return conexion.sql(sql)
            .param(producto_id)
            .query((rs, rowNum) -> new DetallePedido(
                rs.getInt("id"),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getInt("productos_id"),
                rs.getInt("pedidos_id")))
            .list();
    }
    
    // 4. Crear un nuevo detalle de pedido (MODIFICADO PARA OBTENER PRECIO DEL PRODUCTO)
    public DetallePedido crearDetallePedido(int cantidad, int productos_id, int pedidos_id) {
        System.out.println("DAO DEBUG: Creando detalle pedido - cantidad: " + cantidad + 
                         ", producto_id: " + productos_id + ", pedido_id: " + pedidos_id);
        
        // Obtener precio del producto
        var producto = productorepo.busquedaID(productos_id);
        if (producto == null || producto.isEmpty()) {
            throw new RuntimeException("Producto no encontrado con ID: " + productos_id);
        }
        
        double precioUnitario = producto.get(0).precio();
        double precioTotal = precioUnitario * cantidad;
        
        System.out.println("DAO DEBUG: Precio unitario: " + precioUnitario + ", Precio total: " + precioTotal);
        
        String sql = "INSERT INTO detalles_pedidos (cantidad, precio, productos_id, pedidos_id) VALUES (?, ?, ?, ?) RETURNING id, cantidad, precio, productos_id, pedidos_id";
        
        return conexion.sql(sql)
            .param(cantidad)
            .param(precioTotal)
            .param(productos_id)
            .param(pedidos_id)
            .query((rs, rowNum) -> new DetallePedido(
                rs.getInt("id"),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getInt("productos_id"),
                rs.getInt("pedidos_id")))
            .single();
    }
    
    // 5. Método adicional: Buscar por rango de precios
    public List<DetallePedido> buscarPorRangoPrecio(double precioMin, double precioMax) {
        String sql = "SELECT id, cantidad, precio, productos_id, pedidos_id FROM detalles_pedidos WHERE precio BETWEEN ? AND ? ORDER BY precio";
        
        return conexion.sql(sql)
            .param(precioMin)
            .param(precioMax)
            .query((rs, rowNum) -> new DetallePedido(
                rs.getInt("id"),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getInt("productos_id"),
                rs.getInt("pedidos_id")))
            .list();
    }
    
    // 6. Método adicional: Obtener el total de un pedido específico
    public double obtenerTotalPorPedido(int pedido_id) {
        String sql = "SELECT SUM(precio) as total FROM detalles_pedidos WHERE pedidos_id = ?";
        
        return conexion.sql(sql)
            .param(pedido_id)
            .query(Double.class)
            .single();
    }
    
    // 7. Método adicional: Contar productos vendidos por producto_id
    public int contarCantidadVendidaPorProducto(int producto_id) {
        String sql = "SELECT SUM(cantidad) as total_vendido FROM detalles_pedidos WHERE productos_id = ?";
        
        Integer resultado = conexion.sql(sql)
            .param(producto_id)
            .query(Integer.class)
            .optional()
            .orElse(0);
            
        return resultado != null ? resultado : 0;
    }
    
    // 8. Método adicional: Obtener detalles con información de producto
    public List<DetallePedido> buscarDetallesConProducto(int pedido_id) {
        String sql = """
            SELECT dp.id, dp.cantidad, dp.precio, dp.productos_id, dp.pedidos_id 
            FROM detalles_pedidos dp
            WHERE dp.pedidos_id = ?
            ORDER BY dp.id
            """;
        
        return conexion.sql(sql)
            .param(pedido_id)
            .query((rs, rowNum) -> new DetallePedido(
                rs.getInt("id"),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getInt("productos_id"),
                rs.getInt("pedidos_id")))
            .list();
    }
    
    // 9. Método para eliminar un detalle de pedido
    public boolean eliminarDetallePedido(int id) {
        String sql = "DELETE FROM detalles_pedidos WHERE id = ?";
        
        int filasEliminadas = conexion.sql(sql)
            .param(id)
            .update();
            
        return filasEliminadas > 0;
    }
    
    // 10. Método para actualizar un detalle de pedido (MODIFICADO PARA OBTENER PRECIO DEL PRODUCTO)
    public DetallePedido actualizarDetallePedido(int id, int nuevaCantidad) {
        System.out.println("DAO DEBUG: Actualizando detalle pedido ID: " + id + ", nueva cantidad: " + nuevaCantidad);
        
        // Primero obtener el producto_id del detalle
        String selectSql = "SELECT productos_id FROM detalles_pedidos WHERE id = ?";
        Integer productoId = conexion.sql(selectSql)
            .param(id)
            .query(Integer.class)
            .optional()
            .orElse(null);
            
        if (productoId == null) {
            throw new RuntimeException("Detalle de pedido no encontrado con ID: " + id);
        }
        
        // Obtener precio del producto
        var producto = productorepo.busquedaID(productoId);
        if (producto == null || producto.isEmpty()) {
            throw new RuntimeException("Producto no encontrado con ID: " + productoId);
        }
        
        double precioUnitario = producto.get(0).precio();
        double nuevoPrecioTotal = precioUnitario * nuevaCantidad;
        
        System.out.println("DAO DEBUG: Nuevo precio total: " + nuevoPrecioTotal);
        
        String updateSql = "UPDATE detalles_pedidos SET cantidad = ?, precio = ? WHERE id = ? RETURNING id, cantidad, precio, productos_id, pedidos_id";
        
        return conexion.sql(updateSql)
            .param(nuevaCantidad)
            .param(nuevoPrecioTotal)
            .param(id)
            .query((rs, rowNum) -> new DetallePedido(
                rs.getInt("id"),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getInt("productos_id"),
                rs.getInt("pedidos_id")))
            .single();
    }
}