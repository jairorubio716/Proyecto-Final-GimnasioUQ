package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public class Externo extends Usuario {
    private String institucion;

    public Externo() {}

    public Externo(String nombre, String identificacion, String edad, String telefono,
                   String institucion) {
        super(nombre, identificacion, edad, telefono);
        this.institucion = institucion;
    }

    public String getInstitucion() { return institucion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }
}
