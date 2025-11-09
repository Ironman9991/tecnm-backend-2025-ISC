package mx.tecnm.backend.api;

public class Producto {
   public String nombre;
    public double precio;
    public String codigoBarras;
    
    public Producto() {}
    
    public Producto(String nombre, double precio, String codigoBarras) {
        this.nombre = nombre;
        this.precio = precio;
        this.codigoBarras = codigoBarras;
    } 
}
