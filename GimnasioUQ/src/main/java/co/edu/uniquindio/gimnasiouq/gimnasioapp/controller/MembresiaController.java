package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Membresia;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Usuario;
import javafx.collections.ObservableList;

public class MembresiaController {

    private ModelFactory modelFactory;

    public MembresiaController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Membresia> obtenerMembresias() {
        return modelFactory.getMembresiasObservable();
    }

    public boolean crearMembresia(Membresia membresia, Usuario usuario) {
        return modelFactory.crearMembresia(membresia, usuario);
    }

    public boolean actualizarMembresia(Membresia membresia) {
        return modelFactory.actualizarMembresia(membresia);
    }

    public boolean eliminarMembresia(String codigo) {
        return modelFactory.eliminarMembresia(codigo);
    }

    public Membresia obtenerMembresia(String codigo) {
        return modelFactory.obtenerMembresia(codigo);
    }

    public boolean usuarioTieneMembresiaActiva(String identificacionUsuario) {
        return modelFactory.usuarioTieneMembresiaActiva(identificacionUsuario);
    }

    public Membresia obtenerMembresiaActivaUsuario(String identificacionUsuario) {
        return modelFactory.obtenerMembresiaActivaUsuario(identificacionUsuario);
    }
}
