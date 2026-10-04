# Proyecto de Microservicios Cloud - Banco XYZ

## 1. Objetivo del Proyecto
El objetivo de este proyecto es implementar una arquitectura de microservicios en un entorno Cloud para la modernización y procesamiento de datos legacy del Banco XYZ. El sistema incorpora mecanismos de seguridad corporativa (OAuth 2.0), tolerancia a fallos (Resilience4j), descubrimiento de servicios, y comunicación asíncrona orientada a eventos mediante mensajería JMS (Apache ActiveMQ). Todo esto orquestado en contenedores.

## 2. Estructura del Código
El proyecto utiliza una arquitectura distribuida compuesta por los siguientes módulos:
* **banco-service:** Microservicio de gestión general del banco.
* **cuenta-service:** Microservicio encargado de la administración de cuentas.
* **transaccion-service:** Microservicio para el procesamiento de transacciones.
* **eureka-server:** Servidor de descubrimiento (Service Discovery) para que los servicios se encuentren entre sí.
* **config-server:** Servidor de configuración centralizada para los microservicios.
* **docker-compose.yaml:** Archivo de orquestación que integra todos los microservicios, además de un contenedor de ActiveMQ.
* **data/**: Directorio que contiene los archivos CSV legacy, los cuales son montados como un volumen en los contenedores para su procesamiento en el arranque.

## 3. Instrucciones para Ejecutar el Proyecto
Para desplegar la infraestructura completa, sigue estos pasos:
1. Asegúrate de tener Podman (o Docker) Desktop instalado y ejecutándose.
2. Abre una terminal ubicándote en la carpeta raíz del proyecto (donde se encuentra el archivo `docker-compose.yaml`).
3. Ejecuta el siguiente comando para levantar toda la arquitectura en segundo plano:
   ```bash
   docker-compose up -d