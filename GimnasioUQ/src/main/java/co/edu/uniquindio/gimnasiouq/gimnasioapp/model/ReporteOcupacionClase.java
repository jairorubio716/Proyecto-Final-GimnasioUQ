package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public class ReporteOcupacionClase {
    private String nombreClase;
    private int cuposOcupados;
    private int cupoMaximo;

    public ReporteOcupacionClase(String nombreClase, int cuposOcupados, int cupoMaximo) {
        this.nombreClase = nombreClase;
        this.cuposOcupados = cuposOcupados;
        this.cupoMaximo = cupoMaximo;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public int getCuposOcupados() {
        return cuposOcupados;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public double getPorcentajeOcupacion() {
        if (cupoMaximo == 0) {
            return 0.0;
        }
        return (double) cuposOcupados / cupoMaximo;
    }

    public String getOcupacionFormateada() {
        return cuposOcupados + " / " + cupoMaximo;
    }
}
