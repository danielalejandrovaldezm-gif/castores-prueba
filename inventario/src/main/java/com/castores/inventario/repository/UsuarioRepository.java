package com.castores.inventario.repository;

import com.castores.inventario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    
    boolean existsByCorreo(String correo);
    boolean existsByCorreoAndIdUsuarioNot(String correo, Long idUsuario);
}
