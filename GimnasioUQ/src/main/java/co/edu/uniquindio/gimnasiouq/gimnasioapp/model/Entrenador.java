package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

import java.util.Objects;

public class Entrenador {
    private String identificacion;
    private String nombre;
    private String telefono;
    private String correo;
    private double sueldo;
    private boolean disponible;

    public Entrenador() {
        this.disponible = true; // Disponible por defecto
    }

    public Entrenador(String identificacion, String nombre, String telefono,
                      String correo, double sueldo) {
        this();
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.sueldo = sueldo;
    }

    // Getters y Setters
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public double getSueldo() { return sueldo; }
    public void setSueldo(double sueldo) { this.sueldo = sueldo; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    // SOLUCIÓN: Implementar equals y hashCode basados en la identificación
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrenador that = (Entrenador) o;
        return Objects.equals(identificacion, that.identificacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificacion);
    }
}
