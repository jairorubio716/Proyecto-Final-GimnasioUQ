package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.EntrenadorController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Entrenador;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.utils.AlertasUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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

    @FXML void onActionAgregar() { crearEntrenador(); }
    @FXML void onActionActualizar() { actualizarEntrenador(); }
    @FXML void onActionEliminar() { eliminarEntrenador(); }
    @FXML void onActionNuevo() { limpiarFormulario(); }

    private void crearEntrenador() {
        try {
            if (!validarCampos()) return;
            
            if (entrenadorController.crearEntrenador(
                    txtIdentificacion.getText(),
                    txtNombre.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText(),
                    Double.parseDouble(txtSueldo.getText()),
                    checkDisponible.isSelected()
            )) {
                listaEntrenadores = entrenadorController.obtenerEntrenadores();
                tableEntrenador.setItems(listaEntrenadores);
                AlertasUtil.mostrarInformacion("Creación Exitosa", "El entrenador ha sido creado.");
                limpiarFormulario();
            } else {
                AlertasUtil.mostrarError("La identificación ya existe.");
            }
        } catch (NumberFormatException e) {
            AlertasUtil.mostrarError("El sueldo debe ser un número válido.");
        } catch (Exception e) {
            AlertasUtil.mostrarError("Ocurrió un error inesperado al crear el entrenador: " + e.getMessage());
        }
    }

    private void actualizarEntrenador() {
        if (entrenadorSeleccionado == null) {
            AlertasUtil.mostrarAdvertencia("Debe seleccionar un entrenador.");
            return;
        }
        try {
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
                AlertasUtil.mostrarInformacion("Actualización Exitosa", "El entrenador ha sido actualizado.");
                limpiarFormulario();
            } else {
                AlertasUtil.mostrarError("No se pudo actualizar el entrenador.");
            }
        } catch (NumberFormatException e) {
            AlertasUtil.mostrarError("El sueldo debe ser un número válido.");
        } catch (Exception e) {
            AlertasUtil.mostrarError("Ocurrió un error inesperado al actualizar el entrenador: " + e.getMessage());
        }
    }

    private void eliminarEntrenador() {
        if (entrenadorSeleccionado != null && AlertasUtil.mostrarConfirmacion("¿Está seguro de que desea eliminar el entrenador?")) {
            try {
                if (entrenadorController.eliminarEntrenador(entrenadorSeleccionado.getIdentificacion())) {
                    listaEntrenadores.remove(entrenadorSeleccionado);
                    AlertasUtil.mostrarInformacion("Eliminación Exitosa", "El entrenador ha sido eliminado.");
                    limpiarFormulario();
                } else {
                    AlertasUtil.mostrarError("No se pudo eliminar el entrenador.");
                }
            } catch (Exception e) {
                AlertasUtil.mostrarError("Ocurrió un error inesperado al eliminar el entrenador: " + e.getMessage());
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
        entrenadorSeleccionado = null;
    }

    private boolean validarCampos() {
        if (txtIdentificacion.getText().isEmpty() || txtNombre.getText().isEmpty() || txtSueldo.getText().isEmpty()) {
            AlertasUtil.mostrarError("Identificación, Nombre y Sueldo son obligatorios.");
            return false;
        }
        try {
            Double.parseDouble(txtSueldo.getText());
        } catch (NumberFormatException e) {
            AlertasUtil.mostrarError("El sueldo debe ser un número.");
            return false;
        }
        return true;
    }
}
