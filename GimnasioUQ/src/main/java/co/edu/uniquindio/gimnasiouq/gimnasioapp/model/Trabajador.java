package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public class Trabajador extends Usuario {
    private String cargo;

    public Trabajador() {}

    public Trabajador(String nombre, String identificacion, String edad, String telefono,
                      String cargo) {
        super(nombre, identificacion, edad, telefono);
        this.cargo = cargo;
    }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
}