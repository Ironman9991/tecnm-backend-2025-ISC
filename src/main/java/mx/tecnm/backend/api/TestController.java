package mx.tecnm.backend.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
// Hello World hasta arriba como solicitaste
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    // ✅ Primer producto - fiel a tu código
    @GetMapping("/producto")
    public Producto getProducto() {
        Producto p = new Producto();
        p.nombre = "Coca cola";
        p.precio = 18.5;
        p.codigoBarras = "456789678";
        return p;
    }

    // ✅ Segundo producto - fiel a tu código
    @GetMapping("/producto2")
    public Producto getProducto2() {
        Producto p1 = new Producto();
        p1.nombre = "Pepsi";
        p1.precio = 67;
        p1.codigoBarras = "345678";
        return p1;
    }

    // ✅ Método para todos los productos
    @GetMapping("/todos")
    public List<Producto> getAllProductos() {
        List<Producto> productos = new ArrayList<>();
        
        // Producto 1
        Producto p1 = new Producto();
        p1.nombre = "Coca cola";
        p1.precio = 18.5;
        p1.codigoBarras = "456789678";
        productos.add(p1);
        
        // Producto 2
        Producto p2 = new Producto();
        p2.nombre = "Pepsi";
        p2.precio = 67;
        p2.codigoBarras = "345678";
        productos.add(p2);
        
        // Producto 3
        Producto p3 = new Producto();
        p3.nombre = "Sprite";
        p3.precio = 15.0;
        p3.codigoBarras = "789012";
        productos.add(p3);
        
        return productos;
    }

    // ✅ Método para mostrar todo en la raíz
    @GetMapping("/")
    public String mostrarTodo() {
        return 
            "=== HELLO WORLD ===\n" +
            "Hello World!\n\n" +
            
            "=== PRODUCTO 1 ===\n" +
            "Nombre: Coca cola\n" +
            "Precio: $18.5\n" +
            "Código Barras: 456789678\n\n" +
            
            "=== PRODUCTO 2 ===\n" +
            "Nombre: Pepsi\n" +
            "Precio: $67\n" +
            "Código Barras: 345678\n\n" +
            
            "=== TODOS LOS PRODUCTOS ===\n" +
            "1. Coca cola - $18.5 - 456789678\n" +
            "2. Pepsi - $67 - 345678\n" +
            "3. Sprite - $15.0 - 789012";
    }
}