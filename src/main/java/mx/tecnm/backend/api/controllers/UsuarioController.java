package mx.tecnm.backend.api.controllers;

import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mx.tecnm.backend.api.models.Usuario;
import mx.tecnm.backend.api.models.Usuario.UsuarioRequest;
import mx.tecnm.backend.api.models.Usuario.usuarios;
import mx.tecnm.backend.api.repository.UsuarioDAO;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioDAO repo;


    @GetMapping()
    public ResponseEntity<List<usuarios>> obtenerUsuario() {
        List<usuarios> usuarios = repo.consultarUsuario();
        return ResponseEntity.ok(usuarios);
    } 
    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<usuarios> obtenerUsuarioPorId(@PathVariable int id) {
        Optional<usuarios> usuario = repo.consultarUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // POST - Crear usuario simple
    @PostMapping("/simple")
    public ResponseEntity<usuarios> crearUsuario(
            @RequestParam String nombre) {
        try {
            usuarios nuevoUsuario = repo.crearUsuario(nombre);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // POST - Crear usuario con JSON
    // @PostMapping("/completo")
    // public ResponseEntity<usuarios> crearUsuarioCompleto(
    //         @Validated @RequestBody Usuario.UsuarioRequest usuarioRequest) {
    //     try {
    //         usuarios nuevoUsuario = repo.crearUsuarioCompleto(usuarioRequest);
    //         return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().build();
    //     }
      //  }
    @PostMapping("/añadir")
    public ResponseEntity<usuarios> annadirUsuario(@RequestParam int id, @RequestParam String nombre, @RequestParam String email, @RequestParam String telefono, @RequestParam char sexo, @RequestParam java.sql.Date fechaNacimiento, @RequestParam String contrasenna){
        Usuario nuevoUsuario = repo.crearUsuarioCompleto(new UsuarioRequest(id, nombre, email, telefono));
    }
    // PUT - Actualizar usuario completo
    @PutMapping("/{id}")
    public ResponseEntity<usuarios> actualizarUsuario(
            @PathVariable int id,
            @Valid @RequestBody Usuario.UsuarioRequest usuarioRequest) {
        Optional<usuarios> usuarioActualizado = repo.actualizarUsuarioCompleto(id, usuarioRequest);
        return usuarioActualizado.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
    }
    
    // PUT - Actualizar solo nombre
    @PutMapping("/{id}/nombre")
    public ResponseEntity<usuarios> actualizarNombreUsuario(
            @PathVariable int id,
            @RequestParam String nombre) {
        Optional<usuarios> usuarioActualizado = repo.actualizarUsuario(id, nombre);
        return usuarioActualizado.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
    }
    
    // PATCH - Actualización parcial
    @PatchMapping("/{id}")
    public ResponseEntity<usuarios> actualizarParcialUsuario(
            @PathVariable int id,
            @RequestBody Usuario.UsuarioRequest usuarioRequest) {
        Optional<usuarios> usuarioActualizado = repo.actualizarParcialUsuario(id, usuarioRequest);
        return usuarioActualizado.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
    }
    
    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable int id) {
        boolean eliminado = repo.eliminarUsuario(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE con confirmación
    @DeleteMapping("/{id}/confirmar")
    public ResponseEntity<String> eliminarUsuarioConConfirmacion(
            @PathVariable int id,
            @RequestParam(defaultValue = "false") boolean confirmar) {
        if (!confirmar) {
            return ResponseEntity.badRequest()
                .body("Debe confirmar la eliminación con confirmar=true");
        }
        
        boolean eliminado = repo.eliminarUsuario(id);
        if (eliminado) {
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Usuario no encontrado");
        }
    }
}
