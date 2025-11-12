package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Usuario;
import javafx.collections.ObservableList;

public class UsuarioController {
    ModelFactory modelFactory;

    public UsuarioController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public boolean crearUsuario(Usuario usuario) {
        return modelFactory.crearUsuario(usuario);
    }

    public boolean actualizarUsuario(Usuario usuario) {
        return modelFactory.actualizarUsuario(usuario);
    }

    public boolean eliminarUsuario(String identificacion) {
        return modelFactory.eliminarUsuario(identificacion);
    }

    public Usuario obtenerUsuario(String identificacion) {
        return modelFactory.obtenerUsuario(identificacion);
    }

    public ObservableList<Usuario> obtenerUsuarios() {
        return modelFactory.getUsuariosObservable();
    }

    public boolean existeUsuario(String identificacion) {
        return modelFactory.existeUsuario(identificacion);
    }
}
