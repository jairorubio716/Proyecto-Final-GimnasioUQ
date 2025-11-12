package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Entrenador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class EntrenadorController {
    private ModelFactory modelFactory;

    public EntrenadorController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Entrenador> obtenerEntrenadores() {
        return modelFactory.getEntrenadoresObservable();
    }

    public ObservableList<Entrenador> obtenerEntrenadoresDisponibles() {
        return FXCollections.observableArrayList(
            modelFactory.getEntrenadoresObservable().stream()
                .filter(Entrenador::isDisponible)
                .collect(Collectors.toList())
        );
    }

    public ObservableList<Entrenador> obtenerEntrenadoresDisponiblesParaHorario(DayOfWeek dia, LocalTime hora) {
        return FXCollections.observableArrayList(
            modelFactory.obtenerEntrenadoresDisponiblesParaHorario(dia, hora)
        );
    }

    public boolean crearEntrenador(Entrenador entrenador) {
        return modelFactory.crearEntrenador(entrenador);
    }

    public boolean actualizarEntrenador(Entrenador entrenador) {
        return modelFactory.actualizarEntrenador(entrenador);
    }

    public boolean eliminarEntrenador(String identificacion) {
        return modelFactory.eliminarEntrenador(identificacion);
    }
}
