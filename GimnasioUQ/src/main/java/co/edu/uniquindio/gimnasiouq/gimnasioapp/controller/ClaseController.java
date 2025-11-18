package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Clase;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Entrenador;
import javafx.collections.ObservableList;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class ClaseController {
    ModelFactory modelFactory;

    public ClaseController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Clase> obtenerClases() {
        return modelFactory.getClasesObservable();
    }

    public boolean crearClase(String codigo, String nombre, DayOfWeek dia, LocalTime horario, LocalTime horaFin, int cupo, Entrenador entrenador) {
        return modelFactory.crearClase(codigo, nombre, dia, horario, horaFin, cupo, entrenador);
    }

    public boolean actualizarClase(String codigo, Clase c) {
        return modelFactory.actualizarClase(codigo, c);
    }

    public boolean eliminarClase(String codigo) {
        return modelFactory.eliminarClase(codigo);
    }
}
