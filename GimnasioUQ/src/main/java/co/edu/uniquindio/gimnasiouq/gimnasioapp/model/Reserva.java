package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;


public class Reserva {
    private String codigoReserva;
    private String nombreUsuario;
    private String identificacionUsuario;
    private String nombreClase;
    private String fecha;
    // Solo fecha, sin hora

    public Reserva() {
    }

    // En tu clase Reserva.java - AGREGAR ESTE CONSTRUCTOR:
    public Reserva(String codigoReserva, String nombreUsuario, String identificacionUsuario, String nombreClase, String fecha) {
        this.codigoReserva = codigoReserva;
        this.nombreUsuario = nombreUsuario;
        this.identificacionUsuario = identificacionUsuario;
        this.nombreClase = nombreClase;
        this.fecha = fecha;
    }
    public String getIdentificacionUsuario() {
        return identificacionUsuario;
    }

    public void setIdentificacionUsuario(String identificacionUsuario) {
        this.identificacionUsuario = identificacionUsuario;
    }

    // Getters y Setters (ELIMINAR getHora() y setHora())
    public String getCodigoReserva() { return codigoReserva; }
    public void setCodigoReserva(String codigoReserva) { this.codigoReserva = codigoReserva; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getNombreClase() { return nombreClase; }
    public void setNombreClase(String nombreClase) { this.nombreClase = nombreClase; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}