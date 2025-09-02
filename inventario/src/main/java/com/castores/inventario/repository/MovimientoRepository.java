package com.castores.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.castores.inventario.model.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
}
