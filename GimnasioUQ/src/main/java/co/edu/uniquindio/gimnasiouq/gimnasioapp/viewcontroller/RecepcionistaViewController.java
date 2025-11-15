package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.GimnasioApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class RecepcionistaViewController {

    @FXML
    private TabPane tabPane;

    @FXML
    private void handleCerrarSesionMenuAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cierre de Sesión");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que quieres cerrar sesión?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Stage stageActual = (Stage) tabPane.getScene().getWindow();
                stageActual.close();

                FXMLLoader loader = new FXMLLoader(GimnasioApp.class.getResource("LoginView.fxml"));
                AnchorPane root = loader.load();
                Scene newScene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Login - Gimnasio UQ Fit");
                stage.setScene(newScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
