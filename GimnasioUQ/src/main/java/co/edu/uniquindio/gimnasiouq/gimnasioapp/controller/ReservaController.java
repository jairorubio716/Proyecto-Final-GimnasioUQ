package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Clase;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Reserva;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class ReservaController {

    private ModelFactory modelFactory;

    public ReservaController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Reserva> obtenerReservas() {
        return modelFactory.getReservasObservable();
    }

    public boolean crearReserva(Reserva reserva) {
        return modelFactory.crearReserva(reserva);
    }

    public boolean cancelarReserva(String codigoReserva) {
        return modelFactory.cancelarReserva(codigoReserva);
    }

    public boolean usuarioPuedeReservar(String identificacionUsuario) {
        return modelFactory.usuarioPuedeReservar(identificacionUsuario);
    }

    public int cuposDisponibles(Clase clase, LocalDate fecha) {
        return modelFactory.cuposDisponibles(clase, fecha);
    }

    public boolean usuarioTieneReservaMismoHorario(String identificacionUsuario, Clase clase, LocalDate fecha) {
        return modelFactory.usuarioTieneReservaMismoHorario(identificacionUsuario, clase, fecha);
    }
}
