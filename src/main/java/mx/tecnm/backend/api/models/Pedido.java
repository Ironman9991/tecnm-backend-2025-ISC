package mx.tecnm.backend.api.models;

public class Pedido {
    public record pedido(int id, String numero_envio, String importe_productos) {
 }
}
