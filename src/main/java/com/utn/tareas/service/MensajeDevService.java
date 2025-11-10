package com.utn.tareas.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class MensajeDevService implements MensajeService {

    @Override
    public void mostrarBienvenida() {
        System.out.println("✨ ¡Bienvenido(a) desarrollador(a)! Estás en el Entorno de Desarrollo. ✨");
        System.out.println("Configuración de logs en DEBUG y límite de 10 tareas.");
    }

    @Override
    public void mostrarDespedida() {
        System.out.println("👋 ¡Trabajo de desarrollo completado! ¡Hasta pronto!");
    }
}
