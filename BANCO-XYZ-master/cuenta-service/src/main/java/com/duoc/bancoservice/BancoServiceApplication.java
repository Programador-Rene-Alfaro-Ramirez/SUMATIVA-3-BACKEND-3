package com.duoc.bancoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BancoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancoServiceApplication.class, args);
    }
}