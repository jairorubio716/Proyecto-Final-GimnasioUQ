package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.ClaseController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.EntrenadorController;
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
    ClaseController claseController;
    EntrenadorController entrenadorController;

    // Observable Lists
    ObservableList<Reserva> listaReservas;
    ObservableList<Usuario> listaUsuarios;
    ObservableList<Clase> listaClases;
    FilteredList<Entrenador> entrenadoresDisponibles;

    // Selection
    Reserva reservaSeleccionada;
    Usuario usuarioSeleccionado;

    // FXML Components
    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private Label lblTipoMembresia, lblEstadoMembresia;
    
    @FXML private VBox panelReservaClases;
    @FXML private ComboBox<Clase> comboClase;
    @FXML private DatePicker dateFechaClase;
    @FXML private Label lblCuposDisponibles;
    
    @FXML private VBox panelEntrenadorVip;
    @FXML private ComboBox<Entrenador> comboEntrenador;
    
    @FXML private Button btnCrearReserva;
    @FXML private TableView<Reserva> tableReserva;
    @FXML private TableColumn<Reserva, String> tcCodigo, tcClase, tcFechaClase, tcFechaReserva, tcEstado;
    @FXML private Button btnCancelarReserva;

    @FXML
    void initialize() {
        reservaController = new ReservaController();
        usuarioController = new UsuarioController();
        membresiaController = new MembresiaController();
        claseController = new ClaseController();
        entrenadorController = new EntrenadorController();
        initView();
    }

    private void initView() {
        listaUsuarios = usuarioController.obtenerUsuarios();
        listaReservas = reservaController.obtenerReservas();
        listaClases = claseController.obtenerClases();
        
        entrenadoresDisponibles = new FilteredList<>(entrenadorController.obtenerEntrenadores());
        entrenadoresDisponibles.setPredicate(Entrenador::isDisponible);

        initDataBinding();
        configurarCombos();
        listenerSelection();
        limpiarTodaLaVista();
    }

    private void initDataBinding() {
        tableReserva.setItems(listaReservas.filtered(r -> usuarioSeleccionado != null && r.getUsuario().getIdentificacion().equals(usuarioSeleccionado.getIdentificacion())));
        tcCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        
        tcClase.setCellValueFactory(cellData -> {
            Reserva reserva = cellData.getValue();
            String textoClase = reserva.getClase().getNombre();
            if (reserva.getEntrenador() != null) {
                textoClase += " con " + reserva.getEntrenador().getNombre();
            }
            return new SimpleStringProperty(textoClase);
        });
        
        tcFechaClase.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaClase().toString()));
        tcFechaReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaReserva().toString()));
        tcEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado()));
    }

    private void configurarCombos() {
        comboUsuario.setItems(listaUsuarios);
        comboClase.setItems(listaClases);
        comboEntrenador.setItems(entrenadoresDisponibles);

        // AHORA SÍ: CellFactories para mostrar texto legible, dentro de configurarCombos()
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

        comboClase.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Clase item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });
        comboClase.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Clase item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleccione una clase" : item.getNombre());
            }
        });

        comboEntrenador.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Entrenador item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        comboEntrenador.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Entrenador item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleccione un entrenador" : item.getNombre());
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
        
        comboClase.valueProperty().addListener((obs, oldV, newV) -> actualizarEntrenadoresDisponibles());
        dateFechaClase.valueProperty().addListener((obs, oldV, newV) -> actualizarCuposDisponibles());
    }

    private void actualizarVistaUsuarioSeleccionado() {
        panelReservaClases.setVisible(false);
        panelReservaClases.setManaged(false);
        panelEntrenadorVip.setVisible(false);
        panelEntrenadorVip.setManaged(false);
        
        if (usuarioSeleccionado == null) {
            limpiarTodaLaVista();
            return;
        }

        tableReserva.setItems(listaReservas.filtered(r -> r.getUsuario().getIdentificacion().equals(usuarioSeleccionado.getIdentificacion())));
        Membresia membresia = membresiaController.obtenerMembresiaActivaUsuario(usuarioSeleccionado.getIdentificacion());
        actualizarInfoMembresia(membresia);

        if (membresia != null && membresia.estaActiva()) {
            boolean esPremiumOVip = membresia.getTipo() == TipoMembresia.PREMIUM || membresia.getTipo() == TipoMembresia.VIP;
            panelReservaClases.setVisible(esPremiumOVip);
            panelReservaClases.setManaged(esPremiumOVip);

            boolean esVip = membresia.getTipo() == TipoMembresia.VIP;
            panelEntrenadorVip.setVisible(esVip);
            panelEntrenadorVip.setManaged(esVip);
        }
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

    @FXML void onActionCrearReserva(ActionEvent event) {
        if (!validarCamposReserva()) {
            return;
        }

        Clase claseSeleccionada = comboClase.getValue();
        Entrenador entrenadorFinal = claseSeleccionada.getEntrenadorPorDefecto();
        
        if (panelEntrenadorVip.isVisible() && comboEntrenador.getValue() != null) {
            entrenadorFinal = comboEntrenador.getValue();
        }

        Reserva nuevaReserva = new Reserva(
            "RES-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase(),
            usuarioSeleccionado,
            claseSeleccionada,
            dateFechaClase.getValue(),
            entrenadorFinal
        );

        if (reservaController.crearReserva(nuevaReserva)) {
            mostrarMensaje("Notificación", "Reserva Exitosa", "La reserva ha sido creada.", Alert.AlertType.INFORMATION);
            limpiarFormularioReserva();
        } else {
            mostrarMensaje("Error", "Error de Creación", "No se pudo crear la reserva.", Alert.AlertType.ERROR);
        }
    }

    @FXML void onActionCancelarReserva(ActionEvent event) {
        if (reservaSeleccionada != null) {
            if (mostrarMensajeConfirmacion("¿Desea cancelar la reserva " + reservaSeleccionada.getCodigo() + "?")) {
                if (reservaController.cancelarReserva(reservaSeleccionada.getCodigo())) {
                    tableReserva.refresh();
                }
            }
        }
    }

    private boolean validarCamposReserva() {
        Clase claseSeleccionada = comboClase.getValue();
        LocalDate fechaSeleccionada = dateFechaClase.getValue();
        if (claseSeleccionada == null || fechaSeleccionada == null) {
            mostrarMensaje("Validación", "Campos Incompletos", "Debe seleccionar una clase y una fecha.", Alert.AlertType.WARNING);
            return false;
        }
        if (fechaSeleccionada.isBefore(LocalDate.now())) {
            mostrarMensaje("Validación", "Fecha Inválida", "La fecha no puede ser en el pasado.", Alert.AlertType.WARNING);
            return false;
        }
        if (claseSeleccionada.getDia() != null && fechaSeleccionada.getDayOfWeek() != claseSeleccionada.getDia()) {
            mostrarMensaje("Validación", "Día Incorrecto", "La clase seleccionada no se dicta ese día de la semana.", Alert.AlertType.WARNING);
            return false;
        }
        if (reservaController.cuposDisponibles(claseSeleccionada, fechaSeleccionada) <= 0) {
            mostrarMensaje("Validación", "Sin Cupos", "No hay cupos disponibles para esta clase.", Alert.AlertType.WARNING);
            return false;
        }
        if (reservaController.usuarioTieneReservaMismoHorario(usuarioSeleccionado.getIdentificacion(), claseSeleccionada, fechaSeleccionada)) {
            mostrarMensaje("Validación", "Reserva Duplicada", "Ya tiene una reserva para esta clase en la misma fecha.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void actualizarCuposDisponibles() {
        Clase clase = comboClase.getValue();
        LocalDate fecha = dateFechaClase.getValue();
        lblCuposDisponibles.setText(clase != null && fecha != null ? String.valueOf(reservaController.cuposDisponibles(clase, fecha)) : "-");
    }
    
    private void actualizarEntrenadoresDisponibles() {
        Clase claseSeleccionada = comboClase.getValue();
        if (claseSeleccionada != null) {
            ObservableList<Entrenador> disponibles = entrenadorController.obtenerEntrenadoresDisponiblesParaHorario(
                claseSeleccionada.getDia(),
                claseSeleccionada.getHorario()
            );
            comboEntrenador.setItems(disponibles);
        } else {
            comboEntrenador.getItems().clear();
        }
        actualizarCuposDisponibles();
    }
    
    private void actualizarEstadoBotonesAccion() {
        boolean esActiva = reservaSeleccionada != null && "ACTIVA".equals(reservaSeleccionada.getEstado());
        btnCancelarReserva.setDisable(!esActiva);
    }

    private void limpiarFormularioReserva() {
        comboClase.getSelectionModel().clearSelection();
        comboEntrenador.getSelectionModel().clearSelection();
        dateFechaClase.setValue(null);
        lblCuposDisponibles.setText("-");
        tableReserva.getSelectionModel().clearSelection();
        actualizarEstadoBotonesAccion();
    }

    private void limpiarTodaLaVista() {
        comboUsuario.getSelectionModel().clearSelection();
        actualizarInfoMembresia(null);
        panelReservaClases.setVisible(false);
        panelReservaClases.setManaged(false);
        tableReserva.setItems(null);
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
