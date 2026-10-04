package com.duoc.bancoservice.service;

import com.duoc.bancoservice.dto.TransaccionEvent;
import com.duoc.bancoservice.model.Transaccion;
import com.duoc.bancoservice.repository.TransaccionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.jms.core.JmsTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransaccionJmsServiceTest {

    @Test
    void debeEjecutarFallbackCuandoJmsFalla() {

        JmsTemplate jmsTemplate = mock(JmsTemplate.class);
        TransaccionRepository repository = mock(TransaccionRepository.class);

        TransaccionJmsService service =
                new TransaccionJmsService(jmsTemplate, repository);

        Transaccion transaccion = new Transaccion();
        transaccion.setMonto(10000.0);
        transaccion.setTipo("DEPOSITO");

        TransaccionEvent evento = new TransaccionEvent(
                "1",
                "CTA-GENERICA",
                BigDecimal.valueOf(10000),
                "DEPOSITO"
        );

        doThrow(new RuntimeException("ActiveMQ no disponible"))
                .when(jmsTemplate)
                .convertAndSend("cola.transacciones.banco", evento);

        boolean resultado;

        try {
            resultado = service.enviarTransaccion(transaccion, evento);
        } catch (Exception e) {
            resultado = service.fallbackEnvioTransaccion(
                    transaccion,
                    evento,
                    e
            );
        }

        assertFalse(resultado);
        assertEquals(
                "PENDIENTE_SINCRONIZACION",
                transaccion.getEstado()
        );

        verify(repository).save(transaccion);
    }
}