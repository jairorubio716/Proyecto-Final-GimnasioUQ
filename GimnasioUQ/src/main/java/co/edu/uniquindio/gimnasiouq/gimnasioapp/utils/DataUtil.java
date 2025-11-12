package co.edu.uniquindio.gimnasiouq.gimnasioapp.utils;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;

import java.time.LocalDate;

public class DataUtil {

    public static GimnasioUQ inicializarDatos() {
        GimnasioUQ gimnasio = new GimnasioUQ("Gimnasio UQ Fit");

        // ============================================================
        //                   CREAR USUARIOS
        // ============================================================

        Estudiante estudiante1 = new Estudiante(
                "Juan Pérez", "2001", "20", "3001234567", "5", "Ingeniería"
        );

        Trabajador trabajador1 = new Trabajador(
                "María García", "2002", "35", "3007654321", "Profesor"
        );

        Externo externo1 = new Externo(
                "Carlos López", "2003", "28", "3001112233", "Empresa ABC"
        );

        // ============================================================
        //                   CREAR MEMBRESÍAS
        // ============================================================

        String fechaHoy = LocalDate.now().toString();
        String fechaMensual = LocalDate.now().plusMonths(1).toString();
        String fechaAnual = LocalDate.now().plusYears(1).toString();

        Membresia mem1 = new Membresia(
                "MEM001", "2001", TipoMembresia.PREMIUM,
                TipoMembresiaDuracion.MENSUAL, 80000, fechaHoy,
                fechaMensual, EstadoMembresia.INACTIVA
        );

        Membresia mem2 = new Membresia(
                "MEM002", "2002", TipoMembresia.VIP,
                TipoMembresiaDuracion.ANUAL, 1200000, fechaHoy,
                fechaAnual, EstadoMembresia.ACTIVA
        );

        Membresia mem3 = new Membresia(
                "MEM003", "2003", TipoMembresia.BASICA,
                TipoMembresiaDuracion.TRIMESTRAL, 135000, fechaHoy,
                LocalDate.now().plusMonths(3).toString(), EstadoMembresia.ACTIVA
        );



        // Usuarios
        gimnasio.getListaUsuarios().add(estudiante1);
        gimnasio.getListaUsuarios().add(trabajador1);
        gimnasio.getListaUsuarios().add(externo1);

        // Membresías
        gimnasio.getListaMembresias().add(mem1);
        gimnasio.getListaMembresias().add(mem2);
        gimnasio.getListaMembresias().add(mem3);

        return gimnasio;
    }
}