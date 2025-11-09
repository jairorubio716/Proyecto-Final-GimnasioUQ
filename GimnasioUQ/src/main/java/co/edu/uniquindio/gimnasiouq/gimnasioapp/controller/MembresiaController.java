package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Membresia;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Usuario;
import javafx.collections.ObservableList;

import java.util.List;

public class MembresiaController {
    ModelFactory modelFactory;

    public MembresiaController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public boolean crearMembresia(Membresia membresia) {
        return modelFactory.crearMembresia(membresia);
    }

    public boolean actualizarMembresia(Membresia membresia) {
        return modelFactory.actualizarMembresia(membresia);
    }

    public boolean eliminarMembresia(String codigo) {
        return modelFactory.eliminarMembresia(codigo);
    }

    public List<Membresia> obtenerMembresias() {
        return modelFactory.obtenerMembresias();
    }

    public ObservableList<Membresia> obtenerMembresiasObservable() {
        return modelFactory.getMembresiasObservable();
    }

    public ObservableList<Usuario> obtenerUsuariosObservable() {
        return modelFactory.getUsuariosObservable();
    }

    public boolean usuarioTieneMembresiaActiva(String identificacionUsuario) {
        return modelFactory.usuarioTieneMembresiaActiva(identificacionUsuario);
    }
}