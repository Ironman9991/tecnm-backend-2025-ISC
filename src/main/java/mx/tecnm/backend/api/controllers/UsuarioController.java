package mx.tecnm.backend.api.controllers;

import java.time.LocalDate;
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

import mx.tecnm.backend.api.models.Usuario;
import mx.tecnm.backend.api.repository.UsuarioDAO;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioDAO repo;

    @GetMapping()
    public ResponseEntity<List<Usuario>> consultarUsuario() {
        List<Usuario> usuarios = repo.consultarUsuario();
        return ResponseEntity.ok(usuarios);
    } 

    @PostMapping()
    public ResponseEntity<Usuario> agregarUsuario(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String sexo,
            @RequestParam LocalDate fechaNacimiento,
            @RequestParam String contrasena) {
        
        // La fecha_registro se manejará automáticamente en la BD
        Usuario nuevoUsuario = repo.agregarUsuario(nombre, email, telefono, sexo, fechaNacimiento, contrasena);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PutMapping()
    public ResponseEntity<Usuario> actualizarUsuario(
            @RequestParam int id,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String sexo,
            @RequestParam LocalDate fechaNacimiento,
            @RequestParam String contrasena) {
        
        Usuario usuarioActualizado = repo.actualizarUsuario(id, nombre, email, telefono, sexo, fechaNacimiento, contrasena);
        return ResponseEntity.ok(usuarioActualizado);
    } 
    
    @GetMapping("/busquedaid")
    public ResponseEntity<List<Usuario>> busquedaID(@RequestParam int id) {
        List<Usuario> usuarios = repo.busquedaID(id);
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping()
    public ResponseEntity<List<Usuario>> eliminarUsuario(@RequestParam int id) {
        List<Usuario> usuarioeliminado = repo.eliminarUsuario(id);
        return ResponseEntity.ok(usuarioeliminado);
    }
    
    // Método adicional para buscar por email (como en la imagen)
    @GetMapping("/buscaremail")
    public ResponseEntity<List<Usuario>> buscarPorEmail(@RequestParam String email) {
        List<Usuario> usuarios = repo.buscarPorEmail(email);
        return ResponseEntity.ok(usuarios);
    }
}