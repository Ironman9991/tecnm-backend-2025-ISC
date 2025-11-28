package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Pedido;
import mx.tecnm.backend.api.models.Pedido.pedido;

@Repository
public class PedidoDAO {

    @Autowired
   private JdbcClient conexion;

    public List<pedido> consultarPedidos() {
     String sql = "SELECT id, numero, importe_productos FROM pedidos";
     return conexion.sql(sql)
        .query((rs, rowNum) -> new Pedido.pedido(
            rs.getInt("id"),
            rs.getString("numero"),
            rs.getString("importe_productos")))
        .list();

    }
}
