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