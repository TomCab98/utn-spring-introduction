package com.utn.tareas;

import com.utn.tareas.model.Prioridad;
import com.utn.tareas.model.Tarea;
import com.utn.tareas.service.MensajeService;
import com.utn.tareas.service.TareaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
class Application implements CommandLineRunner {

    private final TareaService tareaService;
    private final MensajeService mensajeService;

    /**
     * Inyecta TareaService y MensajeService por constructor
     * (Spring encuentra el MensajeService correcto según el profile activo)
     */
    public Application(TareaService tareaService, MensajeService mensajeService) {
        this.tareaService = tareaService;
        this.mensajeService = mensajeService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Método run(...) que implementa el flujo de prueba.
     */
    @Override
    public void run(String... args) throws Exception {

        // 1. Mostrar mensaje de bienvenida (usando MensajeService)
        mensajeService.mostrarBienvenida();
        System.out.println("----------------------------------------------");

        // 2. Mostrar la configuración actual
        System.out.println(tareaService.imprimirConfiguracion());

        // 3. Listar todas las tareas iniciales
        System.out.println("3. TAREAS INICIALES:");
        tareaService.listarTodas().forEach(System.out::println);
        System.out.println();

        // 4. Agregar una nueva tarea
        System.out.println("4. AGREGANDO NUEVA TAREA...");
        try {
            tareaService.agregarTarea("Completar TP Programación III", Prioridad.ALTA);
            System.out.println("Tarea agregada exitosamente.");
        } catch (IllegalStateException e) {
            System.err.println("ERROR al agregar tarea: " + e.getMessage());
        }
        System.out.println();

        // 5. Listar tareas pendientes
        System.out.println("5. TAREAS PENDIENTES:");
        tareaService.listarPendientes().forEach(System.out::println);
        System.out.println();

        // 6. Marcar una tarea como completada (usaremos el ID 1, si existe)
        Long idACambiar = 1L;
        System.out.println("6. MARCANDO TAREA CON ID " + idACambiar + " COMO COMPLETADA...");
        if (tareaService.marcarComoCompletada(idACambiar).isPresent()) {
            System.out.println("Tarea " + idACambiar + " marcada como completada.");
        } else {
            System.out.println("No se encontró la tarea con ID " + idACambiar + ".");
        }
        System.out.println();

        // 7. Mostrar estadísticas
        System.out.println("7. MOSTRANDO ESTADÍSTICAS:");
        System.out.println(tareaService.obtenerEstadisticas());
        System.out.println();

        // 8. Listar tareas completadas
        System.out.println("8. TAREAS COMPLETADAS:");
        tareaService.listarCompletadas().forEach(System.out::println);
        System.out.println();

        // 9. Mostrar mensaje de despedida
        System.out.println("----------------------------------------------");
        mensajeService.mostrarDespedida();
    }
}
