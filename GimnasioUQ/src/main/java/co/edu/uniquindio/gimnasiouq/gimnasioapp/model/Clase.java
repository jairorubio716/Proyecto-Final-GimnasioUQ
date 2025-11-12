package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public class Clase {
    private String codigo;
    private String nombre;
    private TipoClase tipo;
    private String dia;
    private String horaInicio;
    private String horaFin;
    private int cupoMaximo;
    private Entrenador entrenador;
    private int cuposDisponibles;

    public Clase() {}

    public Clase(String codigo, String nombre, TipoClase tipo, String dia,
                 String horaInicio, String horaFin, int cupoMaximo, Entrenador entrenador) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cupoMaximo = cupoMaximo;
        this.entrenador = entrenador;
        this.cuposDisponibles = cupoMaximo;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoClase getTipo() { return tipo; }
    public void setTipo(TipoClase tipo) { this.tipo = tipo; }

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public Entrenador getEntrenador() { return entrenador; }
    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }

    public int getCuposDisponibles() { return cuposDisponibles; }
    public void setCuposDisponibles(int cuposDisponibles) { this.cuposDisponibles = cuposDisponibles; }

    public boolean hayCuposDisponibles() {
        return cuposDisponibles > 0;
    }

    public void reservarCupo() {
        if (hayCuposDisponibles()) {
            cuposDisponibles--;
        }
    }

    public void liberarCupo() {
        if (cuposDisponibles < cupoMaximo) {
            cuposDisponibles++;
        }
    }
}