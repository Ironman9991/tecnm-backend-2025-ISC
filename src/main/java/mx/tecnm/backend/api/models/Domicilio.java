package mx.tecnm.backend.api.models;

public record Domicilio(
    int id, 
    String calle, 
    String numero, 
    String colonia, 
    String cp,  // "eg" en la imagen parece ser "cp" (código postal)
    String estado, 
    String ciudad, 
    int usuarios_id
) {}