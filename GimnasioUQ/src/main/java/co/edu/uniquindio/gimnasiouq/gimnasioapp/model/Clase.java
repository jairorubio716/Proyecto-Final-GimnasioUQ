package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public class Clase {
    private String nombre;
    private TipoClase tipo;
    private String horaInicio;    // NUEVO: "08:00"
    private String horaFin;       // NUEVO: "09:00"
    private String cupoMaximo;
    private Entrenador entrenador;

    public Clase() {
    }

    public Clase(String nombre, TipoClase tipo, String horaInicio, String horaFin,
                 String cupoMaximo, Entrenador entrenador) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cupoMaximo = cupoMaximo;
        this.entrenador = entrenador;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoClase getTipo() { return tipo; }
    public void setTipo(TipoClase tipo) { this.tipo = tipo; }

    public String getHoraInicio() { return horaInicio; }      // NUEVO
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }            // NUEVO
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public String getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(String cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public Entrenador getEntrenador() { return entrenador; }
    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }

    // ✅ MÉTODO PARA OBTENER HORARIO COMPLETO (opcional, para mostrar)
    public String getHorarioCompleto() {
        return horaInicio + " - " + horaFin;
    }
}