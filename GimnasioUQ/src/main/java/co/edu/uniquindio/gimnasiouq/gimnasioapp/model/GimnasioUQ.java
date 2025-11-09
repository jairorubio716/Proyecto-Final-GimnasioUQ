package co.edu.uniquindio.gimnasiouq.gimnasioapp.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class GimnasioUQ {

    private String nombre;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private ArrayList<Membresia> listaMembresias = new ArrayList<>();
    private ArrayList<Clase> listaClases = new ArrayList<>();
    private ArrayList<Entrenador> listaEntrenadores = new ArrayList<>();
    private ArrayList<Reserva> listaReservas = new ArrayList<>();
    private ArrayList<Recepcionista> listaRecepcionistas = new ArrayList<>();
    private ArrayList<Administrador> listaAdministradores = new ArrayList<>();

    public GimnasioUQ() {
    }

    public GimnasioUQ(String nombre) {
        this.nombre = nombre;
    }


    // ============================================================
    //                   CRUD USUARIOS (Actualizado)
    // ============================================================

    public boolean crearUsuario(Usuario usuario) {
        // Validar que no exista usuario con misma identificación
        if (buscarUsuario(usuario.getIdentificacion()) != null) {
            return false;
        }
        return listaUsuarios.add(usuario);
    }

    public boolean actualizarUsuario(Usuario usuarioActualizado) {
        Usuario existente = buscarUsuario(usuarioActualizado.getIdentificacion());
        if (existente != null) {
            // Eliminar el viejo y agregar el nuevo (para cambio de tipo)
            listaUsuarios.remove(existente);
            return listaUsuarios.add(usuarioActualizado);
        }
        return false;
    }

    public boolean eliminarUsuario(String identificacion) {
        Usuario u = buscarUsuario(identificacion);
        if (u != null) {
            return listaUsuarios.remove(u);
        }
        return false;
    }

    public Usuario buscarUsuario(String identificacion) {
        for (Usuario u : listaUsuarios) {
            if (u.getIdentificacion().equals(identificacion)) {
                return u;
            }
        }
        return null;
    }

    public Usuario obtenerUsuario(String identificacion) {
        return buscarUsuario(identificacion);
    }



    //                   CRUD MEMBRESIAS

    public boolean crearMembresia(Membresia membresia) {
        if (buscarMembresia(membresia.getCodigo()) != null) {
            return false;
        }
        return listaMembresias.add(membresia);
    }

    public boolean actualizarMembresia(Membresia membresiaActualizada) {
        Membresia existente = buscarMembresia(membresiaActualizada.getCodigo());
        if (existente != null) {
            listaMembresias.remove(existente);
            return listaMembresias.add(membresiaActualizada);
        }
        return false;
    }

    public boolean eliminarMembresia(String codigo) {
        Membresia m = buscarMembresia(codigo);
        if (m != null) {
            return listaMembresias.remove(m);
        }
        return false;
    }

    public Membresia buscarMembresia(String codigo) {
        for (Membresia m : listaMembresias) {
            if (m.getCodigo().equals(codigo)) {
                return m;
            }
        }
        return null;
    }

    public Membresia obtenerMembresia(String codigo) {
        return buscarMembresia(codigo);
    }






    // Métodos de entrenadores
    public boolean crearEntrenador(Entrenador entrenador) {
        if (buscarEntrenador(entrenador.getIdentificacion()) != null) {
            return false;
        }
        return listaEntrenadores.add(entrenador);
    }

    public boolean actualizarEntrenador(Entrenador entrenadorActualizado) {
        Entrenador existente = buscarEntrenador(entrenadorActualizado.getIdentificacion());
        if (existente != null) {
            listaEntrenadores.remove(existente);
            return listaEntrenadores.add(entrenadorActualizado);
        }
        return false;
    }

    public boolean eliminarEntrenador(String identificacion) {
        Entrenador e = buscarEntrenador(identificacion);
        if (e != null) {
            return listaEntrenadores.remove(e);
        }
        return false;
    }

    public Entrenador buscarEntrenador(String identificacion) {
        for (Entrenador e : listaEntrenadores) {
            if (e.getIdentificacion().equals(identificacion)) {
                return e;
            }
        }
        return null;
    }

    public Entrenador obtenerEntrenador(String identificacion) {
        return buscarEntrenador(identificacion);
    }












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
}