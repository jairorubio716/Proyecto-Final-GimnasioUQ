package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

import java.time.LocalDate;

public class Reserva {
    private String codigo;
    private Usuario usuario;
    private Clase clase;
    private Entrenador entrenador;
    private LocalDate fechaReserva;
    private LocalDate fechaClase;
    private String estado;

    public Reserva() {}

    public Reserva(String codigo, Usuario usuario, Clase clase, LocalDate fechaClase, Entrenador entrenador) {
        this.codigo = codigo;
        this.usuario = usuario;
        this.clase = clase;
        this.fechaClase = fechaClase;
        this.entrenador = entrenador;
        this.fechaReserva = LocalDate.now();
        this.estado = "ACTIVA";
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Clase getClase() { return clase; }
    public void setClase(Clase clase) { this.clase = clase; }

    public Entrenador getEntrenador() { return entrenador; }
    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }

    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }

    public LocalDate getFechaClase() { return fechaClase; }
    public void setFechaClase(LocalDate fechaClase) { this.fechaClase = fechaClase; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
