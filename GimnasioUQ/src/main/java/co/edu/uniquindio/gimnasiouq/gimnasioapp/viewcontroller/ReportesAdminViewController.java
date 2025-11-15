package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.ReportesAdminController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Reserva;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Map;

public class ReportesAdminViewController {

    ReportesAdminController reportesAdminController;

    @FXML private BarChart<String, Number> chartIngresosMembresia;
    @FXML private BarChart<String, Number> chartClasesPopulares;
    @FXML private TableView<Reserva> tableHistorialAsistencias;
    @FXML private TableColumn<Reserva, String> tcUsuarioAsistencia;
    @FXML private TableColumn<Reserva, String> tcClaseAsistencia;
    @FXML private TableColumn<Reserva, String> tcFechaAsistencia;

    @FXML
    void initialize() {
        reportesAdminController = new ReportesAdminController();
        initDataBinding();
        cargarReportes();
    }

    @FXML
    void onActionRefrescar(ActionEvent event) {
        cargarReportes();
    }

    private void initDataBinding() {
        tcUsuarioAsistencia.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsuario().getNombre()));
        tcClaseAsistencia.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClase().getNombre()));
        tcFechaAsistencia.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaClase().toString()));
    }

    private void cargarReportes() {
        chartIngresosMembresia.getData().clear();
        XYChart.Series<String, Number> seriesIngresos = new XYChart.Series<>();
        seriesIngresos.setName("Ingresos ($)");
        Map<String, Double> ingresos = reportesAdminController.obtenerIngresosPorTipoMembresia();
        for (Map.Entry<String, Double> entry : ingresos.entrySet()) {
            seriesIngresos.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        chartIngresosMembresia.getData().add(seriesIngresos);

        chartClasesPopulares.getData().clear();
        XYChart.Series<String, Number> seriesClases = new XYChart.Series<>();
        seriesClases.setName("N° de Asistencias");
        Map<String, Long> ranking = reportesAdminController.obtenerRankingClasesAsistidas();
        for (Map.Entry<String, Long> entry : ranking.entrySet()) {
            seriesClases.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        chartClasesPopulares.getData().add(seriesClases);

        ObservableList<Reserva> historialAsistencias = FXCollections.observableArrayList(reportesAdminController.obtenerHistorialAsistencias());
        tableHistorialAsistencias.setItems(historialAsistencias);
    }
}
