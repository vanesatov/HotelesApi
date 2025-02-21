# Hoteles API
Hoteles API es una aplicación RESTful construida con Spring Boot y MongoDB que permite gestionar información de hoteles. Ofrece endpoints para consultar, crear y eliminar hoteles, así como una interfaz web básica para listar los hoteles registrados.

 Tecnologías utilizadas
- Java 17
- Spring Boot
- Spring Data MongoDB
- Lombok
- MongoDB
- Thymeleaf (para la vista web)


# Estructura del proyecto
```plaintext
hoteles-api/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── example/
│       │           └── hotelesapi/
│       │               ├── ApiController.java          # Controlador REST de la API
│       │               ├── WebController.java          # Controlador para la vista web
│       │               ├── Hotel.java                  # Modelo de datos para Hotel
│       │               ├── User.java                   # Modelo de datos para Usuario
│       │               ├── HotelRepository.java        # Repositorio MongoDB para Hoteles
│       │               ├── UserRepository.java         # Repositorio MongoDB para Usuarios
│       │               ├── SecurityService.java        # Servicio para validación de tokens
│       │               └── HotelesApiApplication.java  # Clase principal (Spring Boot)
│       └── resources/
│           ├── application.properties                  # Configuración del proyecto
│           └── templates/
│               └── index.html                          # Vista web para listado de hoteles
│
├── pom.xml                                              # Archivo Maven con dependencias
└── README.md                                            # Documentación del proyecto
```


# Accede a la API y vista web:

- API REST: http://localhost:8080/api/
- Vista web: http://localhost:8080/web/


# Gestión de Usuarios
User contiene los siguientes campos:
- user: Nombre de usuario
- email: Correo electrónico
- token: Token único para autenticación
  
Estos usuarios están almacenados en la colección users en MongoDB.

# Endpoints de la API


| **Método** | **Endpoint**                                              | **Descripción**                                                        | **Parámetros**                              | **Seguridad**    |
|------------|------------------------------------------------------------|------------------------------------------------------------------------|---------------------------------------------|------------------|
| GET        | `/api/hoteles`                                             | Obtiene la lista de todos los hoteles                                  | -                                           | No               |
| GET        | `/api/hoteles/id/{id}`                                     | Obtiene un hotel por su ID                                             | `id` (path)                                 | No               |
| GET        | `/api/hoteles/provincia/{provinces}`                       | Obtiene hoteles por provincia                                          | `provinces` (path)                          | No               |
| GET        | `/api/hoteles/estrellas`                                   | Obtiene todos los hoteles ordenados por estrellas                      | -                                           | No               |
| GET        | `/api/hoteles/estrellas/{estrellas}`                       | Obtiene hoteles con un número específico de estrellas                  | `estrellas` (path)                          | No               |
| GET        | `/api/hoteles/provincia/{provinces}/estrellas`             | Obtiene hoteles por provincia ordenados por estrellas                  | `provinces` (path)                          | No               |
| GET        | `/api/hoteles/provincia/{provinces}/estrellas/{estrellas}` | Obtiene hoteles por provincia y número de estrellas                    | `provinces`, `estrellas` (path)             | No               |
| GET        | `/api/hoteles/lujo`                                        | Obtiene todos los hoteles de lujo                                      | -                                           | No               |
| GET        | `/api/hoteles/provincia/{provinces}/lujo`                  | Obtiene hoteles de lujo en una provincia específica                    | `provinces` (path)                          | No               |
| GET        | `/api/hoteles/modalidad/{modalities}`                      | Obtiene hoteles por modalidad                                          | `modalities` (path)                         | No               |
| GET        | `/api/hoteles/modalidad/{modalities}/estrellas`            | Obtiene hoteles por modalidad ordenados por estrellas                  | `modalities` (path)                         | No               |
| GET        | `/api/hoteles/modalidad/{modalities}/estrellas/{estrellas}`| Obtiene hoteles por modalidad y número de estrellas                    | `modalities`, `estrellas` (path)            | No               |
| GET        | `/api/hoteles/provincia/{provinces}/modalidad/{modalities}`| Obtiene hoteles por provincia y modalidad                              | `provinces`, `modalities` (path)            | No               |
| GET        | `/api/hoteles/provincia/{provinces}/modalidad/{modalities}/estrellas` | Obtiene hoteles por provincia y modalidad ordenados por estrellas     | `provinces`, `modalities` (path)            | No               |
| GET        | `/api/hoteles/provincia/{provinces}/modalidad/{modalities}/lujo` | Obtiene hoteles de lujo por provincia y modalidad                    | `provinces`, `modalities` (path)            | No               |
| POST       | `/api/`                                                    | Crea un nuevo hotel                                                    | Cuerpo JSON con los datos del hotel         | No               |
| DELETE     | `/api/hoteles/{id}?token={token}`                          | Elimina un hotel por su ID (requiere token de seguridad)               | `id` (path), `token` (query param)          | **Sí** (Token)   |



# Seguridad
Para eliminar un hotel se requiere un token válido. El sistema verifica el token mediante el servicio SecurityService y el repositorio de usuarios.

