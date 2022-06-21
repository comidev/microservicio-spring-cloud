# MICROSERVICIO - SPRING CLOUD BY COMIDEV

## BIENVENIDO

El presente proyecto es una práctica de Microservicios con Spring Cloud y Spring Boot.

Está descompuesto por subdominios. La infraestructura está centralizada con Spring Cloud Cofig, que recupera las configuraciones de Github.

El registro y descubrimiento está realizado con Spring Cloud Eureka ya que se integra muy bien con muchos proyectos de Spring Cloud.

La comunicación está realizada con OpenFeign, y para el Circuit Breaker me apoyé en Histryx.

La integración de los microservicios se hizo con Spring Cloud Gateway o una API Gateway, que corre en el puerto "8080" del localhost.

La observabilidad, seguimiento y monitoreo está hecha con Spring Cloud Admin.

Puertos

-   API Gateway: 8080
-   Eureka: 8089
-   Config Server: 8081
-   Admin Server: 8086

## SERVICIOS

### 1. AUTH-SERVICE

Maneja las entidades Usuario y Rol, sirve para autenticar y autorizar.

Base de datos: PostgreSQL
Nombre: usersDB
Puerto: 5432

### 2. CUSTOMER-SERVICE

Maneja las entidades Customer y Region, sirve para gestionar a los clientes.

Base de datos: Mysql
Nombre: customersdb
Puerto: 3306

### 3. SHOPPING-SERVICE

Maneja las entidades Invoice y InvoiceItem, sirve para gestionar a las compras.

Base de datos: Mysql
Nombre: shoppingdb
Puerto: 3306

### 4. PRODUCT-SERVICE

Maneja las entidades Product y Category, sirve para gestionar a los productos.

Base de datos: MongoDB
Nombre: productsDB
Puerto: 27017

## ORDEN DE EJECUCIÓN

1. config-service
2. registry-service
3. admin-service
4. gateway-service

5. Servicios:

-   auth-service (postgresql)
-   customer-service (mysql)
-   product-service (mongodb)
-   shopping-service (mysql)

## ENDPOINTS

### 1. AUTH-SERVICE

Ruta: /users

#### Login

Petición:
Ruta: /login
Método: POST
Cuerpo: Usuario

```json
{
    "username": "string",
    "password": "string"
}
```

Respuesta: Token y Token refresh

```json
{
    "accessToken": "string",
    "refreshToken": "string"
}
```
