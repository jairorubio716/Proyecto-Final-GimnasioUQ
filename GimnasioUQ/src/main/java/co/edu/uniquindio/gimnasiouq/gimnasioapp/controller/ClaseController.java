package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Clase;
import javafx.collections.ObservableList;

public class ClaseController {

    private ModelFactory modelFactory;

    public ClaseController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Clase> obtenerClases() {
        return modelFactory.getClasesObservable();
    }
}
