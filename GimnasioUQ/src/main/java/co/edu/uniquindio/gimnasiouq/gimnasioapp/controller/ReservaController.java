package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Reserva;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.TipoClase;
import javafx.collections.ObservableList;

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

    public boolean registrarAsistencia(String codigoReserva) {
        return modelFactory.registrarAsistencia(codigoReserva);
    }

    public List<Reserva> obtenerReservasUsuario(String identificacionUsuario) {
        return modelFactory.obtenerReservasUsuario(identificacionUsuario);
    }

    // Validaciones
    public boolean usuarioPuedeReservar(String identificacionUsuario) {
        return modelFactory.usuarioPuedeReservar(identificacionUsuario);
    }

    public int cuposDisponibles(TipoClase tipoClase, String fechaClase) {
        return modelFactory.cuposDisponibles(tipoClase, fechaClase);
    }

    public boolean usuarioTieneReservaMismoHorario(String identificacionUsuario, TipoClase tipoClase, String fechaClase) {
        return modelFactory.usuarioTieneReservaMismoHorario(identificacionUsuario, tipoClase, fechaClase);
    }
}
