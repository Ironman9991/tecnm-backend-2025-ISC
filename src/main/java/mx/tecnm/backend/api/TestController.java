package mx.tecnm.backend.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    // Hello World
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    // Primer producto
    @GetMapping("/producto")
    public Producto getProducto() {
        Producto p = new Producto();
        p.nombre = "Coca cola";
        p.precio = 18.5;
        p.codigoBarras = "456789678";
        return p;
    }

    // Segundo producto
    @GetMapping("/producto2")
    public Producto getProducto2() {
        Producto p1 = new Producto();
        p1.nombre = "Pepsi";
        p1.precio = 67;
        p1.codigoBarras = "345678";
        return p1;
    }

    // Tercer producto
    @GetMapping("/producto3")
    public Producto getProducto3() {
        Producto p2 = new Producto();
        p2.nombre = "Sprite";
        p2.precio = 15.0;
        p2.codigoBarras = "789012";
        return p2;
    }


    // Todos los productos
    @GetMapping("/todos")
    public List<Producto> getAllProductos() {
        Producto p1 = new Producto();
        p1.nombre = "Coca cola";
        p1.precio = 18.5;
        p1.codigoBarras = "456789678";
        
        Producto p2 = new Producto();
        p2.nombre = "Pepsi";
        p2.precio = 67;
        p2.codigoBarras = "345678";
        
        Producto p3 = new Producto();
        p3.nombre = "Sprite";
        p3.precio = 15.0;
        p3.codigoBarras = "789012";
        
        return Arrays.asList(p1, p2, p3);
    }

    // Mostrar todo en la raíz (Hello World + Productos)
    @GetMapping("/")
    public Map<String, Object> mostrarTodo() {
        Map<String, Object> respuesta = new HashMap<>();
        
        // Hello World
        respuesta.put("mensaje", "Hello World!");
        
        // Productos
        Producto p1 = new Producto();
        p1.nombre = "Coca cola";
        p1.precio = 18.5;
        p1.codigoBarras = "456789678";
        
        Producto p2 = new Producto();
        p2.nombre = "Pepsi";
        p2.precio = 67;
        p2.codigoBarras = "345678";
        
        Producto p3 = new Producto();
        p3.nombre = "Sprite";
        p3.precio = 15.0;
        p3.codigoBarras = "789012";
        
        List<Producto> productos = Arrays.asList(p1, p2, p3);
        respuesta.put("productos", productos);
        
        return respuesta;
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable int id) {
        if(id < 1 || id > 3){
            return ResponseEntity.notFound().build();
        }   

        Producto[] productos = new Producto[4]; 
        
        Producto p1 = new Producto();
        p1.nombre = "Coca cola";
        p1.precio = 18.5;
        p1.codigoBarras = "456789678";
        productos[1] = p1; // Posición 1

        Producto p2 = new Producto();
        p2.nombre = "Pepsi";
        p2.precio = 67;
        p2.codigoBarras = "345678";
        productos[2] = p2; // Posición 2

        Producto p3 = new Producto();
        p3.nombre = "Sprite";
        p3.precio = 15.0;
        p3.codigoBarras = "789012";
        productos[3] = p3; // Posición 3

        Producto resultado = productos[id];
        return ResponseEntity.ok(resultado);
    }
}