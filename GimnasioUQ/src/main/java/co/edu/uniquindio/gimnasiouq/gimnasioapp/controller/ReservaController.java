package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class ReservaController {
    ModelFactory modelFactory;

    public ReservaController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public ObservableList<Reserva> obtenerReservas() {
        return modelFactory.getReservasObservable();
    }

    public boolean crearReserva(String codigo, Usuario usuario, Clase clase, LocalDate fecha, Entrenador entrenador) {
        return modelFactory.crearReserva(codigo, usuario, clase, fecha, entrenador);
    }

    public boolean cancelarReserva(String codigo) {
        return modelFactory.cancelarReserva(codigo);
    }

    public int cuposDisponibles(Clase clase, LocalDate fecha) {
        return modelFactory.cuposDisponibles(clase, fecha);
    }

    // SOLUCIÓN: Añadir el método usuarioTieneReservaMismoHorario para que actúe como puente
    public boolean usuarioTieneReservaMismoHorario(String identificacionUsuario, Clase clase, LocalDate fecha) {
        return modelFactory.usuarioTieneReservaMismoHorario(identificacionUsuario, clase, fecha);
    }
}
