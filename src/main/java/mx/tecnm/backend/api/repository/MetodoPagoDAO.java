package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.MetodoPago;

@Repository
public class MetodoPagoDAO {
    
    @Autowired
    private JdbcClient conexion;
    
    // 1. Consultar todos los métodos de pago
    public List<MetodoPago> consultarMetodosPago() {
        String sql = "SELECT id, nombre FROM metodos_pago ORDER BY id";
        return conexion.sql(sql)
            .query((rs, rowNum) -> new MetodoPago(
                rs.getInt("id"),
                rs.getString("nombre")))
            .list();
    }
    
    // 2. Buscar método de pago por ID
    public MetodoPago buscarPorId(int id) {
        String sql = "SELECT id, nombre FROM metodos_pago WHERE id = ?";
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new MetodoPago(
                rs.getInt("id"),
                rs.getString("nombre")))
            .optional()
            .orElse(null);
    }
    
    // 3. Buscar métodos de pago por nombre (búsqueda parcial)
    public List<MetodoPago> buscarPorNombre(String nombre) {
        String sql = "SELECT id, nombre FROM metodos_pago WHERE nombre ILIKE ? ORDER BY nombre";
        return conexion.sql(sql)
            .param("%" + nombre + "%")
            .query((rs, rowNum) -> new MetodoPago(
                rs.getInt("id"),
                rs.getString("nombre")))
            .list();
    }
    
    // 4. Crear nuevo método de pago
    public MetodoPago crearMetodoPago(MetodoPago metodoPago) {
        String sql = "INSERT INTO metodos_pago (nombre) VALUES (?) RETURNING id, nombre";
        return conexion.sql(sql)
            .param(metodoPago.nombre())
            .query((rs, rowNum) -> new MetodoPago(
                rs.getInt("id"),
                rs.getString("nombre")))
            .single();
    }
    
    // 5. Crear nuevo método de pago (versión con parámetros separados)
    public MetodoPago crearMetodoPago(String nombre) {
        String sql = "INSERT INTO metodos_pago (nombre) VALUES (?) RETURNING id, nombre";
        return conexion.sql(sql)
            .param(nombre)
            .query((rs, rowNum) -> new MetodoPago(
                rs.getInt("id"),
                rs.getString("nombre")))
            .single();
    }
    
    // 6. Actualizar método de pago completo
    public MetodoPago actualizarMetodoPago(int id, MetodoPago metodoPago) {
        String sql = "UPDATE metodos_pago SET nombre = ? WHERE id = ? RETURNING id, nombre";
        return conexion.sql(sql)
            .param(metodoPago.nombre())
            .param(id)
            .query((rs, rowNum) -> new MetodoPago(
                rs.getInt("id"),
                rs.getString("nombre")))
            .optional()
            .orElse(null);
    }
    
    // 7. Actualizar solo el nombre
    public MetodoPago actualizarNombreMetodoPago(int id, String nombre) {
        String sql = "UPDATE metodos_pago SET nombre = ? WHERE id = ? RETURNING id, nombre";
        return conexion.sql(sql)
            .param(nombre)
            .param(id)
            .query((rs, rowNum) -> new MetodoPago(
                rs.getInt("id"),
                rs.getString("nombre")))
            .optional()
            .orElse(null);
    }
    
    // 8. Eliminar método de pago (borrado físico)
    public boolean eliminarMetodoPago(int id) {
        String sql = "DELETE FROM metodos_pago WHERE id = ?";
        int filasEliminadas = conexion.sql(sql)
            .param(id)
            .update();
        return filasEliminadas > 0;
    }
    
    // 9. Eliminar método de pago (versión con soft delete si tuvieras columna activo)
    public boolean desactivarMetodoPago(int id) {
        // Solo si tu tabla tiene columna "activo"
        // String sql = "UPDATE metodos_pago SET activo = FALSE WHERE id = ?";
        // int filasActualizadas = conexion.sql(sql).param(id).update();
        // return filasActualizadas > 0;
        
        // Por ahora solo devolvemos false ya que la tabla no tiene columna activo
        return false;
    }
    
    // 10. Contar métodos de pago
    public int contarMetodosPago() {
        String sql = "SELECT COUNT(*) FROM metodos_pago";
        return conexion.sql(sql)
            .query(Integer.class)
            .single();
    }
    
    // 11. Verificar si existe un método de pago con cierto nombre
    public boolean existeMetodoPago(String nombre) {
        String sql = "SELECT COUNT(*) FROM metodos_pago WHERE nombre = ?";
        Integer count = conexion.sql(sql)
            .param(nombre)
            .query(Integer.class)
            .single();
        return count != null && count > 0;
    }
    
    // 12. Obtener método de pago por nombre exacto
    public MetodoPago buscarPorNombreExacto(String nombre) {
        String sql = "SELECT id, nombre FROM metodos_pago WHERE nombre = ?";
        return conexion.sql(sql)
            .param(nombre)
            .query((rs, rowNum) -> new MetodoPago(
                rs.getInt("id"),
                rs.getString("nombre")))
            .optional()
            .orElse(null);
    }
}
