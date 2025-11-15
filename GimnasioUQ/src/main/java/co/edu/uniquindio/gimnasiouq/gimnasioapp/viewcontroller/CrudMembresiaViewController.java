package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.MembresiaController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.UsuarioController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class CrudMembresiaViewController {

    MembresiaController membresiaController;
    UsuarioController usuarioController;
    ObservableList<Membresia> listaMembresias;
    ObservableList<Usuario> listaUsuarios;
    Membresia membresiaSeleccionada;

    @FXML private TextField txtCodigo, txtCosto;
    @FXML private ComboBox<TipoMembresia> comboTipoMembresia;
    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private ComboBox<TipoMembresiaDuracion> comboDuracion;
    @FXML private ComboBox<EstadoMembresia> comboEstado;
    @FXML private TableView<Membresia> tableMembresia;
    @FXML private TableColumn<Membresia, String> tcCodigo, tcUsuario, tcTipo, tcDuracion, tcCosto, tcFechaInicio, tcFechaFin, tcEstado;

    @FXML
    void initialize() {
        membresiaController = new MembresiaController();
        usuarioController = new UsuarioController();
        initView();
    }

    private void initView() {
        listaMembresias = membresiaController.obtenerMembresias();
        listaUsuarios = usuarioController.obtenerUsuarios();
        initDataBinding();
        configurarCombos();
        tableMembresia.setItems(listaMembresias);
        listenerSelection();
        limpiarFormulario();
    }

    @FXML void onActionAgregar(ActionEvent event) { crearMembresia(); }
    @FXML void onActionActualizar(ActionEvent event) { actualizarMembresia(); }
    @FXML void onActionEliminar(ActionEvent event) { eliminarMembresia(); }
    @FXML void onActionNuevo(ActionEvent event) { limpiarFormulario(); }

    private void crearMembresia() {
        if (!validarCampos(true)) return;
        if (membresiaController.crearMembresia(
                txtCodigo.getText(),
                comboUsuario.getValue(),
                comboTipoMembresia.getValue(),
                comboDuracion.getValue(),
                LocalDate.now().toString(),
                LocalDate.now().plusMonths(comboDuracion.getValue().getMeses()).toString(),
                EstadoMembresia.ACTIVA
        )) {
            mostrarMensaje("Creación Exitosa", "La membresía ha sido creada.");
            limpiarFormulario();
        } else {
            mostrarMensaje("Error", "No se pudo crear la membresía (posiblemente el usuario ya tiene una activa).");
        }
    }

    private void actualizarMembresia() {
        if (membresiaSeleccionada == null) {
            mostrarMensaje("Advertencia", "Debe seleccionar una membresía.");
            return;
        }
        if (!validarCampos(false)) return;
        
        double costoCalculado = calcularCosto();

        Membresia membresiaActualizada = new Membresia(
            membresiaSeleccionada.getCodigo(),
            membresiaSeleccionada.getIdentificacionUsuario(),
            comboTipoMembresia.getValue(),
            comboDuracion.getValue(),
            costoCalculado,
            membresiaSeleccionada.getFechaInicio(),
            LocalDate.parse(membresiaSeleccionada.getFechaInicio()).plusMonths(comboDuracion.getValue().getMeses()).toString(),
            comboEstado.getValue()
        );

        if (membresiaController.actualizarMembresia(membresiaSeleccionada.getCodigo(), membresiaActualizada)) {
            tableMembresia.refresh();
            mostrarMensaje("Actualización Exitosa", "La membresía ha sido actualizada.");
            limpiarFormulario();
        } else {
            mostrarMensaje("Error", "No se pudo actualizar la membresía.");
        }
    }

    private void eliminarMembresia() {
        if (membresiaSeleccionada != null && mostrarMensajeConfirmacion("¿Está seguro de que desea eliminar la membresía?")) {
            if (membresiaController.eliminarMembresia(membresiaSeleccionada.getCodigo())) {
                mostrarMensaje("Eliminación Exitosa", "La membresía ha sido eliminada.");
                limpiarFormulario();
            }
        }
    }
    
    private void initDataBinding() {
        tcCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        tcUsuario.setCellValueFactory(cellData -> {
            String id = cellData.getValue().getIdentificacionUsuario();
            return new SimpleStringProperty(listaUsuarios.stream().filter(u -> u.getIdentificacion().equals(id)).map(Usuario::getNombre).findFirst().orElse(id));
        });
        tcTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipo().getNombre()));
        tcDuracion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDuracion().toString()));
        tcCosto.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("$%,.0f", cellData.getValue().getCosto())));
        tcFechaInicio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaInicio()));
        tcFechaFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaVencimiento()));
        tcEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().toString()));
    }

    private void configurarCombos() {
        comboTipoMembresia.getItems().setAll(TipoMembresia.values());
        comboDuracion.getItems().setAll(TipoMembresiaDuracion.values());
        comboEstado.getItems().setAll(EstadoMembresia.values());
        comboUsuario.setItems(listaUsuarios);

        comboUsuario.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNombre());
            }
        });
        comboUsuario.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNombre());
            }
        });

        comboTipoMembresia.valueProperty().addListener((obs, o, n) -> actualizarCostoEnUI());
        comboDuracion.valueProperty().addListener((obs, o, n) -> actualizarCostoEnUI());
        comboUsuario.valueProperty().addListener((obs, o, n) -> actualizarCostoEnUI());
    }

    private void listenerSelection() {
        tableMembresia.getSelectionModel().selectedItemProperty().addListener((obs, o, newS) -> {
            membresiaSeleccionada = newS;
            mostrarInformacion(membresiaSeleccionada);
        });
    }

    private void mostrarInformacion(Membresia m) {
        if (m != null) {
            txtCodigo.setText(m.getCodigo());
            comboUsuario.setValue(listaUsuarios.stream().filter(u -> u.getIdentificacion().equals(m.getIdentificacionUsuario())).findFirst().orElse(null));
            comboTipoMembresia.setValue(m.getTipo());
            comboDuracion.setValue(m.getDuracion());
            comboEstado.setValue(m.getEstado());
            actualizarCostoEnUI();
            txtCodigo.setDisable(true);
            comboUsuario.setDisable(true);
        } else {
            limpiarFormulario();
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("MEM-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        txtCosto.clear();
        comboUsuario.getSelectionModel().clearSelection();
        comboTipoMembresia.getSelectionModel().clearSelection();
        comboDuracion.getSelectionModel().clearSelection();
        comboEstado.getSelectionModel().clearSelection();
        tableMembresia.getSelectionModel().clearSelection();
        txtCodigo.setDisable(false);
        comboUsuario.setDisable(false);
    }

    private boolean validarCampos(boolean validarUsuario) {
        if (validarUsuario && comboUsuario.getValue() == null) {
            mostrarMensaje("Error de Validación", "Debe seleccionar un usuario.");
            return false;
        }
        if (comboTipoMembresia.getValue() == null || comboDuracion.getValue() == null) {
            mostrarMensaje("Error de Validación", "Debe seleccionar un tipo y duración.");
            return false;
        }
        return true;
    }
    
    private boolean validarCampos() {
        return validarCampos(true);
    }
    
    private double calcularCosto() {
        if (comboUsuario.getValue() != null && comboTipoMembresia.getValue() != null && comboDuracion.getValue() != null) {
            double costoBase = comboTipoMembresia.getValue().getCostoMensual() * comboDuracion.getValue().getMeses();
            double descuento = 1.0;
            if (comboUsuario.getValue() instanceof Estudiante) descuento = 0.8;
            if (comboUsuario.getValue() instanceof Trabajador) descuento = 0.9;
            return costoBase * descuento;
        }
        return 0.0;
    }
    
    private void actualizarCostoEnUI() {
        double costo = calcularCosto();
        if (costo > 0) {
            txtCosto.setText(String.format("$%,.0f", costo));
        } else {
            txtCosto.clear();
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
        alert.setTitle("Confirmación");
        alert.setContentText(mensaje);
        return alert.showAndWait().filter(r -> r == ButtonType.OK).isPresent();
    }
}
