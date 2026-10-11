package com.duoc.bancoservice.config;

import com.duoc.bancoservice.model.Cuenta;
import com.duoc.bancoservice.model.Interes;
import com.duoc.bancoservice.model.Transaccion;
import com.duoc.bancoservice.repository.CuentaRepository;
import com.duoc.bancoservice.repository.InteresRepository;
import com.duoc.bancoservice.repository.TransaccionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

@Configuration
public class DataLoader {

    private static final String BASE_PATH = "..\\bank_legacy_data\\";

    @Bean
    CommandLineRunner cargarDatos(
            CuentaRepository cuentaRepository,
            TransaccionRepository transaccionRepository,
            InteresRepository interesRepository) {
        
        return args -> {
            String[] semanas = {"semana_1", "semana_2", "semana_3"};

            for (String semana : semanas) {
                String rutaSemana = BASE_PATH + semana + "\\";
                
                cargarCuentas(cuentaRepository, rutaSemana);
                cargarTransacciones(transaccionRepository, rutaSemana);
                cargarIntereses(interesRepository, rutaSemana);
            }

            System.out.println("\n--- DATOS CARGADOS EN TRANSACCION-SERVICE ---");
            System.out.println("Cuentas totales: " + cuentaRepository.count());
            System.out.println("Transacciones totales: " + transaccionRepository.count());
            System.out.println("Intereses totales: " + interesRepository.count() + "\n");
        };
    }

    private Double parsearNumero(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        String limpio = valor.trim().replace(",", ".");
        limpio = limpio.replaceAll("[^\\d.-]", "");
        if (limpio.isEmpty() || limpio.equals("-") || limpio.equals(".")) return null;
        return Double.parseDouble(limpio);
    }

    private String[] obtenerColumnas(String linea) {
        String[] datos = linea.split(";", -1);
        if (datos.length < 3) {
            datos = linea.split(",", -1);
        }
        return datos;
    }

    private void cargarCuentas(CuentaRepository repository, String rutaSemana) {
        String archivo = rutaSemana + "estados_financieros_anuales.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(archivo, StandardCharsets.UTF_8))) {
            String linea;
            br.readLine(); 
            while ((linea = br.readLine()) != null) {
                try {
                    String[] datos = obtenerColumnas(linea);
                    if (datos.length < 5) continue;

                    Cuenta cuenta = new Cuenta();
                    cuenta.setCuentaId(Long.parseLong(datos[0].trim().replaceAll("[^\\d]", "")));
                    cuenta.setFecha(datos[1].trim());
                    cuenta.setTransaccion(datos[2].trim());
                    
                    Double monto = parsearNumero(datos[3]);
                    if (monto != null) cuenta.setMonto(monto);
                    
                    cuenta.setDescripcion(datos[4].trim());
                    repository.save(cuenta);
                } catch (Exception e) {
                    // Ignora errores individuales
                }
            }
        } catch (Exception e) {
            System.out.println("No se encontro: " + archivo);
        }
    }

    private void cargarTransacciones(TransaccionRepository repository, String rutaSemana) {
        String archivo = rutaSemana + "movimientos_financieros_diarios.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(archivo, StandardCharsets.UTF_8))) {
            String linea;
            br.readLine();
            long idGenerado = repository.count() + 1; 
            while ((linea = br.readLine()) != null) {
                try {
                    String[] datos = obtenerColumnas(linea);
                    if (datos.length < 3) continue;

                    Transaccion transaccion = new Transaccion();
                    transaccion.setId(idGenerado++);
                    transaccion.setFecha(datos[0].trim());
                    
                    Double monto = parsearNumero(datos[1]);
                    if (monto != null) transaccion.setMonto(monto);
                    
                    transaccion.setTipo(datos[2].trim());
                    repository.save(transaccion);
                } catch (Exception e) {
                    // Ignora errores individuales
                }
            }
        } catch (Exception e) {
            System.out.println("No se encontro: " + archivo);
        }
    }

    private void cargarIntereses(InteresRepository repository, String rutaSemana) {
        String archivo = rutaSemana + "intereses_trimestrales.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(archivo, StandardCharsets.UTF_8))) {
            String linea;
            br.readLine();
            while ((linea = br.readLine()) != null) {
                try {
                    String[] datos = obtenerColumnas(linea);
                    if (datos.length < 4) continue;

                    Interes interes = new Interes();
                    interes.setCuentaId(Long.parseLong(datos[0].trim().replaceAll("[^\\d]", "")));
                    interes.setNombre(datos[1].trim());
                    
                    Double saldo = parsearNumero(datos[2]);
                    if (saldo != null) interes.setSaldo(saldo);
                    
                    if (!datos[3].trim().isEmpty()) {
                        interes.setEdad(Integer.parseInt(datos[3].trim().replaceAll("[^\\d]", "")));
                    }
                    repository.save(interes);
                } catch (Exception e) {
                    // Ignora errores individuales
                }
            }
        } catch (Exception e) {
            System.out.println("No se encontro: " + archivo);
        }
    }
}