package com.duoc.bancoservice.service;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Service
public class ServicioPrueba {

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public ServicioPrueba(CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public String probarFalla() {

        return circuitBreakerFactory.create("servicio-prueba").run(
                () -> {
                    throw new RuntimeException("servicio externo no disponible");
                },
                excepcion -> "fallback: servicio externo no disponible"
        );
    }
}