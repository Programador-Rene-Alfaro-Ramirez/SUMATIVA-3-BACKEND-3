package com.duoc.bancoservice.controller;

import com.duoc.bancoservice.model.Transaccion;
import com.duoc.bancoservice.repository.TransaccionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionRepository repository;

    public TransaccionController(TransaccionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Transaccion> obtenerTransacciones() {
        return repository.findAll();
    }
}