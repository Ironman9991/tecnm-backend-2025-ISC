package mx.tecnm.backend.api.models;

import java.time.LocalDate;

public record Usuario(
    int id,
    String nombre,
    String email,
    String telefono,
    String sexo,
    LocalDate fechaNacimiento,
    String contrasena,
    LocalDate fechaRegistro
){

}