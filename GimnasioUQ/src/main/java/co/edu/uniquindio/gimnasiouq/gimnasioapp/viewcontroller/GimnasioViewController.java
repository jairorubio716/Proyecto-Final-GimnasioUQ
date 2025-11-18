package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Rol;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class GimnasioViewController {

    @FXML private TabPane tabPanePrincipal;
    @FXML private Tab tabControlAcceso;

    @FXML private Tab tabGestionUsuarios;
    @FXML private Tab tabGestionEntrenadores;
    @FXML private Tab tabGestionClases;
    @FXML private Tab tabReportesAdmin;

    public void inicializarConRol(Rol rol) {
        if (rol == Rol.RECEPCIONISTA) {
            tabControlAcceso.setDisable(true);
            tabGestionEntrenadores.setDisable(true);
            tabGestionClases.setDisable(true);
            tabReportesAdmin.setDisable(true);

            tabPanePrincipal.getSelectionModel().select(tabGestionUsuarios);

        } else if (rol == Rol.ADMIN) {
        }
    }
}
