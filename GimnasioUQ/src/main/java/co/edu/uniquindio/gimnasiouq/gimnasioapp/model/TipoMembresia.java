package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public enum TipoMembresia {
    BASICA("Básica", 50000, "Acceso general a máquinas"),
    PREMIUM("Premium", 80000, "Acceso a máquinas y clases grupales"),
    VIP("VIP", 120000, "Acceso ilimitado a todas las clases, área de spa y entrenador personal");

    private final String nombre;
    private final double costoMensual;
    private final String beneficios;

    TipoMembresia(String nombre, double costoMensual, String beneficios) {
        this.nombre = nombre;
        this.costoMensual = costoMensual;
        this.beneficios = beneficios;
    }

    public String getNombre() { return nombre; }
    public double getCostoMensual() { return costoMensual; }
    public String getBeneficios() { return beneficios; }
}