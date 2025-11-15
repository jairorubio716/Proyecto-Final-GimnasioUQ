package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.GimnasioApp;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.ControlAccesoController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Membresia;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Reserva;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ControlAccesoViewController {

    ControlAccesoController controlAccesoController;
    FilteredList<Reserva> listaReservasFiltrada;
    Usuario usuarioValidado;
    private Stage stageUsuariosDentro;
    private static final int GRACE_PERIOD_MINUTES = 15;

    @FXML private TextField txtIdentificacion, txtBuscarDentro;
    @FXML private Label lblNombreUsuario, lblTipoMembresia, lblEstadoMembresia;
    @FXML private VBox panelInfoUsuario, panelReservasHoy, panelBusquedaInterna;
    @FXML private TableView<Reserva> tableReservasHoy;
    @FXML private TableColumn<Reserva, String> tcClase, tcHorario, tcEntrenador, tcEstadoReserva;
    @FXML private Button btnRegistrarAsistencia;

    @FXML
    void initialize() {
        controlAccesoController = new ControlAccesoController();
        initView();
    }

    private void initView() {
        listaReservasFiltrada = new FilteredList<>(controlAccesoController.obtenerReservas(), p -> false);
        initDataBinding();
        tableReservasHoy.setItems(listaReservasFiltrada);
        listenerSelection();
        limpiarVista();
        
        controlAccesoController.obtenerUsuariosDentro().addListener((ListChangeListener<Usuario>) c -> {
            boolean hayAlguienDentro = !c.getList().isEmpty();
            panelBusquedaInterna.setVisible(hayAlguienDentro);
            panelBusquedaInterna.setManaged(hayAlguienDentro);

            if (usuarioValidado != null && !c.getList().contains(usuarioValidado)) {
                limpiarVista();
            }
        });
    }

    @FXML void onActionValidarAcceso(ActionEvent event) { validarAcceso(); }
    @FXML void onActionRegistrarAsistencia(ActionEvent event) { registrarAsistencia(); }
    @FXML void onActionLimpiar(ActionEvent event) { limpiarVista(); }
    @FXML void onActionVerUsuariosDentro(ActionEvent event) { verUsuariosDentro(); }
    @FXML void onActionBuscarDentro(ActionEvent event) { buscarDentro(); }

    private void validarAcceso() {
        String identificacion = txtIdentificacion.getText();
        if (identificacion.isEmpty()) {
            mostrarMensaje("Error de Validación", "Ingrese una identificación.", Alert.AlertType.WARNING);
            return;
        }

        limpiarVista();

        usuarioValidado = controlAccesoController.obtenerUsuario(identificacion);
        if (usuarioValidado == null) {
            mostrarMensaje("Acceso Denegado", "Usuario no encontrado.", Alert.AlertType.ERROR);
            return;
        }

        Membresia membresia = controlAccesoController.obtenerMembresiaActivaUsuario(identificacion);
        actualizarInfoUsuario(usuarioValidado, membresia);

        if (membresia != null && membresia.estaActiva()) {
            if (controlAccesoController.estaDentro(identificacion)) {
                mostrarMensaje("Acceso Permitido", "El usuario ya se encuentra dentro del gimnasio.", Alert.AlertType.INFORMATION);
            } else {
                controlAccesoController.registrarIngreso(usuarioValidado);
                mostrarMensaje("Acceso Permitido", "¡Bienvenido, " + usuarioValidado.getNombre() + "!", Alert.AlertType.INFORMATION);
            }
            
            listaReservasFiltrada.setPredicate(r -> r.getUsuario().getIdentificacion().equals(identificacion));
            panelReservasHoy.setVisible(true);
            panelReservasHoy.setManaged(true);
            
        } else {
            mostrarMensaje("Acceso Denegado", "El usuario no tiene una membresía activa.", Alert.AlertType.ERROR);
            panelReservasHoy.setVisible(false);
            panelReservasHoy.setManaged(false);
        }
    }

    private void buscarDentro() {
        String identificacion = txtBuscarDentro.getText();
        if (identificacion.isEmpty()) {
            mostrarMensaje("Error de Validación", "Ingrese una identificación para buscar.", Alert.AlertType.WARNING);
            return;
        }

        if (!controlAccesoController.estaDentro(identificacion)) {
            mostrarMensaje("Información", "El usuario con esa identificación no se encuentra actualmente en el gimnasio.", Alert.AlertType.INFORMATION);
            return;
        }

        usuarioValidado = controlAccesoController.obtenerUsuario(identificacion);
        Membresia membresia = controlAccesoController.obtenerMembresiaActivaUsuario(identificacion);
        actualizarInfoUsuario(usuarioValidado, membresia);
        listaReservasFiltrada.setPredicate(r -> r.getUsuario().getIdentificacion().equals(identificacion));
        panelReservasHoy.setVisible(true);
        panelReservasHoy.setManaged(true);
    }

    private void registrarAsistencia() {
        Reserva reservaSeleccionada = tableReservasHoy.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            mostrarMensaje("Advertencia", "Debe seleccionar una reserva para registrar la asistencia.", Alert.AlertType.WARNING);
            return;
        }

        if (!controlAccesoController.estaDentro(reservaSeleccionada.getUsuario().getIdentificacion())) {
            mostrarMensaje("Acción no permitida", "El usuario no ha registrado su ingreso al gimnasio.", Alert.AlertType.WARNING);
            return;
        }

        if (!reservaSeleccionada.getFechaClase().equals(LocalDate.now())) {
            mostrarMensaje("Acción no permitida", "Solo se puede registrar asistencia para reservas del día de hoy.", Alert.AlertType.WARNING);
            return;
        }

        LocalTime ahora = LocalTime.now();
        LocalTime horaInicioClase = reservaSeleccionada.getClase().getHorario();
        LocalTime horaFinClase = reservaSeleccionada.getClase().getHoraFin();
        LocalTime inicioCheckin = horaInicioClase.minusMinutes(GRACE_PERIOD_MINUTES);
        boolean enVentana = !ahora.isBefore(inicioCheckin) && ahora.isBefore(horaFinClase);

        if (!enVentana) {
            mostrarMensaje("Fuera de Horario", "Solo se puede registrar asistencia desde " + GRACE_PERIOD_MINUTES + " minutos antes del inicio y hasta el fin de la clase.", Alert.AlertType.WARNING);
            return;
        }

        if (controlAccesoController.registrarAsistencia(reservaSeleccionada.getCodigo())) {
            mostrarMensaje("Asistencia Registrada", "La asistencia ha sido registrada con éxito.", Alert.AlertType.INFORMATION);
            tableReservasHoy.refresh();
        } else {
            mostrarMensaje("Error", "No se pudo registrar la asistencia. La reserva debe estar en estado 'ACTIVA'.", Alert.AlertType.ERROR);
        }
    }

    private void verUsuariosDentro() {
        if (stageUsuariosDentro == null || !stageUsuariosDentro.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(GimnasioApp.class.getResource("UsuariosDentroView.fxml"));
                AnchorPane root = loader.load();
                Scene scene = new Scene(root);
                stageUsuariosDentro = new Stage();
                stageUsuariosDentro.setTitle("Usuarios Dentro del Gimnasio");
                stageUsuariosDentro.setScene(scene);
                stageUsuariosDentro.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            stageUsuariosDentro.toFront();
        }
    }

    private void initDataBinding() {
        tcClase.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClase().getNombre()));
        tcHorario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaClase().toString() + " " + cellData.getValue().getClase().getHorario().toString()));
        tcEntrenador.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEntrenador() != null ? cellData.getValue().getEntrenador().getNombre() : "N/A"));
        tcEstadoReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado()));
    }

    private void listenerSelection() {
        tableReservasHoy.getSelectionModel().selectedItemProperty().addListener((obs, old, newS) -> {
            if (newS == null) {
                btnRegistrarAsistencia.setDisable(true);
                return;
            }
            
            boolean esParaHoy = newS.getFechaClase().equals(LocalDate.now());
            boolean estaActiva = newS.getEstado().equals("ACTIVA");
            
            LocalTime ahora = LocalTime.now();
            LocalTime horaInicioClase = newS.getClase().getHorario();
            LocalTime horaFinClase = newS.getClase().getHoraFin();
            LocalTime inicioCheckin = horaInicioClase.minusMinutes(GRACE_PERIOD_MINUTES);
            boolean enVentana = !ahora.isBefore(inicioCheckin) && ahora.isBefore(horaFinClase);

            btnRegistrarAsistencia.setDisable(!(esParaHoy && estaActiva && enVentana));
        });
    }

    private void actualizarInfoUsuario(Usuario u, Membresia m) {
        panelInfoUsuario.setVisible(true);
        panelInfoUsuario.setManaged(true);
        lblNombreUsuario.setText(u.getNombre());
        if (m != null && m.estaActiva()) {
            lblTipoMembresia.setText(m.getTipo().getNombre());
            lblEstadoMembresia.setText(m.getEstado().toString());
            lblEstadoMembresia.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            lblTipoMembresia.setText("N/A");
            lblEstadoMembresia.setText("INACTIVA");
            lblEstadoMembresia.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }

    private void limpiarVista() {
        txtIdentificacion.clear();
        txtBuscarDentro.clear();
        lblNombreUsuario.setText("");
        lblTipoMembresia.setText("");
        lblEstadoMembresia.setText("");
        lblEstadoMembresia.setStyle("");
        panelInfoUsuario.setVisible(false);
        panelInfoUsuario.setManaged(false);
        panelReservasHoy.setVisible(false);
        panelReservasHoy.setManaged(false);
        if (listaReservasFiltrada != null) {
            listaReservasFiltrada.setPredicate(p -> false);
        }
        btnRegistrarAsistencia.setDisable(true);
        usuarioValidado = null;
    }

    private void mostrarMensaje(String titulo, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
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
