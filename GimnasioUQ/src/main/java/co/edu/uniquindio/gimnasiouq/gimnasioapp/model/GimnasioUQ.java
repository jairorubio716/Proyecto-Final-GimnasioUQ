package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class GimnasioUQ {

    private String nombre;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private ArrayList<Membresia> listaMembresias = new ArrayList<>();
    private ArrayList<Clase> listaClases = new ArrayList<>();
    private ArrayList<Entrenador> listaEntrenadores = new ArrayList<>();
    private ArrayList<Reserva> listaReservas = new ArrayList<>();
    private ArrayList<Usuario> usuariosDentroDelGimnasio = new ArrayList<>();

    private static final int GRACE_PERIOD_MINUTES = 15;

    public GimnasioUQ() {}
    public GimnasioUQ(String nombre) { this.nombre = nombre; }

    //<editor-fold desc="Getters">
    public ArrayList<Usuario> getListaUsuarios() { return listaUsuarios; }
    public ArrayList<Membresia> getListaMembresias() { return listaMembresias; }
    public ArrayList<Clase> getListaClases() { return listaClases; }
    public ArrayList<Entrenador> getListaEntrenadores() { return listaEntrenadores; }
    public ArrayList<Reserva> getListaReservas() { return listaReservas; }
    public ArrayList<Usuario> getUsuariosDentroDelGimnasio() { return usuariosDentroDelGimnasio; }
    //</editor-fold>

    //<editor-fold desc="CRUDs y Lógica">
    public boolean crearUsuario(String nombre, String id, String edad, String tel, String tipo, String... args) {
        if (obtenerUsuario(id) != null) return false;
        Usuario nuevoUsuario = null;
        switch (tipo) {
            case "Estudiante": nuevoUsuario = new Estudiante(nombre, id, edad, tel, args[0], args[1]); break;
            case "Trabajador": nuevoUsuario = new Trabajador(nombre, id, edad, tel, args[0]); break;
            case "Externo": nuevoUsuario = new Externo(nombre, id, edad, tel, args[0]); break;
        }
        if (nuevoUsuario != null) return listaUsuarios.add(nuevoUsuario);
        return false;
    }

    public boolean actualizarUsuario(String identificacion, Usuario data) {
        Usuario usuarioActual = obtenerUsuario(identificacion);
        if (usuarioActual != null) {
            if (!usuarioActual.getClass().equals(data.getClass())) {
                eliminarUsuario(identificacion);
                data.setIdentificacion(identificacion);
                return listaUsuarios.add(data);
            } else {
                usuarioActual.setNombre(data.getNombre());
                usuarioActual.setEdad(data.getEdad());
                usuarioActual.setTelefono(data.getTelefono());
                if (usuarioActual instanceof Estudiante) {
                    ((Estudiante) usuarioActual).setSemestre(((Estudiante) data).getSemestre());
                    ((Estudiante) usuarioActual).setPrograma(((Estudiante) data).getPrograma());
                } else if (usuarioActual instanceof Trabajador) {
                    ((Trabajador) usuarioActual).setCargo(((Trabajador) data).getCargo());
                } else if (usuarioActual instanceof Externo) {
                    ((Externo) usuarioActual).setInstitucion(((Externo) data).getInstitucion());
                }
                return true;
            }
        }
        return false;
    }

    public boolean eliminarUsuario(String id) { return listaUsuarios.removeIf(u -> u.getIdentificacion().equals(id)); }
    public Usuario obtenerUsuario(String id) { return listaUsuarios.stream().filter(u -> u.getIdentificacion().equals(id)).findFirst().orElse(null); }

    public boolean crearClase(String codigo, String nombre, DayOfWeek dia, LocalTime horario, LocalTime horaFin, int cupo, Entrenador entrenador) {
        if (obtenerClase(codigo) != null) return false;
        if (!esEntrenadorDisponibleParaClase(entrenador, dia, horario, horaFin, null)) return false;
        return listaClases.add(new Clase(codigo, nombre, dia, horario, horaFin, cupo, entrenador));
    }
    public boolean actualizarClase(String codigo, Clase data) {
        Clase c = obtenerClase(codigo);
        if (c != null) {
            if (!esEntrenadorDisponibleParaClase(data.getEntrenadorPorDefecto(), data.getDia(), data.getHorario(), data.getHoraFin(), codigo)) return false;
            c.setNombre(data.getNombre());
            c.setDia(data.getDia());
            c.setHorario(data.getHorario());
            c.setHoraFin(data.getHoraFin());
            c.setCupoMaximo(data.getCupoMaximo());
            c.setEntrenadorPorDefecto(data.getEntrenadorPorDefecto());
            return true;
        }
        return false;
    }
    public boolean eliminarClase(String codigo) { return listaClases.removeIf(c -> c.getCodigo().equals(codigo)); }
    public Clase obtenerClase(String codigo) { return listaClases.stream().filter(c -> c.getCodigo().equals(codigo)).findFirst().orElse(null); }
    
    public boolean crearEntrenador(String id, String nombre, String tel, String correo, double sueldo, boolean disponible) {
        if (obtenerEntrenador(id) != null) return false;
        Entrenador e = new Entrenador(id, nombre, tel, correo, sueldo);
        e.setDisponible(disponible);
        return listaEntrenadores.add(e);
    }
    public boolean actualizarEntrenador(String id, Entrenador data) {
        Entrenador e = obtenerEntrenador(id);
        if (e != null) {
            e.setNombre(data.getNombre());
            e.setTelefono(data.getTelefono());
            e.setCorreo(data.getCorreo());
            e.setSueldo(data.getSueldo());
            e.setDisponible(data.isDisponible());
            return true;
        }
        return false;
    }
    public boolean eliminarEntrenador(String id) { return listaEntrenadores.removeIf(e -> e.getIdentificacion().equals(id)); }
    public Entrenador obtenerEntrenador(String id) { return listaEntrenadores.stream().filter(e -> e.getIdentificacion().equals(id)).findFirst().orElse(null); }

    public boolean crearMembresia(String codigo, String idUsuario, TipoMembresia tipo, TipoMembresiaDuracion duracion, double costo, String fechaInicio, String fechaFin, EstadoMembresia estado) {
        boolean tieneActiva = listaMembresias.stream().anyMatch(m -> m.getIdentificacionUsuario().equals(idUsuario) && m.estaActiva());
        if (tieneActiva) return false;
        if (obtenerMembresia(codigo) != null) return false;
        return listaMembresias.add(new Membresia(codigo, idUsuario, tipo, duracion, costo, fechaInicio, fechaFin, estado));
    }
    public boolean actualizarMembresia(String codigo, Membresia data) {
        Membresia m = obtenerMembresia(codigo);
        if (m != null) {
            m.setTipo(data.getTipo());
            m.setDuracion(data.getDuracion());
            m.setCosto(data.getCosto());
            m.setFechaInicio(data.getFechaInicio());
            m.setFechaVencimiento(data.getFechaVencimiento());
            m.setEstado(data.getEstado());
            return true;
        }
        return false;
    }
    public boolean eliminarMembresia(String codigo) { return listaMembresias.removeIf(m -> m.getCodigo().equals(codigo)); }
    public Membresia obtenerMembresia(String codigo) { return listaMembresias.stream().filter(m -> m.getCodigo().equals(codigo)).findFirst().orElse(null); }
    
    public boolean crearReserva(String codigo, Usuario usuario, Clase clase, LocalDate fecha, Entrenador entrenador) {
        if (obtenerReserva(codigo) != null) return false;
        return listaReservas.add(new Reserva(codigo, usuario, clase, fecha, entrenador));
    }
    public boolean cancelarReserva(String codigo) {
        Reserva r = obtenerReserva(codigo);
        if (r != null && "ACTIVA".equals(r.getEstado())) {
            r.setEstado("CANCELADA");
            return true;
        }
        return false;
    }
    public boolean registrarAsistencia(String codigoReserva) {
        Reserva reserva = obtenerReserva(codigoReserva);
        if (reserva == null || !"ACTIVA".equals(reserva.getEstado())) return false;

        // SOLUCIÓN: Añadir la validación de que el usuario esté dentro del gimnasio.
        if (!estaDentro(reserva.getUsuario().getIdentificacion())) {
            return false;
        }

        LocalTime ahora = LocalTime.now();
        LocalTime horaInicioClase = reserva.getClase().getHorario();
        LocalTime horaFinClase = reserva.getClase().getHoraFin();
        LocalTime inicioCheckin = horaInicioClase.minusMinutes(GRACE_PERIOD_MINUTES);
        boolean enVentana = !ahora.isBefore(inicioCheckin) && ahora.isBefore(horaFinClase);

        if (enVentana) {
            reserva.setEstado("COMPLETADA");
            return true;
        }
        return false;
    }
    public Reserva obtenerReserva(String codigo) { return listaReservas.stream().filter(r -> r.getCodigo().equals(codigo)).findFirst().orElse(null); }
    
    public boolean registrarIngreso(Usuario usuario) {
        if (usuario != null && !usuariosDentroDelGimnasio.contains(usuario)) {
            return usuariosDentroDelGimnasio.add(usuario);
        }
        return false;
    }
    public boolean registrarSalida(Usuario usuario) {
        if (usuario != null) {
            return usuariosDentroDelGimnasio.remove(usuario);
        }
        return false;
    }
    public boolean estaDentro(String id) {
        return usuariosDentroDelGimnasio.stream().anyMatch(u -> u.getIdentificacion().equals(id));
    }
    
    public boolean esEntrenadorDisponibleParaClase(Entrenador entrenador, DayOfWeek dia, LocalTime horaInicio, LocalTime horaFin, String codigoClaseAExcluir) {
        if (entrenador == null || dia == null || horaInicio == null || horaFin == null) return false;
        if (!entrenador.isDisponible()) return false;
        for (Clase claseExistente : listaClases) {
            if (codigoClaseAExcluir != null && claseExistente.getCodigo().equals(codigoClaseAExcluir)) continue;
            if (claseExistente.getEntrenadorPorDefecto() != null && claseExistente.getEntrenadorPorDefecto().equals(entrenador) && claseExistente.getDia().equals(dia)) {
                boolean solapa = (horaInicio.isBefore(claseExistente.getHoraFin()) && claseExistente.getHorario().isBefore(horaFin));
                if (solapa) return false;
            }
        }
        return true;
    }
    //</editor-fold>
}
