package mx.tecnm.backend.api.models;

import java.time.OffsetDateTime;

public record Envio(
    int id,
    int pedidos_id,
    int domicilios_id,  
    OffsetDateTime fecha_envio,
    String estado_envio,
    String numero_seguimiento
){}
