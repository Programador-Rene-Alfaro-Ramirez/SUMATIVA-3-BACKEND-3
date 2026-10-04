package com.duoc.bancoservice.controller;

import com.duoc.bancoservice.model.Cuenta;
import com.duoc.bancoservice.repository.CuentaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaRepository repository;

    public CuentaController(CuentaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Cuenta> obtenerCuentas() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Cuenta obtenerCuenta(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }
}