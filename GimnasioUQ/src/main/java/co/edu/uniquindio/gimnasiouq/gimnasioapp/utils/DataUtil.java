package co.edu.uniquindio.gimnasiouq.gimnasioapp.utils;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class DataUtil {

    public static GimnasioUQ inicializarDatos() {
        GimnasioUQ gimnasio = new GimnasioUQ("Gimnasio UQ Fit");

        // ============================================================
        //                   CREAR ENTRENADORES
        // ============================================================
        Entrenador entrenador1 = new Entrenador("3001", "Ana", "3101234567", "ana@email.com", 2000000);
        entrenador1.setDisponible(true);
        Entrenador entrenador2 = new Entrenador("3002", "Pedro", "3207654321", "pedro@email.com", 2200000);
        entrenador2.setDisponible(true);
        Entrenador entrenador3 = new Entrenador("3003", "Luis (No disponible)", "3151112233", "luis@email.com", 2100000);
        entrenador3.setDisponible(false);
        gimnasio.getListaEntrenadores().add(entrenador1);
        gimnasio.getListaEntrenadores().add(entrenador2);
        gimnasio.getListaEntrenadores().add(entrenador3);

        // ============================================================
        //              CREAR CLASES (CON HORA FIN Y ENTRENADOR POR DEFECTO)
        // ============================================================
        Clase clase1 = new Clase("C01", "Yoga Matutino", DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 0), 15, entrenador1);
        Clase clase2 = new Clase("C02", "Spinning Intenso", DayOfWeek.TUESDAY, LocalTime.of(18, 0), LocalTime.of(19, 0), 20, entrenador2);
        Clase clase3 = new Clase("C03", "Yoga Vespertino", DayOfWeek.WEDNESDAY, LocalTime.of(19, 0), LocalTime.of(20, 0), 15, entrenador1);
        Clase clase4 = new Clase("C04", "Full Body", DayOfWeek.FRIDAY, LocalTime.of(7, 0), LocalTime.of(8, 0), 25, entrenador2);
        
        // SOLUCIÓN: Clase de prueba para el día y hora actual
        DayOfWeek hoy = LocalDate.now().getDayOfWeek();
        LocalTime ahora = LocalTime.now();
        Clase clasePruebaHoy = new Clase("C99", "Clase de Prueba Hoy", hoy, ahora.minusMinutes(10), ahora.plusHours(1), 5, entrenador2);

        gimnasio.getListaClases().add(clase1);
        gimnasio.getListaClases().add(clase2);
        gimnasio.getListaClases().add(clase3);
        gimnasio.getListaClases().add(clase4);
        gimnasio.getListaClases().add(clasePruebaHoy);

        // ============================================================
        //                   CREAR USUARIOS
        // ============================================================
        Estudiante estudiante1 = new Estudiante("Juan Pérez (Premium)", "2001", "20", "3001234567", "5", "Ingeniería");
        Trabajador trabajador1 = new Trabajador("María García (Básico)", "2002", "35", "3007654321", "Profesor");
        Externo externo1 = new Externo("Carlos López (VIP)", "2003", "28", "3001112233", "Empresa ABC");
        gimnasio.getListaUsuarios().add(estudiante1);
        gimnasio.getListaUsuarios().add(trabajador1);
        gimnasio.getListaUsuarios().add(externo1);

        // ============================================================
        //                   CREAR MEMBRESÍAS
        // ============================================================
        String fechaHoyStr = LocalDate.now().toString();
        Membresia mem1 = new Membresia("MEM001", "2001", TipoMembresia.PREMIUM, TipoMembresiaDuracion.MENSUAL, 80000, fechaHoyStr, LocalDate.now().plusMonths(1).toString(), EstadoMembresia.ACTIVA);
        Membresia mem2 = new Membresia("MEM002", "2002", TipoMembresia.BASICA, TipoMembresiaDuracion.ANUAL, 500000, fechaHoyStr, LocalDate.now().plusYears(1).toString(), EstadoMembresia.ACTIVA);
        Membresia mem3 = new Membresia("MEM003", "2003", TipoMembresia.VIP, TipoMembresiaDuracion.TRIMESTRAL, 450000, fechaHoyStr, LocalDate.now().plusMonths(3).toString(), EstadoMembresia.ACTIVA);
        gimnasio.getListaMembresias().add(mem1);
        gimnasio.getListaMembresias().add(mem2);
        gimnasio.getListaMembresias().add(mem3);

        // ============================================================
        //                   CREAR RESERVAS DE PRUEBA
        // ============================================================
        // SOLUCIÓN: Reserva para la clase de prueba de hoy
        Reserva reservaPruebaHoy = new Reserva("RES-999", externo1, clasePruebaHoy, LocalDate.now(), entrenador2);
        gimnasio.getListaReservas().add(reservaPruebaHoy);

        return gimnasio;
    }
}
