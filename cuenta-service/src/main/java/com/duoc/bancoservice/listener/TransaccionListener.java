package com.duoc.bancoservice.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransaccionListener {

    // Escucha exactamente el mismo tópico al que banco-service está enviando
    @KafkaListener(topics = "topico.transacciones.banco", groupId = "grupo-cuentas")
    public void recibirTransaccion(String mensaje) {
        
        System.out.println("=====================================================");
        System.out.println("==> [CONSUMIDOR KAFKA - CUENTA SERVICE] ");
        System.out.println("==> Mensaje asíncrono recibido: " + mensaje);
        System.out.println("=====================================================");
        
        // Aquí el microservicio de cuentas ya sabe que se hizo una transacción 
        // y más adelante puede usar esto para actualizar el saldo.
    }
}