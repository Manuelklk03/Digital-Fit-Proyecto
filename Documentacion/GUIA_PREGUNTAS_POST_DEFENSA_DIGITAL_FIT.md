# Guia de preguntas post-defensa - Digital Fit

Esta guia esta pensada para el turno posterior a tu presentacion. La defensa ya explica origen, valor diferencial, stack, arquitectura, funcionalidades, seguridad, demo y conclusion. Aqui se prepara lo que pueden preguntarte para comprobar que entiendes el proyecto por dentro.

## 1. Como enfocar las respuestas

Cuando te pregunten, responde con esta estructura:

1. Explica la idea en una frase.
2. Di donde esta en el codigo.
3. Explica el flujo o la relacion.
4. Reconoce una mejora si procede.

Ejemplo:

"El login lo gestiona Spring Security con sesion. Esta configurado en `SecurityConfig.java`; el usuario se busca en `CustomUserDetailsService.java` y la password se compara con BCrypt. En Angular, `auth-service.ts` llama a `/api/auth/login` y las peticiones usan `withCredentials` para enviar la cookie de sesion."

## 2. Preguntas sobre arquitectura interna

### Por que has separado controller, service y repository?

Respuesta:

Porque cada capa tiene una responsabilidad. El controller recibe la peticion HTTP, el service contiene la logica de negocio y el repository accede a la base de datos. Asi el codigo queda mas mantenible y es mas facil ubicar errores.

Ubicacion:

- Controllers: `Backend/digital-fit/src/main/java/com/example/digital_fit/controller`
- Services: `Backend/digital-fit/src/main/java/com/example/digital_fit/service`
- Repositories: `Backend/digital-fit/src/main/java/com/example/digital_fit/repository`

### Que pasa desde que haces click en Angular hasta que se guarda algo en BD?

Respuesta:

Angular ejecuta una funcion del componente, llama a un service con `HttpClient`, el backend recibe la peticion en un controller, el controller delega en un service, el service valida datos y permisos, el repository guarda o consulta con JPA y MySQL devuelve el resultado.

Ejemplo para "guardar entrenamiento":

- Frontend: `services/entrenamientos/mis-entrenamientos-service.ts`
- Backend controller: `EntrenamientoUsuarioRestController.java`
- Backend service: `EntrenamientoUsuarioService.java`
- Repository: `EntrenamientoUsuarioRepository.java`
- Entidad: `EntrenamientoUsuario.java`

### Donde esta la configuracion principal del backend?

Respuesta:

- Arranque: `DigitalFitApplication.java`
- Dependencias: `pom.xml`
- Base de datos y logs: `application.properties`
- Seguridad: `SecurityConfig.java`
- CORS: `CorsConfig.java`

## 3. Preguntas sobre seguridad

### Como se autentica un usuario?

Respuesta:

El login lo procesa Spring Security en `/api/auth/login`. El usuario se carga con `CustomUserDetailsService`, que busca por username o email. La password se compara cifrada con BCrypt. Si es correcto, Spring crea una sesion.

Ubicacion:

- `config/SecurityConfig.java`
- `service/Auth/CustomUserDetailsService.java`
- `service/Auth/AuthService.java`
- `controller/Auth/AuthController.java`

### Donde se cifra la contrasena?

Respuesta:

En `AuthService`, cuando se registra un usuario, se llama a `passwordEncoder.encode(...)`. El encoder se define en `SecurityConfig.java` como `BCryptPasswordEncoder`.

### Por que usas sesiones y no JWT?

Respuesta:

Para este proyecto, al ser una aplicacion web con frontend y backend coordinados, la sesion de Spring Security es suficiente. Simplifica el login y permite proteger endpoints por rol. JWT seria util si quisiera una API mas desacoplada, mobile o microservicios.

### Como sabes si un usuario es admin?

Respuesta:

El rol se guarda en la entidad `Usuario` mediante el enum `Rol`. En backend, `/api/admin/**` se protege con `hasRole("ADMIN")`. En frontend, `admin-guard.ts` consulta `/api/auth/me` y comprueba que el rol sea `ADMIN`.

Ubicacion:

- `model/Auth/Usuario.java`
- `model/Enums/Rol.java`
- `config/SecurityConfig.java`
- `Frontend/digital-fit-frontend/src/app/guards/admin-guard.ts`

### Como evitas que un usuario acceda a datos de otro?

Respuesta:

Ademas de estar autenticado, en los services se comprueba que el recurso pertenezca al usuario. Se obtiene el usuario por el username de la sesion y se compara el `id` con el propietario del recurso. Si no coincide, se lanza `OperacionNoPermitida`.

Ejemplos:

- `HistorialEntrenamientosService.java`
- `EntrenamientoUsuarioService.java`
- `CentroPrivadoUsuarioService.java`
- `LugarPublicoUsuarioService.java`

## 4. Preguntas sobre base de datos y relaciones

### Cual es la entidad central?

Respuesta:

`Usuario`. Casi todo lo privado depende de un usuario: entrenamientos propios, centros guardados, lugares guardados, historial, valoraciones, soporte y mensajes.

### Que diferencia hay entre tablas base y tablas usuario?

Respuesta:

Las tablas base son catalogos comunes para todos. Las tablas usuario son datos personalizados asociados a una cuenta. Por ejemplo, `entrenamientos_base` es contenido general, mientras `entrenamientos_usuario` pertenece a un usuario concreto.

### Por que no usas una sola tabla para entrenamientos?

Respuesta:

Porque no todos los entrenamientos tienen la misma finalidad. Los base son catalogo general, los de usuario son privados o copiados, y los de comunidad son publicaciones de usuarios. Separarlo hace mas claros los permisos y el uso de cada dato.

Tablas:

- `entrenamientos_base`
- `entrenamientos_usuario`
- `entrenamientos_comunidad`

### Como explicas el historial?

Respuesta:

El historial es la tabla que registra entrenamientos realizados. Une usuario, entrenamiento, fecha, duracion, notas y opcionalmente una ubicacion. Es la fuente de las estadisticas.

Ubicacion:

- Entidad: `model/Entrenamientos/HistorialEntrenamientos.java`
- Service: `service/Entrenamientos/HistorialEntrenamientosService.java`
- Repository: `repository/Entrenamientos/HistorialEntrenamientosRepository.java`

### Por que historial tiene varias claves foraneas opcionales?

Respuesta:

Porque un registro puede referirse a distintos origenes: un entrenamiento base o uno del usuario, y una ubicacion que puede ser centro/lugar, base/usuario. La entidad permite esas opciones, pero el service controla que se elija solo una opcion valida por grupo.

Reglas importantes:

- Debe haber un entrenamiento: base o usuario.
- No pueden ir los dos a la vez.
- Puede haber cero o una ubicacion.
- No puede haber varias ubicaciones a la vez.
- Si el recurso es privado, debe pertenecer al usuario autenticado.

### Como funcionan las valoraciones si pueden valorar varios tipos de contenido?

Respuesta:

`Valoracion` usa una relacion logica o polimorfica. Tiene `tipo_valoracion` para indicar que se esta valorando y `id_relacionado` para guardar el id del contenido. El service valida que ese contenido exista.

Ubicacion:

- Entidad: `model/Valoracion/Valoracion.java`
- Service: `service/Valoracion/ValoracionService.java`
- Enum: `model/Enums/TipoDeValoracion.java`

### Que pega tiene esa relacion de valoraciones?

Respuesta:

No hay una clave foranea fisica directa hacia todas las tablas posibles. La ventaja es flexibilidad; la desventaja es que la integridad depende de la logica del service. Para un proyecto de este tamaño es aceptable, pero en un sistema mayor se podria valorar otra estrategia.

### Como se calculan las estadisticas?

Respuesta:

Se calculan desde `historial_entrenamientos`, porque ahi estan los entrenamientos realmente realizados. `EstadisticasRepository` usa consultas JPQL y SQL nativo para contar entrenamientos, sumar minutos, calcular promedio y obtener ubicaciones/entrenamiento mas usado.

Ubicacion:

- `service/Estadisticas/EstadisticasService.java`
- `repository/Estadisticas/EstadisticasRepository.java`

## 5. Preguntas sobre frontend Angular

### Donde estan las rutas?

Respuesta:

En `Frontend/digital-fit-frontend/src/app/app.routes.ts`. Ahi se definen las paginas y sus guards.

### Que son los guards?

Respuesta:

Son funciones que protegen rutas antes de entrar. Por ejemplo, `authGuard` comprueba que haya sesion, `userGuard` que el rol sea `USER` y `adminGuard` que el rol sea `ADMIN`.

Ubicacion:

- `Frontend/digital-fit-frontend/src/app/guards`

### Donde se hacen las llamadas al backend?

Respuesta:

En los servicios Angular dentro de `src/app/services`. Cada servicio tiene un `apiUrl` y usa `HttpClient`.

Ejemplos:

- Login: `services/auth-service.ts`
- Centros: `services/centros`
- Lugares: `services/publicos`
- Entrenamientos: `services/entrenamientos`
- Soporte: `services/soporte.ts`

### Por que usas `withCredentials: true`?

Respuesta:

Porque el backend usa sesion con cookie. Para que el navegador envie esa cookie en las peticiones al backend, Angular debe usar `withCredentials: true`.

### Donde esta el mapa?

Respuesta:

En el componente `Frontend/digital-fit-frontend/src/app/components/mapa-selector`. Usa Leaflet/OpenStreetMap para seleccionar o visualizar ubicaciones.

## 6. Preguntas sobre validaciones y errores

### Donde validas los datos?

Respuesta:

En backend uso DTOs con Bean Validation. Por ejemplo, `@NotBlank`, `@NotNull`, `@Email`, `@Size`, `@Min` y `@Max`.

Ubicacion:

- `Backend/digital-fit/src/main/java/com/example/digital_fit/dto`

### Como devuelves errores claros?

Respuesta:

Con un manejador global: `GlobalExceptionHandler.java`. Captura excepciones propias como `RecursoNoEncontradoException`, `OperacionNoPermitida`, `UsernameYaExiste` o errores de validacion y devuelve codigos HTTP adecuados.

Ubicacion:

- `Backend/digital-fit/src/main/java/com/example/digital_fit/handler/GlobalExceptionHandler.java`

### Que codigos HTTP usarias?

Respuesta:

- `200 OK`: lectura o actualizacion correcta.
- `201 Created`: recurso creado.
- `400 Bad Request`: datos invalidos.
- `401 Unauthorized`: no autenticado.
- `403 Forbidden`: autenticado pero sin permiso.
- `404 Not Found`: recurso no existe.

## 7. Preguntas sobre despliegue

### Como se ejecuta con Docker?

Respuesta:

`docker-compose.yml` define tres servicios: MySQL, backend y frontend. MySQL guarda la base de datos, backend se conecta a MySQL usando variables de entorno y frontend se sirve con Nginx.

Ubicacion:

- `docker-compose.yml`
- `Backend/digital-fit/Dockerfile`
- `Frontend/digital-fit-frontend/Dockerfile`
- `Frontend/digital-fit-frontend/nginx.conf`

### Que hace Nginx?

Respuesta:

Sirve la aplicacion Angular compilada y redirige las peticiones `/api/` al backend. Asi el frontend puede llamar a `/api/...` sin escribir directamente la URL del backend.

### Donde se inicializan datos de prueba?

Respuesta:

En `Backend/digital-fit/src/main/resources/data.sql`. Se insertan entrenamientos base, centros, lugares y usuarios iniciales.

### Por que `INSERT IGNORE`?

Respuesta:

Porque `data.sql` se ejecuta siempre. `INSERT IGNORE` evita errores por duplicados si esos datos ya existen.

## 8. Preguntas sobre decisiones tecnicas

### Por que MySQL?

Respuesta:

Porque el modelo tiene relaciones claras entre usuarios, entrenamientos, centros, lugares, historial, valoraciones y soporte. Una base relacional encaja mejor que una no relacional para este caso.

### Por que JPA?

Respuesta:

Porque permite mapear entidades Java a tablas y trabajar con repositories, reduciendo SQL repetitivo y manteniendo el codigo orientado a objetos.

### Por que Leaflet/OpenStreetMap?

Respuesta:

Porque es una solucion libre, ligera y suficiente para mapas interactivos, sin depender de costes o limites de APIs como Google Maps.

### Por que Angular?

Respuesta:

Porque ofrece estructura modular, TypeScript, rutas, servicios y guards. Para una SPA con muchas secciones ayuda a mantener orden.

## 9. Preguntas sobre puntos debiles o mejoras

### Que mejorarias del proyecto?

Respuesta recomendable:

Anadiria mas tests automaticos, documentaria la API con Swagger/OpenAPI, usaria migraciones con Flyway o Liquibase, prepararia un despliegue real en produccion y mejoraria estadisticas/recomendaciones.

### Que limitacion tecnica reconoces?

Respuesta:

La relacion de valoraciones es flexible, pero no tiene FK fisica hacia todos los contenidos posibles. La integridad se controla desde el service. Tambien el proyecto podria mejorar con migraciones versionadas de base de datos.

### Que parte fue mas compleja?

Respuesta:

La parte mas compleja fue coordinar permisos, relaciones y flujo entre frontend y backend, especialmente en historial, valoraciones, mapa y roles, porque no basta con mostrar datos: hay que validar que cada recurso pertenece al usuario correcto.

### Como justificas el uso de IA?

Respuesta:

La IA ha sido una herramienta de apoyo para acelerar dudas, revisar codigo o documentar, pero entiendo el flujo de la aplicacion, las relaciones de base de datos, la arquitectura por capas y puedo ubicar las partes principales del proyecto.

## 10. Mini chuleta de ubicacion rapida

| Tema | Archivo o carpeta |
|---|---|
| Login y seguridad | `SecurityConfig.java`, `AuthService.java`, `CustomUserDetailsService.java` |
| Usuario y roles | `Usuario.java`, `Rol.java` |
| CORS | `CorsConfig.java` |
| Entidades BD | `model` |
| Relaciones BD | Anotaciones `@ManyToOne`, `@OneToMany`, `@JoinColumn` |
| API REST | `controller` |
| Logica | `service` |
| Consultas | `repository` |
| Validaciones | `dto` |
| Errores | `GlobalExceptionHandler.java` |
| Rutas frontend | `app.routes.ts` |
| Proteccion frontend | `guards` |
| Llamadas HTTP | `services` |
| Mapa | `components/mapa-selector` |
| Estadisticas | `EstadisticasService.java`, `EstadisticasRepository.java` |
| Historial | `HistorialEntrenamientos.java`, `HistorialEntrenamientosService.java` |
| Valoraciones | `Valoracion.java`, `ValoracionService.java` |
| Soporte | `Soporte.java`, `MensajeSoporte.java`, `SoporteService.java` |
| Docker | `docker-compose.yml` |

## 11. Respuesta de emergencia si te bloqueas

Si no recuerdas el nombre exacto de un archivo, responde por capas:

"No recuerdo ahora mismo el nombre exacto de la clase, pero esta dentro del modulo correspondiente en backend. La peticion entra por un controller, pasa al service donde se valida el usuario y despues usa el repository para acceder a la entidad JPA. En este caso buscaria dentro de `controller/Entrenamientos`, `service/Entrenamientos` y `repository/Entrenamientos`."

Eso demuestra que entiendes la arquitectura aunque no recuerdes cada nombre literal.

