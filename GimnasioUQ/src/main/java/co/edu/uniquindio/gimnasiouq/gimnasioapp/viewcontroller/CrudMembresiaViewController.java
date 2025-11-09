package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.MembresiaController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CrudMembresiaViewController implements Initializable {

    // ============================================================
    //                     COMPONENTES FXML
    // ============================================================

    @FXML private TextField txtCodigo;
    @FXML private TextField txtCosto;
    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private ComboBox<TipoMembresia> comboTipoMembresia;
    @FXML private ComboBox<TipoMembresiaDuracion> comboDuracion;
    @FXML private TableView<Membresia> tableMembresia;
    @FXML private TableColumn<Membresia, String> tcCodigo;
    @FXML private TableColumn<Membresia, String> tcUsuario;
    @FXML private TableColumn<Membresia, String> tcTipo;
    @FXML private TableColumn<Membresia, String> tcDuracion;
    @FXML private TableColumn<Membresia, String> tcCosto;
    @FXML private TableColumn<Membresia, String> tcFechaInicio;
    @FXML private TableColumn<Membresia, String> tcFechaFin;
    @FXML private TableColumn<Membresia, String> tcEstado;

    // ============================================================
    //                     VARIABLES
    // ============================================================

    private MembresiaController membresiaController;
    private ObservableList<Membresia> listaMembresiasObservable;
    private ObservableList<Usuario> listaUsuariosObservable;

    // ============================================================
    //                     INITIALIZE
    // ============================================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        membresiaController = new MembresiaController();
        listaMembresiasObservable = membresiaController.obtenerMembresiasObservable();
        listaUsuariosObservable = membresiaController.obtenerUsuariosObservable();

        configurarCombos();
        configurarTabla();
        generarCodigoAutomatico();

        // Listener para selección en tabla
        tableMembresia.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        llenarFormularioConMembresia(newValue);
                    }
                }
        );
    }

    // ============================================================
    //                     CONFIGURACIONES
    // ============================================================

    private void configurarCombos() {
        comboTipoMembresia.getItems().setAll(TipoMembresia.values());
        comboDuracion.getItems().setAll(TipoMembresiaDuracion.values());
        comboUsuario.setItems(listaUsuariosObservable);

        // PERSONALIZAR COMBOBOX DE USUARIOS
        comboUsuario.setCellFactory(param -> new ListCell<Usuario>() {
            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);
                if (empty || usuario == null) {
                    setText(null);
                } else {
                    setText(usuario.getNombre() + " - " + usuario.getIdentificacion());
                }
            }
        });

        comboUsuario.setButtonCell(new ListCell<Usuario>() {
            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);
                if (empty || usuario == null) {
                    setText("Seleccione usuario");
                } else {
                    setText(usuario.getNombre() + " - " + usuario.getIdentificacion());
                }
            }
        });

        // Escuchar cambios para calcular costo automático
        comboTipoMembresia.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCosto());
        comboDuracion.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCosto());
    }

    private void configurarTabla() {
        tableMembresia.setItems(listaMembresiasObservable);

        tcCodigo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCodigo()));

        tcUsuario.setCellValueFactory(cellData -> {
            String idUsuario = cellData.getValue().getIdentificacionUsuario();
            for (Usuario usuario : listaUsuariosObservable) {
                if (usuario.getIdentificacion().equals(idUsuario)) {
                    return new SimpleStringProperty(usuario.getNombre());
                }
            }
            return new SimpleStringProperty(idUsuario);
        });

        tcTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipo().getNombre()));
        tcDuracion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDuracion().toString()));
        tcCosto.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("$%,.0f", cellData.getValue().getCosto())));
        tcFechaInicio.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaInicio()));
        tcFechaFin.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaVencimiento()));
        tcEstado.setCellValueFactory(cellData -> {
            Membresia membresia = cellData.getValue();
            return new SimpleStringProperty(membresia.estaActiva() ? "ACTIVA" : "VENCIDA");
        });
    }

    // ============================================================
    //                     MÉTODOS DE ACCIÓN
    // ============================================================

    @FXML
    private void onActionNuevaMembresia() {
        limpiarFormulario();
    }

    @FXML
    private void onActionCrearMembresia() {
        if(validarFormularioCrear()) {
            Membresia membresia = crearMembresiaDesdeFormulario();

            if (membresiaController.crearMembresia(membresia)) {
                mostrarAlerta("Éxito", "Membresía creada correctamente");
                limpiarFormulario();
                tableMembresia.setItems(membresiaController.obtenerMembresiasObservable());
            } else {
                mostrarAlerta("Error", "No se pudo crear la membresía");
            }
        }
    }

    @FXML
    private void onActionActualizarMembresia() {
        Membresia membresiaSeleccionada = tableMembresia.getSelectionModel().getSelectedItem();
        if (membresiaSeleccionada != null && validarFormularioActualizar(membresiaSeleccionada)) {
            Membresia membresiaActualizada = crearMembresiaDesdeFormulario();
            membresiaActualizada.setCodigo(membresiaSeleccionada.getCodigo());

            if (membresiaController.actualizarMembresia(membresiaActualizada)) {
                tableMembresia.refresh();
                limpiarFormulario();
                mostrarAlerta("Éxito", "Membresía actualizada correctamente");
            } else {
                mostrarAlerta("Error", "No se pudo actualizar la membresía");
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione una membresía para actualizar");
        }
    }

    @FXML
    private void onActionEliminarMembresia() {
        Membresia membresiaSeleccionada = tableMembresia.getSelectionModel().getSelectedItem();
        if (membresiaSeleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("Eliminar Membresía");
            confirmacion.setContentText("¿Está seguro de eliminar la membresía " + membresiaSeleccionada.getCodigo() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                if (membresiaController.eliminarMembresia(membresiaSeleccionada.getCodigo())) {
                    limpiarFormulario();
                    mostrarAlerta("Éxito", "Membresía eliminada correctamente");
                    tableMembresia.setItems(membresiaController.obtenerMembresiasObservable());
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar la membresía");
                }
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione una membresía para eliminar");
        }
    }

    // ============================================================
    //                     MÉTODOS AUXILIARES
    // ============================================================

    private boolean validarFormularioCrear() {
        if (comboUsuario.getValue() == null) {
            mostrarAlerta("Error", "Seleccione un usuario");
            return false;
        }

        if (comboTipoMembresia.getValue() == null) {
            mostrarAlerta("Error", "Seleccione el tipo de membresía");
            return false;
        }

        if (comboDuracion.getValue() == null) {
            mostrarAlerta("Error", "Seleccione la duración");
            return false;
        }

        // ✅ VALIDACIÓN DE REGLA DE NEGOCIO: Usuario no debe tener membresía activa
        String identificacionUsuario = comboUsuario.getValue().getIdentificacion();
        if (membresiaController.usuarioTieneMembresiaActiva(identificacionUsuario)) {
            mostrarAlerta("Error", "El usuario ya tiene una membresía activa. No puede tener más de una membresía activa al mismo tiempo.");
            return false;
        }

        return true;
    }

    private boolean validarFormularioActualizar(Membresia membresiaOriginal) {
        if (comboUsuario.getValue() == null) {
            mostrarAlerta("Error", "Seleccione un usuario");
            return false;
        }

        if (comboTipoMembresia.getValue() == null) {
            mostrarAlerta("Error", "Seleccione el tipo de membresía");
            return false;
        }

        if (comboDuracion.getValue() == null) {
            mostrarAlerta("Error", "Seleccione la duración");
            return false;
        }


        String identificacionUsuario = comboUsuario.getValue().getIdentificacion();
        if (!identificacionUsuario.equals(membresiaOriginal.getIdentificacionUsuario()) &&
                membresiaController.usuarioTieneMembresiaActiva(identificacionUsuario)) {
            mostrarAlerta("Error", "El usuario ya tiene una membresía activa. No puede tener más de una membresía activa al mismo tiempo.");
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        txtCodigo.clear();
        txtCosto.clear();
        comboUsuario.getSelectionModel().clearSelection();
        comboTipoMembresia.getSelectionModel().clearSelection();
        comboDuracion.getSelectionModel().clearSelection();
        generarCodigoAutomatico();
    }

    private Membresia crearMembresiaDesdeFormulario() {
        String codigo = txtCodigo.getText();
        String identificacionUsuario = comboUsuario.getValue().getIdentificacion();
        TipoMembresia tipo = comboTipoMembresia.getValue();
        TipoMembresiaDuracion duracion = comboDuracion.getValue();

        double costo = calcularCosto(tipo, duracion);
        String fechaInicio = java.time.LocalDate.now().toString();
        String fechaVencimiento = java.time.LocalDate.now().plusMonths(duracion.getMeses()).toString();

        return new Membresia(codigo, identificacionUsuario, tipo, duracion, costo,
                fechaInicio, fechaVencimiento, EstadoMembresia.ACTIVA);
    }

    private double calcularCosto(TipoMembresia tipo, TipoMembresiaDuracion duracion) {
        double costoBase = tipo.getCostoMensual();
        int meses = duracion.getMeses();

        switch (duracion) {
            case TRIMESTRAL:
                return (costoBase * meses);
            case ANUAL:
                return (costoBase * meses);
            case MENSUAL:
            default:
                return costoBase * meses;
        }
    }

    private void actualizarCosto() {
        if (comboTipoMembresia.getValue() != null && comboDuracion.getValue() != null) {
            double costo = calcularCosto(comboTipoMembresia.getValue(), comboDuracion.getValue());
            txtCosto.setText(String.format("$%,.0f", costo));
        }
    }

    private void generarCodigoAutomatico() {
        String nuevoCodigo = "MEM" + (listaMembresiasObservable.size() + 1);
        txtCodigo.setText(nuevoCodigo);
    }

    private void llenarFormularioConMembresia(Membresia membresia) {
        txtCodigo.setText(membresia.getCodigo());

        for (Usuario usuario : comboUsuario.getItems()) {
            if (usuario.getIdentificacion().equals(membresia.getIdentificacionUsuario())) {
                comboUsuario.setValue(usuario);
                break;
            }
        }

        comboTipoMembresia.setValue(membresia.getTipo());
        comboDuracion.setValue(membresia.getDuracion());
        txtCosto.setText(String.format("$%,.0f", membresia.getCosto()));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}