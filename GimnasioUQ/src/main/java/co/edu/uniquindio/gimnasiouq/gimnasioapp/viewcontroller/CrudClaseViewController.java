package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.ClaseController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.EntrenadorController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Clase;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Entrenador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class CrudClaseViewController {

    ClaseController claseController;
    EntrenadorController entrenadorController;
    ObservableList<Clase> listaClases;
    ObservableList<Entrenador> listaEntrenadores;
    Clase claseSeleccionada;

    @FXML private TextField txtNombreClase, txtHorario, txtHoraFin, txtCupoMaximo;
    @FXML private ComboBox<DayOfWeek> comboDia;
    @FXML private ComboBox<Entrenador> comboEntrenadorDefecto;
    @FXML private TableView<Clase> tableClases;
    @FXML private TableColumn<Clase, String> tcNombre, tcDia, tcHorario, tcHoraFin, tcCupo, tcEntrenador;

    @FXML
    void initialize() {
        claseController = new ClaseController();
        entrenadorController = new EntrenadorController();
        initView();
    }

    private void initView() {
        listaClases = claseController.obtenerClases();
        listaEntrenadores = entrenadorController.obtenerEntrenadores();
        initDataBinding();
        configurarCombos();
        tableClases.setItems(listaClases);
        listenerSelection();
    }

    @FXML void onActionAgregar(ActionEvent event) { crearClase(); }
    @FXML void onActionActualizar(ActionEvent event) { actualizarClase(); }
    @FXML void onActionEliminar(ActionEvent event) { eliminarClase(); }
    @FXML void onActionNuevo(ActionEvent event) { limpiarFormulario(); }

    private void crearClase() {
        if (!validarCampos()) return;
        
        LocalTime horaInicio = LocalTime.parse(txtHorario.getText());
        LocalTime horaFin = LocalTime.parse(txtHoraFin.getText());

        Clase nuevaClase = new Clase(
                "CL-" + UUID.randomUUID().toString().substring(0, 4),
                txtNombreClase.getText(),
                comboDia.getValue(),
                horaInicio,
                horaFin,
                Integer.parseInt(txtCupoMaximo.getText()),
                comboEntrenadorDefecto.getValue()
        );

        if (claseController.crearClase(nuevaClase.getCodigo(), nuevaClase.getNombre(), nuevaClase.getDia(), nuevaClase.getHorario(), nuevaClase.getHoraFin(), nuevaClase.getCupoMaximo(), nuevaClase.getEntrenadorPorDefecto())) {
            mostrarMensaje("Creación Exitosa", "La clase ha sido creada.", Alert.AlertType.INFORMATION);
            limpiarFormulario();
        } else {
            mostrarMensaje("Error de Creación", "No se pudo crear la clase. Verifique el código o la disponibilidad del entrenador.", Alert.AlertType.ERROR);
        }
    }

    private void actualizarClase() {
        if (claseSeleccionada == null) {
            // SOLUCIÓN: Corregir la llamada a mostrarMensaje
            mostrarMensaje("Sin Selección", "Debe seleccionar una clase para actualizar.", Alert.AlertType.WARNING);
            return;
        }
        if (!validarCampos()) return;

        LocalTime horaInicio = LocalTime.parse(txtHorario.getText());
        LocalTime horaFin = LocalTime.parse(txtHoraFin.getText());

        Clase claseActualizada = new Clase(
                claseSeleccionada.getCodigo(),
                txtNombreClase.getText(),
                comboDia.getValue(),
                horaInicio,
                horaFin,
                Integer.parseInt(txtCupoMaximo.getText()),
                comboEntrenadorDefecto.getValue()
        );

        if (claseController.actualizarClase(claseSeleccionada.getCodigo(), claseActualizada)) {
            tableClases.refresh();
            mostrarMensaje("Actualización Exitosa", "La clase ha sido actualizada.", Alert.AlertType.INFORMATION);
            limpiarFormulario();
        } else {
            mostrarMensaje("Error de Actualización", "No se pudo actualizar la clase. Verifique la disponibilidad del entrenador.", Alert.AlertType.ERROR);
        }
    }

    private void eliminarClase() {
        if (claseSeleccionada != null && mostrarMensajeConfirmacion("¿Está seguro de que desea eliminar la clase '" + claseSeleccionada.getNombre() + "'?")) {
            if (claseController.eliminarClase(claseSeleccionada.getCodigo())) {
                mostrarMensaje("Eliminación Exitosa", "La clase ha sido eliminada.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
            } else {
                mostrarMensaje("Error de Eliminación", "No se pudo eliminar la clase.", Alert.AlertType.ERROR);
            }
        }
    }
    
    //<editor-fold desc="Métodos Auxiliares">
    private void initDataBinding() {
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tcDia.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDia().getDisplayName(TextStyle.FULL, new Locale("es", "ES"))));
        tcHorario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHorario().toString()));
        tcHoraFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoraFin().toString()));
        tcCupo.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCupoMaximo())));
        tcEntrenador.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEntrenadorPorDefecto().getNombre()));
    }

    private void configurarCombos() {
        comboDia.getItems().setAll(DayOfWeek.values());
        comboEntrenadorDefecto.setItems(listaEntrenadores);
        comboDia.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(DayOfWeek item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getDisplayName(TextStyle.FULL, new Locale("es", "ES"))); }
        });
        comboDia.setButtonCell(new ListCell<>() { // Añadido para mostrar el día seleccionado
            @Override protected void updateItem(DayOfWeek item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getDisplayName(TextStyle.FULL, new Locale("es", "ES"))); }
        });

        comboEntrenadorDefecto.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(Entrenador item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getNombre()); }
        });
        comboEntrenadorDefecto.setButtonCell(new ListCell<>() { // Añadido para mostrar el entrenador seleccionado
            @Override protected void updateItem(Entrenador item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getNombre()); }
        });
    }

    private void listenerSelection() {
        tableClases.getSelectionModel().selectedItemProperty().addListener((obs, o, newS) -> {
            claseSeleccionada = newS;
            mostrarInformacion(claseSeleccionada);
        });
    }

    private void mostrarInformacion(Clase c) {
        if (c != null) {
            txtNombreClase.setText(c.getNombre());
            txtNombreClase.setDisable(true);
            comboDia.setValue(c.getDia());
            txtHorario.setText(c.getHorario().toString());
            txtHoraFin.setText(c.getHoraFin().toString());
            txtCupoMaximo.setText(String.valueOf(c.getCupoMaximo()));
            comboEntrenadorDefecto.setValue(c.getEntrenadorPorDefecto());
        }
    }

    private void limpiarFormulario() {
        txtNombreClase.clear();
        txtNombreClase.setDisable(false);
        comboDia.getSelectionModel().clearSelection();
        txtHorario.clear();
        txtHoraFin.clear();
        txtCupoMaximo.clear();
        comboEntrenadorDefecto.getSelectionModel().clearSelection();
        tableClases.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        String nombre = txtNombreClase.getText();
        String horarioStr = txtHorario.getText();
        String horaFinStr = txtHoraFin.getText();
        String cupoMaximoStr = txtCupoMaximo.getText();
        DayOfWeek dia = comboDia.getValue();
        Entrenador entrenador = comboEntrenadorDefecto.getValue();

        if (nombre.isEmpty() || horarioStr.isEmpty() || horaFinStr.isEmpty() || cupoMaximoStr.isEmpty() || dia == null || entrenador == null) {
            mostrarMensaje("Error de Validación", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
            return false;
        }

        try {
            LocalTime horaInicio = LocalTime.parse(horarioStr);
            LocalTime horaFin = LocalTime.parse(horaFinStr);
            if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
                mostrarMensaje("Error de Validación", "La hora de fin debe ser posterior a la hora de inicio.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (DateTimeParseException e) {
            mostrarMensaje("Error de Formato", "El formato de la hora debe ser HH:mm (Ej: 08:00).", Alert.AlertType.WARNING);
            return false;
        }

        try {
            int cupo = Integer.parseInt(cupoMaximoStr);
            if (cupo <= 0) {
                mostrarMensaje("Error de Validación", "El cupo máximo debe ser un número positivo.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Error de Formato", "El cupo máximo debe ser un número entero.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void mostrarMensaje(String titulo, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // Siempre null para este método
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private boolean mostrarMensajeConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText(mensaje);
        return alert.showAndWait().filter(r -> r == ButtonType.OK).isPresent();
    }
    //</editor-fold>
}
