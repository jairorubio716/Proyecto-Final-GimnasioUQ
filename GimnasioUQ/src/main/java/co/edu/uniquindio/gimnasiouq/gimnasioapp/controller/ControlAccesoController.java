package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Membresia;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Reserva;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Usuario;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class ControlAccesoController {
    ModelFactory modelFactory;

    public ControlAccesoController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public Usuario obtenerUsuario(String identificacion) {
        return modelFactory.obtenerUsuario(identificacion);
    }

    public Membresia obtenerMembresiaActivaUsuario(String identificacion) {
        return modelFactory.obtenerMembresiaActivaUsuario(identificacion);
    }

    public List<Reserva> obtenerReservasActivasUsuarioParaFecha(String idUsuario, LocalDate fecha) {
        return modelFactory.obtenerReservasActivasUsuarioParaFecha(idUsuario, fecha);
    }

    public boolean registrarAsistencia(String codigoReserva) {
        return modelFactory.registrarAsistencia(codigoReserva);
    }
    
    public ObservableList<Reserva> obtenerReservas() {
        return modelFactory.getReservasObservable();
    }


    public ObservableList<Usuario> obtenerUsuariosDentro() {
        return modelFactory.getUsuariosDentroObservable();
    }

    public boolean registrarIngreso(Usuario usuario) {
        return modelFactory.registrarIngreso(usuario);
    }

    public boolean registrarSalida(Usuario usuario) {
        return modelFactory.registrarSalida(usuario);
    }

    public boolean estaDentro(String id) {
        return modelFactory.estaDentro(id);
    }
}
