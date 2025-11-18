package co.edu.uniquindio.gimnasiouq.gimnasioapp.factory;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.utils.DataUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelFactory {
    private static ModelFactory modelFactory;
    private final GimnasioUQ gimnasio;

    private final ObservableList<Usuario> usuariosObservable = FXCollections.observableArrayList();
    private final ObservableList<Clase> clasesObservable = FXCollections.observableArrayList();
    private final ObservableList<Entrenador> entrenadoresObservable = FXCollections.observableArrayList();
    private final ObservableList<Membresia> membresiasObservable = FXCollections.observableArrayList();
    private final ObservableList<Reserva> reservasObservable = FXCollections.observableArrayList();
    private final ObservableList<Usuario> usuariosDentroObservable = FXCollections.observableArrayList();

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

    public ObservableList<Usuario> getUsuariosObservable() { return usuariosObservable; }
    public ObservableList<Clase> getClasesObservable() { return clasesObservable; }
    public ObservableList<Entrenador> getEntrenadoresObservable() { return entrenadoresObservable; }
    public ObservableList<Membresia> getMembresiasObservable() { return membresiasObservable; }
    public ObservableList<Reserva> getReservasObservable() { return reservasObservable; }
    public ObservableList<Usuario> getUsuariosDentroObservable() { return usuariosDentroObservable; }

    public boolean crearUsuario(String nombre, String id, String edad, String tel, String tipo, String... args) {
        if (gimnasio.crearUsuario(nombre, id, edad, tel, tipo, args)) {
            usuariosObservable.add(gimnasio.obtenerUsuario(id));
            return true;
        }
        return false;
    }
    public boolean actualizarUsuario(String id, Usuario u) {
        if (gimnasio.actualizarUsuario(id, u)) {
            return true;
        }
        return false;
    }
    public boolean eliminarUsuario(String id) {
        if (gimnasio.eliminarUsuario(id)) {
            usuariosObservable.removeIf(user -> user != null && user.getIdentificacion() != null && user.getIdentificacion().equals(id));
            membresiasObservable.removeIf(m -> m != null && m.getIdentificacionUsuario() != null && m.getIdentificacionUsuario().equals(id));
            reservasObservable.removeIf(r -> r != null && r.getUsuario() != null && r.getUsuario().getIdentificacion() != null && r.getUsuario().getIdentificacion().equals(id));
            return true;
        }
        return false;
    }

    public boolean crearClase(String codigo, String nombre, DayOfWeek dia, LocalTime horario, LocalTime horaFin, int cupo, Entrenador entrenador) {
        if (gimnasio.crearClase(codigo, nombre, dia, horario, horaFin, cupo, entrenador)) {
            clasesObservable.add(gimnasio.obtenerClase(codigo));
            return true;
        }
        return false;
    }
    public boolean actualizarClase(String codigo, Clase c) {
        if (gimnasio.actualizarClase(codigo, c)) {
            return true;
        }
        return false;
    }
    public boolean eliminarClase(String codigo) {
        if (gimnasio.eliminarClase(codigo)) {
            clasesObservable.removeIf(clase -> clase != null && clase.getCodigo() != null && clase.getCodigo().equals(codigo));
            reservasObservable.removeIf(reserva -> reserva != null && reserva.getClase() != null && reserva.getClase().getCodigo() != null && reserva.getClase().getCodigo().equals(codigo));
            return true;
        }
        return false;
    }
    
    public boolean crearEntrenador(String id, String nombre, String tel, String correo, double sueldo, boolean disponible) {
        if(gimnasio.crearEntrenador(id, nombre, tel, correo, sueldo, disponible)) {
            entrenadoresObservable.add(gimnasio.obtenerEntrenador(id));
            return true;
        }
        return false;
    }
    public boolean actualizarEntrenador(String id, Entrenador e) {
        if(gimnasio.actualizarEntrenador(id, e)) {
            return true;
        }
        return false;
    }
    public boolean eliminarEntrenador(String id) {
        List<Clase> clasesAEliminar = gimnasio.getListaClases().stream()
                .filter(clase -> clase != null && clase.getEntrenadorPorDefecto() != null && clase.getEntrenadorPorDefecto().getIdentificacion() != null && clase.getEntrenadorPorDefecto().getIdentificacion().equals(id))
                .collect(Collectors.toList());

        for (Clase clase : clasesAEliminar) {
            eliminarClase(clase.getCodigo());
        }

        if(gimnasio.eliminarEntrenador(id)) {
            entrenadoresObservable.removeIf(ent -> ent != null && ent.getIdentificacion() != null && ent.getIdentificacion().equals(id));
            return true;
        }
        return false;
    }

    public boolean crearMembresia(String codigo, Usuario usuario, TipoMembresia tipo, TipoMembresiaDuracion duracion, String fechaInicio, String fechaFin, EstadoMembresia estado) {
        double costo = calcularPrecioConDescuento(tipo, duracion, usuario);
        if(gimnasio.crearMembresia(codigo, usuario.getIdentificacion(), tipo, duracion, costo, fechaInicio, fechaFin, estado)) {
            membresiasObservable.add(gimnasio.obtenerMembresia(codigo));
            return true;
        }
        return false;
    }
    public boolean actualizarMembresia(String codigo, Membresia data) {
        if(gimnasio.actualizarMembresia(codigo, data)) {
            return true;
        }
        return false;
    }
    public boolean eliminarMembresia(String codigo) {
        if(gimnasio.eliminarMembresia(codigo)) {
            membresiasObservable.removeIf(m -> m != null && m.getCodigo() != null && m.getCodigo().equals(codigo));
            return true;
        }
        return false;
    }

    public boolean crearReserva(String codigo, Usuario usuario, Clase clase, LocalDate fecha, Entrenador entrenador) {
        if (gimnasio.crearReserva(codigo, usuario, clase, fecha, entrenador)) {
            reservasObservable.add(gimnasio.obtenerReserva(codigo));
            return true;
        }
        return false;
    }
    public boolean cancelarReserva(String codigo) {
        if(gimnasio.cancelarReserva(codigo)) {
            reservasObservable.stream().filter(r -> r != null && r.getCodigo() != null && r.getCodigo().equals(codigo)).findFirst().ifPresent(r -> r.setEstado("CANCELADA"));
            return true;
        }
        return false;
    }
    public boolean registrarAsistencia(String codigoReserva) {
        if(gimnasio.registrarAsistencia(codigoReserva)) {
            reservasObservable.stream().filter(r -> r != null && r.getCodigo() != null && r.getCodigo().equals(codigoReserva)).findFirst().ifPresent(r -> r.setEstado("COMPLETADA"));
            return true;
        }
        return false;
    }
    
    public boolean registrarIngreso(Usuario usuario) {
        if (gimnasio.registrarIngreso(usuario)) {
            usuariosDentroObservable.add(usuario);
            return true;
        }
        return false;
    }
    public boolean registrarSalida(Usuario usuario) {
        if (gimnasio.registrarSalida(usuario)) {
            usuariosDentroObservable.remove(usuario);
            return true;
        }
        return false;
    }
    public boolean estaDentro(String id) { return gimnasio.estaDentro(id); }

    public Rol validarCredenciales(String username, String password) {
        Administrador admin = DataUtil.getAdministradoresQuemados().stream()
                                    .filter(a -> a != null && a.getCorreo() != null && a.getCorreo().equals(username))
                                    .findFirst().orElse(null);
        if (admin != null && admin.getContrasena() != null && admin.getContrasena().equals(password)) {
            return Rol.ADMIN;
        }
        Recepcionista recep = DataUtil.getRecepcionistasQuemados().stream()
                                    .filter(r -> r != null && r.getCorreo() != null && r.getCorreo().equals(username))
                                    .findFirst().orElse(null);
        if (recep != null && recep.getContrasena() != null && recep.getContrasena().equals(password)) {
            return Rol.RECEPCIONISTA;
        }
        return null;
    }
    
    public Usuario obtenerUsuario(String id) { return gimnasio.obtenerUsuario(id); }
    public Membresia obtenerMembresiaActivaUsuario(String id) { return membresiasObservable.stream().filter(m -> m != null && m.getIdentificacionUsuario() != null && m.getIdentificacionUsuario().equals(id) && m.estaActiva()).findFirst().orElse(null); }
    public List<Reserva> obtenerReservasActivasUsuarioParaFecha(String idUsuario, LocalDate fecha) { return reservasObservable.stream().filter(r -> r != null && r.getUsuario() != null && r.getUsuario().getIdentificacion() != null && r.getUsuario().getIdentificacion().equals(idUsuario) && r.getFechaClase() != null && r.getFechaClase().equals(fecha) && "ACTIVA".equals(r.getEstado())).collect(Collectors.toList()); }
    public List<Entrenador> obtenerEntrenadoresDisponiblesParaHorario(DayOfWeek dia, LocalTime horario, LocalDate fechaClase, Clase claseSeleccionada) {
        List<Entrenador> entrenadoresDisponiblesGeneral = entrenadoresObservable.stream().filter(entrenador -> entrenador != null && entrenador.isDisponible()).collect(Collectors.toList());
        List<Entrenador> entrenadoresOcupadosClasesGrupales = clasesObservable.stream().filter(clase -> clase != null && clase.getDia() == dia && clase.getHorario() != null && clase.getHorario().isBefore(horario.plusMinutes(1)) && clase.getHoraFin() != null && clase.getHoraFin().isAfter(horario.plusMinutes(1)) && clase.getEntrenadorPorDefecto() != null).map(Clase::getEntrenadorPorDefecto).collect(Collectors.toList());
        List<Entrenador> entrenadoresOcupadosPorReservas = reservasObservable.stream().filter(reserva -> reserva != null && "ACTIVA".equals(reserva.getEstado()) && reserva.getFechaClase() != null && reserva.getFechaClase().equals(fechaClase) && reserva.getClase() != null && reserva.getClase().getHorario() != null && reserva.getClase().getHorario().isBefore(horario.plusMinutes(1)) && reserva.getClase().getHoraFin() != null && reserva.getClase().getHoraFin().isAfter(horario.plusMinutes(1)) && reserva.getEntrenador() != null).map(Reserva::getEntrenador).collect(Collectors.toList());
        List<Entrenador> entrenadoresOcupadosTotal = new java.util.ArrayList<>();
        entrenadoresOcupadosTotal.addAll(entrenadoresOcupadosClasesGrupales);
        entrenadoresOcupadosTotal.addAll(entrenadoresOcupadosPorReservas);
        return entrenadoresDisponiblesGeneral.stream().filter(entrenador -> entrenador != null && !entrenadoresOcupadosTotal.contains(entrenador)).filter(entrenador -> claseSeleccionada == null || claseSeleccionada.getEntrenadorPorDefecto() == null || !claseSeleccionada.getEntrenadorPorDefecto().equals(entrenador)).collect(Collectors.toList());
    }
    public int cuposDisponibles(Clase clase, LocalDate fecha) {
        if (clase == null || fecha == null) return 0;
        long cuposOcupados = reservasObservable.stream().filter(reserva -> reserva != null && reserva.getClase() != null && reserva.getClase().getCodigo() != null && reserva.getClase().getCodigo().equals(clase.getCodigo()) && reserva.getFechaClase() != null && reserva.getFechaClase().equals(fecha) && "ACTIVA".equals(reserva.getEstado())).count();
        return Math.max(0, clase.getCupoMaximo() - (int) cuposOcupados);
    }
    public boolean usuarioTieneReservaMismoHorario(String identificacionUsuario, Clase clase, LocalDate fecha) {
        return reservasObservable.stream().anyMatch(reserva -> reserva != null && reserva.getUsuario() != null && reserva.getUsuario().getIdentificacion() != null && reserva.getUsuario().getIdentificacion().equals(identificacionUsuario) && reserva.getClase() != null && reserva.getClase().getCodigo() != null && reserva.getClase().getCodigo().equals(clase.getCodigo()) && reserva.getFechaClase() != null && reserva.getFechaClase().equals(fecha) && "ACTIVA".equals(reserva.getEstado()));
    }
    private double calcularPrecioConDescuento(TipoMembresia tipo, TipoMembresiaDuracion duracion, Usuario usuario) {
        double precioBase = tipo.getCostoMensual() * duracion.getMeses();
        if (usuario instanceof Estudiante) return precioBase * 0.8;
        if (usuario instanceof Trabajador) return precioBase * 0.9;
        return precioBase;
    }
    
    public List<Usuario> obtenerUsuariosConMembresiaActiva() {
        return usuariosObservable.stream()
                .filter(u -> u != null && obtenerMembresiaActivaUsuario(u.getIdentificacion()) != null)
                .collect(Collectors.toList());
    }

    public List<Membresia> obtenerMembresiasProximasAVencer(int dias) {
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return membresiasObservable.stream()
                .filter(m -> {
                    try {
                        return m != null && m.estaActiva() && m.getFechaVencimiento() != null && LocalDate.parse(m.getFechaVencimiento()).isBefore(fechaLimite);
                    } catch (java.time.format.DateTimeParseException e) {
                        System.err.println("Error al parsear fecha de vencimiento para membresía " + m.getCodigo() + ": " + e.getMessage());
                        return false;
                    }
                })
                .sorted(Comparator.comparing(m -> {
                    try {
                        return m != null && m.getFechaVencimiento() != null ? LocalDate.parse(m.getFechaVencimiento()) : LocalDate.MAX;
                    } catch (java.time.format.DateTimeParseException e) {
                        return LocalDate.MAX;
                    }
                }))
                .collect(Collectors.toList());
    }
    
    public Map<String, Double> obtenerIngresosPorTipoMembresia() {
        return membresiasObservable.stream()
                .filter(m -> m != null && m.getTipo() != null && m.getTipo().getNombre() != null)
                .collect(Collectors.groupingBy(m -> m.getTipo().getNombre(), Collectors.summingDouble(Membresia::getCosto)));
    }

    public List<Reserva> obtenerHistorialAsistencias() {
        return reservasObservable.stream()
                .filter(r -> r != null && "COMPLETADA".equals(r.getEstado()))
                .sorted(Comparator.comparing(Reserva::getFechaClase, Comparator.nullsLast(LocalDate::compareTo)).reversed())
                .collect(Collectors.toList());
    }

    public Map<String, Long> obtenerRankingClasesReservadas() {
        return reservasObservable.stream()
                .filter(r -> r != null && r.getClase() != null && r.getClase().getNombre() != null)
                .collect(Collectors.groupingBy(r -> r.getClase().getNombre(), Collectors.counting()));
    }

    public Map<String, Long> obtenerRankingClasesAsistidas() {
        return reservasObservable.stream()
                .filter(r -> r != null && "COMPLETADA".equals(r.getEstado()) && r.getClase() != null && r.getClase().getNombre() != null)
                .collect(Collectors.groupingBy(r -> r.getClase().getNombre(), Collectors.counting()));
    }
}