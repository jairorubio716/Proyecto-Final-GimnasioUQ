package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class Clase {
    private String codigo;
    private String nombre;
    private DayOfWeek dia;
    private LocalTime horario;
    private LocalTime horaFin;
    private int cupoMaximo;
    private Entrenador entrenadorPorDefecto;

    public Clase() {}

    public Clase(String codigo, String nombre, DayOfWeek dia, LocalTime horario, LocalTime horaFin, int cupoMaximo, Entrenador entrenadorPorDefecto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.dia = dia;
        this.horario = horario;
        this.horaFin = horaFin;
        this.cupoMaximo = cupoMaximo;
        this.entrenadorPorDefecto = entrenadorPorDefecto;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public DayOfWeek getDia() { return dia; }
    public void setDia(DayOfWeek dia) { this.dia = dia; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public Entrenador getEntrenadorPorDefecto() { return entrenadorPorDefecto; }
    public void setEntrenadorPorDefecto(Entrenador entrenadorPorDefecto) { this.entrenadorPorDefecto = entrenadorPorDefecto; }

    @Override
    public String toString() {
        if (nombre == null || dia == null || horario == null || horaFin == null) {
            return "Clase no definida";
        }
        String diaSemana = dia.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        String texto = nombre + " - " + diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1) + " " + horario + "-" + horaFin;
        if (entrenadorPorDefecto != null) {
            texto += " (con " + entrenadorPorDefecto.getNombre() + ")";
        }
        return texto;
    }
}
