package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public abstract class Usuario {
    private String nombre;
    private String identificacion;
    private String edad;
    private String telefono;
    private Entrenador entrenadorAsignado;

    public Usuario() {}

    public Usuario(String nombre, String identificacion, String edad, String telefono) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.edad = edad;
        this.telefono = telefono;
        this.entrenadorAsignado = null;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getEdad() { return edad; }
    public void setEdad(String edad) { this.edad = edad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Entrenador getEntrenadorAsignado() { return entrenadorAsignado; }
    public void setEntrenadorAsignado(Entrenador entrenadorAsignado) { this.entrenadorAsignado = entrenadorAsignado; }
}
