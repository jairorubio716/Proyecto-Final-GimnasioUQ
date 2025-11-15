package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

import java.time.LocalDate;

public class Membresia {
    private String codigo;
    private String identificacionUsuario;
    private TipoMembresia tipo;
    private TipoMembresiaDuracion duracion;
    private double costo;
    private String fechaInicio;
    private String fechaVencimiento;
    private EstadoMembresia estado;
    private String fechaCompra;

    public Membresia() {}

    public Membresia(String codigo, String identificacionUsuario, TipoMembresia tipo,
                     TipoMembresiaDuracion duracion, double costo, String fechaInicio,
                     String fechaVencimiento, EstadoMembresia estado) {
        this.codigo = codigo;
        this.identificacionUsuario = identificacionUsuario;
        this.tipo = tipo;
        this.duracion = duracion;
        this.costo = costo;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.fechaCompra = LocalDate.now().toString();
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getIdentificacionUsuario() { return identificacionUsuario; }
    public void setIdentificacionUsuario(String identificacionUsuario) { this.identificacionUsuario = identificacionUsuario; }

    public TipoMembresia getTipo() { return tipo; }
    public void setTipo(TipoMembresia tipo) { this.tipo = tipo; }

    public TipoMembresiaDuracion getDuracion() { return duracion; }
    public void setDuracion(TipoMembresiaDuracion duracion) { this.duracion = duracion; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public EstadoMembresia getEstado() { return estado; }
    public void setEstado(EstadoMembresia estado) { this.estado = estado; }

    public String getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(String fechaCompra) { this.fechaCompra = fechaCompra; }

    public boolean estaVencida() {
        try {
            LocalDate fechaVencimiento = LocalDate.parse(this.fechaVencimiento);
            return LocalDate.now().isAfter(fechaVencimiento);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean estaActiva() {
        return this.estado == EstadoMembresia.ACTIVA && !estaVencida();
    }

}
