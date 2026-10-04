package com.duoc.bancoservice.repository;

import com.duoc.bancoservice.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}