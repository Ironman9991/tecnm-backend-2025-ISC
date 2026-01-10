package mx.tecnm.backend.api.models;


public class Usuario {

 public record usuarios(int id, String nombre, String email, String telefono) {
    }

    // Clase para creación/actualización
    public static class UsuarioRequest {
        
        private String nombre;
        
        // Puedes agregar más campos según tu tabla
        private String email;
        private String telefono;
        
        // Constructores
        public UsuarioRequest() {}
         public UsuarioRequest(String nombre, String Email, String telefono) {}
        public UsuarioRequest(String nombre) {
            this.nombre = nombre;
        }
        
        // Getters y Setters
        public String getNombre() {
            return nombre;
        }
        
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getTelefono() {
            return telefono;
        }
        
        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }
    }

}
