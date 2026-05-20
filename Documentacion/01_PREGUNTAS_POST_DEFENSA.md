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

