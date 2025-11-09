package co.edu.uniquindio.gimnasiouq.gimnasioapp.utils;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;
import java.time.LocalDate;

public class DataUtil {

    public static GimnasioUQ inicializarDatos() {
        GimnasioUQ gimnasio = new GimnasioUQ("Gimnasio UQ Fit");

        // Crear entrenadores
        Entrenador entrenador1 = new Entrenador("500000", "Yoga y Pilates", "100");
        Entrenador entrenador2 = new Entrenador("600000", "Spinning y Cardio", "120");

        // Crear clases
        Clase yoga = new Clase("Yoga Matutino", TipoClase.YOGA, "08:00", "09:00", "20", entrenador1);
        Clase spinning = new Clase("Spinning Intenso", TipoClase.SPINNING, "18:00", "19:00", "15", entrenador2);

        // ✅ CREAR USUARIOS (SOLO DATOS PERSONALES)
        Estudiante estudiante1 = new Estudiante("Juan Pérez", "1001", "20", "3001234567", "5", "Ingeniería");
        Trabajador trabajador1 = new Trabajador("María García", "1002", "35", "3007654321", "Profesor");
        Externo externo1 = new Externo("Carlos López", "1003", "28", "3001112233", "Empresa ABC");

        // ✅ CREAR MEMBRESÍAS (SEPARADAS)
        String fechaHoy = LocalDate.now().toString();
        String fechaMensual = LocalDate.now().plusMonths(1).toString();
        String fechaAnual = LocalDate.now().plusYears(1).toString();

        Membresia mem1 = new Membresia("MEM001", "1001", TipoMembresia.PREMIUM,
                TipoMembresiaDuracion.MENSUAL, 80000, fechaHoy,
                fechaMensual, EstadoMembresia.ACTIVA);

        Membresia mem2 = new Membresia("MEM002", "1002", TipoMembresia.VIP,
                TipoMembresiaDuracion.ANUAL, 1200000, fechaHoy,
                fechaAnual, EstadoMembresia.ACTIVA);

        Membresia mem3 = new Membresia("MEM003", "1003", TipoMembresia.BASICA,
                TipoMembresiaDuracion.TRIMESTRAL, 135000, fechaHoy,
                LocalDate.now().plusMonths(3).toString(), EstadoMembresia.ACTIVA);

        // ✅ AGREGAR AL GIMNASIO
        gimnasio.getListaUsuarios().add(estudiante1);
        gimnasio.getListaUsuarios().add(trabajador1);
        gimnasio.getListaUsuarios().add(externo1);

        gimnasio.getListaMembresias().add(mem1);
        gimnasio.getListaMembresias().add(mem2);
        gimnasio.getListaMembresias().add(mem3);

        gimnasio.getListaClases().add(yoga);
        gimnasio.getListaClases().add(spinning);

        gimnasio.getListaEntrenadores().add(entrenador1);
        gimnasio.getListaEntrenadores().add(entrenador2);

        return gimnasio;
    }
}