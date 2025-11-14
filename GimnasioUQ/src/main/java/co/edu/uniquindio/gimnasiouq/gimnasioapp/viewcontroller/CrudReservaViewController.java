package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.*;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class CrudReservaViewController {

    ReservaController reservaController;
    UsuarioController usuarioController;
    MembresiaController membresiaController;
    ClaseController claseController;
    EntrenadorController entrenadorController;

    ObservableList<Reserva> listaReservas;
    ObservableList<Usuario> listaUsuarios;
    ObservableList<Clase> listaClases;
    ObservableList<Entrenador> listaEntrenadoresBase; // Lista base de todos los entrenadores
    FilteredList<Entrenador> entrenadoresFiltradosParaHorario; // Lista filtrada en tiempo real

    Reserva reservaSeleccionada;
    Usuario usuarioSeleccionado;

    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private Label lblTipoMembresia, lblEstadoMembresia;
    @FXML private VBox panelReservaClases, panelEntrenadorVip;
    @FXML private ComboBox<Clase> comboClase;
    @FXML private DatePicker dateFechaClase;
    @FXML private Label lblCuposDisponibles;
    @FXML private ComboBox<Entrenador> comboEntrenador;
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
        listaEntrenadoresBase = entrenadorController.obtenerEntrenadores(); // Obtener la lista base de entrenadores

        entrenadoresFiltradosParaHorario = new FilteredList<>(listaEntrenadoresBase); // Envolver la lista base
        
        initDataBinding();
        configurarCombos();
        tableReserva.setItems(listaReservas); // Mostrar todas las reservas al inicio
        listenerSelection();
        limpiarTodaLaVista();
    }

    @FXML void onActionCrearReserva(ActionEvent event) { crearReserva(); }
    @FXML void onActionCancelarReserva(ActionEvent event) { cancelarReserva(); }

    private void crearReserva() {
        if (!validarCampos()) return;
        Entrenador entrenadorFinal = comboClase.getValue().getEntrenadorPorDefecto();
        if (panelEntrenadorVip.isVisible() && comboEntrenador.getValue() != null) {
            entrenadorFinal = comboEntrenador.getValue();
        }
        if (reservaController.crearReserva(
                "RES-" + UUID.randomUUID().toString().substring(0, 4),
                usuarioSeleccionado,
                comboClase.getValue(),
                dateFechaClase.getValue(),
                entrenadorFinal
        )) {
            mostrarMensaje("Creación Exitosa", "La reserva ha sido creada.");
            limpiarFormularioReserva();
        } else {
            mostrarMensaje("Error", "No se pudo crear la reserva.");
        }
    }

    private void cancelarReserva() {
        if (reservaSeleccionada != null && mostrarMensajeConfirmacion("¿Cancelar reserva?")) {
            if (reservaController.cancelarReserva(reservaSeleccionada.getCodigo())) {
                tableReserva.refresh();
            }
        }
    }
    
    //<editor-fold desc="Métodos Auxiliares">
    private void initDataBinding() {
        tcCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        tcClase.setCellValueFactory(cellData -> {
            String desc = cellData.getValue().getClase().getNombre();
            if (cellData.getValue().getEntrenador() != null) {
                desc += " con " + cellData.getValue().getEntrenador().getNombre();
            }
            return new SimpleStringProperty(desc);
        });
        tcFechaClase.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaClase().toString()));
        tcFechaReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaReserva().toString()));
        tcEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado()));
    }

    private void configurarCombos() {
        comboUsuario.setItems(listaUsuarios);
        comboClase.setItems(listaClases);
        comboEntrenador.setItems(entrenadoresFiltradosParaHorario); // Usar la FilteredList

        comboUsuario.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(Usuario item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getNombre()); }
        });
        comboUsuario.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Usuario item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getNombre()); }
        });

        comboClase.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(Clase item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.toString()); }
        });
        comboClase.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Clase item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.toString()); }
        });

        comboEntrenador.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(Entrenador item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getNombre()); }
        });
        comboEntrenador.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Entrenador item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getNombre()); }
        });
    }

    private void listenerSelection() {
        comboUsuario.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            usuarioSeleccionado = n;
            actualizarVistaUsuarioSeleccionado();
        });
        tableReserva.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> reservaSeleccionada = n);
        
        comboClase.valueProperty().addListener((obs, o, n) -> actualizarDisponibilidad());
        dateFechaClase.valueProperty().addListener((obs, o, n) -> actualizarDisponibilidad());
    }

    private void actualizarVistaUsuarioSeleccionado() {
        panelReservaClases.setVisible(false); panelReservaClases.setManaged(false);
        panelEntrenadorVip.setVisible(false); panelEntrenadorVip.setManaged(false);
        
        if (usuarioSeleccionado == null) {
            limpiarTodaLaVista();
            return;
        }
        tableReserva.setItems(listaReservas.filtered(r -> r.getUsuario().equals(usuarioSeleccionado)));
        Membresia m = membresiaController.obtenerMembresiaActivaUsuario(usuarioSeleccionado.getIdentificacion());
        actualizarInfoMembresia(m);
        if (m != null && m.estaActiva()) {
            boolean puedeReservar = m.getTipo() == TipoMembresia.PREMIUM || m.getTipo() == TipoMembresia.VIP;
            panelReservaClases.setVisible(puedeReservar); panelReservaClases.setManaged(puedeReservar);
            boolean esVip = m.getTipo() == TipoMembresia.VIP;
            panelEntrenadorVip.setVisible(esVip); panelEntrenadorVip.setManaged(esVip);
        }
        actualizarDisponibilidad(); // Asegurarse de que se actualice al cambiar de usuario
    }

    private void actualizarInfoMembresia(Membresia m) {
        if (m != null) {
            lblTipoMembresia.setText(m.getTipo().getNombre());
            lblEstadoMembresia.setText(m.getEstado().toString());
        } else {
            lblTipoMembresia.setText("N/A");
            lblEstadoMembresia.setText("SIN MEMBRESÍA");
        }
    }

    private boolean validarCampos() {
        Clase claseSeleccionada = comboClase.getValue();
        LocalDate fechaSeleccionada = dateFechaClase.getValue();
        if (claseSeleccionada == null || fechaSeleccionada == null) {
            mostrarMensaje("Error de Validación", "Debe seleccionar una clase y una fecha.");
            return false;
        }
        if (fechaSeleccionada.isBefore(LocalDate.now())) {
            mostrarMensaje("Error de Validación", "La fecha no puede ser en el pasado.");
            return false;
        }
        if (claseSeleccionada.getDia() != null && fechaSeleccionada.getDayOfWeek() != claseSeleccionada.getDia()) {
            mostrarMensaje("Error de Validación", "La clase no se dicta ese día de la semana.");
            return false;
        }
        if (reservaController.cuposDisponibles(claseSeleccionada, fechaSeleccionada) <= 0) {
            mostrarMensaje("Error de Validación", "No hay cupos disponibles para esta clase.");
            return false;
        }
        if (reservaController.usuarioTieneReservaMismoHorario(usuarioSeleccionado.getIdentificacion(), claseSeleccionada, fechaSeleccionada)) {
            mostrarMensaje("Error de Validación", "Ya tiene una reserva para esta clase en la misma fecha.");
            return false;
        }
        return true;
    }

    private void actualizarDisponibilidad() {
        Clase c = comboClase.getValue();
        LocalDate f = dateFechaClase.getValue();
        
        // Actualizar Cupos
        if (c != null && f != null) {
            int cupos = reservaController.cuposDisponibles(c, f);
            lblCuposDisponibles.setText(String.valueOf(cupos));
        } else {
            lblCuposDisponibles.setText("-");
        }

        // Actualizar Entrenadores
        if (c != null && f != null) {
            // SOLUCIÓN: Actualizar el predicado de la FilteredList
            entrenadoresFiltradosParaHorario.setPredicate(entrenador -> 
                entrenadorController.obtenerEntrenadoresDisponiblesParaHorario(c.getDia(), c.getHorario(), f, c)
                                    .contains(entrenador)
            );
        } else {
            entrenadoresFiltradosParaHorario.setPredicate(entrenador -> false); // Si no hay clase/fecha, no hay entrenadores
        }
    }

    private void limpiarFormularioReserva() {
        comboClase.getSelectionModel().clearSelection();
        dateFechaClase.setValue(null);
        comboEntrenador.getSelectionModel().clearSelection();
        lblCuposDisponibles.setText("-");
    }

    private void limpiarTodaLaVista() {
        comboUsuario.getSelectionModel().clearSelection();
        tableReserva.setItems(listaReservas); // Mostrar todas las reservas por defecto
        limpiarFormularioReserva();
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
    //</editor-fold>
}
