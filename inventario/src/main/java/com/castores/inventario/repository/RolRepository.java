package com.castores.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.castores.inventario.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
}
