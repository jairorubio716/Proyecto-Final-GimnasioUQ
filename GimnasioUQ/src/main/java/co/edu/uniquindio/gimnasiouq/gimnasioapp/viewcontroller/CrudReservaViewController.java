package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.MembresiaController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.ReservaController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.UsuarioController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class CrudReservaViewController {

    // Controllers
    ReservaController reservaController;
    UsuarioController usuarioController;
    MembresiaController membresiaController;

    // Observable Lists - Se obtienen directamente del controller.
    ObservableList<Reserva> listaReservas;
    ObservableList<Usuario> listaUsuarios;
    FilteredList<Reserva> reservasFiltradas;

    // Selection
    Reserva reservaSeleccionada;
    Usuario usuarioSeleccionado;

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    // FXML Components
    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private Label lblTipoMembresia, lblEstadoMembresia;
    @FXML private Label lblMaquinas, lblClases, lblSpa, lblEntrenador;
    @FXML private VBox panelReservaClases;
    @FXML private ComboBox<TipoClase> comboTipoClase;
    @FXML private DatePicker dateFechaClase;
    @FXML private Label lblCuposDisponibles, lblMensajeReserva;
    @FXML private TableView<Reserva> tableReserva;
    @FXML private TableColumn<Reserva, String> tcCodigo, tcClase, tcFechaClase, tcFechaReserva, tcEstado;
    @FXML private Button btnCrearReserva, btnCancelarReserva, btnRegistrarAsistencia;

    @FXML
    void initialize() {
        reservaController = new ReservaController();
        usuarioController = new UsuarioController();
        membresiaController = new MembresiaController();
        initView();
    }

    private void initView() {
        // SOLUCIÓN: Obtener las listas "vivas" directamente de los controllers.
        listaUsuarios = usuarioController.obtenerUsuarios();
        listaReservas = reservaController.obtenerReservas();

        initDataBinding();
        configurarCombos();

        // Asignar las listas a los componentes de la UI
        comboUsuario.setItems(listaUsuarios);

        listenerSelection();
        limpiarTodaLaVista();
    }

    private void initDataBinding() {
        reservasFiltradas = new FilteredList<>(listaReservas, p -> true);
        tableReserva.setItems(reservasFiltradas);

        tcCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigoReserva()));
        tcClase.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoClase().getNombre()));
        tcFechaClase.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaClase()));
        tcFechaReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaReserva()));
        tcEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado()));
    }

    private void configurarCombos() {
        comboTipoClase.getItems().setAll(TipoClase.values());
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
    }

    private void listenerSelection() {
        comboUsuario.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            usuarioSeleccionado = newV;
            actualizarVistaUsuarioSeleccionado();
        });

        tableReserva.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            reservaSeleccionada = newV;
            actualizarEstadoBotonesAccion();
        });

        comboTipoClase.valueProperty().addListener((obs, oldV, newV) -> actualizarCuposDisponibles());
        dateFechaClase.valueProperty().addListener((obs, oldV, newV) -> actualizarCuposDisponibles());
    }

    private void actualizarVistaUsuarioSeleccionado() {
        if (usuarioSeleccionado == null) {
            limpiarTodaLaVista();
            return;
        }

        Membresia membresia = membresiaController.obtenerMembresiaActivaUsuario(usuarioSeleccionado.getIdentificacion());
        boolean puedeReservar = reservaController.usuarioPuedeReservar(usuarioSeleccionado.getIdentificacion());

        actualizarInfoMembresia(membresia);
        actualizarBeneficios(membresia);
        panelReservaClases.setVisible(puedeReservar);
        panelReservaClases.setManaged(puedeReservar);
        lblMensajeReserva.setText(puedeReservar ? "Usuario habilitado para reservar." : "El plan del usuario no permite reservar clases.");
        
        reservasFiltradas.setPredicate(reserva -> reserva.getIdentificacionUsuario().equals(usuarioSeleccionado.getIdentificacion()));
        limpiarFormularioReserva();
    }

    private void actualizarInfoMembresia(Membresia membresia) {
        if (membresia != null && membresia.estaActiva()) {
            lblTipoMembresia.setText(membresia.getTipo().getNombre());
            lblEstadoMembresia.setText("ACTIVA");
            lblEstadoMembresia.setStyle("-fx-text-fill: #27ae60;");
        } else {
            lblTipoMembresia.setText("N/A");
            lblEstadoMembresia.setText("INACTIVA O SIN MEMBRESÍA");
            lblEstadoMembresia.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    private void actualizarBeneficios(Membresia membresia) {
        lblMaquinas.setText("❌"); lblClases.setText("❌"); lblSpa.setText("❌"); lblEntrenador.setText("❌");

        if (membresia != null && membresia.estaActiva()) {
            lblMaquinas.setText("✅");
            if (membresia.getTipo() == TipoMembresia.PREMIUM || membresia.getTipo() == TipoMembresia.VIP) {
                lblClases.setText("✅");
            }
            if (membresia.getTipo() == TipoMembresia.VIP) {
                lblSpa.setText("✅");
                lblEntrenador.setText("✅");
            }
        }
    }

    @FXML void onActionCrearReserva(ActionEvent event) {
        if (validarCamposReserva()) {
            Reserva nuevaReserva = crearReservaDesdeFormulario();
            if (reservaController.crearReserva(nuevaReserva)) {
                mostrarMensaje("Notificación", "Reserva Exitosa", "La reserva ha sido creada.", Alert.AlertType.INFORMATION);
                limpiarFormularioReserva();
            } else {
                mostrarMensaje("Error", "Error de Creación", "No se pudo crear la reserva.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML void onActionCancelarReserva(ActionEvent event) {
        if (reservaSeleccionada != null) {
            if (mostrarMensajeConfirmacion("¿Desea cancelar la reserva " + reservaSeleccionada.getCodigoReserva() + "?")) {
                if (reservaController.cancelarReserva(reservaSeleccionada.getCodigoReserva())) {
                    tableReserva.refresh();
                    actualizarEstadoBotonesAccion();
                    mostrarMensaje("Notificación", "Reserva Cancelada", "La reserva ha sido cancelada.", Alert.AlertType.INFORMATION);
                }
            }
        }
    }

    @FXML void onActionRegistrarAsistencia(ActionEvent event) {
        if (reservaSeleccionada != null) {
            if (reservaController.registrarAsistencia(reservaSeleccionada.getCodigoReserva())) {
                tableReserva.refresh();
                actualizarEstadoBotonesAccion();
                mostrarMensaje("Notificación", "Asistencia Registrada", "La asistencia ha sido registrada.", Alert.AlertType.INFORMATION);
            }
        }
    }

    private Reserva crearReservaDesdeFormulario() {
        return new Reserva(
            "RES-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase(),
            usuarioSeleccionado.getIdentificacion(),
            comboTipoClase.getValue(),
            dateFechaClase.getValue().toString()
        );
    }

    private boolean validarCamposReserva() {
        if (comboTipoClase.getValue() == null || dateFechaClase.getValue() == null) {
            mostrarMensaje("Validación", "Campos Incompletos", "Debe seleccionar un tipo de clase y una fecha.", Alert.AlertType.WARNING);
            return false;
        }
        if (dateFechaClase.getValue().isBefore(LocalDate.now())) {
            mostrarMensaje("Validación", "Fecha Inválida", "La fecha de la clase no puede ser en el pasado.", Alert.AlertType.WARNING);
            return false;
        }
        if (reservaController.cuposDisponibles(comboTipoClase.getValue(), dateFechaClase.getValue().toString()) <= 0) {
            mostrarMensaje("Validación", "Sin Cupos", "No hay cupos disponibles para esta clase en la fecha seleccionada.", Alert.AlertType.WARNING);
            return false;
        }
        if (reservaController.usuarioTieneReservaMismoHorario(usuarioSeleccionado.getIdentificacion(), comboTipoClase.getValue(), dateFechaClase.getValue().toString())) {
            mostrarMensaje("Validación", "Reserva Duplicada", "El usuario ya tiene una reserva para esta clase en la misma fecha.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void actualizarCuposDisponibles() {
        if (comboTipoClase.getValue() != null && dateFechaClase.getValue() != null) {
            int cupos = reservaController.cuposDisponibles(comboTipoClase.getValue(), dateFechaClase.getValue().toString());
            lblCuposDisponibles.setText(String.valueOf(cupos));
            if (cupos > 5) lblCuposDisponibles.setStyle("-fx-text-fill: #27ae60;");
            else if (cupos > 0) lblCuposDisponibles.setStyle("-fx-text-fill: #f39c12;");
            else lblCuposDisponibles.setStyle("-fx-text-fill: #e74c3c;");
        } else {
            lblCuposDisponibles.setText("-");
            lblCuposDisponibles.setStyle("-fx-text-fill: black;");
        }
    }
    
    private void actualizarEstadoBotonesAccion() {
        boolean esActiva = reservaSeleccionada != null && "ACTIVA".equals(reservaSeleccionada.getEstado());
        btnCancelarReserva.setDisable(!esActiva);
        btnRegistrarAsistencia.setDisable(!esActiva);
    }

    private void limpiarFormularioReserva() {
        comboTipoClase.getSelectionModel().clearSelection();
        dateFechaClase.setValue(null);
        lblCuposDisponibles.setText("-");
        lblCuposDisponibles.setStyle("-fx-text-fill: black;");
        tableReserva.getSelectionModel().clearSelection();
        actualizarEstadoBotonesAccion();
    }

    private void limpiarTodaLaVista() {
        comboUsuario.getSelectionModel().clearSelection();
        reservasFiltradas.setPredicate(p -> true);
        actualizarInfoMembresia(null);
        actualizarBeneficios(null);
        panelReservaClases.setVisible(false);
        panelReservaClases.setManaged(false);
        lblMensajeReserva.setText("Seleccione un usuario para ver su información.");
        limpiarFormularioReserva();
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
