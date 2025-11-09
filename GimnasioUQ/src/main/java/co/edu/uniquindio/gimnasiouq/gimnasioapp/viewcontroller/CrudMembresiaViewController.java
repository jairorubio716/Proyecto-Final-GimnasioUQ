package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.MembresiaController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.UsuarioController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Membresia;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.TipoMembresia;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.TipoMembresiaDuracion;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CrudMembresiaViewController implements Initializable {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtCosto;

    @FXML
    private ComboBox<Usuario> comboUsuario;

    @FXML
    private ComboBox<TipoMembresia> comboTipoMembresia;

    @FXML
    private ComboBox<TipoMembresiaDuracion> comboDuracion;

    @FXML
    private TableView<Membresia> tableMembresia;

    @FXML
    private TableColumn<Membresia, String> tcCodigo;

    @FXML
    private TableColumn<Membresia, String> tcUsuario;

    @FXML
    private TableColumn<Membresia, String> tcTipo;

    @FXML
    private TableColumn<Membresia, String> tcDuracion;

    @FXML
    private TableColumn<Membresia, String> tcCosto;

    @FXML
    private TableColumn<Membresia, String> tcEstado;


    private MembresiaController membresiaController;
    private ObservableList<Membresia> listaMembresiasObservable;


    //inicializador

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        membresiaController = new MembresiaController();
        listaMembresiasObservable = membresiaController.obtenerMembresiasObservable();
        listaUsuariosObservable = membresiaController.obtenerUsuariosObservable();

        configurarCombos();
        configurarTabla();
        generarCodigoAutomatico();
    }


    private void configurarCombos() {
        comboTipoMembresia.getItems().setAll(TipoMembresia.values());
        comboDuracion.getItems().setAll(TipoMembresiaDuracion.values());
        comboUsuario.setItems(listaUsuariosObservable);
    }

    private void configurarTabla(){
        tableMembresia.setItems(listaMembresiasObservable);

        tcCodigo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCodigo()));
        tcUsuario.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIdentificacionUsuario()));
        tcTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipo().getNombre()));
        tcDuracion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDuracion().toString()));
        tcCosto.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("$%,.0f", cellData.getValue().getCosto())));
        tcEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado().toString()));
    }

    private void generarCodigoAutomatico() {
        String nuevoCodigo = "MEM" + (listaMembresiasObservable.size() + 1);
        txtCodigo.setText(nuevoCodigo);
    }


}

