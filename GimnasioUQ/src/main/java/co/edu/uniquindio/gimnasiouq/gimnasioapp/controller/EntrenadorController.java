package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Clase;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Entrenador;
import javafx.collections.ObservableList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EntrenadorController {
    ModelFactory modelFactory;

    public EntrenadorController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Entrenador> obtenerEntrenadores() {
        return modelFactory.getEntrenadoresObservable();
    }
    
    public boolean crearEntrenador(String id, String nombre, String tel, String correo, double sueldo, boolean disponible) {
        return modelFactory.crearEntrenador(id, nombre, tel, correo, sueldo, disponible);
    }

    public boolean actualizarEntrenador(String id, Entrenador e) {
        return modelFactory.actualizarEntrenador(id, e);
    }

    public boolean eliminarEntrenador(String id) {
        return modelFactory.eliminarEntrenador(id);
    }
    
    // SOLUCIÓN: Añadir el parámetro Clase claseSeleccionada
    public List<Entrenador> obtenerEntrenadoresDisponiblesParaHorario(DayOfWeek dia, LocalTime hora, LocalDate fecha, Clase claseSeleccionada) {
        return modelFactory.obtenerEntrenadoresDisponiblesParaHorario(dia, hora, fecha, claseSeleccionada);
    }
}
