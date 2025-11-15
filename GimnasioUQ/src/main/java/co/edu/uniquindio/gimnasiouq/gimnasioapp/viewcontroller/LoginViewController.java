package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.GimnasioApp;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.LoginController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Rol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    LoginController loginController;

    @FXML private ComboBox<Rol> comboRolLogin;
    @FXML private PasswordField txtPassword;

    @FXML
    void initialize() {
        loginController = new LoginController();
        comboRolLogin.getItems().setAll(Rol.values());
        comboRolLogin.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(Rol item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.name()); }
        });
        comboRolLogin.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Rol item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.name()); }
        });
    }

    @FXML
    void onActionLogin(ActionEvent event) {
        Rol rolSeleccionado = comboRolLogin.getValue();
        String password = txtPassword.getText();

        if (rolSeleccionado == null || password.isEmpty()) {
            mostrarMensaje("Error", "Debe seleccionar un rol y ingresar la contraseña.", Alert.AlertType.WARNING);
            return;
        }

        String username = "";
        if (rolSeleccionado == Rol.ADMIN) {
            username = "admin";
        } else if (rolSeleccionado == Rol.RECEPCIONISTA) {
            username = "recep";
        }

        Rol rolAutenticado = loginController.validarCredenciales(username, password);

        if (rolAutenticado != null) {
            abrirVentanaPrincipal(rolAutenticado);
            cerrarVentanaActual();
        } else {
            mostrarMensaje("Error de Autenticación", "Contraseña incorrecta para el rol seleccionado.", Alert.AlertType.ERROR);
        }
    }

    private void abrirVentanaPrincipal(Rol rol) {
        try {
            FXMLLoader loader;
            String fxmlPath;
            String title;

            if (rol == Rol.ADMIN) {
                fxmlPath = "AdminView.fxml";
                title = "Gimnasio UQ Fit - Administrador";
            } else {
                fxmlPath = "RecepcionistaView.fxml";
                title = "Gimnasio UQ Fit - Recepcionista";
            }

            loader = new FXMLLoader(GimnasioApp.class.getResource(fxmlPath));
            AnchorPane root = loader.load();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cerrarVentanaActual() {
        Stage stage = (Stage) comboRolLogin.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(String titulo, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
