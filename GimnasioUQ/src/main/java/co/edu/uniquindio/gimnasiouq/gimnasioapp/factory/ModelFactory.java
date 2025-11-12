package co.edu.uniquindio.gimnasiouq.gimnasioapp.factory;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.utils.DataUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelFactory {

    private static ModelFactory instancia;
    private GimnasioUQ gimnasio;

    private final ObservableList<Usuario> usuariosObservable = FXCollections.observableArrayList();
    private final ObservableList<Membresia> membresiasObservable = FXCollections.observableArrayList();
    private final ObservableList<Entrenador> entrenadoresObservable = FXCollections.observableArrayList();
    private final ObservableList<Reserva> reservasObservable = FXCollections.observableArrayList();
    private final ObservableList<Clase> clasesObservable = FXCollections.observableArrayList();

    public static ModelFactory getInstancia() {
        if (instancia == null) {
            instancia = new ModelFactory();
        }
        return instancia;
    }

    private ModelFactory() {
        gimnasio = DataUtil.inicializarDatos();
        usuariosObservable.setAll(gimnasio.getListaUsuarios());
        membresiasObservable.setAll(gimnasio.getListaMembresias());
        entrenadoresObservable.setAll(gimnasio.getListaEntrenadores());
        reservasObservable.setAll(gimnasio.getListaReservas());
        clasesObservable.setAll(gimnasio.getListaClases());
        verificarMembresiasVencidas();
    }

    //        CONSULTAS DE MEMBRESÍA

    public boolean usuarioTieneMembresiaActiva(String identificacionUsuario) {
        return membresiasObservable.stream().anyMatch(m -> m.getIdentificacionUsuario().equals(identificacionUsuario) && m.estaActiva());
    }

    public Membresia obtenerMembresiaActivaUsuario(String identificacionUsuario) {
        return membresiasObservable.stream()
                .filter(m -> m.getIdentificacionUsuario().equals(identificacionUsuario) && m.estaActiva())
                .findFirst().orElse(null);
    }


    public ObservableList<Clase> getClasesObservable() {
        return clasesObservable;
    }

    public ObservableList<Reserva> getReservasObservable() {
        return reservasObservable;
    }

    public boolean crearReserva(Reserva reserva) {
        boolean resultado = gimnasio.crearReserva(reserva);
        if (resultado) {
            reservasObservable.add(reserva);
        }
        return resultado;
    }

    public boolean cancelarReserva(String codigoReserva) {
        boolean resultado = gimnasio.cancelarReserva(codigoReserva);
        if (resultado) {
            reservasObservable.stream()
                .filter(r -> r.getCodigo().equals(codigoReserva))
                .findFirst()
                .ifPresent(r -> {
                    r.setEstado("CANCELADA");
                    int index = reservasObservable.indexOf(r);
                    reservasObservable.set(index, r);
                });
        }
        return resultado;
    }


    public boolean usuarioPuedeReservar(String identificacionUsuario) {
        return membresiasObservable.stream().anyMatch(m ->
            m.getIdentificacionUsuario().equals(identificacionUsuario) &&
            m.estaActiva() &&
            (m.getTipo() == TipoMembresia.PREMIUM || m.getTipo() == TipoMembresia.VIP)
        );
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

    public boolean usuarioTieneReservaMismoHorario(String identificacionUsuario, Clase clase, LocalDate fecha) {
        return reservasObservable.stream().anyMatch(reserva ->
            reserva.getUsuario().getIdentificacion().equals(identificacionUsuario) &&
            reserva.getClase().getCodigo().equals(clase.getCodigo()) &&
            reserva.getFechaClase().equals(fecha) &&
            "ACTIVA".equals(reserva.getEstado())
        );
    }
    
    public boolean crearUsuario(Usuario usuario) {
        boolean resultado = gimnasio.crearUsuario(usuario);
        if (resultado) {
            usuariosObservable.add(usuario);
        }
        return resultado;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        boolean resultado = gimnasio.actualizarUsuario(usuario);
        if (resultado) {
            Optional<Usuario> usuarioExistente = usuariosObservable.stream()
                    .filter(u -> u.getIdentificacion().equals(usuario.getIdentificacion()))
                    .findFirst();
            usuarioExistente.ifPresent(u -> {
                int index = usuariosObservable.indexOf(u);
                usuariosObservable.set(index, usuario);
            });
        }
        return resultado;
    }

    public boolean eliminarUsuario(String identificacion) {
        List<Membresia> membresiasAEliminar = membresiasObservable.stream()
                .filter(m -> m.getIdentificacionUsuario().equals(identificacion))
                .collect(Collectors.toList());
        for (Membresia membresia : membresiasAEliminar) {
            eliminarMembresia(membresia.getCodigo());
        }
        List<Reserva> reservasAEliminar = reservasObservable.stream()
                .filter(r -> r.getUsuario().getIdentificacion().equals(identificacion))
                .collect(Collectors.toList());
        for (Reserva reserva : reservasAEliminar) {
            cancelarReserva(reserva.getCodigo());
        }
        boolean resultado = gimnasio.eliminarUsuario(identificacion);
        if (resultado) {
            usuariosObservable.removeIf(u -> u.getIdentificacion().equals(identificacion));
        }
        return resultado;
    }

    public Usuario obtenerUsuario(String identificacion) {
        return gimnasio.obtenerUsuario(identificacion);
    }

    public ObservableList<Usuario> getUsuariosObservable() {
        return usuariosObservable;
    }

    public boolean existeUsuario(String identificacion) {
        return gimnasio.obtenerUsuario(identificacion) != null;
    }

    private double calcularPrecioConDescuento(TipoMembresia tipoMembresia, TipoMembresiaDuracion duracion, Usuario usuario) {
        double precioBase = tipoMembresia.getCostoMensual() * duracion.getMeses();
        if (usuario instanceof Estudiante) return precioBase * 0.8;
        else if (usuario instanceof Trabajador) return precioBase * 0.9;
        else return precioBase;
    }

    public boolean crearMembresia(Membresia membresia, Usuario usuario) {
        double precioConDescuento = calcularPrecioConDescuento(membresia.getTipo(), membresia.getDuracion(), usuario);
        membresia.setCosto(precioConDescuento);
        boolean resultado = gimnasio.crearMembresia(membresia);
        if (resultado) {
            membresiasObservable.add(membresia);
        }
        return resultado;
    }

    public boolean actualizarMembresia(Membresia membresia) {
        boolean resultado = gimnasio.actualizarMembresia(membresia);
        if (resultado) {
            Optional<Membresia> membresiaExistente = membresiasObservable.stream()
                    .filter(m -> m.getCodigo().equals(membresia.getCodigo()))
                    .findFirst();
            membresiaExistente.ifPresent(m -> {
                int index = membresiasObservable.indexOf(m);
                membresiasObservable.set(index, membresia);
            });
        }
        return resultado;
    }

    public boolean eliminarMembresia(String codigo) {
        boolean resultado = gimnasio.eliminarMembresia(codigo);
        if (resultado) {
            membresiasObservable.removeIf(m -> m.getCodigo().equals(codigo));
        }
        return resultado;
    }

    public void verificarMembresiasVencidas() {
        boolean hayCambios = false;
        for (Membresia membresia : gimnasio.getListaMembresias()) {
            if (membresia.getEstado() == EstadoMembresia.ACTIVA && membresia.estaVencida()) {
                membresia.setEstado(EstadoMembresia.INACTIVA);
                hayCambios = true;
            }
        }
        if (hayCambios) {
            membresiasObservable.setAll(gimnasio.getListaMembresias());
        }
    }

    public Membresia obtenerMembresia(String codigo) {
        return gimnasio.obtenerMembresia(codigo);
    }

    public ObservableList<Membresia> getMembresiasObservable() {
        return membresiasObservable;
    }

    public List<Reserva> obtenerReservasUsuario(String identificacionUsuario) {
        return gimnasio.getListaReservas().stream()
                .filter(r -> r.getUsuario().getIdentificacion().equals(identificacionUsuario))
                .collect(Collectors.toList());
    }

    public boolean crearEntrenador(Entrenador entrenador) {
        boolean resultado = gimnasio.crearEntrenador(entrenador);
        if (resultado) {
            entrenadoresObservable.add(entrenador);
        }
        return resultado;
    }

    public boolean actualizarEntrenador(Entrenador entrenador) {
        boolean resultado = gimnasio.actualizarEntrenador(entrenador);
        if (resultado) {
            Optional<Entrenador> entrenadorExistente = entrenadoresObservable.stream()
                    .filter(e -> e.getIdentificacion().equals(entrenador.getIdentificacion()))
                    .findFirst();
            entrenadorExistente.ifPresent(e -> {
                int index = entrenadoresObservable.indexOf(e);
                entrenadoresObservable.set(index, entrenador);
            });
        }
        return resultado;
    }

    public boolean eliminarEntrenador(String identificacion) {
        boolean resultado = gimnasio.eliminarEntrenador(identificacion);
        if (resultado) {
            entrenadoresObservable.removeIf(e -> e.getIdentificacion().equals(identificacion));
        }
        return resultado;
    }

    public Entrenador obtenerEntrenador(String identificacion) {
        return gimnasio.obtenerEntrenador(identificacion);
    }

    public ObservableList<Entrenador> getEntrenadoresObservable() {
        return entrenadoresObservable;
    }

    public List<Entrenador> obtenerEntrenadoresDisponibles() {
        return gimnasio.getListaEntrenadores().stream()
                .filter(Entrenador::isDisponible)
                .collect(Collectors.toList());
    }

}
