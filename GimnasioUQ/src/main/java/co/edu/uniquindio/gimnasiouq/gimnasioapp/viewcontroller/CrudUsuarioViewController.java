package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.UsuarioController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CrudUsuarioViewController {

    UsuarioController usuarioController;
    ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    Usuario usuarioSeleccionado;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    // FXML Components
    @FXML private TextField txtNombre;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtEdad;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox<String> comboTipoUsuario;
    @FXML private TextField txtSemestre;
    @FXML private TextField txtPrograma;
    @FXML private TextField txtCargo;
    @FXML private TextField txtInstitucion;

    @FXML private AnchorPane panelEstudiante;
    @FXML private AnchorPane panelTrabajador;
    @FXML private AnchorPane panelExterno;

    @FXML private TableView<Usuario> tableUsuario;
    @FXML private TableColumn<Usuario, String> tcNombre;
    @FXML private TableColumn<Usuario, String> tcIdentificacion;
    @FXML private TableColumn<Usuario, String> tcEdad;
    @FXML private TableColumn<Usuario, String> tcTelefono;
    @FXML private TableColumn<Usuario, String> tcTipoUsuario;
    @FXML private TableColumn<Usuario, String> tcDetalleEspecifico;

    @FXML private Button btnNuevo;
    @FXML private Button btnAgregar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;

    @FXML
    void initialize() {
        usuarioController = new UsuarioController();
        initView();
        configurarCombos();
    }

    private void initView() {
        initDataBinding();
        obtenerUsuarios();
        tableUsuario.getItems().clear();
        tableUsuario.setItems(listaUsuarios);
        listenerSelection();
        ocultarTodosLosPaneles();
    }

    private void initDataBinding() {
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tcIdentificacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdentificacion()));
        tcEdad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEdad()));
        tcTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        tcTipoUsuario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));
        tcDetalleEspecifico.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            if (usuario instanceof Estudiante) {
                Estudiante est = (Estudiante) usuario;
                return new SimpleStringProperty("Sem " + est.getSemestre() + " - " + est.getPrograma());
            } else if (usuario instanceof Trabajador) {
                Trabajador trab = (Trabajador) usuario;
                return new SimpleStringProperty(trab.getCargo());
            } else if (usuario instanceof Externo) {
                Externo ext = (Externo) usuario;
                return new SimpleStringProperty(ext.getInstitucion());
            }
            return new SimpleStringProperty("");
        });
    }

    private void obtenerUsuarios() {
        listaUsuarios.addAll(usuarioController.obtenerUsuarios());
    }

    private void listenerSelection() {
        tableUsuario.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            usuarioSeleccionado = newSelection;
            mostrarInformacion(usuarioSeleccionado);
        });
    }

    private void mostrarInformacion(Usuario usuario) {
        if (usuario != null) {
            txtNombre.setText(usuario.getNombre());
            txtIdentificacion.setText(usuario.getIdentificacion());
            txtEdad.setText(usuario.getEdad());
            txtTelefono.setText(usuario.getTelefono());

            if (usuario instanceof Estudiante) {
                comboTipoUsuario.setValue("Estudiante");
                Estudiante est = (Estudiante) usuario;
                txtSemestre.setText(est.getSemestre());
                txtPrograma.setText(est.getPrograma());
            } else if (usuario instanceof Trabajador) {
                comboTipoUsuario.setValue("Trabajador");
                Trabajador trab = (Trabajador) usuario;
                txtCargo.setText(trab.getCargo());
            } else if (usuario instanceof Externo) {
                comboTipoUsuario.setValue("Externo");
                Externo ext = (Externo) usuario;
                txtInstitucion.setText(ext.getInstitucion());
            }
        }
    }

    @FXML
    void onActionAgregar(ActionEvent event) {
        crearUsuario();
    }

    @FXML
    void onActionActualizar(ActionEvent event) {
        actualizarUsuario();
    }

    @FXML
    void onActionNuevo(ActionEvent event) {
        limpiarFormulario();
    }

    @FXML
    void onActionEliminar(ActionEvent event) {
        eliminarUsuario();
    }

    private void crearUsuario() {
        if (validarCampos()) {
            Usuario usuario = crearUsuarioDesdeFormulario();
            if (usuarioController.crearUsuario(usuario)) {
                listaUsuarios.add(usuario);
                mostrarMensaje("Notificación", "Creación de Usuario", "Usuario creado con éxito", Alert.AlertType.CONFIRMATION);
                limpiarFormulario();
            } else {
                mostrarMensaje("Notificación", "Creación de Usuario", "El usuario no pudo ser creado (posiblemente identificación duplicada)", Alert.AlertType.WARNING);
            }
        }
    }

    private void actualizarUsuario() {
        if (usuarioSeleccionado != null) {
            if (validarCampos()) {
                Usuario usuarioActualizado = crearUsuarioDesdeFormulario();

                usuarioActualizado.setIdentificacion(usuarioSeleccionado.getIdentificacion());

                if (usuarioController.actualizarUsuario(usuarioActualizado)) {

                    int index = listaUsuarios.indexOf(usuarioSeleccionado);
                    if (index != -1) {
                        listaUsuarios.set(index, usuarioActualizado);
                        tableUsuario.refresh();
                    }
                    mostrarMensaje("Notificación", "Actualización de Usuario", "Usuario actualizado con éxito", Alert.AlertType.INFORMATION);
                    limpiarFormulario();
                } else {
                    mostrarMensaje("Notificación", "Actualización de Usuario", "El usuario no pudo ser actualizado", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarMensaje("Notificación", "Selección de Usuario", "Debe seleccionar un usuario para actualizar", Alert.AlertType.WARNING);
        }
    }

    private void eliminarUsuario() {
        if (usuarioSeleccionado != null) {
            if (mostrarMensajeConfirmacion("¿Está seguro de que desea eliminar al usuario " + usuarioSeleccionado.getNombre() + "?")) {
                if (usuarioController.eliminarUsuario(usuarioSeleccionado.getIdentificacion())) {
                    listaUsuarios.remove(usuarioSeleccionado);
                    usuarioSeleccionado = null;
                    limpiarFormulario();
                    mostrarMensaje("Notificación", "Eliminación de Usuario", "Usuario eliminado con éxito", Alert.AlertType.INFORMATION);
                } else {
                    mostrarMensaje("Notificación", "Eliminación de Usuario", "El usuario no pudo ser eliminado", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarMensaje("Notificación", "Selección de Usuario", "Debe seleccionar un usuario para eliminar", Alert.AlertType.WARNING);
        }
    }

    private Usuario crearUsuarioDesdeFormulario() {
        String tipoUsuario = comboTipoUsuario.getValue();
        String nombre = txtNombre.getText();
        String identificacion = txtIdentificacion.getText();
        String edad = txtEdad.getText();
        String telefono = txtTelefono.getText();

        switch (tipoUsuario) {
            case "Estudiante":
                return new Estudiante(nombre, identificacion, edad, telefono, txtSemestre.getText(), txtPrograma.getText());
            case "Trabajador":
                return new Trabajador(nombre, identificacion, edad, telefono, txtCargo.getText());
            case "Externo":
                return new Externo(nombre, identificacion, edad, telefono, txtInstitucion.getText());
            default:
                return null;
        }
    }

    private boolean validarCampos() {
        String nombre = txtNombre.getText();
        String identificacion = txtIdentificacion.getText();
        String tipoUsuario = comboTipoUsuario.getValue();

        if (nombre == null || nombre.isEmpty() || identificacion == null || identificacion.isEmpty() || tipoUsuario == null) {
            mostrarMensaje("Notificación", "Validación de Campos", "Los campos Nombre, Identificación y Tipo de Usuario son obligatorios.", Alert.AlertType.WARNING);
            return false;
        }

        switch (tipoUsuario) {
            case "Estudiante":
                if (txtSemestre.getText().isEmpty() || txtPrograma.getText().isEmpty()) {
                    mostrarMensaje("Notificación", "Validación de Campos", "Para un estudiante, Semestre y Programa son obligatorios.", Alert.AlertType.WARNING);
                    return false;
                }
                break;
            case "Trabajador":
                if (txtCargo.getText().isEmpty()) {
                    mostrarMensaje("Notificación", "Validación de Campos", "Para un trabajador, el Cargo es obligatorio.", Alert.AlertType.WARNING);
                    return false;
                }
                break;
            case "Externo":
                if (txtInstitucion.getText().isEmpty()) {
                    mostrarMensaje("Notificación", "Validación de Campos", "Para un externo, la Institución es obligatoria.", Alert.AlertType.WARNING);
                    return false;
                }
                break;
        }
        return true;
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtIdentificacion.clear();
        txtEdad.clear();
        txtTelefono.clear();
        txtSemestre.clear();
        txtPrograma.clear();
        txtCargo.clear();
        txtInstitucion.clear();
        comboTipoUsuario.getSelectionModel().clearSelection();
        ocultarTodosLosPaneles();
        tableUsuario.getSelectionModel().clearSelection();
        usuarioSeleccionado = null;
    }


    private void configurarCombos() {
        comboTipoUsuario.getItems().setAll("Estudiante", "Trabajador", "Externo");
        comboTipoUsuario.valueProperty().addListener((obs, oldVal, newVal) -> mostrarCamposEspecificos(newVal));
    }

    private void ocultarTodosLosPaneles() {
        panelEstudiante.setVisible(false);
        panelEstudiante.setManaged(false);
        panelTrabajador.setVisible(false);
        panelTrabajador.setManaged(false);
        panelExterno.setVisible(false);
        panelExterno.setManaged(false);
    }

    private void mostrarCamposEspecificos(String tipoUsuario) {
        ocultarTodosLosPaneles();
        if (tipoUsuario == null) return;

        switch (tipoUsuario) {
            case "Estudiante":
                panelEstudiante.setVisible(true);
                panelEstudiante.setManaged(true);
                break;
            case "Trabajador":
                panelTrabajador.setVisible(true);
                panelTrabajador.setManaged(true);
                break;
            case "Externo":
                panelExterno.setVisible(true);
                panelExterno.setManaged(true);
                break;
        }
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
