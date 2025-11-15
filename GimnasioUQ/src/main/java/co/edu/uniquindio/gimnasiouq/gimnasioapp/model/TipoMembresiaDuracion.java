package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public enum TipoMembresiaDuracion {
    MENSUAL("Mensual", 1),
    TRIMESTRAL("Trimestral", 3),
    ANUAL("Anual", 12);

    private String nombre;
    private int meses;

    TipoMembresiaDuracion(String nombre, int meses) {
        this.nombre = nombre;
        this.meses = meses;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMeses() {
        return meses;
    }

    public void setMeses(int meses) {
        this.meses = meses;
    }
}
