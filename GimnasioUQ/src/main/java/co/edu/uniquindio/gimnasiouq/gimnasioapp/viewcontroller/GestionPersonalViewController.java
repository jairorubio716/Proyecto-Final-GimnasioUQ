package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.PersonalController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Administrador;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Recepcionista;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Rol;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class GestionPersonalViewController {

    PersonalController personalController;
    ObservableList<Object> listaPersonal;
    Object personalSeleccionado;

    @FXML private TextField txtNombre, txtIdentificacion, txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<Rol> comboRol;
    @FXML private TableView<Object> tablePersonal;
    @FXML private TableColumn<Object, String> tcNombre, tcIdentificacion, tcCorreo, tcRol;

    @FXML
    void initialize() {
        personalController = new PersonalController();
        initView();
    }

    private void initView() {
        listaPersonal = personalController.obtenerPersonal();
        initDataBinding();
        configurarCombos();
        tablePersonal.setItems(listaPersonal);
        listenerSelection();
        limpiarFormulario();
    }

    @FXML void onActionAgregar(ActionEvent event) { crearPersonal(); }
    @FXML void onActionActualizar(ActionEvent event) { actualizarPersonal(); }
    @FXML void onActionEliminar(ActionEvent event) { eliminarPersonal(); }
    @FXML void onActionNuevo(ActionEvent event) { limpiarFormulario(); }

    private void crearPersonal() {
        if (!validarCampos()) return;

        String nombre = txtNombre.getText();
        String identificacion = txtIdentificacion.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        Rol rol = comboRol.getValue();

        if (personalController.crearPersonal(nombre, identificacion, correo, contrasena, rol)) {
            mostrarMensaje("Creación Exitosa", "El personal ha sido creado.", Alert.AlertType.INFORMATION);
            limpiarFormulario();
        } else {
            mostrarMensaje("Error de Creación", "No se pudo crear el personal. Verifique la identificación.", Alert.AlertType.ERROR);
        }
    }

    private void actualizarPersonal() {
        if (personalSeleccionado == null) {
            mostrarMensaje("Sin Selección", "Debe seleccionar un personal para actualizar.", Alert.AlertType.WARNING);
            return;
        }
        if (!validarCampos()) return;

        String nombre = txtNombre.getText();
        String identificacion = txtIdentificacion.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        Rol rol = comboRol.getValue();

        if (personalController.actualizarPersonal(identificacion, nombre, correo, contrasena, rol)) {
            tablePersonal.refresh();
            mostrarMensaje("Actualización Exitosa", "El personal ha sido actualizado.", Alert.AlertType.INFORMATION);
            limpiarFormulario();
        } else {
            mostrarMensaje("Error de Actualización", "No se pudo actualizar el personal.", Alert.AlertType.ERROR);
        }
    }

    private void eliminarPersonal() {
        if (personalSeleccionado == null) {
            mostrarMensaje("Sin Selección", "Debe seleccionar un personal para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        String identificacion = "";
        Rol rol = null;
        if (personalSeleccionado instanceof Administrador) {
            identificacion = ((Administrador) personalSeleccionado).getIdentificacion();
            rol = ((Administrador) personalSeleccionado).getRol();
        } else if (personalSeleccionado instanceof Recepcionista) {
            identificacion = ((Recepcionista) personalSeleccionado).getIdentificacion();
            rol = ((Recepcionista) personalSeleccionado).getRol();
        }

        if (identificacion.isEmpty() || rol == null) {
            mostrarMensaje("Error Interno", "No se pudo determinar el tipo de personal seleccionado.", Alert.AlertType.ERROR);
            return;
        }

        String nombrePersonal = "";
        if (personalSeleccionado instanceof Administrador) {
            nombrePersonal = ((Administrador) personalSeleccionado).getNombre();
        } else if (personalSeleccionado instanceof Recepcionista) {
            nombrePersonal = ((Recepcionista) personalSeleccionado).getNombre();
        }

        if (mostrarMensajeConfirmacion("¿Está seguro de que desea eliminar a '" + nombrePersonal + "'?")) {
            if (personalController.eliminarPersonal(identificacion, rol)) {
                mostrarMensaje("Eliminación Exitosa", "El personal ha sido eliminado.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
            } else {
                mostrarMensaje("Error de Eliminación", "No se pudo eliminar el personal.", Alert.AlertType.ERROR);
            }
        }
    }

    private void initDataBinding() {
        tcNombre.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            if (item instanceof Administrador) return new SimpleStringProperty(((Administrador) item).getNombre());
            if (item instanceof Recepcionista) return new SimpleStringProperty(((Recepcionista) item).getNombre());
            return new SimpleStringProperty("");
        });
        tcIdentificacion.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            if (item instanceof Administrador) return new SimpleStringProperty(((Administrador) item).getIdentificacion());
            if (item instanceof Recepcionista) return new SimpleStringProperty(((Recepcionista) item).getIdentificacion());
            return new SimpleStringProperty("");
        });
        tcCorreo.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            if (item instanceof Administrador) return new SimpleStringProperty(((Administrador) item).getCorreo());
            if (item instanceof Recepcionista) return new SimpleStringProperty(((Recepcionista) item).getCorreo());
            return new SimpleStringProperty("");
        });
        tcRol.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            if (item instanceof Administrador) return new SimpleStringProperty(Rol.ADMIN.name());
            if (item instanceof Recepcionista) return new SimpleStringProperty(Rol.RECEPCIONISTA.name());
            return new SimpleStringProperty("");
        });
    }

    private void configurarCombos() {
        comboRol.getItems().setAll(Rol.values());
        comboRol.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(Rol item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.name()); }
        });
        comboRol.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Rol item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.name()); }
        });
    }

    private void listenerSelection() {
        tablePersonal.getSelectionModel().selectedItemProperty().addListener((obs, o, newS) -> {
            personalSeleccionado = newS;
            mostrarInformacion(personalSeleccionado);
        });
    }

    private void mostrarInformacion(Object p) {
        if (p == null) {
            limpiarFormulario();
            return;
        }
        
        txtIdentificacion.setDisable(true);

        if (p instanceof Administrador) {
            Administrador admin = (Administrador) p;
            txtNombre.setText(admin.getNombre());
            txtIdentificacion.setText(admin.getIdentificacion());
            txtCorreo.setText(admin.getCorreo());
            txtContrasena.setText(admin.getContrasena());
            comboRol.setValue(Rol.ADMIN);
        } else if (p instanceof Recepcionista) {
            Recepcionista recep = (Recepcionista) p;
            txtNombre.setText(recep.getNombre());
            txtIdentificacion.setText(recep.getIdentificacion());
            txtCorreo.setText(recep.getCorreo());
            txtContrasena.setText(recep.getContrasena());
            comboRol.setValue(Rol.RECEPCIONISTA);
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtIdentificacion.clear();
        txtIdentificacion.setDisable(false);
        txtCorreo.clear();
        txtContrasena.clear();
        comboRol.getSelectionModel().clearSelection();
        tablePersonal.getSelectionModel().clearSelection();
        personalSeleccionado = null;
    }

    private boolean validarCampos() {
        String nombre = txtNombre.getText();
        String identificacion = txtIdentificacion.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        Rol rol = comboRol.getValue();

        if (nombre.isEmpty() || identificacion.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || rol == null) {
            mostrarMensaje("Error de Validación", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void mostrarMensaje(String titulo, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
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
}
