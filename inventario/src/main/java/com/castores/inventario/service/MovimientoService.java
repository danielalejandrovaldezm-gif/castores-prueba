package com.castores.inventario.service;

import com.castores.inventario.model.Movimiento;
import com.castores.inventario.model.Producto;
import com.castores.inventario.model.Usuario;
import com.castores.inventario.repository.MovimientoRepository;
import com.castores.inventario.repository.ProductoRepository;
import com.castores.inventario.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Movimiento> listarMovimientos() {
        return movimientoRepository.findAll();
    }

    public Movimiento registrarMovimiento(Movimiento movimiento) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String correo = auth.getName();
    System.out.println("DEBUG >> Usuario autenticado: " + correo);

    System.out.println("DEBUG >> Movimiento recibido:");
    System.out.println(" - Producto: " + 
        (movimiento.getProducto() != null ? movimiento.getProducto().getIdProducto() : "NULL"));
    System.out.println(" - Tipo: " + movimiento.getTipo());
    System.out.println(" - Cantidad: " + movimiento.getCantidad());

    Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));

    movimiento.setUsuario(usuario);
    movimiento.setFechaHora(LocalDateTime.now());
    
    Producto producto = productoRepository.findById(movimiento.getProducto().getIdProducto())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    if ("ENTRADA".equalsIgnoreCase(movimiento.getTipo())) {
        producto.setStock(producto.getStock() + movimiento.getCantidad());
    } else if ("SALIDA".equalsIgnoreCase(movimiento.getTipo())) {
        if (producto.getStock() < movimiento.getCantidad()) {
            throw new RuntimeException("Stock insuficiente para salida");
        }
        producto.setStock(producto.getStock() - movimiento.getCantidad());
    }

    productoRepository.save(producto);
    return movimientoRepository.save(movimiento);
}

}
