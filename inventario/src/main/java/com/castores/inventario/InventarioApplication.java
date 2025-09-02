package com.castores.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventarioApplication.class, args);
		String rawPassword = "1234";
		String encodedPassword = com.castores.inventario.util.PasswordUtil.encodePassword(rawPassword);
		System.out.println("Hash generado para 1234: " + encodedPassword);
	}

}
