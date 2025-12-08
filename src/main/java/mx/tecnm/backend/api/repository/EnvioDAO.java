package mx.tecnm.backend.api.repository;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Envio;

@Repository
public class EnvioDAO {

    @Autowired
    private JdbcClient conexion;

    // 1. Consultar todos los envíos
    public List<Envio> consultarEnvios() {
        String sql = "SELECT id, pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento FROM envios";
        return conexion.sql(sql)
            .query((rs, rowNum) -> new Envio(
                rs.getInt("id"),
                rs.getInt("pedidos_id"),
                rs.getInt("domicilios_id"),
                rs.getObject("fecha_envio", OffsetDateTime.class),
                rs.getString("estado_envio"),
                rs.getString("numero_seguimiento")))
            .list();
    }

    
    // 2. Buscar envío por ID CON DEBUG
    public Envio buscarPorId(int id) {
        System.out.println("DAO DEBUG: Buscando envío con ID: " + id);
        
        String sql = "SELECT id, pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento FROM envios WHERE id = ?";
        System.out.println("DAO DEBUG: SQL: " + sql);
        System.out.println("DAO DEBUG: Parámetro: id=" + id);
        
        try {
            return conexion.sql(sql)
                .param(id)
                .query((rs, rowNum) -> {
                    System.out.println("DAO DEBUG: Envío encontrado en BD:");
                    System.out.println("  - id: " + rs.getInt("id"));
                    System.out.println("  - pedidos_id: " + rs.getInt("pedidos_id"));
                    System.out.println("  - domicilios_id: " + rs.getInt("domicilios_id"));
                    System.out.println("  - fecha_envio: " + rs.getObject("fecha_envio", OffsetDateTime.class));
                    System.out.println("  - estado_envio: " + rs.getString("estado_envio"));
                    System.out.println("  - numero_seguimiento: " + rs.getString("numero_seguimiento"));
                    
                    return new Envio(
                        rs.getInt("id"),
                        rs.getInt("pedidos_id"),
                        rs.getInt("domicilios_id"),
                        rs.getObject("fecha_envio", OffsetDateTime.class),
                        rs.getString("estado_envio"),
                        rs.getString("numero_seguimiento")
                    );
                })
                .optional()
                .orElse(null);
                
        } catch (Exception e) {
            System.err.println("DAO ERROR en buscarPorId: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
      // 3. Buscar envíos por pedido_id CON DEBUG
    public List<Envio> buscarPorPedidoId(int pedido_id) {
        System.out.println("DAO DEBUG: Buscando envíos para pedido_id: " + pedido_id);
        
        String sql = "SELECT id, pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento FROM envios WHERE pedidos_id = ? ORDER BY fecha_envio DESC";
        
        try {
            List<Envio> resultados = conexion.sql(sql)
                .param(pedido_id)
                .query((rs, rowNum) -> new Envio(
                    rs.getInt("id"),
                    rs.getInt("pedidos_id"),
                    rs.getInt("domicilios_id"),
                    rs.getObject("fecha_envio", OffsetDateTime.class),
                    rs.getString("estado_envio"),
                    rs.getString("numero_seguimiento")))
                .list();
            
            System.out.println("DAO DEBUG: Envíos encontrados para pedido_id " + pedido_id + ": " + resultados.size());
            return resultados;
            
        } catch (Exception e) {
            System.err.println("DAO ERROR en buscarPorPedidoId: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // 4. Buscar envíos por domicilio_id CON DEBUG
    public List<Envio> buscarPorDomicilioId(int domicilio_id) {
        System.out.println("DAO DEBUG: Buscando envíos para domicilios_id: " + domicilio_id);
        
        String sql = "SELECT id, pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento FROM envios WHERE domicilios_id = ? ORDER BY fecha_envio DESC";
        
        try {
            List<Envio> resultados = conexion.sql(sql)
                .param(domicilio_id)
                .query((rs, rowNum) -> new Envio(
                    rs.getInt("id"),
                    rs.getInt("pedidos_id"),
                    rs.getInt("domicilios_id"),
                    rs.getObject("fecha_envio", OffsetDateTime.class),
                    rs.getString("estado_envio"),
                    rs.getString("numero_seguimiento")))
                .list();
            
            System.out.println("DAO DEBUG: Envíos encontrados para domicilios_id " + domicilio_id + ": " + resultados.size());
            return resultados;
            
        } catch (Exception e) {
            System.err.println("DAO ERROR en buscarPorDomicilioId: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // 6. Crear nuevo envío CON DEBUG
    public Envio crearEnvio(Envio envio) {
        System.out.println("DAO DEBUG: Creando nuevo envío...");
        System.out.println("DAO DEBUG: Datos recibidos:");
        System.out.println("  - pedidos_id: " + envio.pedidos_id());
        System.out.println("  - domicilios_id: " + envio.domicilios_id());
        System.out.println("  - fecha_envio: " + envio.fecha_envio());
        System.out.println("  - estado_envio: " + envio.estado_envio());
        System.out.println("  - numero_seguimiento: " + envio.numero_seguimiento());
        
        String sql = """
            INSERT INTO envios (pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento) 
            VALUES (?, ?, ?, ?, ?) 
            RETURNING id, pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento
            """;
        
        System.out.println("DAO DEBUG: SQL a ejecutar: " + sql);
        System.out.println("DAO DEBUG: Parámetros:");
        System.out.println("  - pedidos_id: " + envio.pedidos_id());
        System.out.println("  - domicilios_id: " + envio.domicilios_id()); // ¡CUIDADO! Diferente nombre
        System.out.println("  - fecha_envio: " + (envio.fecha_envio() != null ? envio.fecha_envio() : "null (usará ahora)"));
        System.out.println("  - estado_envio: " + (envio.estado_envio() != null ? envio.estado_envio() : "Pendiente (default)"));
        System.out.println("  - numero_seguimiento: " + envio.numero_seguimiento());
        
        try {
            return conexion.sql(sql)
                .param(envio.pedidos_id())
                .param(envio.domicilios_id()) // Este es el problema potencial
                .param(envio.fecha_envio() != null ? envio.fecha_envio() : OffsetDateTime.now())
                .param(envio.estado_envio() != null ? envio.estado_envio() : "Pendiente")
                .param(envio.numero_seguimiento())
                .query((rs, rowNum) -> {
                    System.out.println("DAO DEBUG: Envío creado exitosamente:");
                    System.out.println("  - Nuevo ID: " + rs.getInt("id"));
                    return new Envio(
                        rs.getInt("id"),
                        rs.getInt("pedidos_id"),
                        rs.getInt("domicilios_id"),
                        rs.getObject("fecha_envio", OffsetDateTime.class),
                        rs.getString("estado_envio"),
                        rs.getString("numero_seguimiento")
                    );
                })
                .single();
                
        } catch (Exception e) {
            System.err.println("DAO ERROR en crearEnvio:");
            System.err.println("  - Mensaje: " + e.getMessage());
            System.err.println("  - Posible causa: Inconsistencia entre domicilios_id (modelo) y domicilios_id (BD)");
            e.printStackTrace();
            throw e;
        }
    }
    // 7. Actualizar envío
    public Envio actualizarEnvio(int id, Envio envio) {
        String sql = """
            UPDATE envios 
            SET pedidos_id = ?, domicilios_id = ?, fecha_envio = ?, estado_envio = ?, numero_seguimiento = ?
            WHERE id = ? 
            RETURNING id, pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento
            """;
        
        return conexion.sql(sql)
            .param(envio.pedidos_id())
            .param(envio.domicilios_id())
            .param(envio.fecha_envio())
            .param(envio.estado_envio())
            .param(envio.numero_seguimiento())
            .param(id)
            .query((rs, rowNum) -> new Envio(
                rs.getInt("id"),
                rs.getInt("pedidos_id"),
                rs.getInt("domicilios_id"),
                rs.getObject("fecha_envio", OffsetDateTime.class),
                rs.getString("estado_envio"),
                rs.getString("numero_seguimiento")))
            .optional()
            .orElse(null);
    }

    // 8. Actualizar solo el estado del envío
    public Envio actualizarEstadoEnvio(int id, String estado) {
        String sql = """
            UPDATE envios 
            SET estado_envio = ?
            WHERE id = ? 
            RETURNING id, pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento
            """;
        
        return conexion.sql(sql)
            .param(estado)
            .param(id)
            .query((rs, rowNum) -> new Envio(
                rs.getInt("id"),
                rs.getInt("pedidos_id"),
                rs.getInt("domicilios_id"),
                rs.getObject("fecha_envio", OffsetDateTime.class),
                rs.getString("estado_envio"),
                rs.getString("numero_seguimiento")))
            .optional()
            .orElse(null);
    }

    // 9. Actualizar solo el número de seguimiento
    public Envio actualizarNumeroSeguimiento(int id, String numero_seguimiento) {
        String sql = """
            UPDATE envios 
            SET numero_seguimiento = ?
            WHERE id = ? 
            RETURNING id, pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento
            """;
        
        return conexion.sql(sql)
            .param(numero_seguimiento)
            .param(id)
            .query((rs, rowNum) -> new Envio(
                rs.getInt("id"),
                rs.getInt("pedidos_id"),
                rs.getInt("domicilios_id"),
                rs.getObject("fecha_envio", OffsetDateTime.class),
                rs.getString("estado_envio"),
                rs.getString("numero_seguimiento")))
            .optional()
            .orElse(null);
    }

    // 10. Eliminar envío
    public boolean eliminarEnvio(int id) {
        String sql = "DELETE FROM envios WHERE id = ?";
        int filasEliminadas = conexion.sql(sql)
            .param(id)
            .update();
        return filasEliminadas > 0;
    }

    // 11. Buscar envíos por rango de fechas
    public List<Envio> buscarPorRangoFechas(String fechaInicio, String fechaFin) {
        String sql = """
            SELECT id, pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento 
            FROM envios 
            WHERE fecha_envio BETWEEN ?::timestamp AND ?::timestamp 
            ORDER BY fecha_envio DESC
            """;
        
        return conexion.sql(sql)
            .param(fechaInicio)
            .param(fechaFin)
            .query((rs, rowNum) -> new Envio(
                rs.getInt("id"),
                rs.getInt("pedidos_id"),
                rs.getInt("domicilios_id"),
                rs.getObject("fecha_envio", OffsetDateTime.class),
                rs.getString("estado_envio"),
                rs.getString("numero_seguimiento")))
            .list();
    }

    // 12. Contar envíos por estado
    public int contarEnviosPorEstado(String estado) {
        String sql = "SELECT COUNT(*) FROM envios WHERE estado_envio = ?";
        return conexion.sql(sql)
            .param(estado)
            .query(Integer.class)
            .single();
    }

    // 13. Obtener envío por número de seguimiento
    public Envio buscarPorNumeroSeguimiento(String numero_seguimiento) {
        String sql = "SELECT id, pedidos_id, domicilios_id, fecha_envio, estado_envio, numero_seguimiento FROM envios WHERE numero_seguimiento = ?";
        return conexion.sql(sql)
            .param(numero_seguimiento)
            .query((rs, rowNum) -> new Envio(
                rs.getInt("id"),
                rs.getInt("pedidos_id"),
                rs.getInt("domicilios_id"),
                rs.getObject("fecha_envio", OffsetDateTime.class),
                rs.getString("estado_envio"),
                rs.getString("numero_seguimiento")))
            .optional()
            .orElse(null);
    }
}