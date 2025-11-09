module co.edu.uniquindio.gimnasiouq.gimnasiouqapp {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller to javafx.fxml;
    opens co.edu.uniquindio.gimnasiouq.gimnasioapp.model to javafx.fxml;


    exports co.edu.uniquindio.gimnasiouq.gimnasioapp;
    exports co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;
    exports co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

}