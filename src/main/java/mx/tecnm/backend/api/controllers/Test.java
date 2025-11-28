package mx.tecnm.backend.api.controllers;


import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/test")
@RestController
public class Test {

    @GetMapping("/hello")
    public String helloworld(){
        return "Hola API rest";
    }

    /* 
    @GetMapping("/producto")
    public Producto getProducto(){
        Producto p = new Producto();
        p.nombre = "Coca Cola";
        p.precio = 17.2;
        p.codigoBarras= "7501053123606";
        return p;
    }


    @GetMapping("/productos")
    public Producto[] getProductos(){
        Producto p1 = new Producto();
        p1.nombre = "Coca Cola";
        p1.precio = 18.5 ;
        p1.codigoBarras = "751927392383";

              Producto p2 = new Producto();
        p2.nombre = "Fanta";
        p2.precio = 14.5 ;
        p2.codigoBarras = "751927392383";

              Producto p3 = new Producto();
        p3.nombre = "Pepsi";
        p3.precio = 12.5 ;
        p3.codigoBarras = "751927392383";

        return new Producto[]{p1,p3,p2};
    }

    @GetMapping("/productos/{id}") // Parametros con {}
    public ResponseEntity<Producto> buscarPorId(@PathVariable int id){  //Para recibir un entero
        if (id < 0 || id >2) {
            return ResponseEntity.notFound().build(); //Devolver un codigo de estado 404 "error"
        }
        
        Producto [] productos = new Producto[3];
        Producto p1 = new Producto();
        p1.nombre = "Coca Cola";
        p1.precio = 18.5 ;
        p1.codigoBarras = "751927392383";
        productos[0]= p1;

              Producto p2 = new Producto();
        p2.nombre = "Fanta";
        p2.precio = 14.5 ;
        p2.codigoBarras = "751927392383";
        productos[1]= p2;

              Producto p3 = new Producto();
        p3.nombre = "Pepsi";
        p3.precio = 12.5 ;
        p3.codigoBarras = "751927392383";
        productos[2]= p3;

        Producto resultado = productos[id];
        return ResponseEntity.ok(resultado);


    } 
    */

    
}
