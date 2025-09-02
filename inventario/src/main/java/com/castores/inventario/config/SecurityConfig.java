package com.castores.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/login", "/css/**", "/js/**").permitAll()
        .requestMatchers("/usuarios/**").hasRole("ADMIN")
        .requestMatchers("/productos/**").hasAnyRole("ADMIN","ALMACENISTA")
        .requestMatchers("/movimientos/**").hasAnyRole("ADMIN","ALMACENISTA")
        .anyRequest().authenticated()
    )
        .formLogin(login -> login
            .loginPage("/login").permitAll()
            .loginProcessingUrl("/login")     
            .usernameParameter("correo")      
            .passwordParameter("password")
            .defaultSuccessUrl("/", true)
        )
        .logout(logout -> logout
            .logoutUrl("/logout")              
            .logoutSuccessUrl("/login?logout") 
            .invalidateHttpSession(true)       
            .deleteCookies("JSESSIONID")       
            .permitAll()
        );

    return http.build();
}

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}
