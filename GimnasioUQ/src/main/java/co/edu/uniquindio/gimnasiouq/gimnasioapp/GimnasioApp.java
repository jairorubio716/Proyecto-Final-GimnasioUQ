package co.edu.uniquindio.gimnasiouq.gimnasioapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GimnasioApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                GimnasioApp.class.getResource("/co/edu/uniquindio/gimnasiouq/gimnasioapp/GimnasioView.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Gimnasio UQ Fit");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}