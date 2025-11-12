package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.EntrenadorController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Entrenador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CrudEntrenadorViewController {

    EntrenadorController entrenadorController;
    ObservableList<Entrenador> listaEntrenadores = FXCollections.observableArrayList();
    Entrenador entrenadorSeleccionado;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    // FXML Components
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtSueldo;
    @FXML private CheckBox checkDisponible;

    @FXML private TableView<Entrenador> tableEntrenador;
    @FXML private TableColumn<Entrenador, String> tcIdentificacion;
    @FXML private TableColumn<Entrenador, String> tcNombre;
    @FXML private TableColumn<Entrenador, String> tcTelefono;
    @FXML private TableColumn<Entrenador, String> tcCorreo;
    @FXML private TableColumn<Entrenador, String> tcSueldo;
    @FXML private TableColumn<Entrenador, String> tcDisponible;

    @FXML private Button btnNuevo;
    @FXML private Button btnAgregar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;

    @FXML
    void initialize() {
        entrenadorController = new EntrenadorController();
        initView();
    }

    private void initView() {
        initDataBinding();
        obtenerEntrenadores();
        tableEntrenador.getItems().clear();
        tableEntrenador.setItems(listaEntrenadores);
        listenerSelection();
    }

    private void initDataBinding() {
        tcIdentificacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdentificacion()));
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tcTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        tcCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorreo()));
        tcSueldo.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("$%,.0f", cellData.getValue().getSueldo())));
        tcDisponible.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isDisponible() ? "SÍ" : "NO"));
    }

    private void obtenerEntrenadores() {
        listaEntrenadores.addAll(entrenadorController.obtenerEntrenadores());
    }

    private void listenerSelection() {
        tableEntrenador.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            entrenadorSeleccionado = newSelection;
            mostrarInformacion(entrenadorSeleccionado);
        });
    }

    private void mostrarInformacion(Entrenador entrenador) {
        if (entrenador != null) {
            txtIdentificacion.setText(entrenador.getIdentificacion());
            txtNombre.setText(entrenador.getNombre());
            txtTelefono.setText(entrenador.getTelefono());
            txtCorreo.setText(entrenador.getCorreo());
            txtSueldo.setText(String.valueOf(entrenador.getSueldo()));
            checkDisponible.setSelected(entrenador.isDisponible());
            txtIdentificacion.setDisable(true); // No se puede editar la identificación
        } else {
            limpiarFormulario();
        }
    }

    @FXML
    void onActionAgregar(ActionEvent event) {
        crearEntrenador();
    }

    @FXML
    void onActionActualizar(ActionEvent event) {
        actualizarEntrenador();
    }

    @FXML
    void onActionNuevo(ActionEvent event) {
        limpiarFormulario();
    }

    @FXML
    void onActionEliminar(ActionEvent event) {
        eliminarEntrenador();
    }

    private void crearEntrenador() {
        if (validarCampos(true)) { // Validar con identificación
            Entrenador entrenador = crearEntrenadorDesdeFormulario();
            if (entrenadorController.crearEntrenador(entrenador)) {
                listaEntrenadores.add(entrenador);
                mostrarMensaje("Notificación", "Creación de Entrenador", "Entrenador creado con éxito", Alert.AlertType.CONFIRMATION);
                limpiarFormulario();
            } else {
                mostrarMensaje("Notificación", "Creación de Entrenador", "El entrenador no pudo ser creado (posiblemente identificación duplicada)", Alert.AlertType.WARNING);
            }
        }
    }

    private void actualizarEntrenador() {
        if (entrenadorSeleccionado != null) {
            if (validarCampos(false)) { // Validar sin identificación
                Entrenador entrenadorActualizado = crearEntrenadorDesdeFormulario();
                entrenadorActualizado.setIdentificacion(entrenadorSeleccionado.getIdentificacion()); // Mantener la ID original

                if (entrenadorController.actualizarEntrenador(entrenadorActualizado)) {
                    int index = listaEntrenadores.indexOf(entrenadorSeleccionado);
                    if (index != -1) {
                        listaEntrenadores.set(index, entrenadorActualizado);
                        tableEntrenador.refresh();
                    }
                    mostrarMensaje("Notificación", "Actualización de Entrenador", "Entrenador actualizado con éxito", Alert.AlertType.INFORMATION);
                    limpiarFormulario();
                } else {
                    mostrarMensaje("Notificación", "Actualización de Entrenador", "El entrenador no pudo ser actualizado", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarMensaje("Notificación", "Selección de Entrenador", "Debe seleccionar un entrenador para actualizar", Alert.AlertType.WARNING);
        }
    }

    private void eliminarEntrenador() {
        if (entrenadorSeleccionado != null) {
            if (mostrarMensajeConfirmacion("¿Está seguro de que desea eliminar al entrenador " + entrenadorSeleccionado.getNombre() + "?")) {
                if (entrenadorController.eliminarEntrenador(entrenadorSeleccionado.getIdentificacion())) {
                    listaEntrenadores.remove(entrenadorSeleccionado);
                    limpiarFormulario();
                    mostrarMensaje("Notificación", "Eliminación de Entrenador", "Entrenador eliminado con éxito", Alert.AlertType.INFORMATION);
                } else {
                    mostrarMensaje("Notificación", "Eliminación de Entrenador", "El entrenador no pudo ser eliminado", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarMensaje("Notificación", "Selección de Entrenador", "Debe seleccionar un entrenador para eliminar", Alert.AlertType.WARNING);
        }
    }

    private Entrenador crearEntrenadorDesdeFormulario() {
        Entrenador entrenador = new Entrenador(
                txtIdentificacion.getText(),
                txtNombre.getText(),
                txtTelefono.getText(),
                txtCorreo.getText(),
                Double.parseDouble(txtSueldo.getText())
        );
        entrenador.setDisponible(checkDisponible.isSelected());
        return entrenador;
    }

    private boolean validarCampos(boolean validarId) {
        if (validarId && txtIdentificacion.getText().isEmpty()) {
            mostrarMensaje("Validación", "Campo Vacío", "La identificación es obligatoria.", Alert.AlertType.WARNING);
            return false;
        }
        if (txtNombre.getText().isEmpty()) {
            mostrarMensaje("Validación", "Campo Vacío", "El nombre es obligatorio.", Alert.AlertType.WARNING);
            return false;
        }
        if (txtSueldo.getText().isEmpty()) {
            mostrarMensaje("Validación", "Campo Vacío", "El sueldo es obligatorio.", Alert.AlertType.WARNING);
            return false;
        }
        try {
            Double.parseDouble(txtSueldo.getText());
        } catch (NumberFormatException e) {
            mostrarMensaje("Validación", "Formato Incorrecto", "El sueldo debe ser un número válido.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        txtIdentificacion.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtSueldo.clear();
        checkDisponible.setSelected(true);
        tableEntrenador.getSelectionModel().clearSelection();
        txtIdentificacion.setDisable(false);
        entrenadorSeleccionado = null;
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private boolean mostrarMensajeConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText(mensaje);
        Optional<ButtonType> action = alert.showAndWait();
        return action.isPresent() && action.get() == ButtonType.OK;
    }
}
