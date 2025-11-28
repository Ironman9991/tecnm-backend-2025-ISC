package mx.tecnm.backend.api.repository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

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
}
