package mx.tecnm.backend.api.models;

public class DetalleCarrito {
    public record detallecarrito(int id, int cantidad, String precio, String fecha_agregado) {
    }
}
