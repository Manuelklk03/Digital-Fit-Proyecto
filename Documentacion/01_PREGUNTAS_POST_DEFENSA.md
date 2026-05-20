# 01 - Preguntas Post-Defensa

Este es el documento principal para preparar el turno de preguntas. La defensa ya explica el proyecto por encima; aqui se responde lo que pueden preguntar para comprobar si entiendes codigo, seguridad, base de datos y decisiones tecnicas.

## Metodo para responder

Usa siempre esta estructura:

1. Idea en una frase.
2. Donde esta en el codigo.
3. Como funciona por dentro.
4. Mejora posible si procede.

Ejemplo:

> El login lo gestiona Spring Security con sesion. Esta configurado en `SecurityConfig.java`; el usuario se carga en `CustomUserDetailsService.java`; la contrasena se compara con BCrypt; Angular envia la cookie de sesion con `withCredentials`.

## Arquitectura

### Por que separas controller, service y repository?

Porque cada capa tiene una responsabilidad:

- `controller`: recibe peticiones HTTP y devuelve respuestas.
- `service`: contiene reglas de negocio y permisos.
- `repository`: accede a MySQL mediante JPA.
- `model`: define entidades y relaciones.
- `dto`: define datos de entrada/salida y validaciones.

Ubicacion:

- `Backend/digital-fit/src/main/java/com/example/digital_fit/controller`
- `Backend/digital-fit/src/main/java/com/example/digital_fit/service`
- `Backend/digital-fit/src/main/java/com/example/digital_fit/repository`
- `Backend/digital-fit/src/main/java/com/example/digital_fit/model`
- `Backend/digital-fit/src/main/java/com/example/digital_fit/dto`

### Que ocurre desde Angular hasta MySQL?

Flujo:

```text
Componente Angular
-> Service Angular con HttpClient
-> Endpoint REST de Spring Boot
-> Controller
-> Service
-> Repository
-> Entidad JPA
-> MySQL
```

Ejemplo: crear entrenamiento propio.

- Frontend: `services/entrenamientos/mis-entrenamientos-service.ts`
- Controller: `EntrenamientoUsuarioRestController.java`
- Service: `EntrenamientoUsuarioService.java`
- Repository: `EntrenamientoUsuarioRepository.java`
- Entidad: `EntrenamientoUsuario.java`

### Que es REST?

Una forma de organizar APIs con URLs y metodos HTTP:

- `GET`: leer.
- `POST`: crear.
- `PUT/PATCH`: actualizar.
- `DELETE`: eliminar.

En tu proyecto los endpoints estan bajo `/api/...`.

## Seguridad

### Es hackeable el frontend?

Respuesta corta:

Si, cualquier frontend es manipulable porque llega al navegador del usuario. Pueden abrir DevTools, cambiar botones, modificar formularios, llamar endpoints manualmente o intentar entrar en rutas ocultas.

Respuesta buena:

> El frontend no es una barrera de seguridad real. Los guards de Angular ayudan a la navegacion, pero la seguridad importante esta en backend: Spring Security, roles, validaciones DTO y comprobaciones de propiedad de recursos.

Ejemplos:

- Aunque alguien muestre una ruta admin en Angular, backend protege `/api/admin/**`.
- Aunque alguien quite una validacion del formulario, backend valida DTOs.
- Aunque alguien cambie un `id` en la URL, los services verifican que el recurso sea suyo.

Ubicacion:

- Backend: `config/SecurityConfig.java`
- Frontend: `Frontend/digital-fit-frontend/src/app/guards`

### Como funciona el login?

Spring Security procesa `POST /api/auth/login`. Busca el usuario con `CustomUserDetailsService`, compara la contrasena cifrada con BCrypt y, si es correcta, crea una sesion.

Ubicacion:

- `SecurityConfig.java`
- `CustomUserDetailsService.java`
- `AuthService.java`
- `AuthController.java`
- `auth-service.ts`

### Por que usas sesiones y no JWT?

Porque para una aplicacion web con frontend y backend coordinados, la sesion de Spring Security es suficiente y mas sencilla. JWT seria mas interesante para una API movil, microservicios o integraciones externas.

### Donde se cifra la contrasena?

En `AuthService`, al registrar usuario:

- Se llama a `passwordEncoder.encode(...)`.
- El encoder se define en `SecurityConfig.java` como `BCryptPasswordEncoder`.

### Como proteges admin?

Backend:

- `SecurityConfig.java`
- Regla: `/api/admin/**` requiere `hasRole("ADMIN")`.

Frontend:

- `admin-guard.ts`
- Consulta `/api/auth/me`.
- Comprueba `usuario.rol === 'ADMIN'`.

### Como evitas que un usuario vea datos de otro?

En los services se obtiene el usuario autenticado y se compara con el propietario del recurso. Si no coincide, se lanza `OperacionNoPermitida`.

Ejemplos:

- `HistorialEntrenamientosService.java`
- `EntrenamientoUsuarioService.java`
- `CentroPrivadoUsuarioService.java`
- `LugarPublicoUsuarioService.java`

Frase:

> No confio solo en que Angular oculte botones. El backend comprueba permisos antes de operar.

## Base de Datos

### Cual es la entidad central?

`Usuario`. Casi todo lo privado cuelga de usuario:

- Entrenamientos personales.
- Centros guardados.
- Lugares guardados.
- Historial.
- Valoraciones.
- Soporte.
- Mensajes de soporte.

### Por que hay tablas base y tablas usuario?

Porque separas catalogo general de datos personales.

- `*_base`: informacion comun gestionada por la app/admin.
- `*_usuario`: informacion guardada o creada por un usuario.

Ejemplo:

- `entrenamientos_base`: rutinas para todos.
- `entrenamientos_usuario`: rutinas propias o copiadas por un usuario.

### Por que hay 3 tipos de entrenamientos?

- `entrenamientos_base`: catalogo de la app.
- `entrenamientos_usuario`: entrenamientos personales.
- `entrenamientos_comunidad`: entrenamientos publicados por usuarios para otros.

Ventaja:

Cada tabla tiene una responsabilidad y permisos mas claros.

### Que es el historial?

Es la tabla que registra entrenamientos realizados. Es la fuente de verdad del seguimiento.

Contiene:

- Usuario.
- Entrenamiento base o entrenamiento usuario.
- Fecha.
- Duracion.
- Notas.
- Ubicacion opcional.

Ubicacion:

- `HistorialEntrenamientos.java`
- `HistorialEntrenamientosService.java`
- `HistorialEntrenamientosRepository.java`

### Por que historial tiene varias FK opcionales?

Porque un registro puede usar distintos origenes:

- Entrenamiento base o entrenamiento de usuario.
- Centro privado base o centro privado de usuario.
- Lugar publico base o lugar publico de usuario.

Regla importante:

El modelo permite flexibilidad, pero el service impone coherencia:

- Un unico entrenamiento.
- Cero o una ubicacion.
- Si el recurso es privado, debe pertenecer al usuario.

### Como funcionan las valoraciones?

`Valoracion` puede valorar varios tipos de contenido. Por eso usa:

- `tipo_valoracion`: que tipo de cosa se valora.
- `id_relacionado`: id del contenido valorado.

Esto es una relacion logica/polimorfica.

Ubicacion:

- `Valoracion.java`
- `ValoracionService.java`
- `TipoDeValoracion.java`

Punto debil reconocido:

No hay FK fisica directa a cada tabla posible; la integridad se comprueba en el service. A cambio, una sola tabla sirve para valorar muchos tipos de contenido.

### Como se calculan estadisticas?

Desde `historial_entrenamientos`.

Se calcula:

- Total de entrenamientos.
- Minutos entrenados.
- Promedio.
- Centros visitados.
- Lugares visitados.
- Entrenamiento mas realizado.

Ubicacion:

- `EstadisticasService.java`
- `EstadisticasRepository.java`

## Frontend Angular

### Donde estan las rutas?

En `Frontend/digital-fit-frontend/src/app/app.routes.ts`.

### Que son los guards?

Funciones que deciden si puedes entrar a una ruta:

- `authGuard`: usuario autenticado.
- `userGuard`: rol `USER`.
- `adminGuard`: rol `ADMIN`.
- `publicGuard`: evita login/register si ya estas logueado.

### Donde se llama al backend?

En `Frontend/digital-fit-frontend/src/app/services`.

Ejemplos:

- Login: `auth-service.ts`
- Centros: `services/centros`
- Lugares: `services/publicos`
- Entrenamientos: `services/entrenamientos`
- Soporte: `soporte.ts`

### Por que usas `withCredentials: true`?

Porque el backend usa sesion con cookie. Angular debe enviar esa cookie en cada peticion.

### Donde esta el mapa?

En `Frontend/digital-fit-frontend/src/app/components/mapa-selector`.

Usa Leaflet/OpenStreetMap.

## Validaciones y Errores

### Donde validas datos?

En DTOs con Bean Validation:

- `@NotBlank`
- `@NotNull`
- `@Email`
- `@Size`
- `@Min`
- `@Max`

Ubicacion:

- `Backend/digital-fit/src/main/java/com/example/digital_fit/dto`

### Como devuelves errores claros?

Con `GlobalExceptionHandler.java`.

Excepciones:

- `RecursoNoEncontradoException`: 404.
- `OperacionNoPermitida`: 403.
- `UsernameYaExiste`: 400.
- `EmailYaExisteException`: 400.
- Errores de validacion DTO: 400.

## Despliegue

### Como funciona Docker Compose?

`docker-compose.yml` levanta:

- `mysql`: base de datos.
- `backend`: Spring Boot.
- `frontend`: Angular servido por Nginx.

### Que hace Nginx?

Sirve Angular compilado y redirige `/api/` al backend.

Ubicacion:

- `Frontend/digital-fit-frontend/nginx.conf`

### Donde estan los datos iniciales?

En `Backend/digital-fit/src/main/resources/data.sql`.

Usa `INSERT IGNORE` para evitar duplicados porque se ejecuta siempre.

## Decisiones Tecnicas

### Por que MySQL?

Porque hay relaciones claras entre usuarios, entrenamientos, lugares, centros, historial, valoraciones y soporte. Una base relacional encaja mejor.

### Por que JPA?

Porque permite mapear clases Java a tablas y usar repositories para consultar sin escribir todo el SQL manualmente.

### Por que Angular?

Por su estructura modular, TypeScript, rutas, servicios y guards.

### Por que Leaflet/OpenStreetMap?

Porque es libre, ligero y suficiente para mapas interactivos sin depender de costes o limites de APIs comerciales.

## Puntos Debiles y Mejoras

Si preguntan que mejorarias:

- Mas tests automaticos.
- Swagger/OpenAPI para documentar endpoints.
- Flyway o Liquibase para migraciones de BD.
- Despliegue real en produccion.
- Estadisticas mas avanzadas.
- Recomendaciones personalizadas.

Si preguntan por IA:

> He usado IA como apoyo para acelerar, revisar y documentar, pero entiendo la arquitectura, las entidades, las relaciones, el flujo frontend-backend y puedo ubicar el codigo principal.

## Respuesta si te bloqueas

> No recuerdo el nombre exacto ahora mismo, pero por arquitectura estaria en el modulo correspondiente: controller para endpoint, service para logica, repository para BD y model para entidad. Lo buscaria en esa carpeta y podria seguir el flujo desde ahi.

## Preguntas Teoricas DAW Probables

### Que es una SPA?

Una SPA, Single Page Application, es una aplicacion web donde se carga una pagina principal y despues Angular cambia las vistas sin recargar toda la web. En mi proyecto Angular gestiona las rutas en `app.routes.ts`.

### Que diferencia hay entre frontend y backend?

Frontend es la parte visual que usa el usuario: Angular, componentes, rutas y servicios HTTP. Backend es la API: Spring Boot, seguridad, logica de negocio, entidades y acceso a MySQL.

### Que es MVC o arquitectura por capas?

Es una forma de separar responsabilidades. En mi proyecto no es MVC clasico de vistas del servidor, porque la vista esta en Angular, pero si hay capas claras:

- Controller: entrada HTTP.
- Service: logica.
- Repository: datos.
- Model: entidades.

### Que es un DTO?

Un DTO es un objeto para transportar datos entre frontend y backend. Sirve para no exponer directamente entidades y para validar datos de entrada. Ejemplo: `UsuarioDTO`, `CrearValoracionDTO`, `CrearEntrenamientoUsuario`.

### Que es ORM?

ORM significa Object Relational Mapping. JPA/Hibernate convierte clases Java en tablas y objetos Java en registros de base de datos. Por ejemplo, `Usuario.java` se corresponde con `usuarios`.

### Que es JPA?

JPA es la especificacion de Java para persistencia. En el proyecto se usa con Spring Data JPA, que permite crear repositories y consultar entidades sin escribir todo el SQL manual.

### Que es una clave primaria?

Es el identificador unico de una fila. En las entidades suele ser `id` con `@Id` y `@GeneratedValue`.

### Que es una clave foranea?

Es un campo que apunta a la clave primaria de otra tabla. Ejemplo: `usuario_id` en `entrenamientos_usuario` apunta a `usuarios.id`.

### Que es una relacion 1:N?

Un registro de una tabla puede tener muchos registros asociados en otra. Ejemplo: un usuario puede tener muchos entrenamientos personales.

### Que es `@ManyToOne`?

En JPA significa que muchos registros de esta entidad apuntan a uno de otra entidad. Ejemplo: muchos `EntrenamientoUsuario` pertenecen a un `Usuario`.

### Que es `@OneToMany`?

Significa que un registro tiene una lista de otros registros. Ejemplo: `Soporte` tiene una lista de `MensajeSoporte`.

### Que es CRUD?

CRUD son las operaciones basicas: crear, leer, actualizar y eliminar. En el proyecto aparece en entrenamientos, lugares, centros, soporte y admin.

### Que diferencia hay entre PUT y PATCH?

`PUT` suele actualizar un recurso completo. `PATCH` actualiza solo una parte. En soporte admin se usa `PATCH` para cambiar el estado de un ticket.

### Que es CORS?

CORS controla si una web puede llamar a una API desde otro origen. En local Angular puede estar en `localhost:4200` y Spring Boot en otro puerto, por eso se configura `CorsConfig.java`.

## Seguridad Web Normal que Pueden Preguntar

### Puede alguien saltarse un guard de Angular?

Si. Un guard solo protege la navegacion del frontend. Por eso el backend tambien protege endpoints. Si alguien llama manualmente a `/api/admin/...`, Spring Security comprueba el rol.

### Que es XSS?

Cross-Site Scripting: inyectar JavaScript malicioso en una pagina. Angular reduce el riesgo porque escapa contenido por defecto, pero igualmente hay que evitar pintar HTML no confiable.

### Que es CSRF?

Cross-Site Request Forgery: hacer que un usuario autenticado envie una peticion sin querer. En el proyecto CSRF esta desactivado para facilitar la API con Angular, pero en produccion habria que revisarlo y proteger mejor operaciones sensibles.

### Puede haber inyeccion SQL?

El riesgo baja porque se usa JPA y repositories, no concatenacion manual de SQL con strings de usuario. Aun asi, las entradas se validan y en consultas nativas se deben usar parametros.

### Que pasa si alguien cambia el id en la URL?

El backend debe comprobar propiedad del recurso. Por ejemplo, si intentas ver un historial que no es tuyo, el service compara el usuario del historial con el usuario autenticado y lanza `OperacionNoPermitida`.

### Que informacion sensible no deberia estar en frontend?

Passwords, secretos, claves privadas o reglas de seguridad reales. El frontend puede tener rutas y llamadas, pero las decisiones de permiso deben estar en backend.

## Preguntas Sobre Codigo Con Respuesta Corta

### Donde esta el endpoint de registro?

En `AuthController.java`, metodo `registrar`, bajo `/api/auth/registro`.

### Donde esta el login?

No esta como metodo normal del controller. Lo procesa Spring Security con `loginProcessingUrl("/api/auth/login")` en `SecurityConfig.java`.

### Donde se obtiene el usuario actual?

En controllers se usa `Authentication authentication`. Con `authentication.getName()` se obtiene el username.

### Donde se crean admins?

En `AdminRestController.java`, endpoint `/api/admin/crear-admin`, y la logica en `AuthService.registrarAdmin`.

### Donde se controlan errores?

En `GlobalExceptionHandler.java`.

### Donde se cargan datos iniciales?

En `data.sql`.

### Donde se definen enums?

En `model/Enums`: roles, estados de soporte, tipos de valoracion, categorias, niveles y tipos de lugar publico.

### Donde esta la configuracion de MySQL?

En `application.properties` y tambien en `docker-compose.yml` para Docker.

## Calidad y Mejoras

### Que pruebas has hecho?

He hecho pruebas manuales de los flujos principales: registro, login, navegacion, CRUD, mapa, historial, valoraciones, estadisticas, soporte y admin. Hay archivos `.spec.ts` en Angular y una prueba base en Spring Boot, aunque una mejora seria ampliar tests automaticos.

### Que mejorarias antes de produccion?

- Activar y configurar CSRF correctamente si se mantiene sesion por cookies.
- Usar variables de entorno para secretos.
- Usar Flyway/Liquibase para migraciones.
- Anadir Swagger/OpenAPI.
- Anadir tests de integracion.
- Revisar logs y errores.
- Desplegar con HTTPS.

### Que parte tiene mas complejidad?

Historial y valoraciones. Historial conecta usuario, entrenamiento y ubicacion con varias opciones. Valoraciones puede apuntar a muchos tipos de contenido mediante `tipo_valoracion + id_relacionado`.

### Como sabes que tu BD tiene sentido?

Porque cada tabla representa una responsabilidad clara. No guardo listas dentro de usuarios; uso relaciones. Distingo catalogo base de datos personales. Historial guarda hechos reales y estadisticas se calculan desde ahi.
