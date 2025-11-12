package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

import java.time.LocalDate;

public class Reserva {
    private String codigoReserva;
    private String identificacionUsuario;
    private TipoClase tipoClase;
    private String fechaReserva;
    private String fechaClase;
    private String estado;

    public Reserva() {}

    public Reserva(String codigoReserva, String identificacionUsuario,
                   TipoClase tipoClase, String fechaClase) {
        this.codigoReserva = codigoReserva;
        this.identificacionUsuario = identificacionUsuario;
        this.tipoClase = tipoClase;
        this.fechaReserva = LocalDate.now().toString();
        this.fechaClase = fechaClase;
        this.estado = "CONFIRMADA";
    }

    // Getters y Setters
    public String getCodigoReserva() { return codigoReserva; }
    public void setCodigoReserva(String codigoReserva) { this.codigoReserva = codigoReserva; }

    public String getIdentificacionUsuario() { return identificacionUsuario; }
    public void setIdentificacionUsuario(String identificacionUsuario) { this.identificacionUsuario = identificacionUsuario; }

    public TipoClase getTipoClase() { return tipoClase; }
    public void setTipoClase(TipoClase tipoClase) { this.tipoClase = tipoClase; }

    public String getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(String fechaReserva) { this.fechaReserva = fechaReserva; }

    public String getFechaClase() { return fechaClase; }
    public void setFechaClase(String fechaClase) { this.fechaClase = fechaClase; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}