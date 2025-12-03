package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;


import mx.tecnm.backend.api.models.Categoria;
import mx.tecnm.backend.api.models.Categoria.categoria;

@Repository

public class CategoriaDAO {

    @Autowired
   private JdbcClient conexion;

    public List<categoria> consultarCategorias() {
     String sql = "SELECT id, nombre FROM categorias";
     return conexion.sql(sql)
        .query((rs, rowNum) -> new Categoria.categoria(
            rs.getInt("id"),
            rs.getString("nombre")))
        .list();

    }




    }



// Esto nomas es pa que detecte cambios :v
