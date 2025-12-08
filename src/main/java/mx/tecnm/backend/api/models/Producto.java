package mx.tecnm.backend.api.models;

public record Producto(
    int id, 
    String nombre, 
    double precio, 
    String sku, 
    String color, 
    String marca, 
    String descripcion, 
    int categorias_id
){}