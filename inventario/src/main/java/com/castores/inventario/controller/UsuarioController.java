package com.castores.inventario.controller;

import com.castores.inventario.model.Usuario;
import com.castores.inventario.model.Rol;
import com.castores.inventario.repository.UsuarioRepository;
import com.castores.inventario.service.UsuarioService;
import jakarta.validation.Valid;
import com.castores.inventario.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioService usuarioService;

    // LISTAR USUARIOS
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    // FORMULARIO NUEVO USUARIO
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Usuario usuario = new Usuario();
        List<Rol> roles = rolRepository.findAll();

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", roles);
        return "usuario_form";
    }

    // GUARDAR NUEVO USUARIO
    // @PostMapping("/guardar")
    // public String guardar(@ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
    //     if (result.hasErrors()) {
    //         model.addAttribute("roles", rolRepository.findAll());
    //         return "usuario_form";
    //     }

    //     Rol rol = rolRepository.findById(usuario.getRol().getIdRol())
    //             .orElseThrow(() -> new IllegalArgumentException("Rol no válido"));
    //     usuario.setRol(rol);

    //     usuarioRepository.save(usuario);
    //     return "redirect:/usuarios";
    // }

    @PostMapping("/usuarios/guardar")
public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
    if (result.hasErrors()) {
        return "usuarios_form";
    }

    if (usuario.getContrasena() != null && !usuario.getContrasena().isBlank()) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
    } else if (usuario.getIdUsuario() != null) {
        Usuario existente = usuarioRepository.findById(usuario.getIdUsuario()).orElseThrow();
        usuario.setContrasena(existente.getContrasena());
    }

    usuarioRepository.save(usuario);
    return "redirect:/usuarios?ok";
}

    // EDITAR USUARIO
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id de usuario inválido: " + id));

        List<Rol> roles = rolRepository.findAll();

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", roles);
        return "usuario_form"; 
    }

    // ACTUALIZAR USUARIO
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable("id") Long id, @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolRepository.findAll());
            return "usuario_form";
        }

        Rol rol = rolRepository.findById(usuario.getRol().getIdRol())
                .orElseThrow(() -> new IllegalArgumentException("Rol no válido"));
        usuario.setRol(rol);

        usuario.setIdUsuario(id);
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    // ELIMINAR USUARIO
    // @GetMapping("/eliminar/{id}")
    // public String eliminar(@PathVariable("id") Long id) {
    //     Usuario usuario = usuarioRepository.findById(id)
    //             .orElseThrow(() -> new IllegalArgumentException("Id de usuario inválido: " + id));
    //     usuarioRepository.delete(usuario);
    //     return "redirect:/usuarios";
    // }
    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}
