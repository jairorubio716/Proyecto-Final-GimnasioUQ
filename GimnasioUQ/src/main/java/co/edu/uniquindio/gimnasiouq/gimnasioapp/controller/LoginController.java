package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Rol;

public class LoginController {
    ModelFactory modelFactory;

    public LoginController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public Rol validarCredenciales(String username, String password) {
        return modelFactory.validarCredenciales(username, password);
    }
}
