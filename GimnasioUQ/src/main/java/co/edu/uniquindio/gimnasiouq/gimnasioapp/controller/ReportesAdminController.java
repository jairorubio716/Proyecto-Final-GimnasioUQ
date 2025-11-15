package co.edu.uniquindio.gimnasiouq.gimnasioapp.controller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.factory.ModelFactory;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Reserva;

import java.util.List;
import java.util.Map;

public class ReportesAdminController {
    ModelFactory modelFactory;

    public ReportesAdminController() {
        this.modelFactory = ModelFactory.getInstancia();
    }

    public Map<String, Double> obtenerIngresosPorTipoMembresia() {
        return modelFactory.obtenerIngresosPorTipoMembresia();
    }

    public Map<String, Long> obtenerRankingClasesAsistidas() {
        return modelFactory.obtenerRankingClasesAsistidas();
    }

    public List<Reserva> obtenerHistorialAsistencias() {
        return modelFactory.obtenerHistorialAsistencias();
    }
}
