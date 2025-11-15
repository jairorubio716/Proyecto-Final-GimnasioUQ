package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.ControlAccesoController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Membresia;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UsuariosDentroViewController {

    ControlAccesoController controlAccesoController;
    ObservableList<Usuario> listaUsuariosDentro;
    
    @FXML private TableView<Usuario> tableUsuariosDentro;
    @FXML private TableColumn<Usuario, String> tcNombre, tcIdentificacion, tcTipoUsuario, tcTipoMembresia;
    @FXML private Button btnRegistrarSalida;

    @FXML
    void initialize() {
        controlAccesoController = new ControlAccesoController();
        initView();
    }

    private void initView() {
        listaUsuariosDentro = controlAccesoController.obtenerUsuariosDentro();
        initDataBinding();
        tableUsuariosDentro.setItems(listaUsuariosDentro);
        listenerSelection();
    }

    @FXML
    void onActionRegistrarSalida(ActionEvent event) {
        registrarSalida();
    }

    private void registrarSalida() {
        Usuario usuarioSeleccionado = tableUsuariosDentro.getSelectionModel().getSelectedItem();
        
        if (usuarioSeleccionado != null) {
            if (controlAccesoController.registrarSalida(usuarioSeleccionado)) {
                mostrarMensaje("Salida Registrada", "La salida de " + usuarioSeleccionado.getNombre() + " ha sido registrada.", Alert.AlertType.INFORMATION);
            } else {
                mostrarMensaje("Error", "No se pudo registrar la salida.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarMensaje("Sin Selección", "Debe seleccionar un usuario de la tabla para registrar su salida.", Alert.AlertType.WARNING);
        }
    }

    private void initDataBinding() {
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tcIdentificacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdentificacion()));
        tcTipoUsuario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));
        
        tcTipoMembresia.setCellValueFactory(cellData -> {
            Membresia membresia = controlAccesoController.obtenerMembresiaActivaUsuario(cellData.getValue().getIdentificacion());
            if (membresia != null) {
                return new SimpleStringProperty(membresia.getTipo().getNombre());
            }
            return new SimpleStringProperty("N/A");
        });
    }

    private void listenerSelection() {
        tableUsuariosDentro.getSelectionModel().selectedItemProperty().addListener((obs, old, newS) -> {
            btnRegistrarSalida.setDisable(newS == null);
        });
    }

    private void mostrarMensaje(String titulo, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
