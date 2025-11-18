package co.edu.uniquindio.gimnasiouq.gimnasioapp.utils;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DataUtil {


    private static final List<Administrador> administradoresQuemados = new ArrayList<>();
    private static final List<Recepcionista> recepcionistasQuemados = new ArrayList<>();

    static {
        
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
        Entrenador e2 = new Entrenador( "3002", "Pedro", "3207654321", "pedro@email.com", 2200000);
        Entrenador e3 = new Entrenador("3003", "Luis", "3151112233", "luis@email.com", 2100000);
        e3.setDisponible(false);
        Entrenador e4 = new Entrenador("3004", "Sofia", "3118889900", "sofia@email.com", 2300000);
        Entrenador e5 = new Entrenador("3005", "Miguel", "3005551122", "miguel@email.com", 1900000);
        Entrenador e6 = new Entrenador("3006", "Elena", "3124443322", "elena@email.com", 2500000);
        gimnasio.getListaEntrenadores().add(e1);
        gimnasio.getListaEntrenadores().add(e2);
        gimnasio.getListaEntrenadores().add(e3);
        gimnasio.getListaEntrenadores().add(e4);
        gimnasio.getListaEntrenadores().add(e5);
        gimnasio.getListaEntrenadores().add(e6);

        Clase c1 = new Clase("C01", "Yoga Matutino", DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 0), 15, e1);
        Clase c2 = new Clase("C02", "Spinning Intenso", DayOfWeek.TUESDAY, LocalTime.of(18, 0), LocalTime.of(19, 0), 20, e2);
        Clase c3 = new Clase("C03", "Yoga Vespertino", DayOfWeek.WEDNESDAY, LocalTime.of(19, 0), LocalTime.of(20, 0), 15, e1);
        Clase c4 = new Clase("C04", "Full Body", DayOfWeek.FRIDAY, LocalTime.of(7, 0), LocalTime.of(8, 0), 25, e4);
        Clase c5 = new Clase("C05", "Cardio Box", DayOfWeek.THURSDAY, LocalTime.of(17, 0), LocalTime.of(18, 0), 18, e2);
        Clase c6 = new Clase("C06", "Pilates", DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(11, 0), 12, e1);
        Clase c7 = new Clase("C07", "Zumba Fiesta", DayOfWeek.MONDAY, LocalTime.of(17, 30), LocalTime.of(18, 30), 30, e5);
        Clase c8 = new Clase("C08", "CrossFit Express", DayOfWeek.WEDNESDAY, LocalTime.of(7, 30), LocalTime.of(8, 30), 10, e6);
        Clase c9 = new Clase("C09", "Meditación Guiada", DayOfWeek.SUNDAY, LocalTime.of(9, 0), LocalTime.of(10, 0), 8, e1);
        Clase c10 = new Clase("C10", "Entrenamiento Funcional", DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0), 20, e4);
        
        DayOfWeek hoyDiaSemana = LocalDate.now().getDayOfWeek();

        LocalTime horaInicioPrueba = LocalTime.now().plusMinutes(5);
        LocalTime horaFinPrueba = horaInicioPrueba.plusHours(1);

        Clase clasePruebaHoy = new Clase("C99", "Clase de Prueba INMEDIATA", hoyDiaSemana, horaInicioPrueba, horaFinPrueba, 5, e2);

        gimnasio.getListaClases().add(c1);
        gimnasio.getListaClases().add(c2);
        gimnasio.getListaClases().add(c3);
        gimnasio.getListaClases().add(c4);
        gimnasio.getListaClases().add(c5);
        gimnasio.getListaClases().add(c6);
        gimnasio.getListaClases().add(c7);
        gimnasio.getListaClases().add(c8);
        gimnasio.getListaClases().add(c9);
        gimnasio.getListaClases().add(c10);
        gimnasio.getListaClases().add(clasePruebaHoy);

        Usuario u1 = new Estudiante("Juan Pérez ", "2001", "20", "3001234567", "5", "Ingeniería");
        Usuario u2 = new Trabajador("María García", "2002", "35", "3007654321", "Profesor");
        Usuario u3 = new Externo("Carlos López", "2003", "28", "3001112233", "Empresa ABC");
        Usuario u4 = new Estudiante("Laura G", "2004", "22", "3145556677", "8", "Medicina");
        Usuario u5 = new Trabajador("Andrés M", "2005", "45", "3214443322", "Administrativo");
        Usuario u6 = new Externo("Sofía R", "2006", "31", "3189990011", "Independiente");
        Usuario u7 = new Estudiante("Diego V", "2007", "19", "3012223344", "3", "Diseño Gráfico");
        Usuario u8 = new Trabajador("Ana P", "2008", "40", "3109998877", "Contadora");
        Usuario u9 = new Externo("Javier S", "2009", "25", "3047776655", "Freelancer");
        Usuario u10 = new Estudiante("Valeria C", "2010", "21", "3136665544", "7", "Derecho");
        gimnasio.getListaUsuarios().add(u1);
        gimnasio.getListaUsuarios().add(u2);
        gimnasio.getListaUsuarios().add(u3);
        gimnasio.getListaUsuarios().add(u4);
        gimnasio.getListaUsuarios().add(u5);
        gimnasio.getListaUsuarios().add(u6);
        gimnasio.getListaUsuarios().add(u7);
        gimnasio.getListaUsuarios().add(u8);
        gimnasio.getListaUsuarios().add(u9);
        gimnasio.getListaUsuarios().add(u10);

        String fechaHoyStr = LocalDate.now().toString();

        gimnasio.crearMembresia("MEM001", "2001", TipoMembresia.PREMIUM, TipoMembresiaDuracion.MENSUAL, 80000, fechaHoyStr, LocalDate.now().plusMonths(1).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM002", "2002", TipoMembresia.BASICA, TipoMembresiaDuracion.ANUAL, 500000, fechaHoyStr, LocalDate.now().plusYears(1).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM003", "2003", TipoMembresia.VIP, TipoMembresiaDuracion.TRIMESTRAL, 450000, fechaHoyStr, LocalDate.now().plusMonths(3).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM004", "2004", TipoMembresia.PREMIUM, TipoMembresiaDuracion.MENSUAL, 80000, fechaHoyStr, LocalDate.now().plusMonths(1).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM005", "2005", TipoMembresia.VIP, TipoMembresiaDuracion.MENSUAL, 150000, LocalDate.now().minusDays(25).toString(), LocalDate.now().plusDays(5).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM006", "2006", TipoMembresia.BASICA, TipoMembresiaDuracion.MENSUAL, 50000, LocalDate.now().minusMonths(2).toString(), LocalDate.now().minusMonths(1).toString(), EstadoMembresia.INACTIVA);
        gimnasio.crearMembresia("MEM007", "2007", TipoMembresia.PREMIUM, TipoMembresiaDuracion.TRIMESTRAL, 240000, fechaHoyStr, LocalDate.now().plusMonths(3).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM008", "2008", TipoMembresia.BASICA, TipoMembresiaDuracion.MENSUAL, 50000, LocalDate.now().minusDays(10).toString(), LocalDate.now().plusDays(20).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM009", "2009", TipoMembresia.VIP, TipoMembresiaDuracion.ANUAL, 1800000, fechaHoyStr, LocalDate.now().plusYears(1).toString(), EstadoMembresia.ACTIVA);
        gimnasio.crearMembresia("MEM010", "2010", TipoMembresia.PREMIUM, TipoMembresiaDuracion.MENSUAL, 80000, LocalDate.now().minusMonths(1).toString(), LocalDate.now().minusDays(1).toString(), EstadoMembresia.INACTIVA);

        Reserva reservaPruebaHoy = new Reserva("RES-999", u3, clasePruebaHoy, LocalDate.now(), e2);
        gimnasio.getListaReservas().add(reservaPruebaHoy);
        gimnasio.getListaReservas().add(new Reserva("RES-001", u1, c1, LocalDate.now().plusDays(1), e1));
        gimnasio.getListaReservas().add(new Reserva("RES-002", u2, c2, LocalDate.now().minusDays(5), e2));
        gimnasio.getListaReservas().add(new Reserva("RES-003", u4, c7, LocalDate.now().plusDays(2), e5));
        gimnasio.getListaReservas().add(new Reserva("RES-004", u7, c8, LocalDate.now().plusDays(3), e6));
        gimnasio.getListaReservas().add(new Reserva("RES-005", u8, c10, LocalDate.now().plusDays(1), e4));
        gimnasio.getListaReservas().add(new Reserva("RES-006", u1, c3, LocalDate.now().minusDays(10), e1));
        gimnasio.getListaReservas().add(new Reserva("RES-007", u5, c5, LocalDate.now().minusDays(2), e2));
        gimnasio.getListaReservas().add(new Reserva("RES-008", u9, c9, LocalDate.now().plusDays(4), e1));
        gimnasio.getListaReservas().add(new Reserva("RES-009", u10, c4, LocalDate.now().plusDays(5), e4));
        gimnasio.getListaReservas().add(new Reserva("RES-010", u3, c6, LocalDate.now().minusDays(1), e1));

        gimnasio.getListaReservas().stream()
                .filter(r -> r.getCodigo().equals("RES-002") || r.getCodigo().equals("RES-006") || r.getCodigo().equals("RES-007") || r.getCodigo().equals("RES-010"))
                .forEach(r -> r.setEstado("COMPLETADA"));

        gimnasio.getListaReservas().stream()
                .filter(r -> r.getCodigo().equals("RES-001"))
                .forEach(r -> r.setEstado("CANCELADA"));


        return gimnasio;
    }
}