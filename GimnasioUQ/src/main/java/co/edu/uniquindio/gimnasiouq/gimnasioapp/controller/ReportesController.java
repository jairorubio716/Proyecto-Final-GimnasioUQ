package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Membresia;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Usuario;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Map;

public class ReportesController {
    ModelFactory modelFactory;

    public ReportesController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public List<Usuario> obtenerUsuariosConMembresiaActiva() {
        return modelFactory.obtenerUsuariosConMembresiaActiva();
    }

    public Map<String, Long> obtenerRankingClasesReservadas() {
        return modelFactory.obtenerRankingClasesReservadas();
    }

    public List<Membresia> obtenerMembresiasProximasAVencer(int dias) {
        return modelFactory.obtenerMembresiasProximasAVencer(dias);
    }

    public Membresia obtenerMembresiaActivaUsuario(String id) {
        return modelFactory.obtenerMembresiaActivaUsuario(id);
    }

    public Usuario obtenerUsuario(String id) {
        return modelFactory.obtenerUsuario(id);
    }
    
    public ObservableList<Usuario> obtenerTodosLosUsuarios() {
        return modelFactory.getUsuariosObservable();
    }
}
