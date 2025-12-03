package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Envio;
import mx.tecnm.backend.api.models.Envio.envio;

@Repository
public class EnvioDAO {

    @Autowired
   private JdbcClient conexion;
   public List<envio> consultarEnvios() {
     String sql = "SELECT id, fecha_envio, numero_seguimiento, estado_envio FROM envios";
     return conexion.sql(sql)
        .query((rs, rowNum) -> new Envio.envio(
            rs.getInt("id"),
            rs.getObject("fecha_envio", java.time.OffsetDateTime.class).toString(),
            rs.getString("numero_seguimiento"),
            rs.getString("estado_envio")))
        .list();

    }
}
