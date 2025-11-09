package co.edu.uniquindio.gimnasiouq.gimnasioapp.factory;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import co.edu.uniquindio.gimnasiouq.gimnasioapp.utils.DataUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ModelFactory {

    private static ModelFactory instancia;
    private GimnasioUQ gimnasio;

    // ✅ LISTAS OBSERVABLES COMPARTIDAS
    private ObservableList<Usuario> usuariosObservable = FXCollections.observableArrayList();
    private ObservableList<Membresia> membresiasObservable = FXCollections.observableArrayList();


    public static ModelFactory getInstancia() {
        if (instancia == null) {
            instancia = new ModelFactory();
        }
        return instancia;
    }

    private ModelFactory() {
        gimnasio = DataUtil.inicializarDatos();
        actualizarListasObservables();
    }

    // ✅ ACTUALIZAR LISTAS OBSERVABLES
    private void actualizarListasObservables() {
        usuariosObservable.setAll(gimnasio.getListaUsuarios());
        membresiasObservable.setAll(gimnasio.getListaMembresias());

    }


    // ============================================================
    //                     CRUD USUARIOS (EXISTENTE)
    // ============================================================

    public boolean crearUsuario(Usuario usuario) {
        // ✅ VALIDAR que no exista usuario con misma identificación
        if (obtenerUsuario(usuario.getIdentificacion()) != null) {
            return false;
        }

        boolean resultado = gimnasio.crearUsuario(usuario);
        if (resultado) {
            usuariosObservable.setAll(gimnasio.getListaUsuarios());
        }
        return resultado;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        // ✅ VALIDAR que el usuario exista
        if (obtenerUsuario(usuario.getIdentificacion()) == null) {
            return false;
        }

        boolean resultado = gimnasio.actualizarUsuario(usuario);
        if (resultado) {
            usuariosObservable.setAll(gimnasio.getListaUsuarios());
        }
        return resultado;
    }

    public boolean eliminarUsuario(String identificacion) {
        // ✅ VALIDAR que el usuario exista
        if (obtenerUsuario(identificacion) == null) {
            return false;
        }

        boolean resultado = gimnasio.eliminarUsuario(identificacion);
        if (resultado) {
            usuariosObservable.setAll(gimnasio.getListaUsuarios());
        }
        return resultado;
    }

    public Usuario obtenerUsuario(String identificacion) {
        return gimnasio.obtenerUsuario(identificacion);
    }

    public List<Usuario> obtenerUsuarios() {
        return gimnasio.getListaUsuarios();
    }

    // CRUD MEMBRESIAS

    public boolean crearMembresia(Membresia membresia) {
        if (obtenerMembresia(membresia.getCodigo()) != null) {
            return false;
        }

        boolean resultado = gimnasio.crearMembresia(membresia);
        if (resultado) {
            membresiasObservable.setAll(gimnasio.getListaMembresias());
        }
        return resultado;
    }

    public boolean actualizarMembresia(Membresia membresia) {
        if (obtenerMembresia(membresia.getCodigo()) == null) {
            return false;
        }

        boolean resultado = gimnasio.actualizarMembresia(membresia);
        if (resultado) {
            membresiasObservable.setAll(gimnasio.getListaMembresias());
        }
        return resultado;
    }

    public boolean eliminarMembresia(String codigo) {
        if (obtenerMembresia(codigo) == null) {
            return false;
        }

        boolean resultado = gimnasio.eliminarMembresia(codigo);
        if (resultado) {
            membresiasObservable.setAll(gimnasio.getListaMembresias());
        }
        return resultado;
    }

    public Membresia obtenerMembresia(String codigo) {
        return gimnasio.obtenerMembresia(codigo);
    }

    public List<Membresia> obtenerMembresias() {
        return gimnasio.getListaMembresias();
    }

    public ObservableList<Membresia> getMembresiasObservable() {
        return membresiasObservable;
    }

    public boolean usuarioTieneMembresiaActiva(String identificacionUsuario) {
        for (Membresia membresia : gimnasio.getListaMembresias()) {
            if (membresia.getIdentificacionUsuario().equals(identificacionUsuario) &&
                    membresia.estaActiva()) {
                return true;
            }
        }
        return false;
    }


    // ============================================================
    //                   GETTERS LISTAS OBSERVABLES (EXISTENTE)
    // ============================================================

    public ObservableList<Usuario> getUsuariosObservable() {
        return usuariosObservable;
    }


    public boolean existeUsuario(String identificacion) {
        return obtenerUsuario(identificacion) != null; // ✅ CORREGIDO
    }
}