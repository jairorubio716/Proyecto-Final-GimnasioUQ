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
    private int cupoMaximo;

    public Clase() {}

    // Constructor SIN entrenador
    public Clase(String codigo, String nombre, DayOfWeek dia, LocalTime horario, int cupoMaximo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.dia = dia;
        this.horario = horario;
        this.cupoMaximo = cupoMaximo;
    }

    // Getters y Setters (sin los de entrenador)
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public DayOfWeek getDia() { return dia; }
    public void setDia(DayOfWeek dia) { this.dia = dia; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    @Override
    public String toString() {
        // Formato amigable para mostrar en ComboBoxes
        if (nombre == null || dia == null || horario == null) {
            return "Clase no definida";
        }
        String diaSemana = dia.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        return nombre + " - " + diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1) + " " + horario;
    }
}
