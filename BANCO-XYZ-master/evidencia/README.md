# Actividad Formativa Semana 7: Tolerancia a fallos y arquitectura de eventos con microservicios

## 1. Descripcion del proyecto

El proyecto Banco XYZ utiliza una arquitectura basada en microservicios desarrollada con Spring Boot y Spring Cloud.

En esta etapa se incorporo una arquitectura de eventos mediante JMS y ActiveMQ, junto con un mecanismo de tolerancia a fallos utilizando Resilience4j.

El objetivo es permitir que las transacciones puedan enviarse de forma asincrona a otros microservicios y evitar que una falla temporal del broker provoque la perdida de la transaccion.

## 2. Estructura del proyecto

* **config-server:** servidor de configuracion centralizada. Puerto `8888`.
* **eureka-server:** servidor de descubrimiento de servicios. Puerto `8761`.
* **banco-service:** microservicio principal. Puerto `8081`.
* **cuenta-service:** microservicio encargado del procesamiento de cuentas. Puerto `8082`.
* **transaccion-service:** microservicio para transacciones. Puerto `8083`.
* **bank_legacy_data:** datos provenientes del sistema legacy.

## 3. Tecnologias utilizadas

* Java 17
* Spring Boot
* Spring Cloud
* Eureka
* Config Server
* Spring Security
* JWT
* Resilience4j
* JMS
* ActiveMQ Classic
* H2
* Maven

## 4. Arquitectura de eventos

Para la comunicacion asincrona entre microservicios se utiliza JMS junto con ActiveMQ Classic.

El flujo principal es:

```text
banco-service
     |
     | publica evento
     v
cola.transacciones.banco
     |
     | consume evento
     v
cuenta-service
```

El productor se encuentra en `banco-service`, mientras que `cuenta-service` contiene el consumidor JMS.

La cola utilizada es:

```text
cola.transacciones.banco
```

El evento contiene informacion relacionada con la transaccion, como:

* id de la transaccion
* cuenta de origen
* monto
* tipo de operacion
* fecha

## 5. Patron de comunicacion

Se utiliza comunicacion asincrona basada en eventos.

Cuando `banco-service` recibe una nueva transaccion, primero la almacena localmente y posteriormente genera un evento JMS.

El evento es enviado a la cola `cola.transacciones.banco`.

`cuenta-service` consume posteriormente este evento mediante un listener JMS.

Este enfoque permite desacoplar al productor del consumidor, ya que ambos microservicios no necesitan ejecutar la operacion de manera simultanea.

## 6. Saga coreografiada

La arquitectura utiliza el concepto de Saga mediante coreografia.

Cada microservicio participa en el flujo reaccionando a los eventos publicados en la cola.

El flujo simplificado es:

```text
Transaccion recibida
        |
        v
   banco-service
        |
        | evento JMS
        v
cola.transacciones.banco
        |
        v
   cuenta-service
        |
        v
Procesamiento de la transaccion
```

No existe un orquestador central que controle todas las operaciones. Cada servicio reacciona al evento que corresponde a su responsabilidad.

## 7. Tolerancia a fallos con Resilience4j

El envio del evento JMS desde `banco-service` se encuentra protegido mediante un Circuit Breaker de Resilience4j.

La operacion protegida corresponde al envio real del mensaje:

```java
@CircuitBreaker(
    name = "envioJms",
    fallbackMethod = "fallbackEnvioTransaccion"
)
```

El Circuit Breaker utiliza una ventana basada en cantidad de llamadas y permite detectar fallos consecutivos en la comunicacion con ActiveMQ.

Configuracion principal:

```properties
resilience4j.circuitbreaker.instances.envioJms.slidingWindowType=COUNT_BASED
resilience4j.circuitbreaker.instances.envioJms.slidingWindowSize=5
resilience4j.circuitbreaker.instances.envioJms.minimumNumberOfCalls=3
resilience4j.circuitbre
```
