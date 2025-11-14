package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Usuario;
import javafx.collections.ObservableList;

public class UsuarioController {
    ModelFactory modelFactory;

    public UsuarioController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Usuario> obtenerUsuarios() {
        return modelFactory.getUsuariosObservable();
    }

    public boolean crearUsuario(String nombre, String id, String edad, String tel, String tipo, String... args) {
        return modelFactory.crearUsuario(nombre, id, edad, tel, tipo, args);
    }

    public boolean actualizarUsuario(String id, Usuario u) {
        return modelFactory.actualizarUsuario(id, u);
    }

    public boolean eliminarUsuario(String id) {
        return modelFactory.eliminarUsuario(id);
    }
}
