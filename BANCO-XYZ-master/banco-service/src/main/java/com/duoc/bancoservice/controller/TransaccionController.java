package com.duoc.bancoservice.controller;

import com.duoc.bancoservice.dto.TransaccionEvent;
import com.duoc.bancoservice.model.Transaccion;
import com.duoc.bancoservice.repository.TransaccionRepository;
import com.duoc.bancoservice.service.TransaccionJmsService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionRepository repository;
    private final TransaccionJmsService transaccionJmsService;

    public TransaccionController(
            TransaccionRepository repository,
            TransaccionJmsService transaccionJmsService) {
        this.repository = repository;
        this.transaccionJmsService = transaccionJmsService;
    }

    @GetMapping
    public List<Transaccion> obtenerTransacciones() {
        return repository.findAll();
    }

    @PostMapping
    public Transaccion crearTransaccion(@RequestBody Transaccion transaccion) {

        // guardamos la transaccion primero
        transaccion.setEstado("PENDIENTE_SINCRONIZACION");
        Transaccion transaccionGuardada = repository.save(transaccion);

        // creamos el evento JMS
        TransaccionEvent evento = new TransaccionEvent(
                transaccionGuardada.getId().toString(),
                "CTA-GENERICA",
                BigDecimal.valueOf(transaccionGuardada.getMonto()),
                transaccionGuardada.getTipo()
        );

        // enviamos el evento protegido por Resilience4j
        boolean enviada = transaccionJmsService.enviarTransaccion(
                transaccionGuardada,
                evento
        );

        // solo marcamos como sincronizada si JMS funciono
        if (enviada) {
            transaccionGuardada.setEstado("SINCRONIZADA");
        }

        return repository.save(transaccionGuardada);
    }
}