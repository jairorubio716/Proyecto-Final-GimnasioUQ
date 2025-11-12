package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.MembresiaController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.UsuarioController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class CrudMembresiaViewController {

    MembresiaController membresiaController;
    UsuarioController usuarioController;

    ObservableList<Membresia> listaMembresias;
    ObservableList<Usuario> listaUsuarios;

    Membresia membresiaSeleccionada;

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TextField txtCodigo;
    @FXML private ComboBox<TipoMembresia> comboTipoMembresia;
    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private ComboBox<TipoMembresiaDuracion> comboDuracion;
    @FXML private TextField txtCosto;
    @FXML private ComboBox<EstadoMembresia> comboEstado;
    @FXML private TableView<Membresia> tableMembresia;
    @FXML private TableColumn<Membresia, String> tcCodigo, tcUsuario, tcTipo, tcDuracion, tcCosto;
    @FXML private TableColumn<Membresia, String> tcFechaInicio, tcFechaFin, tcEstado;
    @FXML private Button btnNuevo, btnAgregar, btnActualizar, btnEliminar;

    @FXML
    void initialize() {
        membresiaController = new MembresiaController();
        usuarioController = new UsuarioController();
        initView();
    }

    private void initView() {

        listaUsuarios = usuarioController.obtenerUsuarios();
        listaMembresias = membresiaController.obtenerMembresias();

        initDataBinding();
        configurarCombos();

        comboUsuario.setItems(listaUsuarios);
        tableMembresia.setItems(listaMembresias);

        listenerSelection();
        limpiarFormulario();
    }

    private void initDataBinding() {
        tcCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        tcUsuario.setCellValueFactory(cellData -> {
            String idUsuario = cellData.getValue().getIdentificacionUsuario();
            return new SimpleStringProperty(
                listaUsuarios.stream()
                    .filter(u -> u.getIdentificacion().equals(idUsuario))
                    .map(Usuario::getNombre)
                    .findFirst()
                    .orElse(idUsuario)
            );
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

        comboUsuario.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre() + " (" + item.getIdentificacion() + ")");
            }
        });
        comboUsuario.setButtonCell(new ListCell<>() {
             @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleccione un usuario" : item.getNombre());
            }
        });

        comboTipoMembresia.valueProperty().addListener((obs, oldV, newV) -> actualizarCostoEnUI());
        comboDuracion.valueProperty().addListener((obs, oldV, newV) -> actualizarCostoEnUI());
        comboUsuario.valueProperty().addListener((obs, oldV, newV) -> actualizarCostoEnUI());
    }

    private void listenerSelection() {
        tableMembresia.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            membresiaSeleccionada = newSelection;
            mostrarInformacion(membresiaSeleccionada);
        });
    }

    private void mostrarInformacion(Membresia membresia) {
        if (membresia != null) {
            txtCodigo.setText(membresia.getCodigo());
            comboUsuario.setValue(
                listaUsuarios.stream()
                    .filter(u -> u.getIdentificacion().equals(membresia.getIdentificacionUsuario()))
                    .findFirst().orElse(null)
            );
            comboTipoMembresia.setValue(membresia.getTipo());
            comboDuracion.setValue(membresia.getDuracion());
            comboEstado.setValue(membresia.getEstado());
            actualizarCostoEnUI();
            
            txtCodigo.setDisable(true);
            comboUsuario.setDisable(true);
        }
    }

    @FXML void onActionNuevo(ActionEvent event) { limpiarFormulario(); }
    @FXML void onActionAgregar(ActionEvent event) { crearMembresia(); }
    @FXML void onActionActualizar(ActionEvent event) { actualizarMembresia(); }
    @FXML void onActionEliminar(ActionEvent event) { eliminarMembresia(); }

    private void crearMembresia() {
        if (validarCampos()) {
            Usuario usuarioSeleccionado = comboUsuario.getValue();
            if (membresiaController.usuarioTieneMembresiaActiva(usuarioSeleccionado.getIdentificacion())) {
                mostrarMensaje("Validación", "Membresía Existente", "El usuario seleccionado ya tiene una membresía activa.", Alert.AlertType.WARNING);
                return;
            }

            Membresia nuevaMembresia = crearMembresiaDesdeFormulario();
            if (membresiaController.crearMembresia(nuevaMembresia, usuarioSeleccionado)) {
                mostrarMensaje("Notificación", "Creación Exitosa", "La membresía ha sido creada.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
            } else {
                mostrarMensaje("Error", "Error de Creación", "No se pudo crear la membresía.", Alert.AlertType.ERROR);
            }
        }
    }

    private void actualizarMembresia() {
        if (membresiaSeleccionada != null && validarCampos()) {
            Membresia membresiaActualizada = crearMembresiaDesdeFormulario();
            membresiaActualizada.setCodigo(membresiaSeleccionada.getCodigo());
            membresiaActualizada.setIdentificacionUsuario(membresiaSeleccionada.getIdentificacionUsuario());

            if (membresiaController.actualizarMembresia(membresiaActualizada)) {
                mostrarMensaje("Notificación", "Actualización Exitosa", "La membresía ha sido actualizada.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
            } else {
                mostrarMensaje("Error", "Error de Actualización", "No se pudo actualizar la membresía.", Alert.AlertType.ERROR);
            }
        } else if (membresiaSeleccionada == null) {
            mostrarMensaje("Advertencia", "Sin Selección", "Debe seleccionar una membresía para actualizar.", Alert.AlertType.WARNING);
        }
    }

    private void eliminarMembresia() {
        if (membresiaSeleccionada != null) {
            if (mostrarMensajeConfirmacion("¿Está seguro de que desea eliminar la membresía " + membresiaSeleccionada.getCodigo() + "?")) {
                if (membresiaController.eliminarMembresia(membresiaSeleccionada.getCodigo())) {
                    limpiarFormulario();
                    mostrarMensaje("Notificación", "Eliminación Exitosa", "La membresía ha sido eliminada.", Alert.AlertType.INFORMATION);
                } else {
                    mostrarMensaje("Error", "Error de Eliminación", "No se pudo eliminar la membresía.", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarMensaje("Advertencia", "Sin Selección", "Debe seleccionar una membresía para eliminar.", Alert.AlertType.WARNING);
        }
    }

    private Membresia crearMembresiaDesdeFormulario() {
        String codigo = txtCodigo.getText();
        String idUsuario = comboUsuario.getValue().getIdentificacion();
        TipoMembresia tipo = comboTipoMembresia.getValue();
        TipoMembresiaDuracion duracion = comboDuracion.getValue();

        double costo = calcularCosto();

        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = fechaInicio.plusMonths(duracion.getMeses());

        return new Membresia(codigo, idUsuario, tipo, duracion, costo, fechaInicio.toString(), fechaFin.toString(), comboEstado.getValue());
    }

    private boolean validarCampos() {
        if (comboUsuario.getValue() == null) {
            mostrarMensaje("Validación", "Campo Requerido", "Debe seleccionar un usuario.", Alert.AlertType.WARNING);
            return false;
        }
        if (comboTipoMembresia.getValue() == null) {
            mostrarMensaje("Validación", "Campo Requerido", "Debe seleccionar un tipo de membresía.", Alert.AlertType.WARNING);
            return false;
        }
        if (comboDuracion.getValue() == null) {
            mostrarMensaje("Validación", "Campo Requerido", "Debe seleccionar una duración.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        txtCodigo.setText("MEM-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        txtCosto.clear();
        comboUsuario.getSelectionModel().clearSelection();
        comboTipoMembresia.getSelectionModel().clearSelection();
        comboDuracion.getSelectionModel().clearSelection();
        comboEstado.setValue(EstadoMembresia.ACTIVA);
        tableMembresia.getSelectionModel().clearSelection();
        
        txtCodigo.setDisable(true);
        comboUsuario.setDisable(false);
        membresiaSeleccionada = null;
    }

    private double calcularCosto() {
        TipoMembresia tipo = comboTipoMembresia.getValue();
        TipoMembresiaDuracion duracion = comboDuracion.getValue();
        Usuario usuario = comboUsuario.getValue();

        if (tipo != null && duracion != null && usuario != null) {
            double costoBase = tipo.getCostoMensual() * duracion.getMeses();
            double descuento = 1.0;
            if (usuario instanceof Estudiante) descuento = 0.8; // 20%
            else if (usuario instanceof Trabajador) descuento = 0.9; // 10%
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

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private boolean mostrarMensajeConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText(mensaje);
        Optional<ButtonType> action = alert.showAndWait();
        return action.isPresent() && action.get() == ButtonType.OK;
    }
}
