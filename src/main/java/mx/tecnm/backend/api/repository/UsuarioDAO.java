package mx.tecnm.backend.api.repository;
import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.tecnm.backend.api.models.Usuario.usuarios;

@Repository
public class UsuarioDAO {

    @Autowired
   private JdbcClient conexion;

    public List<usuarios> consultarUsuario() {
     String sql = "SELECT id, nombre FROM usuarios";
     return conexion.sql(sql)
        .query((rs, rowNum) -> new mx.tecnm.backend.api.models.Usuario.usuarios(
            rs.getInt("id"),
            rs.getString("nombre")))
        .list();
            
    }
    
    // GET BY ID
    public Optional<usuarios> consultarUsuarioPorId(int id) {
        String sql = "SELECT id, nombre FROM usuarios WHERE id = ?";
        try {
            usuarios usuario = conexion.sql(sql)
                .param(id)
                .query((rs, rowNum) -> new usuarios(
                    rs.getInt("id"),
                    rs.getString("nombre")))
                .single();
            return Optional.of(usuario);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    // POST - Crear usuario
    @Transactional
    public usuarios crearUsuario(String nombre) {
        String sql = "INSERT INTO usuarios (nombre) VALUES (?) RETURNING id, nombre";
        return conexion.sql(sql)
            .param(nombre)
            .query((rs, rowNum) -> new usuarios(
                rs.getInt("id"),
                rs.getString("nombre")))
            .single();
    }
    
    // POST - Crear usuario con objeto completo
    @Transactional
    public usuarios crearUsuarioCompleto(mx.tecnm.backend.api.models.Usuario.UsuarioRequest usuarioRequest) {
        // Asumiendo que tu tabla tiene más columnas
        String sql = """
            INSERT INTO usuarios (nombre, email, telefono) 
            VALUES (?, ?, ?) 
            RETURNING id, nombre
            """;
        return conexion.sql(sql)
            .param(usuarioRequest.getNombre())
            .param(usuarioRequest.getEmail() != null ? usuarioRequest.getEmail() : "")
            .param(usuarioRequest.getTelefono() != null ? usuarioRequest.getTelefono() : "")
            .query((rs, rowNum) -> new usuarios(
                rs.getInt("id"),
                rs.getString("nombre")))
            .single();
    }
    
    // PUT - Actualizar usuario
    @Transactional
    public Optional<usuarios> actualizarUsuario(int id, String nombre) {
        String sql = "UPDATE usuarios SET nombre = ? WHERE id = ? RETURNING id, nombre";
        try {
            usuarios usuario = conexion.sql(sql)
                .param(nombre)
                .param(id)
                .query((rs, rowNum) -> new usuarios(
                    rs.getInt("id"),
                    rs.getString("nombre")))
                .single();
            return Optional.of(usuario);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    // PUT - Actualizar usuario completo
    @Transactional
    public Optional<usuarios> actualizarUsuarioCompleto(int id, mx.tecnm.backend.api.models.Usuario.UsuarioRequest usuarioRequest) {
        String sql = """
            UPDATE usuarios 
            SET nombre = ?, email = ?, telefono = ? 
            WHERE id = ? 
            RETURNING id, nombre
            """;
        try {
            usuarios usuario = conexion.sql(sql)
                .param(usuarioRequest.getNombre())
                .param(usuarioRequest.getEmail() != null ? usuarioRequest.getEmail() : "")
                .param(usuarioRequest.getTelefono() != null ? usuarioRequest.getTelefono() : "")
                .param(id)
                .query((rs, rowNum) -> new usuarios(
                    rs.getInt("id"),
                    rs.getString("nombre")))
                .single();
            return Optional.of(usuario);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    // DELETE - Eliminar usuario por ID
    @Transactional
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        int filasEliminadas = conexion.sql(sql)
            .param(id)
            .update();
        return filasEliminadas > 0;
    }
    
    // PATCH - Actualizar parcialmente (solo algunos campos)
    @Transactional
    public Optional<usuarios> actualizarParcialUsuario(int id, mx.tecnm.backend.api.models.Usuario.UsuarioRequest usuarioRequest) {
        // Construir consulta dinámica basada en los campos no nulos
        StringBuilder sqlBuilder = new StringBuilder("UPDATE usuarios SET ");
        boolean first = true;
        
        if (usuarioRequest.getNombre() != null) {
            sqlBuilder.append("nombre = ?");
            first = false;
        }
        
        if (usuarioRequest.getEmail() != null) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("email = ?");
            first = false;
        }
        
        if (usuarioRequest.getTelefono() != null) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("telefono = ?");
        }
        
        sqlBuilder.append(" WHERE id = ? RETURNING id, nombre");
        
        String sql = sqlBuilder.toString();
        
        try {
            // Construir parámetros dinámicamente
            JdbcClient.StatementSpec statement = conexion.sql(sql);
            
            int paramIndex = 1;
            if (usuarioRequest.getNombre() != null) {
                statement = statement.param(paramIndex++, usuarioRequest.getNombre());
            }
            if (usuarioRequest.getEmail() != null) {
                statement = statement.param(paramIndex++, usuarioRequest.getEmail());
            }
            if (usuarioRequest.getTelefono() != null) {
                statement = statement.param(paramIndex++, usuarioRequest.getTelefono());
            }
            statement = statement.param(paramIndex, id);
            
            usuarios usuario = statement
                .query((rs, rowNum) -> new usuarios(
                    rs.getInt("id"),
                    rs.getString("nombre")))
                .single();
            return Optional.of(usuario);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
