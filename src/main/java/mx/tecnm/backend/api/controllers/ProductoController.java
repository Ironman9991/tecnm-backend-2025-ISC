package mx.tecnm.backend.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.tecnm.backend.api.models.Producto;
import mx.tecnm.backend.api.repository.ProductoDAO;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    ProductoDAO repo;
    
    @GetMapping()
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> productos = repo.consultarProductos();
        return ResponseEntity.ok(productos);
    }

    @PostMapping()
    public ResponseEntity<Producto> crearProducto(
            @RequestParam String nombre, 
            @RequestParam double precio, 
            @RequestParam String sku, 
            @RequestParam String color, 
            @RequestParam String marca, 
            @RequestParam String descripcion, 
            @RequestParam int categorias_id) {
        
        Producto productoCreado = repo.crearProducto(nombre, precio, sku, color, marca, descripcion, categorias_id);
        return ResponseEntity.ok(productoCreado);             
    }

    @GetMapping("/busquedaid")
    public ResponseEntity<List<Producto>> busquedaID(@RequestParam int id) {
        List<Producto> productos = repo.busquedaID(id);
        return ResponseEntity.ok(productos);
    }
    
    @PutMapping()
    public ResponseEntity<Producto> actualizarProducto(
            @RequestParam int id, 
            @RequestParam String nombre, 
            @RequestParam double precio, 
            @RequestParam String sku, 
            @RequestParam String color, 
            @RequestParam String marca, 
            @RequestParam String descripcion, 
            @RequestParam int categorias_id) {
        
        Producto productoActualizado = repo.actualizarProducto(id, nombre, precio, sku, color, marca, descripcion, categorias_id);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping()
    public ResponseEntity<List<Producto>> desactivar(@RequestParam int id) {
        List<Producto> productoEliminado = repo.desactivar(id);
        return ResponseEntity.ok(productoEliminado);
    }
}