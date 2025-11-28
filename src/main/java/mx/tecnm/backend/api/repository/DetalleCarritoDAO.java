package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.DetalleCarrito;
import mx.tecnm.backend.api.models.DetalleCarrito.detallecarrito;

@Repository
public class DetalleCarritoDAO {

    @Autowired
   private JdbcClient conexion;
    public List<detallecarrito> consultarDetalleCarritos() {
     String sql = "SELECT id, cantidad, precio FROM detalles_carrito";
     return conexion.sql(sql)
        .query((rs, rowNum) -> new DetalleCarrito.detallecarrito(
            rs.getInt("id"),
            rs.getInt("cantidad"),
            rs.getString("precio")))
        .list();
        }
    }
