# Proyecto Auth-API

Este proyecto consiste en una API RESTful desarrollada con Spring Boot que implementa:

- Registro de usuarios
- Autenticación (generación de token JWT)
- Validación de formato de correo
- Validación de formato de contraseña
- Persistencia de datos en base de datos en memoria (H2)
- Exposición de documentación con Springdoc OpenAPI/Swagger
- Gestión de usuarios (creación, actualización, eliminación)
- Pruebas unitarias con JUnit y Mockito

## Tabla de Contenidos

1. [Tecnologías y versiones](#tecnologías-y-versiones)
2. [Requisitos](#requisitos)
3. [Ejecución y despliegue](#ejecución-y-despliegue)
4. [Uso de la aplicación](#uso-de-la-aplicación)
5. [Endpoints principales](#endpoints-principales)
6. [Estructura del proyecto](#estructura-del-proyecto)
7. [Pruebas unitarias](#pruebas-unitarias)
8. [Diagrama de la solución](#diagrama-de-la-solución)
9. [Contacto](#contacto)

---

## Tecnologías y versiones

- Java 17
- Spring Boot 3.4.3
- Spring Data JPA (H2 como base de datos en memoria)
- Spring Security (JWT)
- Maven 3.x
- Lombok 1.18.x
- Springdoc OpenAPI Starter (para la documentación)

## Requisitos

- Java 17 o superior instalado
- Maven 3.x o superior
- Opcional: Un IDE compatible con Spring Boot (IntelliJ, Eclipse)

## Ejecución y despliegue

1. Clona o descarga este repositorio.
2. Ubícate en la carpeta raíz del proyecto.
3. Ejecuta `mvn clean package` para compilar y empacar la aplicación.
4. Inicia la aplicación con:

`mvn spring-boot:run`

5. La aplicación estará disponible en `http://localhost:8080` por defecto.

## Uso de la aplicación

1. Revisa la documentación Swagger/OpenAPI:

- Abre tu navegador en `http://localhost:8080/swagger-ui.html` o la ruta que haya configurado.

2. Los principales endpoints de registro y autenticación se encuentran bajo `/auth`.
3. Otros endpoints para la gestión de usuarios se encuentran bajo `/users`.


## Endpoints principales

1. **POST** `/auth/signup`

- Registra un nuevo usuario.
- Campos requeridos :
  ```json
  {
    "nombre": "Luis Martinez",
    "correo": "l.martinezm22@gmail.com",
    "password": "@Luis2025",
    "telefonos": [
      {
        "numero": "1234567",
        "codigoCiudad": "1",
        "codigoPais": "57"
      }
    ]
  }
  ```
- Respuesta exitosa: Un objeto `UsuarioResponseDto`.

2. **POST** `/auth/login`

- Autentica un usuario existente.
- Campos requeridos:
  ```json
  {
    "correo": "l.martinezm22@gmail.com",
    "password": "@Luis2025"
  }
  ```
- Respuesta exitosa: Un objeto `UsuarioResponseDto` que incluye el token JWT.

3. **GET** `/api/{uuid}`

- Retorna un usuario por ID.
- Requiere autenticación mediante un token JWT válido en `Authorization`.

4. **GET** `/api`

- Retorna la lista de usuarios registrados.
- Requiere autenticación mediante un token JWT válido en `Authorization`.

5. **PUT** `/api/{uuid}`

- Actualiza los datos de un usuario por ID.
- Campos requeridos:
  ```json
  {
    "nombre": "Luis Martinez",
    "correo": "l.martinezm22@gmail.com",
    "password": "@CambioClave123",
    "telefonos": [
      {
        "numero": "98765432",
        "codigoCiudad": "12",
        "codigoPais": "58"
      }
    ]
  }
  ```
- Respuesta exitosa: Un objeto `UsuarioResponseDto`.

6. **PATCH** `/api/{uuid}`

- Actualiza parcialmente los datos de un usuario por ID.
- Campos requeridos:
  ```json
  {
    "correo": "l.martinezm22@gmail.com",
    "password": "C@mbioclave444"
  }
  ```
- Respuesta exitosa: Un objeto `UsuarioResponseDto`.

7. **DELETE** `/api/{uuid}`

- Elimina un usuario por ID.
- Requiere autenticación mediante un token JWT válido en `Authorization`.

## Pruebas unitarias

- Se utilizan JUnit y Mockito para las pruebas unitarias.
- Las clases de prueba se ubican bajo `src/test/java/com/nttdata/api`.
- Para ejecutar las pruebas:

`mvn test`

- Ejemplos:
  - `AutenticacionControllerTest.java`
  - `UsuarioControllerTest.java`
  - `AutenticacionServiceTest.java`
  - `UsuarioServiceTest.java`
  - `JwtServiceTest.java`

## Diagramas

- Diagrama de secuencia : /signup
- Diagrama de Clases
- Diagrama de Componentes

## Contacto

- Autor: [Luis Martinez]
- Correo: [l.martinezm22@gmail.com]
