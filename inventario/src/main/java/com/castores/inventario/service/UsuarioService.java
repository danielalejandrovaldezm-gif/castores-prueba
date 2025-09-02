package com.castores.inventario.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.castores.inventario.model.Usuario;
import com.castores.inventario.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario crear(Usuario u) {
        // Siempre encripta al crear
        if (u.getContrasena() != null && !u.getContrasena().isBlank()) {
            u.setContrasena(passwordEncoder.encode(u.getContrasena()));
        }
        return usuarioRepository.save(u);
    }

    public Usuario actualizar(Long id, Usuario cambios) {
        Usuario actual = usuarioRepository.findById(id).orElse(null);
        if (actual == null) return null;

        actual.setNombre(cambios.getNombre());
        actual.setCorreo(cambios.getCorreo());
        actual.setRol(cambios.getRol());
        actual.setEstatus(cambios.getEstatus());

        // Si viene password vacío → conservar. Si viene algo → encriptar nuevo.
        if (cambios.getContrasena() != null && !cambios.getContrasena().isBlank()) {
            actual.setContrasena(passwordEncoder.encode(cambios.getContrasena()));
        }

        return usuarioRepository.save(actual);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean correoDuplicadoAlCrear(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    public boolean correoDuplicadoAlEditar(String correo, Long idUsuario) {
        return usuarioRepository.existsByCorreoAndIdUsuarioNot(correo, idUsuario);
    }
}
