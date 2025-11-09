package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.UsuarioController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CrudUsuarioViewController implements Initializable {

    // COMPONENTES DATOS PERSONALES
    @FXML private TextField txtNombre;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtEdad;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox<String> comboTipoUsuario;

    // CAMPOS ESPECÍFICOS SEGÚN TIPO
    @FXML private TextField txtSemestre;
    @FXML private TextField txtPrograma;
    @FXML private TextField txtCargo;
    @FXML private TextField txtInstitucion;

    // LABELS PARA CAMPOS ESPECÍFICOS
    @FXML private Label lblSemestre;
    @FXML private Label lblPrograma;
    @FXML private Label lblCargo;
    @FXML private Label lblInstitucion;

    // PANELES PARA ORGANIZAR
    @FXML private AnchorPane panelEstudiante;
    @FXML private AnchorPane panelTrabajador;
    @FXML private AnchorPane panelExterno;

    // TABLA
    @FXML private TableView<Usuario> tableUsuario;
    @FXML private TableColumn<Usuario, String> tcNombre;
    @FXML private TableColumn<Usuario, String> tcIdentificacion;
    @FXML private TableColumn<Usuario, String> tcEdad;
    @FXML private TableColumn<Usuario, String> tcTelefono;
    @FXML private TableColumn<Usuario, String> tcTipoUsuario;
    @FXML private TableColumn<Usuario, String> tcDetalleEspecifico;

    // BOTONES
    @FXML private Button btnNuevoUsuario;
    @FXML private Button btnCrearUsuario;
    @FXML private Button btnActualizarUsuario;
    @FXML private Button btnEliminarUsuario;

    private UsuarioController usuarioController;
    private ObservableList<Usuario> listaUsuariosObservable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuarioController = new UsuarioController();
        listaUsuariosObservable = usuarioController.obtenerUsuariosObservable();

        configurarCombos();
        configurarTabla();
        configurarCamposEspecificos();
        ocultarTodosLosPaneles();
    }

    private void configurarCombos() {
        comboTipoUsuario.getItems().setAll("Estudiante", "Trabajador", "Externo");

        // Listener para mostrar campos específicos
        comboTipoUsuario.valueProperty().addListener((obs, oldVal, newVal) -> {
            mostrarCamposEspecificos(newVal);
        });
    }

    private void configurarCamposEspecificos() {

        ocultarTodosLosPaneles();
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

    private void configurarTabla() {

        tcNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombre()));
        tcIdentificacion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIdentificacion()));
        tcEdad.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEdad()));
        tcTelefono.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTelefono()));
        tcTipoUsuario.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));

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

        tableUsuario.setItems(listaUsuariosObservable);

        // Listener para selección
        tableUsuario.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        cargarUsuarioEnFormulario(newSelection);
                    }
                }
        );
    }


    @FXML
    private void onActionNuevoUsuario() {
        limpiarFormulario();
    }

    @FXML
    private void onActionCrearUsuario() {
        if (validarFormulario()) {
            Usuario usuario = crearUsuarioDesdeFormulario();

            if (usuarioController.crearUsuario(usuario)) {
                mostrarAlerta("Éxito", "Usuario creado correctamente");
                limpiarFormulario();
                // Actualizar tabla
                tableUsuario.setItems(usuarioController.obtenerUsuariosObservable());
            } else {
                mostrarAlerta("Error", "No se pudo crear el usuario. Ya existe un usuario con esta identificación.");
            }
        }
    }

    @FXML
    private void onActionActualizarUsuario() {
        Usuario usuarioSeleccionado = tableUsuario.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null && validarFormulario()) {
            // Crear nuevo usuario con los datos actualizados
            Usuario usuarioActualizado = crearUsuarioDesdeFormulario();
            usuarioActualizado.setIdentificacion(usuarioSeleccionado.getIdentificacion()); // Mantener misma ID

            if (usuarioController.actualizarUsuario(usuarioActualizado)) {
                tableUsuario.refresh();
                limpiarFormulario();
                mostrarAlerta("Éxito", "Usuario actualizado correctamente");
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el usuario");
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione un usuario para actualizar");
        }
    }

    @FXML
    private void onActionEliminarUsuario() {
        Usuario usuarioSeleccionado = tableUsuario.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("Eliminar Usuario");
            confirmacion.setContentText("¿Está seguro de eliminar a " + usuarioSeleccionado.getNombre() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                if (usuarioController.eliminarUsuario(usuarioSeleccionado.getIdentificacion())) {
                    limpiarFormulario();
                    mostrarAlerta("Éxito", "Usuario eliminado correctamente");
                    // Actualizar tabla
                    tableUsuario.setItems(usuarioController.obtenerUsuariosObservable());
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el usuario");
                }
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione un usuario para eliminar");
        }
    }

    // ============================================================
    //                   MÉTODOS AUXILIARES
    // ============================================================

    private Usuario crearUsuarioDesdeFormulario() {
        String tipoUsuario = comboTipoUsuario.getValue();
        String nombre = txtNombre.getText();
        String identificacion = txtIdentificacion.getText();
        String edad = txtEdad.getText();
        String telefono = txtTelefono.getText();

        switch (tipoUsuario) {
            case "Estudiante":
                return new Estudiante(nombre, identificacion, edad, telefono,
                        txtSemestre.getText(), txtPrograma.getText());
            case "Trabajador":
                return new Trabajador(nombre, identificacion, edad, telefono,
                        txtCargo.getText());
            case "Externo":
                return new Externo(nombre, identificacion, edad, telefono,
                        txtInstitucion.getText());
            default:
                return new Estudiante(nombre, identificacion, edad, telefono, "1", "Ingeniería");
        }
    }

    private void cargarUsuarioEnFormulario(Usuario usuario) {
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

    private boolean validarFormulario() {
        // Validaciones básicas
        if (txtNombre.getText().isEmpty()) {
            mostrarAlerta("Validación", "El nombre es obligatorio");
            return false;
        }
        if (txtIdentificacion.getText().isEmpty()) {
            mostrarAlerta("Validación", "La identificación es obligatoria");
            return false;
        }
        if (comboTipoUsuario.getValue() == null) {
            mostrarAlerta("Validación", "Seleccione el tipo de usuario");
            return false;
        }

        // Validaciones específicas por tipo
        String tipoUsuario = comboTipoUsuario.getValue();
        switch (tipoUsuario) {
            case "Estudiante":
                if (txtSemestre.getText().isEmpty() || txtPrograma.getText().isEmpty()) {
                    mostrarAlerta("Validación", "Complete semestre y programa para estudiante");
                    return false;
                }
                break;
            case "Trabajador":
                if (txtCargo.getText().isEmpty()) {
                    mostrarAlerta("Validación", "El cargo es obligatorio para trabajador");
                    return false;
                }
                break;
            case "Externo":
                if (txtInstitucion.getText().isEmpty()) {
                    mostrarAlerta("Validación", "La institución es obligatoria para externo");
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
        comboTipoUsuario.setValue(null);
        ocultarTodosLosPaneles();
        tableUsuario.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}