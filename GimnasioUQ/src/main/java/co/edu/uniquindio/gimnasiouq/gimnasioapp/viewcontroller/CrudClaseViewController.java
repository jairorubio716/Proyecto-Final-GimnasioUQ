package co.edu.uniquindio.gimnasiouq.gimnasioapp.viewcontroller;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.ClaseController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.controller.EntrenadorController;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Clase;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.Entrenador;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.utils.AlertasUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.UUID;

public class CrudClaseViewController {

    ClaseController claseController;
    EntrenadorController entrenadorController;
    ObservableList<Clase> listaClases;
    ObservableList<Entrenador> listaEntrenadores;
    Clase claseSeleccionada;

    @FXML private TextField txtNombreClase, txtHorario, txtHoraFin, txtCupoMaximo;
    @FXML private ComboBox<DayOfWeek> comboDia;
    @FXML private ComboBox<Entrenador> comboEntrenadorDefecto;
    @FXML private TableView<Clase> tableClases;
    @FXML private TableColumn<Clase, String> tcNombre, tcDia, tcHorario, tcHoraFin, tcCupo, tcEntrenador;

    @FXML
    void initialize() {
        claseController = new ClaseController();
        entrenadorController = new EntrenadorController();
        initView();
    }

    private void initView() {
        listaClases = claseController.obtenerClases();
        listaEntrenadores = entrenadorController.obtenerEntrenadores();
        initDataBinding();
        configurarCombos();
        tableClases.setItems(listaClases);
        listenerSelection();
    }

    @FXML void onActionAgregar() { crearClase(); }
    @FXML void onActionActualizar() { actualizarClase(); }
    @FXML void onActionEliminar() { eliminarClase(); }
    @FXML void onActionNuevo() { limpiarFormulario(); }

    private void crearClase() {
        try {
            if (!validarCampos()) return;

            LocalTime horaInicio = LocalTime.parse(txtHorario.getText());
            LocalTime horaFin = LocalTime.parse(txtHoraFin.getText());
            int cupoMaximo = Integer.parseInt(txtCupoMaximo.getText());
            String codigo = "CL-" + UUID.randomUUID().toString().substring(0, 4);

            if (claseController.crearClase(codigo, txtNombreClase.getText(), comboDia.getValue(), horaInicio, horaFin, cupoMaximo, comboEntrenadorDefecto.getValue())) {
                listaClases = claseController.obtenerClases();
                tableClases.setItems(listaClases);
                AlertasUtil.mostrarInformacion("Creación Exitosa", "La clase ha sido creada.");
                limpiarFormulario();
            } else {
                AlertasUtil.mostrarError("No se pudo crear la clase. Verifique que el código no esté duplicado o la disponibilidad del entrenador.");
            }
        } catch (DateTimeParseException e) {
            AlertasUtil.mostrarError("El formato de la hora debe ser HH:mm (Ej: 08:00).");
        } catch (NumberFormatException e) {
            AlertasUtil.mostrarError("El cupo máximo debe ser un número entero válido.");
        } catch (Exception e) {
            AlertasUtil.mostrarError("Ocurrió un error inesperado al crear la clase: " + e.getMessage());
        }
    }

    private void actualizarClase() {
        if (claseSeleccionada == null) {
            AlertasUtil.mostrarAdvertencia("Debe seleccionar una clase para actualizar.");
            return;
        }
        try {
            if (!validarCampos()) return;

            LocalTime horaInicio = LocalTime.parse(txtHorario.getText());
            LocalTime horaFin = LocalTime.parse(txtHoraFin.getText());
            int cupoMaximo = Integer.parseInt(txtCupoMaximo.getText());

            Clase claseActualizada = new Clase(
                    claseSeleccionada.getCodigo(),
                    txtNombreClase.getText(),
                    comboDia.getValue(),
                    horaInicio,
                    horaFin,
                    cupoMaximo,
                    comboEntrenadorDefecto.getValue()
            );

            if (claseController.actualizarClase(claseSeleccionada.getCodigo(), claseActualizada)) {
                tableClases.refresh();
                AlertasUtil.mostrarInformacion("Actualización Exitosa", "La clase ha sido actualizada.");
                limpiarFormulario();
            } else {
                AlertasUtil.mostrarError("No se pudo actualizar la clase. Verifique la disponibilidad del entrenador.");
            }
        } catch (DateTimeParseException e) {
            AlertasUtil.mostrarError("El formato de la hora debe ser HH:mm (Ej: 08:00).");
        } catch (NumberFormatException e) {
            AlertasUtil.mostrarError("El cupo máximo debe ser un número entero válido.");
        } catch (Exception e) {
            AlertasUtil.mostrarError("Ocurrió un error inesperado al actualizar la clase: " + e.getMessage());
        }
    }

    private void eliminarClase() {
        if (claseSeleccionada != null && AlertasUtil.mostrarConfirmacion("¿Está seguro de que desea eliminar la clase '" + claseSeleccionada.getNombre() + "'?")) {
            try {
                if (claseController.eliminarClase(claseSeleccionada.getCodigo())) {
                    listaClases.remove(claseSeleccionada);
                    AlertasUtil.mostrarInformacion("Eliminación Exitosa", "La clase ha sido eliminada.");
                    limpiarFormulario();
                } else {
                    AlertasUtil.mostrarError("No se pudo eliminar la clase.");
                }
            } catch (Exception e) {
                AlertasUtil.mostrarError("Ocurrió un error inesperado al eliminar la clase: " + e.getMessage());
            }
        }
    }
    
    private void initDataBinding() {
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tcDia.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDia().getDisplayName(TextStyle.FULL, new Locale("es", "ES"))));
        tcHorario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHorario().toString()));
        tcHoraFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoraFin().toString()));
        tcCupo.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCupoMaximo())));
        tcEntrenador.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEntrenadorPorDefecto().getNombre()));
    }

    private void configurarCombos() {
        comboDia.getItems().setAll(DayOfWeek.values());
        comboEntrenadorDefecto.setItems(listaEntrenadores);
        comboDia.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(DayOfWeek item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getDisplayName(TextStyle.FULL, new Locale("es", "ES"))); }
        });
        comboDia.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(DayOfWeek item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getDisplayName(TextStyle.FULL, new Locale("es", "ES"))); }
        });

        comboEntrenadorDefecto.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(Entrenador item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getNombre()); }
        });
        comboEntrenadorDefecto.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Entrenador item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item.getNombre()); }
        });
    }

    private void listenerSelection() {
        tableClases.getSelectionModel().selectedItemProperty().addListener((obs, o, newS) -> {
            claseSeleccionada = newS;
            mostrarInformacion(claseSeleccionada);
        });
    }

    private void mostrarInformacion(Clase c) {
        if (c != null) {
            txtNombreClase.setText(c.getNombre());
            txtNombreClase.setDisable(true);
            comboDia.setValue(c.getDia());
            txtHorario.setText(c.getHorario().toString());
            txtHoraFin.setText(c.getHoraFin().toString());
            txtCupoMaximo.setText(String.valueOf(c.getCupoMaximo()));
            comboEntrenadorDefecto.setValue(c.getEntrenadorPorDefecto());
        }
    }

    private void limpiarFormulario() {
        txtNombreClase.clear();
        txtNombreClase.setDisable(false);
        comboDia.getSelectionModel().clearSelection();
        txtHorario.clear();
        txtHoraFin.clear();
        txtCupoMaximo.clear();
        comboEntrenadorDefecto.getSelectionModel().clearSelection();
        tableClases.getSelectionModel().clearSelection();
        claseSeleccionada = null;
    }

    private boolean validarCampos() {
        String nombre = txtNombreClase.getText();
        String horarioStr = txtHorario.getText();
        String horaFinStr = txtHoraFin.getText();
        String cupoMaximoStr = txtCupoMaximo.getText();
        DayOfWeek dia = comboDia.getValue();
        Entrenador entrenador = comboEntrenadorDefecto.getValue();

        if (nombre.isEmpty() || horarioStr.isEmpty() || horaFinStr.isEmpty() || cupoMaximoStr.isEmpty() || dia == null || entrenador == null) {
            AlertasUtil.mostrarError("Todos los campos son obligatorios.");
            return false;
        }

        try {
            LocalTime horaInicio = LocalTime.parse(horarioStr);
            LocalTime horaFin = LocalTime.parse(horaFinStr);
            if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
                AlertasUtil.mostrarError("La hora de fin debe ser posterior a la hora de inicio.");
                return false;
            }
        } catch (DateTimeParseException e) {
            AlertasUtil.mostrarError("El formato de la hora debe ser HH:mm (Ej: 08:00).");
            return false;
        }

        try {
            int cupo = Integer.parseInt(cupoMaximoStr);
            if (cupo <= 0) {
                AlertasUtil.mostrarError("El cupo máximo debe ser un número positivo.");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertasUtil.mostrarError("El cupo máximo debe ser un número entero.");
            return false;
        }

        return true;
    }
}
