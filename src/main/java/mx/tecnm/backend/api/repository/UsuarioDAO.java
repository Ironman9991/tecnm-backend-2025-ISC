package mx.tecnm.backend.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Usuario;

@Repository
public class UsuarioDAO {

    @Autowired
    private JdbcClient conexion;

    public List<Usuario> consultarUsuario() {
        String sql = """
            SELECT id, nombre, email, telefono, sexo, 
                   fecha_nacimiento, contrasena, fecha_registro 
            FROM usuarios 
            ORDER BY id
            """;
        
        return conexion.sql(sql)
            .query((rs, rowNum) -> new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("sexo"),
                rs.getDate("fecha_nacimiento") != null ? 
                    rs.getDate("fecha_nacimiento").toLocalDate() : null,
                rs.getString("contrasena"),
                rs.getDate("fecha_registro") != null ? 
                    rs.getDate("fecha_registro").toLocalDate() : null
            ))
            .list();
    }

    public Usuario agregarUsuario(String nombre, String email, String telefono, 
                                  String sexo, LocalDate fechaNacimiento, String contrasena) {
        String sql = """
            INSERT INTO usuarios (nombre, email, telefono, sexo, 
                                 fecha_nacimiento, contrasena, fecha_registro) 
            VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE) 
            RETURNING id, nombre, email, telefono, sexo, 
                     fecha_nacimiento, contrasena, fecha_registro
            """;
        
        return conexion.sql(sql)
            .param(nombre)
            .param(email)
            .param(telefono != null ? telefono : "")
            .param(sexo != null ? sexo : "")
            .param(fechaNacimiento)
            .param(contrasena)
            .query((rs, rowNum) -> new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("sexo"),
                rs.getDate("fecha_nacimiento") != null ? 
                    rs.getDate("fecha_nacimiento").toLocalDate() : null,
                rs.getString("contrasena"),
                rs.getDate("fecha_registro") != null ? 
                    rs.getDate("fecha_registro").toLocalDate() : null
            ))
            .single();
    }

    public Usuario actualizarUsuario(int id, String nombre, String email, String telefono, 
                                     String sexo, LocalDate fechaNacimiento, String contrasena) {
        String sql = """
            UPDATE usuarios 
            SET nombre = ?,
                email = ?,
                telefono = ?,
                sexo = ?,
                fecha_nacimiento = ?,
                contrasena = ?
            WHERE id = ? 
            RETURNING id, nombre, email, telefono, sexo, 
                     fecha_nacimiento, contrasena, fecha_registro
            """;
        
        return conexion.sql(sql)
            .param(nombre)
            .param(email)
            .param(telefono)
            .param(sexo)
            .param(fechaNacimiento)
            .param(contrasena)
            .param(id)
            .query((rs, rowNum) -> new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("sexo"),
                rs.getDate("fecha_nacimiento") != null ? 
                    rs.getDate("fecha_nacimiento").toLocalDate() : null,
                rs.getString("contrasena"),
                rs.getDate("fecha_registro") != null ? 
                    rs.getDate("fecha_registro").toLocalDate() : null
            ))
            .single();
    }

    public List<Usuario> busquedaID(int id) {
        String sql = """
            SELECT id, nombre, email, telefono, sexo, 
                   fecha_nacimiento, contrasena, fecha_registro 
            FROM usuarios 
            WHERE id = ?
            """;
        
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("sexo"),
                rs.getDate("fecha_nacimiento") != null ? 
                    rs.getDate("fecha_nacimiento").toLocalDate() : null,
                rs.getString("contrasena"),
                rs.getDate("fecha_registro") != null ? 
                    rs.getDate("fecha_registro").toLocalDate() : null
            ))
            .list();
    }

    public List<Usuario> eliminarUsuario(int id) {
        String sql = """
            DELETE FROM usuarios 
            WHERE id = ? 
            RETURNING id, nombre, email, telefono, sexo, 
                     fecha_nacimiento, contrasena, fecha_registro
            """;
        
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("sexo"),
                rs.getDate("fecha_nacimiento") != null ? 
                    rs.getDate("fecha_nacimiento").toLocalDate() : null,
                rs.getString("contrasena"),
                rs.getDate("fecha_registro") != null ? 
                    rs.getDate("fecha_registro").toLocalDate() : null
            ))
            .list();
    }
    
    public List<Usuario> buscarPorEmail(String email) {
        String sql = """
            SELECT id, nombre, email, telefono, sexo, 
                   fecha_nacimiento, contrasena, fecha_registro 
            FROM usuarios 
            WHERE email = ?
            """;
        
        return conexion.sql(sql)
            .param(email)
            .query((rs, rowNum) -> new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("sexo"),
                rs.getDate("fecha_nacimiento") != null ? 
                    rs.getDate("fecha_nacimiento").toLocalDate() : null,
                rs.getString("contrasena"),
                rs.getDate("fecha_registro") != null ? 
                    rs.getDate("fecha_registro").toLocalDate() : null
            ))
            .list();
    }
}