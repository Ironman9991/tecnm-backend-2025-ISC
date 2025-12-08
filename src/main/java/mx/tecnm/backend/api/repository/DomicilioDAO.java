package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Domicilio;

@Repository
public class DomicilioDAO {

    @Autowired
    private JdbcClient conexion;

    // 1. Consultar todos los domicilios
    public List<Domicilio> consultarDomicilios() {
        String sql = "SELECT id, calle, numero, colonia,cp, estado, ciudad, usuarios_id FROM domicilios";
        return conexion.sql(sql)
            .query((rs, rowNum) -> new Domicilio(
                rs.getInt("id"),
                rs.getString("calle"),
                rs.getString("numero"),
                rs.getString("colonia"),
                rs.getString("cp"), // Mapeado desde "eg"
                rs.getString("estado"),
                rs.getString("ciudad"),
                rs.getInt("usuarios_id")))
            .list();
    }

    // 2. Buscar domicilio por ID
    public Domicilio buscarPorId(int id) {
        String sql = "SELECT id, calle, numero, colonia, cp, estado, ciudad, usuarios_id FROM domicilios WHERE id = ?";
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new Domicilio(
                rs.getInt("id"),
                rs.getString("calle"),
                rs.getString("numero"),
                rs.getString("colonia"),
                rs.getString("cp"),
                rs.getString("estado"),
                rs.getString("ciudad"),
                rs.getInt("usuarios_id")))
            .optional()
            .orElse(null);
    }

    // 3. Buscar domicilios por usuario_id
    public List<Domicilio> buscarPorUsuarioId(int usuario_id) {
        String sql = "SELECT id, calle, numero, colonia, cp, estado, ciudad, usuarios_id FROM domicilios WHERE usuarios_id = ? ORDER BY id";
        return conexion.sql(sql)
            .param(usuario_id)
            .query((rs, rowNum) -> new Domicilio(
                rs.getInt("id"),
                rs.getString("calle"),
                rs.getString("numero"),
                rs.getString("colonia"),
                rs.getString("cp"),
                rs.getString("estado"),
                rs.getString("ciudad"),
                rs.getInt("usuarios_id")))
            .list();
    }

    // 4. Crear nuevo domicilio
    public Domicilio crearDomicilio(Domicilio domicilio) {
        String sql = """
            INSERT INTO domicilios (calle, numero, colonia, cp, estado, ciudad, usuarios_id) 
            VALUES (?, ?, ?, ?, ?, ?, ?) 
            RETURNING id, calle, numero, colonia,  cp, estado, ciudad, usuarios_id
            """;
        
        return conexion.sql(sql)
            .param(domicilio.calle())
            .param(domicilio.numero())
            .param(domicilio.colonia())
            .param(domicilio.cp()) // Se guarda como "eg" en la BD
            .param(domicilio.estado())
            .param(domicilio.ciudad())
            .param(domicilio.usuarios_id())
            .query((rs, rowNum) -> new Domicilio(
                rs.getInt("id"),
                rs.getString("calle"),
                rs.getString("numero"),
                rs.getString("colonia"),
                rs.getString("cp"),
                rs.getString("estado"),
                rs.getString("ciudad"),
                rs.getInt("usuarios_id")))
            .single();
    }

    // 5. Actualizar domicilio
    public Domicilio actualizarDomicilio(int id, Domicilio domicilio) {
        String sql = """
            UPDATE domicilios 
            SET calle = ?, numero = ?, colonia = ?, cp = ?, estado = ?, ciudad = ?, usuarios_id = ?
            WHERE id = ? 
            RETURNING id, calle, numero, colonia, cp, estado, ciudad, usuarios_id
            """;
        
        return conexion.sql(sql)
            .param(domicilio.calle())
            .param(domicilio.numero())
            .param(domicilio.colonia())
            .param(domicilio.cp())
            .param(domicilio.estado())
            .param(domicilio.ciudad())
            .param(domicilio.usuarios_id())
            .param(id)
            .query((rs, rowNum) -> new Domicilio(
                rs.getInt("id"),
                rs.getString("calle"),
                rs.getString("numero"),
                rs.getString("colonia"),
                rs.getString("cp"),
                rs.getString("estado"),
                rs.getString("ciudad"),
                rs.getInt("usuarios_id")))
            .optional()
            .orElse(null);
    }

    // 6. Eliminar domicilio
    public boolean eliminarDomicilio(int id) {
        String sql = "DELETE FROM domicilios WHERE id = ?";
        int filasEliminadas = conexion.sql(sql)
            .param(id)
            .update();
        return filasEliminadas > 0;
    }

    // 7. Buscar domicilios por ciudad
    public List<Domicilio> buscarPorCiudad(String ciudad) {
        String sql = "SELECT id, calle, numero, colonia,cp, estado, ciudad, usuarios_id FROM domicilios WHERE ciudad ILIKE ? ORDER BY ciudad";
        return conexion.sql(sql)
            .param("%" + ciudad + "%")
            .query((rs, rowNum) -> new Domicilio(
                rs.getInt("id"),
                rs.getString("calle"),
                rs.getString("numero"),
                rs.getString("colonia"),
                rs.getString("cp"),
                rs.getString("estado"),
                rs.getString("ciudad"),
                rs.getInt("usuarios_id")))
            .list();
    }

    // 8. Buscar domicilios por estado
    public List<Domicilio> buscarPorEstado(String estado) {
        String sql = "SELECT id, calle, numero, colonia, cp, estado, ciudad, usuarios_id FROM domicilios WHERE estado ILIKE ? ORDER BY estado";
        return conexion.sql(sql)
            .param("%" + estado + "%")
            .query((rs, rowNum) -> new Domicilio(
                rs.getInt("id"),
                rs.getString("calle"),
                rs.getString("numero"),
                rs.getString("colonia"),
                rs.getString("cp"),
                rs.getString("estado"),
                rs.getString("ciudad"),
                rs.getInt("usuarios_id")))
            .list();
    }

    // 9. Método adicional: Contar domicilios por usuario
    public int contarDomiciliosPorUsuario(int usuario_id) {
        String sql = "SELECT COUNT(*) FROM domicilios WHERE usuarios_id = ?";
        return conexion.sql(sql)
            .param(usuario_id)
            .query(Integer.class)
            .single();
    }

    // 10. Método adicional: Obtener domicilio principal de un usuario (el primero)
    public Domicilio obtenerDomicilioPrincipal(int usuario_id) {
        String sql = """
            SELECT id, calle, numero, colonia, cp, estado, ciudad, usuarios_id 
            FROM domicilios 
            WHERE usuarios_id = ? 
            ORDER BY id 
            LIMIT 1
            """;
        
        return conexion.sql(sql)
            .param(usuario_id)
            .query((rs, rowNum) -> new Domicilio(
                rs.getInt("id"),
                rs.getString("calle"),
                rs.getString("numero"),
                rs.getString("colonia"),
                rs.getString("cp"),
                rs.getString("estado"),
                rs.getString("ciudad"),
                rs.getInt("usuarios_id")))
            .optional()
            .orElse(null);
    }
}