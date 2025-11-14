package co.edu.uniquindio.gimnasiouq.gimnasioapp.factory;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.utils.DataUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ModelFactory {
    private static ModelFactory modelFactory;
    private GimnasioUQ gimnasio;

    private final ObservableList<Usuario> usuariosObservable = FXCollections.observableArrayList();
    private final ObservableList<Clase> clasesObservable = FXCollections.observableArrayList();
    private final ObservableList<Entrenador> entrenadoresObservable = FXCollections.observableArrayList();
    private final ObservableList<Membresia> membresiasObservable = FXCollections.observableArrayList();
    private final ObservableList<Reserva> reservasObservable = FXCollections.observableArrayList();

    public static ModelFactory getInstancia() {
        if(modelFactory == null) {
            modelFactory = new ModelFactory();
        }
        return modelFactory;
    }

    private ModelFactory(){
        gimnasio = DataUtil.inicializarDatos();
        // Cargar las listas vivas con los datos iniciales
        usuariosObservable.setAll(gimnasio.getListaUsuarios());
        clasesObservable.setAll(gimnasio.getListaClases());
        entrenadoresObservable.setAll(gimnasio.getListaEntrenadores());
        membresiasObservable.setAll(gimnasio.getListaMembresias());
        reservasObservable.setAll(gimnasio.getListaReservas());
    }

    // --- GETTERS DE LISTAS VIVAS ---
    public ObservableList<Usuario> getUsuariosObservable() { return usuariosObservable; }
    public ObservableList<Clase> getClasesObservable() { return clasesObservable; }
    public ObservableList<Entrenador> getEntrenadoresObservable() { return entrenadoresObservable; }
    public ObservableList<Membresia> getMembresiasObservable() { return membresiasObservable; }
    public ObservableList<Reserva> getReservasObservable() { return reservasObservable; }

    // --- LÓGICA DE NEGOCIO ---

    //<editor-fold desc="CRUDs">
    public boolean crearUsuario(String nombre, String id, String edad, String tel, String tipo, String... args) {
        boolean fueCreado = gimnasio.crearUsuario(nombre, id, edad, tel, tipo, args);
        if (fueCreado) {
            usuariosObservable.setAll(gimnasio.getListaUsuarios()); // Recargar para reflejar el cambio
        }
        return fueCreado;
    }
    public boolean actualizarUsuario(String id, Usuario u) {
        boolean fueActualizado = gimnasio.actualizarUsuario(id, u);
        if (fueActualizado) {
            usuariosObservable.setAll(gimnasio.getListaUsuarios());
        }
        return fueActualizado;
    }
    public boolean eliminarUsuario(String id) {
        boolean fueEliminado = gimnasio.eliminarUsuario(id);
        if (fueEliminado) {
            usuariosObservable.removeIf(user -> user.getIdentificacion().equals(id));
            membresiasObservable.removeIf(m -> m.getIdentificacionUsuario().equals(id));
            reservasObservable.removeIf(r -> r.getUsuario().getIdentificacion().equals(id));
            return true;
        }
        return false;
    }

    public boolean crearClase(String codigo, String nombre, DayOfWeek dia, LocalTime horario, LocalTime horaFin, int cupo, Entrenador entrenador) {
        boolean fueCreada = gimnasio.crearClase(codigo, nombre, dia, horario, horaFin, cupo, entrenador);
        if (fueCreada) {
            clasesObservable.setAll(gimnasio.getListaClases());
            return true;
        }
        return false;
    }
    public boolean actualizarClase(String codigo, Clase c) {
        if (gimnasio.actualizarClase(codigo, c)) {
            clasesObservable.setAll(gimnasio.getListaClases());
            return true;
        }
        return false;
    }
    public boolean eliminarClase(String codigo) {
        if (gimnasio.eliminarClase(codigo)) {
            clasesObservable.removeIf(clase -> clase.getCodigo().equals(codigo));
            reservasObservable.removeIf(reserva -> reserva.getClase().getCodigo().equals(codigo)); // Eliminar reservas huérfanas
            return true;
        }
        return false;
    }
    
    public boolean crearEntrenador(String id, String nombre, String tel, String correo, double sueldo, boolean disponible) {
        boolean fueCreado = gimnasio.crearEntrenador(id, nombre, tel, correo, sueldo, disponible);
        if(fueCreado) {
            entrenadoresObservable.setAll(gimnasio.getListaEntrenadores());
        }
        return fueCreado;
    }
    public boolean actualizarEntrenador(String id, Entrenador e) {
        if(gimnasio.actualizarEntrenador(id, e)) {
            entrenadoresObservable.setAll(gimnasio.getListaEntrenadores());
            return true;
        }
        return false;
    }
    public boolean eliminarEntrenador(String id) {
        if(gimnasio.eliminarEntrenador(id)) {
            entrenadoresObservable.removeIf(ent -> ent.getIdentificacion().equals(id));
            return true;
        }
        return false;
    }

    public boolean crearMembresia(String codigo, Usuario usuario, TipoMembresia tipo, TipoMembresiaDuracion duracion, String fechaInicio, String fechaFin, EstadoMembresia estado) {
        double costo = calcularPrecioConDescuento(tipo, duracion, usuario);
        boolean fueCreada = gimnasio.crearMembresia(codigo, usuario.getIdentificacion(), tipo, duracion, costo, fechaInicio, fechaFin, estado);
        if(fueCreada) {
            membresiasObservable.setAll(gimnasio.getListaMembresias());
        }
        return fueCreada;
    }
    public boolean actualizarMembresia(String codigo, Membresia data) {
        if(gimnasio.actualizarMembresia(codigo, data)) {
            membresiasObservable.setAll(gimnasio.getListaMembresias());
            return true;
        }
        return false;
    }
    public boolean eliminarMembresia(String codigo) {
        if(gimnasio.eliminarMembresia(codigo)) {
            membresiasObservable.removeIf(m -> m.getCodigo().equals(codigo));
            return true;
        }
        return false;
    }

    public boolean crearReserva(String codigo, Usuario usuario, Clase clase, LocalDate fecha, Entrenador entrenador) {
        boolean fueCreada = gimnasio.crearReserva(codigo, usuario, clase, fecha, entrenador);
        if(fueCreada) {
            reservasObservable.setAll(gimnasio.getListaReservas());
        }
        return fueCreada;
    }
    public boolean cancelarReserva(String codigo) {
        boolean fueCancelada = gimnasio.cancelarReserva(codigo);
        if(fueCancelada) {
            reservasObservable.stream()
                .filter(r -> r.getCodigo().equals(codigo))
                .findFirst()
                .ifPresent(r -> r.setEstado("CANCELADA"));
            return true;
        }
        return false;
    }
    //</editor-fold>
    
    //<editor-fold desc="Lógica Adicional">
    public Membresia obtenerMembresiaActivaUsuario(String id) {
        return membresiasObservable.stream()
                .filter(m -> m.getIdentificacionUsuario().equals(id) && m.estaActiva())
                .findFirst().orElse(null);
    }
    
    public List<Entrenador> obtenerEntrenadoresDisponiblesParaHorario(DayOfWeek dia, LocalTime horario, LocalDate fechaClase, Clase claseSeleccionada) {
        // 1. Obtener todos los entrenadores que están disponibles en general.
        List<Entrenador> entrenadoresDisponiblesGeneral = entrenadoresObservable.stream()
                .filter(Entrenador::isDisponible)
                .collect(Collectors.toList());

        // 2. Obtener la lista de entrenadores que ya están OCUPADOS por CLASES GRUPALES a esa hora.
        List<Entrenador> entrenadoresOcupadosClasesGrupales = clasesObservable.stream()
                .filter(clase -> clase.getDia() == dia &&
                                 clase.getHorario().isBefore(horario.plusMinutes(1)) && // Solapamiento de inicio
                                 clase.getHoraFin().isAfter(horario.plusMinutes(1)) && // Solapamiento de fin
                                 clase.getEntrenadorPorDefecto() != null)
                .map(Clase::getEntrenadorPorDefecto)
                .collect(Collectors.toList());

        // 3. Obtener la lista de entrenadores que ya están OCUPADOS por RESERVAS ACTIVAS a esa hora y fecha.
        List<Entrenador> entrenadoresOcupadosPorReservas = reservasObservable.stream()
                .filter(reserva -> "ACTIVA".equals(reserva.getEstado()) &&
                                   reserva.getFechaClase().equals(fechaClase) &&
                                   reserva.getClase().getHorario().isBefore(horario.plusMinutes(1)) && // Solapamiento de inicio
                                   reserva.getClase().getHoraFin().isAfter(horario.plusMinutes(1)) && // Solapamiento de fin
                                   reserva.getEntrenador() != null)
                .map(Reserva::getEntrenador)
                .collect(Collectors.toList());

        // 4. Combinar las listas de entrenadores ocupados.
        List<Entrenador> entrenadoresOcupadosTotal = new java.util.ArrayList<>();
        entrenadoresOcupadosTotal.addAll(entrenadoresOcupadosClasesGrupales);
        entrenadoresOcupadosTotal.addAll(entrenadoresOcupadosPorReservas);

        // 5. Devolver solo los que están disponibles en general y NO están en la lista de ocupados totales.
        return entrenadoresDisponiblesGeneral.stream()
                .filter(entrenador -> !entrenadoresOcupadosTotal.contains(entrenador))
                .filter(entrenador -> claseSeleccionada == null || claseSeleccionada.getEntrenadorPorDefecto() == null || !claseSeleccionada.getEntrenadorPorDefecto().equals(entrenador))
                .collect(Collectors.toList());
    }

    public int cuposDisponibles(Clase clase, LocalDate fecha) {
        if (clase == null || fecha == null) return 0;
        long cuposOcupados = reservasObservable.stream().filter(reserva ->
            reserva.getClase().getCodigo().equals(clase.getCodigo()) &&
            reserva.getFechaClase().equals(fecha) &&
            "ACTIVA".equals(reserva.getEstado())
        ).count();
        return Math.max(0, clase.getCupoMaximo() - (int) cuposOcupados);
    }

    // SOLUCIÓN: Añadir el método usuarioTieneReservaMismoHorario al ModelFactory
    public boolean usuarioTieneReservaMismoHorario(String identificacionUsuario, Clase clase, LocalDate fecha) {
        return reservasObservable.stream().anyMatch(reserva ->
            reserva.getUsuario().getIdentificacion().equals(identificacionUsuario) &&
            reserva.getClase().getCodigo().equals(clase.getCodigo()) &&
            reserva.getFechaClase().equals(fecha) &&
            "ACTIVA".equals(reserva.getEstado())
        );
    }
    
    private double calcularPrecioConDescuento(TipoMembresia tipo, TipoMembresiaDuracion duracion, Usuario usuario) {
        double precioBase = tipo.getCostoMensual() * duracion.getMeses();
        if (usuario instanceof Estudiante) return precioBase * 0.8;
        if (usuario instanceof Trabajador) return precioBase * 0.9;
        return precioBase;
    }
    //</editor-fold>
}
