package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.UsuarioController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class CrudUsuarioViewController {

    UsuarioController usuarioController;
    ObservableList<Usuario> listaUsuarios;
    Usuario usuarioSeleccionado;

    @FXML private TextField txtNombre, txtIdentificacion, txtEdad, txtTelefono;
    @FXML private ComboBox<String> comboTipoUsuario;
    @FXML private TextField txtSemestre, txtPrograma, txtCargo, txtInstitucion;
    @FXML private AnchorPane panelEstudiante, panelTrabajador, panelExterno;
    @FXML private TableView<Usuario> tableUsuario;
    @FXML private TableColumn<Usuario, String> tcNombre, tcIdentificacion, tcEdad, tcTelefono, tcTipoUsuario, tcDetalleEspecifico;

    @FXML
    void initialize() {
        usuarioController = new UsuarioController();
        initView();
    }

    private void initView() {
        listaUsuarios = usuarioController.obtenerUsuarios();
        initDataBinding();
        tableUsuario.setItems(listaUsuarios);
        listenerSelection();
        configurarCombos();
        ocultarTodosLosPaneles();
    }

    @FXML void onActionAgregar(ActionEvent event) { crearUsuario(); }
    @FXML void onActionActualizar(ActionEvent event) { actualizarUsuario(); }
    @FXML void onActionEliminar(ActionEvent event) { eliminarUsuario(); }
    @FXML void onActionNuevo(ActionEvent event) { limpiarFormulario(); }

    private void crearUsuario() {
        String nombre = txtNombre.getText();
        String id = txtIdentificacion.getText();
        String edad = txtEdad.getText();
        String tel = txtTelefono.getText();
        String tipo = comboTipoUsuario.getValue();
        String arg1 = "", arg2 = "";

        if (!validarCampos(nombre, id, edad, tel, tipo)) return;

        switch (tipo) {
            case "Estudiante": arg1 = txtSemestre.getText(); arg2 = txtPrograma.getText(); break;
            case "Trabajador": arg1 = txtCargo.getText(); break;
            case "Externo": arg1 = txtInstitucion.getText(); break;
        }

        if (usuarioController.crearUsuario(nombre, id, edad, tel, tipo, arg1, arg2)) {
            mostrarMensaje("Creación Exitosa", "El usuario ha sido creado.");
            limpiarFormulario();
        } else {
            mostrarMensaje("Error", "La identificación ya existe.");
        }
    }

    private void actualizarUsuario() {
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Advertencia", "Debe seleccionar un usuario.");
            return;
        }
        String nombre = txtNombre.getText();
        String edad = txtEdad.getText();
        String tel = txtTelefono.getText();
        String tipo = comboTipoUsuario.getValue();
        
        if (!validarCampos(nombre, "dummyId", edad, tel, tipo)) return;

        Usuario usuarioActualizado = null;
        switch (tipo) {
            case "Estudiante":
                usuarioActualizado = new Estudiante(nombre, "", edad, tel, txtSemestre.getText(), txtPrograma.getText());
                break;
            case "Trabajador":
                usuarioActualizado = new Trabajador(nombre, "", edad, tel, txtCargo.getText());
                break;
            case "Externo":
                usuarioActualizado = new Externo(nombre, "", edad, tel, txtInstitucion.getText());
                break;
        }

        if (usuarioActualizado != null && usuarioController.actualizarUsuario(usuarioSeleccionado.getIdentificacion(), usuarioActualizado)) {
            tableUsuario.refresh();
            mostrarMensaje("Actualización Exitosa", "El usuario ha sido actualizado.");
            limpiarFormulario();
        } else {
            mostrarMensaje("Error", "No se pudo actualizar el usuario.");
        }
    }

    private void eliminarUsuario() {
        if (usuarioSeleccionado != null && mostrarMensajeConfirmacion("¿Está seguro de que desea eliminar el usuario?")) {
            if (usuarioController.eliminarUsuario(usuarioSeleccionado.getIdentificacion())) {
                mostrarMensaje("Eliminación Exitosa", "El usuario ha sido eliminado.");
                limpiarFormulario();
            }
        }
    }
    

    private void initDataBinding() {
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tcIdentificacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdentificacion()));
        tcEdad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEdad()));
        tcTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        tcTipoUsuario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));
        
        tcDetalleEspecifico.setCellValueFactory(cellData -> {
            Usuario u = cellData.getValue();
            if (u instanceof Estudiante) {
                return new SimpleStringProperty("Sem " + ((Estudiante) u).getSemestre() + " - " + ((Estudiante) u).getPrograma());
            }
            if (u instanceof Trabajador) {
                return new SimpleStringProperty(((Trabajador) u).getCargo());
            }
            if (u instanceof Externo) {
                return new SimpleStringProperty(((Externo) u).getInstitucion());
            }
            return new SimpleStringProperty("");
        });
    }

    private void listenerSelection() {
        tableUsuario.getSelectionModel().selectedItemProperty().addListener((obs, old, newS) -> {
            usuarioSeleccionado = newS;
            mostrarInformacion(usuarioSeleccionado);
        });
    }

    private void mostrarInformacion(Usuario u) {
        if (u != null) {
            txtNombre.setText(u.getNombre());
            txtIdentificacion.setText(u.getIdentificacion());
            txtIdentificacion.setDisable(true);
            txtEdad.setText(u.getEdad());
            txtTelefono.setText(u.getTelefono());
            if (u instanceof Estudiante) {
                comboTipoUsuario.setValue("Estudiante");
                txtSemestre.setText(((Estudiante) u).getSemestre());
                txtPrograma.setText(((Estudiante) u).getPrograma());
            } else if (u instanceof Trabajador) {
                comboTipoUsuario.setValue("Trabajador");
                txtCargo.setText(((Trabajador) u).getCargo());
            } else if (u instanceof Externo) {
                comboTipoUsuario.setValue("Externo");
                txtInstitucion.setText(((Externo) u).getInstitucion());
            }
        }
    }
    
    private void limpiarFormulario() {
        txtNombre.clear();
        txtIdentificacion.clear();
        txtIdentificacion.setDisable(false);
        txtEdad.clear();
        txtTelefono.clear();
        comboTipoUsuario.getSelectionModel().clearSelection();
    }

    private boolean validarCampos(String n, String id, String e, String t, String tipo) {
        if (n.isEmpty() || id.isEmpty() || e.isEmpty() || t.isEmpty() || tipo == null) {
            mostrarMensaje("Error de Validación", "Todos los campos son obligatorios.");
            return false;
        }
        return true;
    }

    private void configurarCombos() {
        comboTipoUsuario.getItems().setAll("Estudiante", "Trabajador", "Externo");
        comboTipoUsuario.valueProperty().addListener((obs, o, n) -> mostrarCamposEspecificos(n));
    }

    private void ocultarTodosLosPaneles() {
        panelEstudiante.setVisible(false); panelEstudiante.setManaged(false);
        panelTrabajador.setVisible(false); panelTrabajador.setManaged(false);
        panelExterno.setVisible(false); panelExterno.setManaged(false);
    }

    private void mostrarCamposEspecificos(String tipo) {
        txtSemestre.clear();
        txtPrograma.clear();
        txtCargo.clear();
        txtInstitucion.clear();
        
        ocultarTodosLosPaneles();
        if (tipo == null) return;
        switch (tipo) {
            case "Estudiante": panelEstudiante.setVisible(true); panelEstudiante.setManaged(true); break;
            case "Trabajador": panelTrabajador.setVisible(true); panelTrabajador.setManaged(true); break;
            case "Externo": panelExterno.setVisible(true); panelExterno.setManaged(true); break;
        }
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
