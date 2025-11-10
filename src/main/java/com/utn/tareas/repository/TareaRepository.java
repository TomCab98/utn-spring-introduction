package com.utn.tareas.repository;


import com.utn.tareas.model.Prioridad;
import com.utn.tareas.model.Tarea;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

    @Repository // Anota con @Repository [cite: 48]
    public class TareaRepository {


        public final List<Tarea> tareas;


        private final AtomicLong nextId = new AtomicLong(1);


        public TareaRepository() {
            this.tareas = new ArrayList<>();

            // Inicialización de 3-5 tareas de ejemplo
            tareas.add(new Tarea(nextId.getAndIncrement(), "Preparar la clase de Programación III", false, Prioridad.ALTA));
            tareas.add(new Tarea(nextId.getAndIncrement(), "Comprar café", false, Prioridad.BAJA));
            tareas.add(new Tarea(nextId.getAndIncrement(), "Terminar el TP de Spring Boot", false, Prioridad.MEDIA));
        }




        public Tarea guardar(Tarea tarea) {
            if (tarea.getId() == null) {
                // Asigna un nuevo ID si es una tarea nueva
                tarea.setId(nextId.getAndIncrement());
            } else {
                // En una aplicación real, aquí se actualizaría la tarea existente.
                // Para el ejercicio en memoria, simplemente nos aseguramos de que no haya duplicados si el ID ya existe.
                eliminar(tarea.getId());
            }
            this.tareas.add(tarea);
            return tarea;
        }


        public List<Tarea> obtenerTodas() {
            return new ArrayList<>(this.tareas); // Devuelve una copia para inmutabilidad
        }


        public Optional<Tarea> buscarPorId(Long id) {
            return this.tareas.stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst();
        }


        public boolean eliminar(Long id) {
            return this.tareas.removeIf(t -> t.getId().equals(id));
        }
    }

