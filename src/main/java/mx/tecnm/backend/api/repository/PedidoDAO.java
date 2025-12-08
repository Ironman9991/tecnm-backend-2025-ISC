package mx.tecnm.backend.api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Pedido;

@Repository
public class PedidoDAO {

    @Autowired
    private JdbcClient conexion;

    // CONSULTAR TODOS
    public List<Pedido> Consultarpedido() {
        String sql = "SELECT id, usuarios_id, metodos_pago_id, fecha, total, numero_envio, importe_envio, importe_iva, importe_productos FROM pedidos ORDER BY fecha DESC";
        return conexion.sql(sql)
            .query((rs, rowNum) -> new Pedido(
                rs.getInt("id"),
                rs.getInt("usuarios_id"),
                rs.getInt("metodos_pago_id"),
                rs.getTimestamp("fecha"),
                rs.getBigDecimal("total"),
                rs.getString("numero_envio"),
                rs.getBigDecimal("importe_envio"),
                rs.getBigDecimal("importe_iva"),
                rs.getBigDecimal("importe_productos")))
            .list();
    }

    // CONSULTAR POR ID
    public Pedido ConsultarpedidoPorID(int id) {
        String sql = "SELECT id, usuarios_id, metodos_pago_id, fecha, total, numero_envio, importe_envio, importe_iva, importe_productos FROM pedidos WHERE id = ?";
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new Pedido(
                rs.getInt("id"),
                rs.getInt("usuarios_id"),
                rs.getInt("metodos_pago_id"),
                rs.getTimestamp("fecha"),
                rs.getBigDecimal("total"),
                rs.getString("numero_envio"),
                rs.getBigDecimal("importe_envio"),
                rs.getBigDecimal("importe_iva"),
                rs.getBigDecimal("importe_productos")))
            .single();
    }

    // CREAR PEDIDO BÁSICO
    public Pedido crearPedido(int usuarios_id, int metodos_pago_id, BigDecimal total) {
        String sql = "INSERT INTO pedidos (usuarios_id, metodos_pago_id, total, fecha, numero_envio) VALUES (?, ?, ?, CURRENT_TIMESTAMP, gen_random_uuid()) RETURNING id, usuarios_id, metodos_pago_id, fecha, total, numero_envio, importe_envio, importe_iva, importe_productos";
        
        return conexion.sql(sql)
            .param(usuarios_id)
            .param(metodos_pago_id)
            .param(total)
            .query((rs, rowNum) -> new Pedido(
                rs.getInt("id"),
                rs.getInt("usuarios_id"),
                rs.getInt("metodos_pago_id"),
                rs.getTimestamp("fecha"),
                rs.getBigDecimal("total"),
                rs.getString("numero_envio"),
                rs.getBigDecimal("importe_envio"),
                rs.getBigDecimal("importe_iva"),
                rs.getBigDecimal("importe_productos")))
            .single();
    }

    // CREAR PEDIDO COMPLETO
    public Pedido crearPedidoCompleto(int usuarios_id, int metodos_pago_id, BigDecimal total, 
                                      BigDecimal importe_envio, BigDecimal importe_iva, BigDecimal importe_productos) {
        String sql = "INSERT INTO pedidos (usuarios_id, metodos_pago_id, total, importe_envio, importe_iva, importe_productos, fecha, numero_envio) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, gen_random_uuid()) RETURNING id, usuarios_id, metodos_pago_id, fecha, total, numero_envio, importe_envio, importe_iva, importe_productos";
        
        return conexion.sql(sql)
            .param(usuarios_id)
            .param(metodos_pago_id)
            .param(total != null ? total : BigDecimal.ZERO)
            .param(importe_envio != null ? importe_envio : BigDecimal.ZERO)
            .param(importe_iva != null ? importe_iva : BigDecimal.ZERO)
            .param(importe_productos != null ? importe_productos : total)
            .query((rs, rowNum) -> new Pedido(
                rs.getInt("id"),
                rs.getInt("usuarios_id"),
                rs.getInt("metodos_pago_id"),
                rs.getTimestamp("fecha"),
                rs.getBigDecimal("total"),
                rs.getString("numero_envio"),
                rs.getBigDecimal("importe_envio"),
                rs.getBigDecimal("importe_iva"),
                rs.getBigDecimal("importe_productos")))
            .single();
    }

    // ACTUALIZAR PEDIDO
    public Pedido actualizarPedido(int id, BigDecimal total, BigDecimal importe_envio, BigDecimal importe_iva, BigDecimal importe_productos) {
        // Construir consulta dinámica
        StringBuilder sqlBuilder = new StringBuilder("UPDATE pedidos SET ");
        boolean tieneCambios = false;
        
        if (total != null) {
            sqlBuilder.append("total = ?");
            tieneCambios = true;
        }
        
        if (importe_envio != null) {
            if (tieneCambios) sqlBuilder.append(", ");
            sqlBuilder.append("importe_envio = ?");
            tieneCambios = true;
        }
        
        if (importe_iva != null) {
            if (tieneCambios) sqlBuilder.append(", ");
            sqlBuilder.append("importe_iva = ?");
            tieneCambios = true;
        }
        
        if (importe_productos != null) {
            if (tieneCambios) sqlBuilder.append(", ");
            sqlBuilder.append("importe_productos = ?");
            tieneCambios = true;
        }
        
        if (!tieneCambios) {
            // No hay cambios, devolver el pedido actual
            return ConsultarpedidoPorID(id);
        }
        
        sqlBuilder.append(" WHERE id = ? RETURNING id, usuarios_id, metodos_pago_id, fecha, total, numero_envio, importe_envio, importe_iva, importe_productos");
        
        JdbcClient.StatementSpec statement = conexion.sql(sqlBuilder.toString());
        int paramIndex = 1;
        
        if (total != null) {
            statement = statement.param(paramIndex++, total);
        }
        if (importe_envio != null) {
            statement = statement.param(paramIndex++, importe_envio);
        }
        if (importe_iva != null) {
            statement = statement.param(paramIndex++, importe_iva);
        }
        if (importe_productos != null) {
            statement = statement.param(paramIndex++, importe_productos);
        }
        
        statement = statement.param(paramIndex, id);
        
        return statement
            .query((rs, rowNum) -> new Pedido(
                rs.getInt("id"),
                rs.getInt("usuarios_id"),
                rs.getInt("metodos_pago_id"),
                rs.getTimestamp("fecha"),
                rs.getBigDecimal("total"),
                rs.getString("numero_envio"),
                rs.getBigDecimal("importe_envio"),
                rs.getBigDecimal("importe_iva"),
                rs.getBigDecimal("importe_productos")))
            .single();
    }

    // ACTUALIZAR MÉTODO DE PAGO
    public Pedido actualizarMetodoPago(int id, int metodos_pago_id) {
        String sql = "UPDATE pedidos SET metodos_pago_id = ? WHERE id = ? RETURNING id, usuarios_id, metodos_pago_id, fecha, total, numero_envio, importe_envio, importe_iva, importe_productos";
        
        return conexion.sql(sql)
            .param(metodos_pago_id)
            .param(id)
            .query((rs, rowNum) -> new Pedido(
                rs.getInt("id"),
                rs.getInt("usuarios_id"),
                rs.getInt("metodos_pago_id"),
                rs.getTimestamp("fecha"),
                rs.getBigDecimal("total"),
                rs.getString("numero_envio"),
                rs.getBigDecimal("importe_envio"),
                rs.getBigDecimal("importe_iva"),
                rs.getBigDecimal("importe_productos")))
            .single();
    }

    // ELIMINAR PEDIDO
    public List<Pedido> eliminarPedido(int id) {
        String sql = "DELETE FROM pedidos WHERE id = ? RETURNING id, usuarios_id, metodos_pago_id, fecha, total, numero_envio, importe_envio, importe_iva, importe_productos";
        
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new Pedido(
                rs.getInt("id"),
                rs.getInt("usuarios_id"),
                rs.getInt("metodos_pago_id"),
                rs.getTimestamp("fecha"),
                rs.getBigDecimal("total"),
                rs.getString("numero_envio"),
                rs.getBigDecimal("importe_envio"),
                rs.getBigDecimal("importe_iva"),
                rs.getBigDecimal("importe_productos")))
            .list();
    }

    // BUSCAR POR USUARIO
    public List<Pedido> buscarPorUsuario(int usuarios_id) {
        String sql = "SELECT id, usuarios_id, metodos_pago_id, fecha, total, numero_envio, importe_envio, importe_iva, importe_productos FROM pedidos WHERE usuarios_id = ? ORDER BY fecha DESC";
        
        return conexion.sql(sql)
            .param(usuarios_id)
            .query((rs, rowNum) -> new Pedido(
                rs.getInt("id"),
                rs.getInt("usuarios_id"),
                rs.getInt("metodos_pago_id"),
                rs.getTimestamp("fecha"),
                rs.getBigDecimal("total"),
                rs.getString("numero_envio"),
                rs.getBigDecimal("importe_envio"),
                rs.getBigDecimal("importe_iva"),
                rs.getBigDecimal("importe_productos")))
            .list();
    }

    // OBTENER PEDIDOS RECIENTES
    public List<Pedido> obtenerPedidosRecientes() {
        String sql = "SELECT id, usuarios_id, metodos_pago_id, fecha, total, numero_envio, importe_envio, importe_iva, importe_productos FROM pedidos ORDER BY fecha DESC LIMIT 10";
        
        return conexion.sql(sql)
            .query((rs, rowNum) -> new Pedido(
                rs.getInt("id"),
                rs.getInt("usuarios_id"),
                rs.getInt("metodos_pago_id"),
                rs.getTimestamp("fecha"),
                rs.getBigDecimal("total"),
                rs.getString("numero_envio"),
                rs.getBigDecimal("importe_envio"),
                rs.getBigDecimal("importe_iva"),
                rs.getBigDecimal("importe_productos")))
            .list();
    }

    // OBTENER POR RANGO DE FECHAS
// Método CORREGIDO en PedidoDAO.java
public List<Pedido> obtenerPorRangoFechas(String fecha_inicio, String fecha_fin) {
    String sql = """
        SELECT id, usuarios_id, metodos_pago_id, fecha, total, numero_envio, 
               importe_envio, importe_iva, importe_productos 
        FROM pedidos 
        WHERE fecha >= ?::timestamp AND fecha <= ?::timestamp + interval '1 day'
        ORDER BY fecha DESC
        """;
    
    return conexion.sql(sql)
        .param(fecha_inicio + " 00:00:00")  // Inicio del día
        .param(fecha_fin + " 23:59:59")     // Fin del día
        .query((rs, rowNum) -> new Pedido(
            rs.getInt("id"),
            rs.getInt("usuarios_id"),
            rs.getInt("metodos_pago_id"),
            rs.getTimestamp("fecha"),
            rs.getBigDecimal("total"),
            rs.getString("numero_envio"),
            rs.getBigDecimal("importe_envio"),
            rs.getBigDecimal("importe_iva"),
            rs.getBigDecimal("importe_productos")))
        .list();
}
    // OBTENER TOTAL DE VENTAS
    public BigDecimal obtenerTotalVentas() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM pedidos";
        
        return conexion.sql(sql)
            .query(BigDecimal.class)
            .single();
    }
}