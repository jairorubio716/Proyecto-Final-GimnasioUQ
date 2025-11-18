package co.edu.uniquindio.gimnasiouq.gimnasioapp.utils;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    // Listas quemadas para Administradores y Recepcionistas
    private static final List<Administrador> administradoresQuemados = new ArrayList<>();
    private static final List<Recepcionista> recepcionistasQuemados = new ArrayList<>();

    static {
        // Inicializar administradores y recepcionistas quemados
        administradoresQuemados.add(new Administrador("Admin General", "ADM001", "admin", "admin"));
        recepcionistasQuemados.add(new Recepcionista("Recepcionista Principal", "REC001", "recep", "1234"));
    }

    public static List<Administrador> getAdministradoresQuemados() {
        return administradoresQuemados;
    }

    public static List<Recepcionista> getRecepcionistasQuemados() {
        return recepcionistasQuemados;
    }

    public static GimnasioUQ inicializarDatos() {
        GimnasioUQ gimnasio = new GimnasioUQ("Gimnasio UQ Fit");

        Entrenador e1 = new Entrenador("3001", "Ana", "3101234567", "ana@email.com", 2000000);
        Entrenador e2 = new Entrenador("3002", "Pedro", "3207654321", "pedro@email.com", 2200000);
        Entrenador e3 = new Entrenador("3003", "Luis", "3151112233", "luis@email.com", 2100000);
        e3.setDisponible(false);
        Entrenador e4 = new Entrenador("3004", "Sofia", "3118889900", "sofia@email.com", 2300000);
        gimnasio.getListaEntrenadores().add(e1);
        gimnasio.getListaEntrenadores().add(e2);
        gimnasio.getListaEntrenadores().add(e3);
        gimnasio.getListaEntrenadores().add(e4);

        Clase c1 = new Clase("C01", "Yoga Matutino", DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 0), 15, e1);
        Clase c2 = new Clase("C02", "Spinning Intenso", DayOfWeek.TUESDAY, LocalTime.of(18, 0), LocalTime.of(19, 0), 20, e2);
        Clase c3 = new Clase("C03", "Yoga Vespertino", DayOfWeek.WEDNESDAY, LocalTime.of(19, 0), LocalTime.of(20, 0), 15, e1);
        Clase c4 = new Clase("C04", "Full Body", DayOfWeek.FRIDAY, LocalTime.of(7, 0), LocalTime.of(8, 0), 25, e4);
        Clase c5 = new Clase("C05", "Cardio Box", DayOfWeek.THURSDAY, LocalTime.of(17, 0), LocalTime.of(18, 0), 18, e2);
        Clase c6 = new Clase("C06", "Pilates", DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(11, 0), 12, e1);
        
        DayOfWeek hoyDiaSemana = LocalDate.now().getDayOfWeek();
        LocalTime ahora = LocalTime.now();
        Clase clasePruebaHoy = new Clase("C99", "Clase de Prueba INMEDIATA", hoyDiaSemana, ahora.minusMinutes(10), ahora.plusHours(1), 5, e2);

        gimnasio.getListaClases().add(c1);
        gimnasio.getListaClases().add(c2);
        gimnasio.getListaClases().add(c3);
        gimnasio.getListaClases().add(c4);
        gimnasio.getListaClases().add(c5);
        gimnasio.getListaClases().add(c6);
        gimnasio.getListaClases().add(clasePruebaHoy);

        Usuario u1 = new Estudiante("Juan Pérez ", "2001", "20", "3001234567", "5", "Ingeniería");
        Usuario u2 = new Trabajador("María García", "2002", "35", "3007654321", "Profesor");
        Usuario u3 = new Externo("Carlos López", "2003", "28", "3001112233", "Empresa ABC");
        Usuario u4 = new Estudiante("Laura G", "2004", "22", "3145556677", "8", "Medicina");
        Usuario u5 = new Trabajador("Andrés M", "2005", "45", "3214443322", "Administrativo");
        Usuario u6 = new Externo("Sofía R", "2006", "31", "3189990011", "Independiente");
        gimnasio.getListaUsuarios().add(u1);
        gimnasio.getListaUsuarios().add(u2);
        gimnasio.getListaUsuarios().add(u3);
        gimnasio.getListaUsuarios().add(u4);
        gimnasio.getListaUsuarios().add(u5);
        gimnasio.getListaUsuarios().add(u6);

        String fechaHoyStr = LocalDate.now().toString();
        gimnasio.crearMembresia("MEM001", "2001", TipoMembresia.PREMIUM, TipoMembresiaDuracion.MENSUAL, 80000, fechaHoyStr, LocalDate.now().plusMonths(1).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM002", "2002", TipoMembresia.BASICA, TipoMembresiaDuracion.ANUAL, 500000, fechaHoyStr, LocalDate.now().plusYears(1).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM003", "2003", TipoMembresia.VIP, TipoMembresiaDuracion.TRIMESTRAL, 450000, fechaHoyStr, LocalDate.now().plusMonths(3).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM004", "2004", TipoMembresia.PREMIUM, TipoMembresiaDuracion.MENSUAL, 80000, fechaHoyStr, LocalDate.now().plusMonths(1).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM005", "2005", TipoMembresia.VIP, TipoMembresiaDuracion.MENSUAL, 150000, LocalDate.now().minusDays(25).toString(), LocalDate.now().plusDays(5).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM006", "2006", TipoMembresia.BASICA, TipoMembresiaDuracion.MENSUAL, 50000, LocalDate.now().minusMonths(2).toString(), LocalDate.now().minusMonths(1).toString(), EstadoMembresia.INACTIVA);

        Reserva reservaPruebaHoy = new Reserva("RES-999", u3, clasePruebaHoy, LocalDate.now(), e2);
        gimnasio.getListaReservas().add(reservaPruebaHoy);

        return gimnasio;
    }
}
