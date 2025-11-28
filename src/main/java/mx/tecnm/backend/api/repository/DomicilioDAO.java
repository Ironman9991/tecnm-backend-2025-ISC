package mx.tecnm.backend.api.repository;

import java.util.List;

import mx.tecnm.backend.api.models.Domicilio;
import mx.tecnm.backend.api.models.Domicilio.domicilio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class DomicilioDAO {

    @Autowired
   private JdbcClient conexion;

    public List<domicilio> consultarDomicilios() {
     String sql = "SELECT id, calle, numero, estado FROM domicilios";
     return conexion.sql(sql)
        .query((rs, rowNum) -> new Domicilio.domicilio(
            rs.getInt("id"),
            rs.getString("calle"),
            rs.getString("numero"),
            rs.getString("estado")))
        .list();

    }
}
