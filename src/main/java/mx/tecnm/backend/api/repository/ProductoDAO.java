package mx.tecnm.backend.api.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

import mx.tecnm.backend.api.models.Producto.producto;
import org.springframework.stereotype.Repository;

@Repository
public class ProductoDAO {


     @Autowired
   private JdbcClient conexion;

    public List<producto> consultarProductos() {
     String sql = "SELECT id, nombre, precio FROM productos";
     return conexion.sql(sql)
        .query((rs, rowNum) -> new mx.tecnm.backend.api.models.Producto.producto(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getDouble("precio")))
        .list();
            
    }
}
