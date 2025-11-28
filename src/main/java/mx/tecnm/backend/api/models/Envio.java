package mx.tecnm.backend.api.models;

public class Envio {
 public record envio(int id, String fecha, String numero_seguimiento, String estado) {
 }
}
