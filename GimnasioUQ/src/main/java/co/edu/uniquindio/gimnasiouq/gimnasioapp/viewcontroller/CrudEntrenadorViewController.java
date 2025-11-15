package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.EntrenadorController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Entrenador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CrudEntrenadorViewController {

    EntrenadorController entrenadorController;
    ObservableList<Entrenador> listaEntrenadores;
    Entrenador entrenadorSeleccionado;

    @FXML private TextField txtIdentificacion, txtNombre, txtTelefono, txtCorreo, txtSueldo;
    @FXML private CheckBox checkDisponible;
    @FXML private TableView<Entrenador> tableEntrenador;
    @FXML private TableColumn<Entrenador, String> tcIdentificacion, tcNombre, tcTelefono, tcCorreo, tcSueldo, tcDisponible;

    @FXML
    void initialize() {
        entrenadorController = new EntrenadorController();
        initView();
    }

    private void initView() {
        listaEntrenadores = entrenadorController.obtenerEntrenadores();
        initDataBinding();
        tableEntrenador.setItems(listaEntrenadores);
        listenerSelection();
    }

    @FXML void onActionAgregar(ActionEvent event) { crearEntrenador(); }
    @FXML void onActionActualizar(ActionEvent event) { actualizarEntrenador(); }
    @FXML void onActionEliminar(ActionEvent event) { eliminarEntrenador(); }
    @FXML void onActionNuevo(ActionEvent event) { limpiarFormulario(); }

    private void crearEntrenador() {
        if (!validarCampos()) return;
        if (entrenadorController.crearEntrenador(
                txtIdentificacion.getText(),
                txtNombre.getText(),
                txtTelefono.getText(),
                txtCorreo.getText(),
                Double.parseDouble(txtSueldo.getText()),
                checkDisponible.isSelected()
        )) {
            mostrarMensaje("Creación Exitosa", "El entrenador ha sido creado.");
            limpiarFormulario();
        } else {
            mostrarMensaje("Error", "La identificación ya existe.");
        }
    }

    private void actualizarEntrenador() {
        if (entrenadorSeleccionado == null) {
            mostrarMensaje("Advertencia", "Debe seleccionar un entrenador.");
            return;
        }
        if (!validarCampos()) return;
        Entrenador entrenadorActualizado = new Entrenador(
                entrenadorSeleccionado.getIdentificacion(),
                txtNombre.getText(),
                txtTelefono.getText(),
                txtCorreo.getText(),
                Double.parseDouble(txtSueldo.getText())
        );
        entrenadorActualizado.setDisponible(checkDisponible.isSelected());
        
        if (entrenadorController.actualizarEntrenador(entrenadorSeleccionado.getIdentificacion(), entrenadorActualizado)) {
            tableEntrenador.refresh();
            mostrarMensaje("Actualización Exitosa", "El entrenador ha sido actualizado.");
            limpiarFormulario();
        } else {
            mostrarMensaje("Error", "No se pudo actualizar el entrenador.");
        }
    }

    private void eliminarEntrenador() {
        if (entrenadorSeleccionado != null && mostrarMensajeConfirmacion("¿Está seguro de que desea eliminar el entrenador?")) {
            if (entrenadorController.eliminarEntrenador(entrenadorSeleccionado.getIdentificacion())) {
                mostrarMensaje("Eliminación Exitosa", "El entrenador ha sido eliminado.");
                limpiarFormulario();
            }
        }
    }
    
    private void initDataBinding() {
        tcIdentificacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdentificacion()));
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tcTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        tcCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorreo()));
        tcSueldo.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("$%,.0f", cellData.getValue().getSueldo())));
        tcDisponible.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isDisponible() ? "SÍ" : "NO"));
    }

    private void listenerSelection() {
        tableEntrenador.getSelectionModel().selectedItemProperty().addListener((obs, o, newS) -> {
            entrenadorSeleccionado = newS;
            mostrarInformacion(entrenadorSeleccionado);
        });
    }

    private void mostrarInformacion(Entrenador e) {
        if (e != null) {
            txtIdentificacion.setText(e.getIdentificacion());
            txtIdentificacion.setDisable(true);
            txtNombre.setText(e.getNombre());
            txtTelefono.setText(e.getTelefono());
            txtCorreo.setText(e.getCorreo());
            txtSueldo.setText(String.valueOf(e.getSueldo()));
            checkDisponible.setSelected(e.isDisponible());
        }
    }

    private void limpiarFormulario() {
        txtIdentificacion.clear();
        txtIdentificacion.setDisable(false);
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtSueldo.clear();
        checkDisponible.setSelected(true);
        tableEntrenador.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        if (txtIdentificacion.getText().isEmpty() || txtNombre.getText().isEmpty() || txtSueldo.getText().isEmpty()) {
            mostrarMensaje("Error de Validación", "Identificación, Nombre y Sueldo son obligatorios.");
            return false;
        }
        try {
            Double.parseDouble(txtSueldo.getText());
        } catch (NumberFormatException e) {
            mostrarMensaje("Error de Formato", "El sueldo debe ser un número.");
            return false;
        }
        return true;
    }

    private void mostrarMensaje(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private boolean mostrarMensajeConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().filter(r -> r == ButtonType.OK).isPresent();
    }
}
