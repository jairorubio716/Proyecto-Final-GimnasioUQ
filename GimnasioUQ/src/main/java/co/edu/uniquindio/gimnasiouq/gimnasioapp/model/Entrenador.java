package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

import java.util.ArrayList;
import java.util.List;

public class Entrenador {
    private String identificacion;
    private String nombre;
    private String telefono;
    private String correo;
    private String especialidad;
    private double sueldo;
    private List<Clase> clasesAsignadas;

    public Entrenador() {
        this.clasesAsignadas = new ArrayList<>();
    }

    public Entrenador(String identificacion, String nombre, String telefono,
                      String correo, String especialidad, double sueldo) {
        this();
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.especialidad = especialidad;
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

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public double getSueldo() { return sueldo; }
    public void setSueldo(double sueldo) { this.sueldo = sueldo; }

    public List<Clase> getClasesAsignadas() { return clasesAsignadas; }
    public void setClasesAsignadas(List<Clase> clasesAsignadas) { this.clasesAsignadas = clasesAsignadas; }
}