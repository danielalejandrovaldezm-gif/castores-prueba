package com.castores.inventario.controller;

import com.castores.inventario.model.Movimiento;
import com.castores.inventario.model.Producto;
import com.castores.inventario.service.MovimientoService;
import com.castores.inventario.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private ProductoService productoService;

    // Listar movimientos
    @GetMapping
    public String listarMovimientos(Model model) {
        model.addAttribute("movimientos", movimientoService.listarMovimientos()); 
        return "movimientos";
    }

    // Formulario para nuevo movimiento
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("movimiento", new Movimiento());
        model.addAttribute("productos", productoService.obtenerTodos());
        return "movimiento_form";
    }

    // Guardar movimiento
    @PostMapping
    public String guardarMovimiento(@ModelAttribute("movimiento") Movimiento movimiento) {
        movimientoService.registrarMovimiento(movimiento); 
        return "redirect:/movimientos";
    }
}
