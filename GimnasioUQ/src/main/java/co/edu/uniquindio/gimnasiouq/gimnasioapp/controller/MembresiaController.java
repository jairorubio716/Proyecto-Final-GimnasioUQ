package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import javafx.collections.ObservableList;

public class MembresiaController {
    ModelFactory modelFactory;

    public MembresiaController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Membresia> obtenerMembresias() {
        return modelFactory.getMembresiasObservable();
    }

    public boolean crearMembresia(String codigo, Usuario usuario, TipoMembresia tipo, TipoMembresiaDuracion duracion, String fechaInicio, String fechaFin, EstadoMembresia estado) {
        return modelFactory.crearMembresia(codigo, usuario, tipo, duracion, fechaInicio, fechaFin, estado);
    }

    public boolean actualizarMembresia(String codigo, Membresia data) {
        return modelFactory.actualizarMembresia(codigo, data);
    }

    public boolean eliminarMembresia(String codigo) {
        return modelFactory.eliminarMembresia(codigo);
    }
    
    public Membresia obtenerMembresiaActivaUsuario(String id) {
        return modelFactory.obtenerMembresiaActivaUsuario(id);
    }
}
