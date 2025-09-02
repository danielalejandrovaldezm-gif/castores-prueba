package com.castores.inventario.service;

import com.castores.inventario.model.Usuario;
import com.castores.inventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        if (!"1".equals(usuario.getEstatus())) {
            throw new UsernameNotFoundException("El usuario está inactivo");
        }

        String roleName = (usuario.getRol() != null && usuario.getRol().getNombre() != null)
                ? usuario.getRol().getNombre().toUpperCase()
                : "USER";

        return org.springframework.security.core.userdetails.User.builder()
            .username(usuario.getCorreo())
            .password(usuario.getContrasena())  
            .roles(usuario.getRol().getNombre().toUpperCase())
            .build();
    }
}
