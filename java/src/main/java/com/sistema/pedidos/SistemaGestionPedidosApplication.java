package com.sistema.pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sistema.pedidos.repository")
@EnableMongoRepositories(basePackages = "com.sistema.pedidos.repository")
public class SistemaGestionPedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaGestionPedidosApplication.class, args);
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTIÓN DE PEDIDOS - INICIADO           ║");
        System.out.println("║   Accede a: http://localhost:8080                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
    }
}