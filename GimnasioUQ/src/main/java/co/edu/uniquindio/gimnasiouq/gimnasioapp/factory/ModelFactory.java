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
    private final ObservableList<Usuario> usuariosDentroObservable = FXCollections.observableArrayList(); // NUEVA LISTA VIVA

    public static ModelFactory getInstancia() {
        if(modelFactory == null) {
            modelFactory = new ModelFactory();
        }
        return modelFactory;
    }

    private ModelFactory(){
        gimnasio = DataUtil.inicializarDatos();
        usuariosObservable.setAll(gimnasio.getListaUsuarios());
        clasesObservable.setAll(gimnasio.getListaClases());
        entrenadoresObservable.setAll(gimnasio.getListaEntrenadores());
        membresiasObservable.setAll(gimnasio.getListaMembresias());
        reservasObservable.setAll(gimnasio.getListaReservas());
        usuariosDentroObservable.setAll(gimnasio.getUsuariosDentroDelGimnasio());
    }

    // --- GETTERS DE LISTAS VIVAS ---
    public ObservableList<Usuario> getUsuariosObservable() { return usuariosObservable; }
    public ObservableList<Clase> getClasesObservable() { return clasesObservable; }
    public ObservableList<Entrenador> getEntrenadoresObservable() { return entrenadoresObservable; }
    public ObservableList<Membresia> getMembresiasObservable() { return membresiasObservable; }
    public ObservableList<Reserva> getReservasObservable() { return reservasObservable; }
    public ObservableList<Usuario> getUsuariosDentroObservable() { return usuariosDentroObservable; } // NUEVO GETTER

    //<editor-fold desc="CRUDs y Lógica">
    public boolean crearUsuario(String nombre, String id, String edad, String tel, String tipo, String... args) {
        if (gimnasio.crearUsuario(nombre, id, edad, tel, tipo, args)) {
            usuariosObservable.setAll(gimnasio.getListaUsuarios());
            return true;
        }
        return false;
    }
    public boolean actualizarUsuario(String id, Usuario u) {
        if (gimnasio.actualizarUsuario(id, u)) {
            usuariosObservable.setAll(gimnasio.getListaUsuarios());
            return true;
        }
        return false;
    }
    public boolean eliminarUsuario(String id) {
        if (gimnasio.eliminarUsuario(id)) {
            usuariosObservable.removeIf(user -> user.getIdentificacion().equals(id));
            membresiasObservable.removeIf(m -> m.getIdentificacionUsuario().equals(id));
            reservasObservable.removeIf(r -> r.getUsuario().getIdentificacion().equals(id));
            return true;
        }
        return false;
    }

    public boolean crearClase(String codigo, String nombre, DayOfWeek dia, LocalTime horario, LocalTime horaFin, int cupo, Entrenador entrenador) {
        if (gimnasio.crearClase(codigo, nombre, dia, horario, horaFin, cupo, entrenador)) {
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
            reservasObservable.removeIf(reserva -> reserva.getClase().getCodigo().equals(codigo));
            return true;
        }
        return false;
    }
    
    public boolean crearEntrenador(String id, String nombre, String tel, String correo, double sueldo, boolean disponible) {
        if(gimnasio.crearEntrenador(id, nombre, tel, correo, sueldo, disponible)) {
            entrenadoresObservable.setAll(gimnasio.getListaEntrenadores());
            return true;
        }
        return false;
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
        if(gimnasio.crearMembresia(codigo, usuario.getIdentificacion(), tipo, duracion, costo, fechaInicio, fechaFin, estado)) {
            membresiasObservable.setAll(gimnasio.getListaMembresias());
            return true;
        }
        return false;
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
        if(gimnasio.crearReserva(codigo, usuario, clase, fecha, entrenador)) {
            reservasObservable.setAll(gimnasio.getListaReservas());
            return true;
        }
        return false;
    }
    public boolean cancelarReserva(String codigo) {
        if(gimnasio.cancelarReserva(codigo)) {
            reservasObservable.stream().filter(r -> r.getCodigo().equals(codigo)).findFirst().ifPresent(r -> r.setEstado("CANCELADA"));
            return true;
        }
        return false;
    }
    public boolean registrarAsistencia(String codigoReserva) {
        if(gimnasio.registrarAsistencia(codigoReserva)) {
            reservasObservable.stream().filter(r -> r.getCodigo().equals(codigoReserva)).findFirst().ifPresent(r -> r.setEstado("COMPLETADA"));
            return true;
        }
        return false;
    }
    
    // NUEVOS MÉTODOS
    public boolean registrarIngreso(Usuario usuario) {
        if (gimnasio.registrarIngreso(usuario)) {
            usuariosDentroObservable.setAll(gimnasio.getUsuariosDentroDelGimnasio());
            return true;
        }
        return false;
    }
    public boolean registrarSalida(Usuario usuario) {
        if (gimnasio.registrarSalida(usuario)) {
            usuariosDentroObservable.setAll(gimnasio.getUsuariosDentroDelGimnasio());
            return true;
        }
        return false;
    }
    public boolean estaDentro(String id) {
        return gimnasio.estaDentro(id);
    }
    //</editor-fold>
    
    //<editor-fold desc="Lógica Adicional">
    public Usuario obtenerUsuario(String id) { return gimnasio.obtenerUsuario(id); }
    public Membresia obtenerMembresiaActivaUsuario(String id) { return membresiasObservable.stream().filter(m -> m.getIdentificacionUsuario().equals(id) && m.estaActiva()).findFirst().orElse(null); }
    public List<Reserva> obtenerReservasActivasUsuarioParaFecha(String idUsuario, LocalDate fecha) { return reservasObservable.stream().filter(r -> r.getUsuario().getIdentificacion().equals(idUsuario) && r.getFechaClase().equals(fecha) && r.getEstado().equals("ACTIVA")).collect(Collectors.toList()); }
    public List<Entrenador> obtenerEntrenadoresDisponiblesParaHorario(DayOfWeek dia, LocalTime horario, LocalDate fechaClase, Clase claseSeleccionada) {
        List<Entrenador> entrenadoresDisponiblesGeneral = entrenadoresObservable.stream().filter(Entrenador::isDisponible).collect(Collectors.toList());
        List<Entrenador> entrenadoresOcupadosClasesGrupales = clasesObservable.stream().filter(clase -> clase.getDia() == dia && clase.getHorario().isBefore(horario.plusMinutes(1)) && clase.getHoraFin().isAfter(horario.plusMinutes(1)) && clase.getEntrenadorPorDefecto() != null).map(Clase::getEntrenadorPorDefecto).collect(Collectors.toList());
        List<Entrenador> entrenadoresOcupadosPorReservas = reservasObservable.stream().filter(reserva -> "ACTIVA".equals(reserva.getEstado()) && reserva.getFechaClase().equals(fechaClase) && reserva.getClase().getHorario().isBefore(horario.plusMinutes(1)) && reserva.getClase().getHoraFin().isAfter(horario.plusMinutes(1)) && reserva.getEntrenador() != null).map(Reserva::getEntrenador).collect(Collectors.toList());
        List<Entrenador> entrenadoresOcupadosTotal = new java.util.ArrayList<>();
        entrenadoresOcupadosTotal.addAll(entrenadoresOcupadosClasesGrupales);
        entrenadoresOcupadosTotal.addAll(entrenadoresOcupadosPorReservas);
        return entrenadoresDisponiblesGeneral.stream().filter(entrenador -> !entrenadoresOcupadosTotal.contains(entrenador)).filter(entrenador -> claseSeleccionada == null || claseSeleccionada.getEntrenadorPorDefecto() == null || !claseSeleccionada.getEntrenadorPorDefecto().equals(entrenador)).collect(Collectors.toList());
    }
    public int cuposDisponibles(Clase clase, LocalDate fecha) {
        if (clase == null || fecha == null) return 0;
        long cuposOcupados = reservasObservable.stream().filter(reserva -> reserva.getClase().getCodigo().equals(clase.getCodigo()) && reserva.getFechaClase().equals(fecha) && "ACTIVA".equals(reserva.getEstado())).count();
        return Math.max(0, clase.getCupoMaximo() - (int) cuposOcupados);
    }
    public boolean usuarioTieneReservaMismoHorario(String identificacionUsuario, Clase clase, LocalDate fecha) {
        return reservasObservable.stream().anyMatch(reserva -> reserva.getUsuario().getIdentificacion().equals(identificacionUsuario) && reserva.getClase().getCodigo().equals(clase.getCodigo()) && reserva.getFechaClase().equals(fecha) && "ACTIVA".equals(reserva.getEstado()));
    }
    private double calcularPrecioConDescuento(TipoMembresia tipo, TipoMembresiaDuracion duracion, Usuario usuario) {
        double precioBase = tipo.getCostoMensual() * duracion.getMeses();
        if (usuario instanceof Estudiante) return precioBase * 0.8;
        if (usuario instanceof Trabajador) return precioBase * 0.9;
        return precioBase;
    }
    //</editor-fold>
}
