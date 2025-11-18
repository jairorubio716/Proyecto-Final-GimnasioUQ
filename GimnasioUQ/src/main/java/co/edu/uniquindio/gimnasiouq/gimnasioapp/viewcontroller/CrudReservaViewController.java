package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.*;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.utils.AlertasUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.util.UUID;

public class CrudReservaViewController {

    ReservaController reservaController;
    UsuarioController usuarioController;
    MembresiaController membresiaController;
    ClaseController claseController;
    EntrenadorController entrenadorController;

    ObservableList<Reserva> listaReservas;
    ObservableList<Usuario> listaUsuarios;
    ObservableList<Clase> listaClases;
    ObservableList<Entrenador> listaEntrenadoresBase;
    FilteredList<Entrenador> entrenadoresFiltradosParaHorario;

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
        listaEntrenadoresBase = entrenadorController.obtenerEntrenadores();

        entrenadoresFiltradosParaHorario = new FilteredList<>(listaEntrenadoresBase);
        
        initDataBinding();
        configurarCombos();
        listenerSelection();
        limpiarTodaLaVista();
    }

    @FXML void onActionCrearReserva() { crearReserva(); }
    @FXML void onActionCancelarReserva() { cancelarReserva(); }

    private void crearReserva() {
        try {
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
                actualizarTablaReservasUsuario();
                AlertasUtil.mostrarInformacion("Creación Exitosa", "La reserva ha sido creada.");
                limpiarFormularioReserva();
            } else {
                AlertasUtil.mostrarError("No se pudo crear la reserva. Verifique los datos o la disponibilidad.");
            }
        } catch (Exception e) {
            AlertasUtil.mostrarError("Ocurrió un error inesperado al crear la reserva: " + e.getMessage());
        }
    }

    private void cancelarReserva() {
        if (reservaSeleccionada != null && AlertasUtil.mostrarConfirmacion("¿Está seguro de que desea cancelar la reserva?")) {
            try {
                if (reservaController.cancelarReserva(reservaSeleccionada.getCodigo())) {
                    actualizarTablaReservasUsuario();
                    AlertasUtil.mostrarInformacion("Cancelación Exitosa", "La reserva ha sido cancelada.");
                } else {
                    AlertasUtil.mostrarError("No se pudo cancelar la reserva.");
                }
            } catch (Exception e) {
                AlertasUtil.mostrarError("Ocurrió un error inesperado al cancelar la reserva: " + e.getMessage());
            }
        }
    }
    
    private void initDataBinding() {
        tcCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        tcClase.setCellValueFactory(cellData -> {
            String desc = cellData.getValue().getClase().getNombre();
            if (cellData.getValue().getEntrenador() != null && !cellData.getValue().getEntrenador().equals(cellData.getValue().getClase().getEntrenadorPorDefecto())) {
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
        comboEntrenador.setItems(entrenadoresFiltradosParaHorario);

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
        
        actualizarTablaReservasUsuario();
        
        Membresia m = membresiaController.obtenerMembresiaActivaUsuario(usuarioSeleccionado.getIdentificacion());
        actualizarInfoMembresia(m);
        
        if (m != null && m.estaActiva()) {
            boolean puedeReservar = m.getTipo() == TipoMembresia.PREMIUM || m.getTipo() == TipoMembresia.VIP;
            panelReservaClases.setVisible(puedeReservar); panelReservaClases.setManaged(puedeReservar);
            boolean esVip = m.getTipo() == TipoMembresia.VIP;
            panelEntrenadorVip.setVisible(esVip); panelEntrenadorVip.setManaged(esVip);
        }
        actualizarDisponibilidad();
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
        
        if (usuarioSeleccionado == null) {
            AlertasUtil.mostrarError("Debe seleccionar un usuario primero.");
            return false;
        }
        if (claseSeleccionada == null || fechaSeleccionada == null) {
            AlertasUtil.mostrarError("Debe seleccionar una clase y una fecha.");
            return false;
        }
        if (fechaSeleccionada.isBefore(LocalDate.now())) {
            AlertasUtil.mostrarError("La fecha de la clase no puede ser en el pasado.");
            return false;
        }
        if (claseSeleccionada.getDia() != null && fechaSeleccionada.getDayOfWeek() != claseSeleccionada.getDia()) {
            AlertasUtil.mostrarError("La clase seleccionada no se dicta el día de la semana elegido.");
            return false;
        }
        if (reservaController.cuposDisponibles(claseSeleccionada, fechaSeleccionada) <= 0) {
            AlertasUtil.mostrarError("No hay cupos disponibles para esta clase en la fecha seleccionada.");
            return false;
        }
        if (reservaController.usuarioTieneReservaMismoHorario(usuarioSeleccionado.getIdentificacion(), claseSeleccionada, fechaSeleccionada)) {
            AlertasUtil.mostrarError("El usuario ya tiene una reserva para esta clase en la misma fecha y hora.");
            return false;
        }
        return true;
    }

    private void actualizarDisponibilidad() {
        Clase c = comboClase.getValue();
        LocalDate f = dateFechaClase.getValue();
        
        if (c != null && f != null) {
            int cupos = reservaController.cuposDisponibles(c, f);
            lblCuposDisponibles.setText(String.valueOf(cupos));
        } else {
            lblCuposDisponibles.setText("-");
        }

        if (c != null && f != null) {
            entrenadoresFiltradosParaHorario.setPredicate(entrenador -> 
                entrenadorController.obtenerEntrenadoresDisponiblesParaHorario(c.getDia(), c.getHorario(), f, c)
                                    .contains(entrenador)
            );
        } else {
            entrenadoresFiltradosParaHorario.setPredicate(entrenador -> false);
        }
    }
    
    private void actualizarTablaReservasUsuario() {
        if (usuarioSeleccionado != null) {
            tableReserva.setItems(listaReservas.filtered(r -> r.getUsuario().equals(usuarioSeleccionado)));
        } else {
            tableReserva.setItems(listaReservas);
        }
        tableReserva.refresh();
    }

    private void limpiarFormularioReserva() {
        comboClase.getSelectionModel().clearSelection();
        dateFechaClase.setValue(null);
        comboEntrenador.getSelectionModel().clearSelection();
        lblCuposDisponibles.setText("-");
    }

    private void limpiarTodaLaVista() {
        comboUsuario.getSelectionModel().clearSelection();
        tableReserva.setItems(listaReservas);
        limpiarFormularioReserva();
        lblTipoMembresia.setText("N/A");
        lblEstadoMembresia.setText("N/A");
    }
}
