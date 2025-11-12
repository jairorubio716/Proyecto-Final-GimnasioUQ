package co.edu.uniquindio.gimnasiouq.gimnasioapp.utils;

import co.edu.uniquindio.gimnasiouq.gimnasioapp.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataUtil {

    public static GimnasioUQ inicializarDatos() {
        GimnasioUQ gimnasio = new GimnasioUQ("Gimnasio UQ Fit");

        //                   CREAR ENTRENADORES

        Entrenador entrenador1 = new Entrenador("3001", "Ana", "3101234567", "ana@email.com", 2000000);
        entrenador1.setDisponible(true);
        Entrenador entrenador2 = new Entrenador("3002", "Pedro", "3207654321", "pedro@email.com", 2200000);
        entrenador2.setDisponible(true);
        Entrenador entrenador3 = new Entrenador("3003", "Luis", "3151112233", "luis@email.com", 2100000);
        entrenador3.setDisponible(false);
        gimnasio.getListaEntrenadores().add(entrenador1);
        gimnasio.getListaEntrenadores().add(entrenador2);
        gimnasio.getListaEntrenadores().add(entrenador3);

        //                   CREAR CLASES

        Clase clase1 = new Clase("C01", "Yoga Matutino", DayOfWeek.MONDAY, LocalTime.of(8, 0), 15);
        Clase clase2 = new Clase("C02", "Spinning Intenso", DayOfWeek.TUESDAY, LocalTime.of(18, 0), 20);
        Clase clase3 = new Clase("C03", "Yoga Vespertino", DayOfWeek.WEDNESDAY, LocalTime.of(19, 0), 15);
        Clase clase4 = new Clase("C04", "Full Body", DayOfWeek.FRIDAY, LocalTime.of(7, 0), 25);
        
        gimnasio.getListaClases().add(clase1);
        gimnasio.getListaClases().add(clase2);
        gimnasio.getListaClases().add(clase3);
        gimnasio.getListaClases().add(clase4);

        //                   CREAR USUARIOS

        Estudiante estudiante1 = new Estudiante("Juan Pérez", "2001", "20", "3001234567", "5", "Ingeniería");
        Trabajador trabajador1 = new Trabajador("María García", "2002", "35", "3007654321", "Profesor");
        Externo externo1 = new Externo("Carlos López", "2003", "28", "3001112233", "Empresa ABC");
        gimnasio.getListaUsuarios().add(estudiante1);
        gimnasio.getListaUsuarios().add(trabajador1);
        gimnasio.getListaUsuarios().add(externo1);

        //                   CREAR MEMBRESÍAS

        String fechaHoy = LocalDate.now().toString();
        String fechaMensual = LocalDate.now().plusMonths(1).toString();
        String fechaAnual = LocalDate.now().plusYears(1).toString();

        Membresia mem1 = new Membresia("MEM001", "2001", TipoMembresia.PREMIUM, TipoMembresiaDuracion.MENSUAL, 80000, fechaHoy, fechaMensual, EstadoMembresia.ACTIVA);
        Membresia mem2 = new Membresia("MEM002", "2002", TipoMembresia.BASICA, TipoMembresiaDuracion.ANUAL, 500000, fechaHoy, fechaAnual, EstadoMembresia.ACTIVA);
        Membresia mem3 = new Membresia("MEM003", "2003", TipoMembresia.VIP, TipoMembresiaDuracion.TRIMESTRAL, 450000, fechaHoy, LocalDate.now().plusMonths(3).toString(), EstadoMembresia.ACTIVA);
        gimnasio.getListaMembresias().add(mem1);
        gimnasio.getListaMembresias().add(mem2);
        gimnasio.getListaMembresias().add(mem3);

        return gimnasio;
    }
}
