package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.ReportesController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.UsuarioController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Membresia;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class ReportesViewController {

    ReportesController reportesController;
    UsuarioController usuarioController;
    ObservableList<Usuario> listaUsuarios;

    @FXML private ListView<Usuario> listUsuariosActivos;
    @FXML private TableView<Membresia> tableMembresiasPorVencer;
    @FXML private TableColumn<Membresia, String> tcUsuarioMembresiaVence;
    @FXML private TableColumn<Membresia, String> tcFechaVencimiento;
    @FXML private TableColumn<Membresia, String> tcDiasRestantes;
    
    @FXML private BarChart<String, Number> chartClasesPopulares;

    @FXML
    void initialize() {
        reportesController = new ReportesController();
        usuarioController = new UsuarioController();
        initView();
    }

    private void initView() {
        listaUsuarios = usuarioController.obtenerUsuarios();
        initDataBinding();
        cargarReportes();
    }

    @FXML
    void onActionRefrescar(ActionEvent event) {
        cargarReportes();
    }

    private void initDataBinding() {
        listUsuariosActivos.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Membresia m = reportesController.obtenerMembresiaActivaUsuario(item.getIdentificacion());
                    setText(item.getNombre() + " (" + (m != null ? m.getTipo().getNombre() : "N/A") + ")");
                }
            }
        });

        tcUsuarioMembresiaVence.setCellValueFactory(cellData -> {
            String idUsuario = cellData.getValue().getIdentificacionUsuario();
            String nombre = listaUsuarios.stream()
                    .filter(u -> u.getIdentificacion().equals(idUsuario))
                    .map(Usuario::getNombre)
                    .findFirst()
                    .orElse("ID: " + idUsuario);
            return new SimpleStringProperty(nombre);
        });
        tcFechaVencimiento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaVencimiento()));
        tcDiasRestantes.setCellValueFactory(cellData -> {
            long dias = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(cellData.getValue().getFechaVencimiento()));
            return new SimpleStringProperty(String.valueOf(dias));
        });
    }

    private void cargarReportes() {
        ObservableList<Usuario> usuariosActivos = FXCollections.observableArrayList(reportesController.obtenerUsuariosConMembresiaActiva());
        listUsuariosActivos.setItems(usuariosActivos);

        ObservableList<Membresia> membresiasPorVencer = FXCollections.observableArrayList(reportesController.obtenerMembresiasProximasAVencer(30));
        tableMembresiasPorVencer.setItems(membresiasPorVencer);

        chartClasesPopulares.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Número de Reservas");
        Map<String, Long> ranking = reportesController.obtenerRankingClasesReservadas();
        for (Map.Entry<String, Long> entry : ranking.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        chartClasesPopulares.getData().add(series);
    }
}
