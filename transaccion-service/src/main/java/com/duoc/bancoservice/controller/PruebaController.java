package com.duoc.bancoservice.controller;

import com.duoc.bancoservice.service.ServicioPrueba;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaController {

    private final ServicioPrueba servicioPrueba;

    public PruebaController(ServicioPrueba servicioPrueba) {
        this.servicioPrueba = servicioPrueba;
    }

    @GetMapping("/api/prueba-falla")
    public String pruebaFalla() {
        return servicioPrueba.probarFalla();
    }
}