package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Categoria;

@Repository
public class CategoriaDAO {

    @Autowired
    private JdbcClient conexion;

    public List<Categoria> consultarCategorias() {
        String sql = "SELECT id, nombre FROM categorias WHERE activo = TRUE";
        return conexion.sql(sql)
            .query((rs, rowNum) -> new Categoria(
                rs.getInt("id"),
                rs.getString("nombre")))
            .list();
    }

    public Categoria crearCategoria(String nombre) {
        String sql = "INSERT INTO categorias (nombre) VALUES (?) RETURNING id, nombre";
        return conexion.sql(sql)
            .param(nombre)
            .query((rs, rowNum) -> new Categoria(
                rs.getInt("id"),
                rs.getString("nombre")))
            .single();
    }

    public List<Categoria> busquedaID(int id) {
        String sql = "SELECT id, nombre FROM categorias WHERE id = ? AND activo = TRUE";
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new Categoria(
                rs.getInt("id"),
                rs.getString("nombre")))
            .list();
    }

    public Categoria actualizarCategoria(int id, String nuevoNombre) {
        String sql = "UPDATE categorias SET nombre = ? WHERE id = ? RETURNING id, nombre";
        return conexion.sql(sql)
            .param(nuevoNombre)
            .param(id)
            .query((rs, rowNum) -> new Categoria(
                rs.getInt("id"),
                rs.getString("nombre")))
            .single();
    }

    public List<Categoria> desactivar(int id) {
        String sql = "UPDATE categorias SET activo = FALSE WHERE id = ? RETURNING id, nombre";
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new Categoria(
                rs.getInt("id"),
                rs.getString("nombre")))
            .list();
    }
}



// Esto nomas es pa que detecte cambios :v
