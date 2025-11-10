package com.utn.tareas.service;

import com.utn.tareas.model.Prioridad;
import com.utn.tareas.model.Tarea;
import com.utn.tareas.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Value; // Importar @Value
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    // Inyecta las propiedades usando @Value("${...}")
    @Value("${app.nombre}")
    private String nombreAplicacion;

    @Value("${app.max-tareas}")
    private int maxTareas; // El valor se inyecta como Integer o int

    @Value("${app.mostrar-estadisticas}")
    private boolean mostrarEstadisticas; // El valor se inyecta como Boolean o boolean

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }



    /**
     * Agrega una nueva tarea y valida que no se supere max-tareas.
     */
    public Tarea agregarTarea(String descripcion, Prioridad prioridad) {
        // Valida que no se supere max-tareas
        if (tareaRepository.obtenerTodas().size() >= maxTareas) {
            throw new IllegalStateException("Límite de tareas alcanzado. Máximo permitido: " + maxTareas);
        }

        Tarea nuevaTarea = new Tarea(null, descripcion, false, prioridad);
        return tareaRepository.guardar(nuevaTarea);
    }

    // ... (El resto de métodos listarTodas, listarPendientes, etc. quedan igual) ...

    /**
     * Obtener estadísticas (modificado para usar la propiedad mostrarEstadisticas).
     */
    public String obtenerEstadisticas() {
        if (!mostrarEstadisticas) {
            return "Las estadísticas están deshabilitadas por la configuración (app.mostrar-estadisticas=false).";
        }

        List<Tarea> todas = listarTodas();
        long total = todas.size();
        long completadas = listarCompletadas().size();
        long pendientes = total - completadas;

        return String.format(
                "--- Estadísticas de Tareas (%s)---\n" +
                        "Total de tareas: %d\n" +
                        "Completadas: %d\n" +
                        "Pendientes: %d\n" +
                        "------------------------------",
                nombreAplicacion, total, completadas, pendientes
        );
    }

    public List<Tarea> listarTodas() {
        return tareaRepository.obtenerTodas();
    }

    public Collection<Object> listarCompletadas() {
        List<Tarea> tareas = tareaRepository.obtenerTodas();
        return tareas.stream()
                .filter(Tarea::isCompletada)
                .collect(Collectors.toSet());
    }

    /**
     * Crea un método que imprima las propiedades de configuración.
     */
    public String imprimirConfiguracion() {
        return String.format(
                "\n*** CONFIGURACIÓN ACTUAL ***\n" +
                        "Nombre de la Aplicación: %s\n" +
                        "Límite Máximo de Tareas: %d\n" +
                        "Mostrar Estadísticas: %s\n" +
                        "******************************\n",
                nombreAplicacion, maxTareas, mostrarEstadisticas
        );
    }

    public Iterable<Object> listarPendientes() {
        List<Tarea> tareas = tareaRepository.obtenerTodas();
        return tareas.stream()
                .filter(tarea -> !tarea.isCompletada())
                .collect(Collectors.toSet());
    }

    public Optional<Tarea> marcarComoCompletada(Long idACambiar) {
        Optional<Tarea> tarea = tareaRepository.buscarPorId(idACambiar);
        if (tarea.isPresent()) {
            if (tarea.get().isCompletada()) {
                return tarea;
            }
            tarea.get().setCompletada(true);
            tareaRepository.guardar(tarea.get());
            return tarea;
        }

        return Optional.empty();

    }
}