package com.castores.inventario.controller;

import com.castores.inventario.model.Producto;
import com.castores.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "productos"; 
    }

    // FORMULARIO NUEVO
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto_form";
    }

    // GUARDAR NUEVO O EDITADO
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        if (producto.getStock() == null) {
            producto.setStock(0); 
        }
        productoRepository.save(producto);
        return "redirect:/productos";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("producto", producto);
        return "producto_form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id) {
        productoRepository.deleteById(id);
        return "redirect:/productos";
    }
}
