package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public class ReporteClase {
    private String nombreClase;
    private long numeroReservas;

    public ReporteClase(String nombreClase, long numeroReservas) {
        this.nombreClase = nombreClase;
        this.numeroReservas = numeroReservas;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public long getNumeroReservas() {
        return numeroReservas;
    }
}
