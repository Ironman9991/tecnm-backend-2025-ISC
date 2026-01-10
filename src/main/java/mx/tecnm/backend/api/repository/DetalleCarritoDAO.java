package mx.tecnm.backend.api.repository;

import java.util.List;

import javax.management.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.websocket.server.PathParam;
import mx.tecnm.backend.api.models.DetalleCarrito;
import mx.tecnm.backend.api.models.DetalleCarrito.detallecarrito;

@Repository
public class DetalleCarritoDAO {

    @Autowired
   private JdbcClient conexion;

    

    public List<detallecarrito> consultarDetalleCarritos() {
     String sql = "SELECT id, cantidad, precio, fecha_agregado FROM detalles_carrito";
     return conexion.sql(sql)
        .query((rs, rowNum) -> new DetalleCarrito.detallecarrito(
            rs.getInt("id"),
            rs.getInt("cantidad"),
            rs.getString("precio"),
            rs.getObject("fecha_agregado", java.time.OffsetDateTime.class).toString()))
           // rs.getString("fecha_agregado")))
        .list();
        }

        
          // Opción 2: Usando JdbcClient
    public Integer eliminarCarritoUsuarioJdbc(Integer userId) {
        String sql = "SELECT f_eleminar_carrito_por_usuario_id(?)";
        return conexion.sql(sql)
            .param(userId)
            .query(Integer.class)
            .single();
    }
        
    }