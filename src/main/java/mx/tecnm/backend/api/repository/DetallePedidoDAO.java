package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.DetallePedido;
import mx.tecnm.backend.api.models.DetallePedido.detallepedido;

@Repository
public class DetallePedidoDAO {

    @Autowired
   private JdbcClient conexion;

    public List<detallepedido> consultarDetallePedidos() {
     String sql = "SELECT id, cantidad, precio FROM detalles_pedidos";
     return conexion.sql(sql)
        .query((rs, rowNum) -> new DetallePedido.detallepedido(
            rs.getInt("id"),
            rs.getString("cantidad"),
            rs.getString("precio")))
        .list();

    }
}
