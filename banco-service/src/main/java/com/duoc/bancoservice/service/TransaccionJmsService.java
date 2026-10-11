package com.duoc.bancoservice.service;

import com.duoc.bancoservice.dto.TransaccionEvent;
import com.duoc.bancoservice.model.Transaccion;
import com.duoc.bancoservice.repository.TransaccionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.kafka.core.KafkaTemplate; // Nueva importación de Kafka
import org.springframework.stereotype.Service;

@Service
public class TransaccionJmsService {

    // Cambiamos JmsTemplate por KafkaTemplate
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TransaccionRepository repository;

    public TransaccionJmsService(
            KafkaTemplate<String, Object> kafkaTemplate,
            TransaccionRepository repository) {
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;
    }

    @CircuitBreaker(name = "envioJms", fallbackMethod = "fallbackEnvioTransaccion")
    public boolean enviarTransaccion(
            Transaccion transaccion,
            TransaccionEvent evento) {

        System.out.println("==> [PRODUCIENDO EVENTO KAFKA] Enviando transaccion ID "
                + evento.getIdTransaccion() + " al topico...");

        // Reemplazamos la cola JMS por un tópico de Kafka
        kafkaTemplate.send("topico.transacciones.banco", evento);

        System.out.println("==> [KAFKA] Transaccion enviada correctamente.");

        return true;
    }

    public boolean fallbackEnvioTransaccion(
            Transaccion transaccion,
            TransaccionEvent evento,
            Throwable excepcion) {

        transaccion.setEstado("PENDIENTE_SINCRONIZACION");
        repository.save(transaccion);

        System.out.println("==> [CIRCUIT BREAKER] Broker de Kafka no disponible.");
        System.out.println("==> [FALLBACK] Transaccion ID "
                + transaccion.getId()
                + " guardada como PENDIENTE_SINCRONIZACION.");

        return false;
    }
}