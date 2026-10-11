package com.duoc.bancoservice.controller;

import com.duoc.bancoservice.model.Interes;
import com.duoc.bancoservice.repository.InteresRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/intereses")
public class InteresController {

    private final InteresRepository repository;

    public InteresController(InteresRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Interes> obtenerIntereses() {
        return repository.findAll();
    }
}