package com.prueba.tecnica.backend.venko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class VenkoApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(VenkoApplication.class, args);
        
        // Verifica los beans cargados
        System.out.println("=== BEANS CARGADOS ===");
        String[] beanNames = ctx.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if (beanName.toLowerCase().contains("auth") || 
                beanName.toLowerCase().contains("controller")) {
                System.out.println("✅ " + beanName);
            }
        }
    }
}