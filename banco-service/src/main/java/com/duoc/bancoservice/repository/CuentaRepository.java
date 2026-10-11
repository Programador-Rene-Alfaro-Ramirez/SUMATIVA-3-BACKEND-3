package com.duoc.bancoservice.repository;

import com.duoc.bancoservice.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
}