package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GimnasioUQ {

    private String nombre;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private ArrayList<Membresia> listaMembresias = new ArrayList<>();
    private ArrayList<Clase> listaClases = new ArrayList<>();
    private ArrayList<Entrenador> listaEntrenadores = new ArrayList<>();
    private ArrayList<Reserva> listaReservas = new ArrayList<>();
    private ArrayList<Recepcionista> listaRecepcionistas = new ArrayList<>();
    private ArrayList<Administrador> listaAdministradores = new ArrayList<>();

    public GimnasioUQ() {}

    public GimnasioUQ(String nombre) {
        this.nombre = nombre;
    }

    // Métodos CRUD para las demás entidades (Usuario, Membresia, Entrenador) se mantienen igual...
    // ...

    // ============================================================
    //                   CRUD CLASES
    // ============================================================

    public boolean crearClase(Clase clase) {
        if (obtenerClase(clase.getCodigo()) != null) {
            return false;
        }
        return listaClases.add(clase);
    }

    public boolean eliminarClase(String codigo) {
        // Antes de eliminar una clase, se deberían cancelar todas las reservas futuras para ella.
        listaReservas.removeIf(reserva -> reserva.getClase().getCodigo().equals(codigo));
        return listaClases.removeIf(clase -> clase.getCodigo().equals(codigo));
    }

    public Clase obtenerClase(String codigo) {
        return listaClases.stream()
                .filter(clase -> clase.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    // ============================================================
    //              CRUD RESERVAS (REFACTORIZADO)
    // ============================================================

    public boolean crearReserva(Reserva reserva) {
        if (obtenerReserva(reserva.getCodigo()) != null) {
            return false;
        }
        return listaReservas.add(reserva);
    }

    public boolean cancelarReserva(String codigoReserva) {
        Reserva reserva = obtenerReserva(codigoReserva);
        if (reserva != null && "ACTIVA".equals(reserva.getEstado())) {
            reserva.setEstado("CANCELADA");
            return true;
        }
        return false;
    }

    public boolean registrarAsistencia(String codigoReserva) {
        Reserva reserva = obtenerReserva(codigoReserva);
        if (reserva != null && "ACTIVA".equals(reserva.getEstado())) {
            reserva.setEstado("COMPLETADA");
            return true;
        }
        return false;
    }

    public Reserva obtenerReserva(String codigoReserva) {
        return listaReservas.stream()
                .filter(reserva -> reserva.getCodigo().equals(codigoReserva))
                .findFirst()
                .orElse(null);
    }
    
    // Getters y Setters (sin cambios)
    // ...
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public ArrayList<Membresia> getListaMembresias() {
        return listaMembresias;
    }

    public void setListaMembresias(ArrayList<Membresia> listaMembresias) {
        this.listaMembresias = listaMembresias;
    }

    public ArrayList<Clase> getListaClases() {
        return listaClases;
    }

    public void setListaClases(ArrayList<Clase> listaClases) {
        this.listaClases = listaClases;
    }

    public ArrayList<Entrenador> getListaEntrenadores() {
        return listaEntrenadores;
    }

    public void setListaEntrenadores(ArrayList<Entrenador> listaEntrenadores) {
        this.listaEntrenadores = listaEntrenadores;
    }

    public ArrayList<Reserva> getListaReservas() {
        return listaReservas;
    }

    public void setListaReservas(ArrayList<Reserva> listaReservas) {
        this.listaReservas = listaReservas;
    }

    public ArrayList<Recepcionista> getListaRecepcionistas() {
        return listaRecepcionistas;
    }

    public void setListaRecepcionistas(ArrayList<Recepcionista> listaRecepcionistas) {
        this.listaRecepcionistas = listaRecepcionistas;
    }

    public ArrayList<Administrador> getListaAdministradores() {
        return listaAdministradores;
    }

    public void setListaAdministradores(ArrayList<Administrador> listaAdministradores) {
        this.listaAdministradores = listaAdministradores;
    }
    
        public boolean crearUsuario(Usuario usuario) {
        // Validar que no exista usuario con misma identificación
        if (obtenerUsuario(usuario.getIdentificacion()) != null) {
            return false;
        }
        return listaUsuarios.add(usuario);
    }

    public boolean actualizarUsuario(Usuario usuarioActualizado) {
        Usuario existente = obtenerUsuario(usuarioActualizado.getIdentificacion());
        if (existente != null) {
            // Eliminar el viejo y agregar el nuevo (para cambio de tipo)
            listaUsuarios.remove(existente);
            return listaUsuarios.add(usuarioActualizado);
        }
        return false;
    }

    public boolean eliminarUsuario(String identificacion) {
        Usuario u = obtenerUsuario(identificacion);
        if (u != null) {
            return listaUsuarios.remove(u);
        }
        return false;
    }

    public Usuario obtenerUsuario(String identificacion) {
        return listaUsuarios.stream()
                .filter(u -> u.getIdentificacion().equals(identificacion))
                .findFirst()
                .orElse(null);
    }



    //                   CRUD MEMBRESIAS

    public boolean crearMembresia(Membresia membresia) {
        if (obtenerMembresia(membresia.getCodigo()) != null) {
            return false;
        }
        return listaMembresias.add(membresia);
    }

    public boolean actualizarMembresia(Membresia membresiaActualizada) {
        Membresia existente = obtenerMembresia(membresiaActualizada.getCodigo());
        if (existente != null) {
            listaMembresias.remove(existente);
            return listaMembresias.add(membresiaActualizada);
        }
        return false;
    }

    public boolean eliminarMembresia(String codigo) {
        Membresia m = obtenerMembresia(codigo);
        if (m != null) {
            return listaMembresias.remove(m);
        }
        return false;
    }

    public Membresia obtenerMembresia(String codigo) {
        return listaMembresias.stream()
                .filter(m -> m.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    // Métodos de entrenadores
    public boolean crearEntrenador(Entrenador entrenador) {
        if (obtenerEntrenador(entrenador.getIdentificacion()) != null) {
            return false;
        }
        return listaEntrenadores.add(entrenador);
    }

    public boolean actualizarEntrenador(Entrenador entrenadorActualizado) {
        Entrenador existente = obtenerEntrenador(entrenadorActualizado.getIdentificacion());
        if (existente != null) {
            listaEntrenadores.remove(existente);
            return listaEntrenadores.add(entrenadorActualizado);
        }
        return false;
    }

    public boolean eliminarEntrenador(String identificacion) {
        Entrenador e = obtenerEntrenador(identificacion);
        if (e != null) {
            return listaEntrenadores.remove(e);
        }
        return false;
    }

    public Entrenador obtenerEntrenador(String identificacion) {
        return listaEntrenadores.stream()
                .filter(e -> e.getIdentificacion().equals(identificacion))
                .findFirst()
                .orElse(null);
    }
}
