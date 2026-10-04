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

    private static final String BASE_PATH =
            "../bank_legacy_data/data/semana_3/";

    @Bean
    CommandLineRunner cargarDatos(
            CuentaRepository cuentaRepository,
            TransaccionRepository transaccionRepository,
            InteresRepository interesRepository) {

        return args -> {

            cargarCuentas(cuentaRepository);
            cargarTransacciones(transaccionRepository);
            cargarIntereses(interesRepository);

            System.out.println("datos del legacy cargados correctamente");
            System.out.println("cuentas: " + cuentaRepository.count());
            System.out.println("transacciones: " + transaccionRepository.count());
            System.out.println("intereses: " + interesRepository.count());
        };
    }

    private void cargarCuentas(CuentaRepository repository) {

        String archivo = BASE_PATH + "cuentas_anuales.csv";

        try (BufferedReader br = new BufferedReader(
                new FileReader(archivo, StandardCharsets.UTF_8))) {

            String linea;
            br.readLine();

            while ((linea = br.readLine()) != null) {

                try {

                    String[] datos = linea.split(",", -1);

                    if (datos.length < 5) {
                        continue;
                    }

                    Cuenta cuenta = new Cuenta();

                    cuenta.setCuentaId(Long.parseLong(datos[0]));
                    cuenta.setFecha(datos[1]);
                    cuenta.setTransaccion(datos[2]);

                    if (!datos[3].isBlank()) {
                        cuenta.setMonto(Double.parseDouble(datos[3]));
                    }

                    cuenta.setDescripcion(datos[4]);

                    repository.save(cuenta);

                } catch (Exception e) {
                    System.out.println("fila de cuenta ignorada: " + linea);
                }
            }

        } catch (Exception e) {
            System.out.println("no se pudo cargar cuentas: " + e.getMessage());
        }
    }

    private void cargarTransacciones(TransaccionRepository repository) {

        String archivo = BASE_PATH + "transacciones.csv";

        try (BufferedReader br = new BufferedReader(
                new FileReader(archivo, StandardCharsets.UTF_8))) {

            String linea;
            br.readLine();

            long idGenerado = 1;

            while ((linea = br.readLine()) != null) {

                try {

                    String[] datos = linea.split(",", -1);

                    if (datos.length < 4) {
                        continue;
                    }

                    Transaccion transaccion = new Transaccion();

                    transaccion.setId(idGenerado++);
                    transaccion.setFecha(datos[1]);

                    if (!datos[2].isBlank()) {
                        transaccion.setMonto(Double.parseDouble(datos[2]));
                    }

                    transaccion.setTipo(datos[3]);

                    repository.save(transaccion);

                } catch (Exception e) {
                    System.out.println("fila de transaccion ignorada: " + linea);
                }
            }

        } catch (Exception e) {
            System.out.println("no se pudo cargar transacciones: " + e.getMessage());
        }
    }

    private void cargarIntereses(InteresRepository repository) {

        String archivo = BASE_PATH + "intereses.csv";

        try (BufferedReader br = new BufferedReader(
                new FileReader(archivo, StandardCharsets.UTF_8))) {

            String linea;
            br.readLine();

            while ((linea = br.readLine()) != null) {

                try {

                    String[] datos = linea.split(",", -1);

                    if (datos.length < 5) {
                        continue;
                    }

                    Interes interes = new Interes();

                    interes.setCuentaId(Long.parseLong(datos[0]));
                    interes.setNombre(datos[1]);

                    if (!datos[2].isBlank()) {
                        interes.setSaldo(Double.parseDouble(datos[2]));
                    }

                    if (!datos[3].isBlank()) {
                        interes.setEdad(Integer.parseInt(datos[3]));
                    }

                    interes.setTipo(datos[4]);

                    repository.save(interes);

                } catch (Exception e) {
                    System.out.println("fila de interes ignorada: " + linea);
                }
            }

        } catch (Exception e) {
            System.out.println("no se pudo cargar intereses: " + e.getMessage());
        }
    }
}