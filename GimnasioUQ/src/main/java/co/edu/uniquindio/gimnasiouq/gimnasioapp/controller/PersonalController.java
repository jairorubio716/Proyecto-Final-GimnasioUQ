package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Administrador;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Recepcionista;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Rol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonalController {
    ModelFactory modelFactory;

    public PersonalController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Object> obtenerPersonal() {
        ObservableList<Object> combinedList = FXCollections.observableArrayList();
        combinedList.addAll(modelFactory.getAdministradoresObservable());
        combinedList.addAll(modelFactory.getRecepcionistasObservable());
        return combinedList;
    }

    public boolean crearPersonal(String nombre, String identificacion, String correo, String contrasena, Rol rol) {
        if (rol == Rol.ADMIN) {
            return modelFactory.crearAdministrador(new Administrador(nombre, identificacion, correo, contrasena));
        } else if (rol == Rol.RECEPCIONISTA) {
            return modelFactory.crearRecepcionista(new Recepcionista(nombre, identificacion, correo, contrasena));
        }
        return false;
    }

    public boolean actualizarPersonal(String id, String nombre, String correo, String contrasena, Rol rol) {
        if (rol == Rol.ADMIN) {
            Administrador admin = new Administrador(nombre, id, correo, contrasena);
            return modelFactory.actualizarAdministrador(id, admin);
        } else if (rol == Rol.RECEPCIONISTA) {
            Recepcionista recep = new Recepcionista(nombre, id, correo, contrasena);
            return modelFactory.actualizarRecepcionista(id, recep);
        }
        return false;
    }

    public boolean eliminarPersonal(String id, Rol rol) {
        if (rol == Rol.ADMIN) {
            return modelFactory.eliminarAdministrador(id);
        } else if (rol == Rol.RECEPCIONISTA) {
            return modelFactory.eliminarRecepcionista(id);
        }
        return false;
    }
}
