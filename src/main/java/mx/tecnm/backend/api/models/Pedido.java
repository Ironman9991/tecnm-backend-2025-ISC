package mx.tecnm.backend.api.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record Pedido(
    int id,
    int usuarios_id,
    int metodos_pago_id,
    Timestamp fecha,
    BigDecimal total,
    String numero_envio,
    BigDecimal importe_envio,
    BigDecimal importe_iva,
    BigDecimal importe_productos
) {}
