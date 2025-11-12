package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public enum TipoClase {
    YOGA("Yoga", 20),
    SPINNING("Spinning", 15),
    PILATES("Pilates", 20),
    BOXEO("Boxeo", 25),
    ZUMBA("Zumba", 25),
    MUSCULACION("Musculación", 30),
    CARDIO("Cardio", 20),
    CROSSFIT("CrossFit", 15);

    private final String nombre;
    private final int cupoMaximo;

    TipoClase(String nombre, int cupoMaximo) {
        this.nombre = nombre;
        this.cupoMaximo = cupoMaximo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    @Override
    public String toString() {
        return nombre + " (" + cupoMaximo + " cupos)";
    }
}