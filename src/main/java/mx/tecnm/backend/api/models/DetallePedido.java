package mx.tecnm.backend.api.models;

public class DetallePedido {
    public record detallepedido(int id, String cantidad, String precio) {
    }
}
