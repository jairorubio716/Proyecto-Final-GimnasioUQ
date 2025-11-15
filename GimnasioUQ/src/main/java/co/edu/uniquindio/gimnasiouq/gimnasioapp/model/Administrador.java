package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public class Administrador {

    private String nombre;
    private String identificacion;
    private String correo;
    private String contrasena;

    public Administrador() {
    }

    public Administrador(String nombre, String identificacion, String correo, String contrasena) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getUsername() {
        return this.correo;
    }

    public String getPassword() {
        return this.contrasena;
    }

    public Rol getRol() {
        return Rol.ADMIN;
    }
}
