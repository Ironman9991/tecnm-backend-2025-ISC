package mx.tecnm.backend.api.models;

import java.time.LocalDateTime;

public record DetalleCarrito(
    Long id,        // Cambiado a Long para BIGINT de PostgreSQL
    int cantidad,   // cantidad de productos
    double precio,  // precio total (cantidad * precio_unitario)
    Long productos_id,   // Cambiado a Long
    Long usuarios_id    // Cambiado a Long
) {
    // Constructor adicional para usar int (opcional)
    public DetalleCarrito(int id, int cantidad, double precio, int productos_id, int usuarios_id) {
        this((long) id, cantidad, precio, (long) productos_id, (long) usuarios_id);
    }
}