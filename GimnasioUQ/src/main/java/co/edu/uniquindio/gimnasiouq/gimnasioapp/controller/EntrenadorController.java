package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Entrenador;
import javafx.collections.ObservableList;

import java.util.List;

public class EntrenadorController {
    private ModelFactory modelFactory;

    public EntrenadorController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Entrenador> obtenerEntrenadores() {
        return modelFactory.getEntrenadoresObservable();
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

    public List<Entrenador> obtenerEntrenadoresDisponibles() {
        return modelFactory.obtenerEntrenadoresDisponibles();
    }
}
