package mx.tecnm.backend.api.models;

public class Pedido {
    public record pedido(int id, String numero, String importe_productos) {
 }
}
