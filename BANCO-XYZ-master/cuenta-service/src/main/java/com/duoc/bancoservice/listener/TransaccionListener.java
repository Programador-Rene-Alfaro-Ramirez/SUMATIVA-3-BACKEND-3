package com.duoc.bancoservice.listener;

import com.duoc.bancoservice.dto.TransaccionEvent;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TransaccionListener {

    @JmsListener(destination = "cola.transacciones.banco")
    public void procesarTransaccionRecibida(TransaccionEvent evento) {
        System.out.println("<== [CONSUMIENDO EVENTO JMS] Transacción recibida con éxito.");
        System.out.println("ID Transacción: " + evento.getIdTransaccion());
        System.out.println("Cuenta Destino: " + evento.getCuentaId());
        System.out.println("Monto: $" + evento.getMonto());
        System.out.println("Tipo Operación: " + evento.getTipoOperacion());
        System.out.println("---------------------------------------------------");
    }
}