package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

public class Estudiante extends Usuario {
    private String semestre;
    private String programa;

    public Estudiante() {}

    public Estudiante(String nombre, String identificacion, String edad, String telefono,
                      String semestre, String programa) {
        super(nombre, identificacion, edad, telefono);
        this.semestre = semestre;
        this.programa = programa;
    }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }
}
