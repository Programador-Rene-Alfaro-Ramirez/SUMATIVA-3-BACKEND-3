package com.duoc.bancoservice.service;

import com.duoc.bancoservice.dto.TransaccionEvent;
import com.duoc.bancoservice.model.Transaccion;
import com.duoc.bancoservice.repository.TransaccionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransaccionJmsService {

    private final JmsTemplate jmsTemplate;
    private final TransaccionRepository repository;

    public TransaccionJmsService(
            JmsTemplate jmsTemplate,
            TransaccionRepository repository) {
        this.jmsTemplate = jmsTemplate;
        this.repository = repository;
    }

    @CircuitBreaker(name = "envioJms", fallbackMethod = "fallbackEnvioTransaccion")
    public boolean enviarTransaccion(
            Transaccion transaccion,
            TransaccionEvent evento) {

        System.out.println("==> [PRODUCIENDO EVENTO] Enviando transaccion ID "
                + evento.getIdTransaccion() + " a la cola JMS...");

        jmsTemplate.convertAndSend("cola.transacciones.banco", evento);

        System.out.println("==> [JMS] Transaccion enviada correctamente.");

        return true;
    }

    public boolean fallbackEnvioTransaccion(
            Transaccion transaccion,
            TransaccionEvent evento,
            Throwable excepcion) {

        transaccion.setEstado("PENDIENTE_SINCRONIZACION");
        repository.save(transaccion);

        System.out.println("==> [CIRCUIT BREAKER] ActiveMQ no disponible.");
        System.out.println("==> [FALLBACK] Transaccion ID "
                + transaccion.getId()
                + " guardada como PENDIENTE_SINCRONIZACION.");

        return false;
    }
}