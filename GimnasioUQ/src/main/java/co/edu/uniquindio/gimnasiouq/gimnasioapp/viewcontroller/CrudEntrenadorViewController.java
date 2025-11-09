package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.EntrenadorController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Entrenador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CrudEntrenadorViewController implements Initializable {

    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<String> comboEspecialidad;
    @FXML private TextField txtSueldo;
    @FXML private TableView<Entrenador> tableEntrenador;
    @FXML private TableColumn<Entrenador, String> tcIdentificacion;
    @FXML private TableColumn<Entrenador, String> tcNombre;
    @FXML private TableColumn<Entrenador, String> tcTelefono;
    @FXML private TableColumn<Entrenador, String> tcCorreo;
    @FXML private TableColumn<Entrenador, String> tcEspecialidad;
    @FXML private TableColumn<Entrenador, String> tcSueldo;

    private EntrenadorController entrenadorController;
    private ObservableList<Entrenador> listaEntrenadoresObservable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entrenadorController = new EntrenadorController();
        listaEntrenadoresObservable = entrenadorController.obtenerEntrenadoresObservable();

        configurarCombos();
        configurarTabla();

        tableEntrenador.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        llenarFormularioConEntrenador(newValue);
                    }
                }
        );
    }

    private void configurarCombos() {
        comboEspecialidad.getItems().setAll(entrenadorController.obtenerEspecialidadesDisponibles());
    }

    private void configurarTabla() {
        tableEntrenador.setItems(listaEntrenadoresObservable);

        tcIdentificacion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIdentificacion()));
        tcNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombre()));
        tcTelefono.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTelefono()));
        tcCorreo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCorreo()));
        tcEspecialidad.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEspecialidad()));
        tcSueldo.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("$%,.0f", cellData.getValue().getSueldo())));
    }

    @FXML
    private void onActionNuevo() {
        limpiarFormulario();
    }

    @FXML
    private void onActionCrear() {
        if (validarFormulario()) {
            Entrenador entrenador = crearEntrenadorDesdeFormulario();
            if (entrenadorController.crearEntrenador(entrenador)) {
                mostrarAlerta("Éxito", "Entrenador creado correctamente");
                limpiarFormulario();
                tableEntrenador.setItems(entrenadorController.obtenerEntrenadoresObservable());
            } else {
                mostrarAlerta("Error", "No se pudo crear el entrenador");
            }
        }
    }

    @FXML
    private void onActionActualizar() {
        Entrenador entrenadorSeleccionado = tableEntrenador.getSelectionModel().getSelectedItem();
        if (entrenadorSeleccionado != null && validarFormulario()) {
            Entrenador entrenadorActualizado = crearEntrenadorDesdeFormulario();
            entrenadorActualizado.setIdentificacion(entrenadorSeleccionado.getIdentificacion());

            if (entrenadorController.actualizarEntrenador(entrenadorActualizado)) {
                tableEntrenador.refresh();
                limpiarFormulario();
                mostrarAlerta("Éxito", "Entrenador actualizado correctamente");
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el entrenador");
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione un entrenador para actualizar");
        }
    }

    @FXML
    private void onActionEliminar() {
        Entrenador entrenadorSeleccionado = tableEntrenador.getSelectionModel().getSelectedItem();
        if (entrenadorSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("Eliminar Entrenador");
            confirmacion.setContentText("¿Está seguro de eliminar al entrenador " + entrenadorSeleccionado.getNombre() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                if (entrenadorController.eliminarEntrenador(entrenadorSeleccionado.getIdentificacion())) {
                    limpiarFormulario();
                    mostrarAlerta("Éxito", "Entrenador eliminado correctamente");
                    tableEntrenador.setItems(entrenadorController.obtenerEntrenadoresObservable());
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el entrenador");
                }
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione un entrenador para eliminar");
        }
    }

    // Métodos auxiliares
    private boolean validarFormulario() {
        if (txtIdentificacion.getText().isEmpty()) {
            mostrarAlerta("Error", "Ingrese la identificación");
            return false;
        }
        if (txtNombre.getText().isEmpty()) {
            mostrarAlerta("Error", "Ingrese el nombre");
            return false;
        }
        if (txtTelefono.getText().isEmpty()) {
            mostrarAlerta("Error", "Ingrese el teléfono");
            return false;
        }
        if (txtCorreo.getText().isEmpty()) {
            mostrarAlerta("Error", "Ingrese el correo");
            return false;
        }
        if (comboEspecialidad.getValue() == null) {
            mostrarAlerta("Error", "Seleccione la especialidad");
            return false;
        }
        if (txtSueldo.getText().isEmpty()) {
            mostrarAlerta("Error", "Ingrese el sueldo");
            return false;
        }
        try {
            Double.parseDouble(txtSueldo.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El sueldo debe ser un número válido");
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        txtIdentificacion.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        comboEspecialidad.getSelectionModel().clearSelection();
        txtSueldo.clear();

        // ✅ AGREGAR ESTO: Deseleccionar cualquier fila de la tabla
        tableEntrenador.getSelectionModel().clearSelection();
    }

    private Entrenador crearEntrenadorDesdeFormulario() {
        return new Entrenador(
                txtIdentificacion.getText(),
                txtNombre.getText(),
                txtTelefono.getText(),
                txtCorreo.getText(),
                comboEspecialidad.getValue(),
                Double.parseDouble(txtSueldo.getText())
        );
    }

    private void llenarFormularioConEntrenador(Entrenador entrenador) {
        txtIdentificacion.setText(entrenador.getIdentificacion());
        txtNombre.setText(entrenador.getNombre());
        txtTelefono.setText(entrenador.getTelefono());
        txtCorreo.setText(entrenador.getCorreo());
        comboEspecialidad.setValue(entrenador.getEspecialidad());
        txtSueldo.setText(String.valueOf(entrenador.getSueldo()));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}