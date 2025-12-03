package mx.tecnm.backend.api.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MetodoPagoDAO {
    
    @Autowired
   private JdbcClient conexion;
   
    public List<mx.tecnm.backend.api.models.MetodoPago.metodopago> consultarMetodosPago() {
     //String sql = "SELECT id, nombre, comision FROM metodos_pago";
     String sql = "SELECT id, nombre FROM metodos_pago";
     return conexion.sql(sql)
        .query((rs, rowNum) -> new mx.tecnm.backend.api.models.MetodoPago.metodopago(
            rs.getInt("id"),
            rs.getString("nombre")))
            //rs.getDouble("comision")))
        .list();
            
    }
}
