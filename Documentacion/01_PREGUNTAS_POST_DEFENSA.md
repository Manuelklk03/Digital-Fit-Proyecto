# 01 - Preguntas Post-Defensa

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

### Donde se envia el token en el frontend?

Respuesta importante:

> En este proyecto no se envia un token JWT ni un `Authorization: Bearer ...` desde Angular. La autenticacion se mantiene con sesion de Spring Security. El backend crea la cookie de sesion al hacer login y Angular la incluye en las peticiones usando `withCredentials: true`.

Si te piden abrir el fichero:

- Frontend login: `Frontend/digital-fit-frontend/src/app/services/auth-service.ts`
- Lineas clave:
  - `login(...)`: construye usuario/password y hace `POST /api/auth/login`.
  - `withCredentials: true`: aparece en login, registro, `/yo`, `/me` y logout.
- Backend login: `Backend/digital-fit/src/main/java/com/example/digital_fit/config/SecurityConfig.java`
- Linea clave: `loginProcessingUrl("/api/auth/login")`.
- CORS con cookies: `Backend/digital-fit/src/main/java/com/example/digital_fit/config/CorsConfig.java`
- Linea clave: `configuration.setAllowCredentials(true)`.

Frase para decir:

> Si el tribunal me pregunta por el token, lo primero es aclarar que no uso JWT. Uso cookie de sesion. Por eso no hay `localStorage`, `sessionStorage` ni cabecera `Authorization`; lo que hay es `withCredentials: true` en los services de Angular.

Como buscarlo rapido:

```text
Buscar en VS Code:
withCredentials
Authorization
Bearer
localStorage
```

En este proyecto `withCredentials` aparece en los services HTTP, pero `Authorization/Bearer/localStorage` no son el mecanismo de autenticacion.

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

Las estadisticas se calculan desde `historial_entrenamientos`, porque esa tabla guarda lo que el usuario ha hecho realmente: entrenamiento, fecha, duracion y ubicacion opcional.

Flujo:

```text
estadisticas page
-> estadisticas-service.ts
-> GET /api/estadisticas
-> EstadisticasRestController.obtenerEstadisticas
-> EstadisticasService.obtenerEstadisticas
-> EstadisticasRepository
-> historial_entrenamientos
```

El controller recibe la peticion y usa `Authentication authentication` para saber que usuario esta logueado:

```text
authentication.getName()
```

Despues `EstadisticasService` busca el usuario en `UsuarioRepository` y llama a varios metodos del repository. Cada metodo calcula una parte concreta.

Se calcula:

- Total de entrenamientos: hace un `COUNT` de los registros del historial de ese usuario.
- Minutos entrenados: hace un `SUM` de `duracionMinutos`.
- Promedio: hace un `AVG` de `duracionMinutos`.
- Centros visitados: cuenta centros privados distintos usados en el historial.
- Lugares visitados: cuenta lugares publicos distintos usados en el historial.
- Entrenamiento mas realizado: agrupa entrenamientos por nombre, cuenta repeticiones y se queda con el mayor.

Ejemplo de idea:

```text
Si un usuario tiene 5 filas en historial_entrenamientos:
-> total entrenamientos = 5
-> minutos entrenados = suma de las duraciones
-> promedio = suma / cantidad
```

Para los datos simples se usa JPQL:

```text
COUNT(h)
SUM(h.duracionMinutos)
AVG(h.duracionMinutos)
```

Para centros y lugares se usa SQL nativo porque hay dos posibles origenes:

```text
centro_privado_base_id
centro_privado_usuario_id

lugar_publico_base_id
lugar_publico_usuario_id
```

Por eso se cuentan valores distintos con `COUNT(DISTINCT ...)` y se suman los de base + usuario. Tambien se usa `COALESCE` para devolver 0 si no hay resultados.

Para el entrenamiento mas realizado se combinan entrenamientos base y entrenamientos del usuario:

```text
entrenamientos_base
UNION ALL
entrenamientos_usuario
GROUP BY nombre
ORDER BY total DESC
LIMIT 1
```

Al final el service mete todos los resultados en `EstadisticaUsuarioDTO`. Si algun resultado viene `null`, por ejemplo porque el usuario todavia no tiene historial, se cambia por un valor por defecto:

```text
0 entrenamientos
0 minutos
0.0 de promedio
"N/A" como entrenamiento mas realizado
```

Frase:

> Las estadisticas no se guardan duplicadas en otra tabla. Se calculan en tiempo real a partir del historial mediante consultas agregadas: contar, sumar, promediar, contar ubicaciones distintas y buscar el entrenamiento mas repetido.

Ubicacion:

- `EstadisticasRestController.java`
- `EstadisticasService.java`
- `EstadisticasRepository.java`
- `EstadisticaUsuarioDTO.java`

## Frontend Angular

### Como arranca Angular?

El arranque esta en `main.ts`.

Flujo:

```text
main.ts
-> bootstrapApplication(App, appConfig)
-> app.config.ts registra rutas y HttpClient
-> App usa <router-outlet>
-> app.routes.ts decide que pantalla se carga
```

Archivos:

- `Frontend/digital-fit-frontend/src/main.ts`
- `Frontend/digital-fit-frontend/src/app/app.config.ts`
- `Frontend/digital-fit-frontend/src/app/app.ts`
- `Frontend/digital-fit-frontend/src/app/app.html`
- `Frontend/digital-fit-frontend/src/app/app.routes.ts`

Frase para decir:

> Angular arranca en `main.ts`, carga el componente raiz `App` y usa `app.config.ts` para registrar rutas y HttpClient. El `router-outlet` es el hueco donde Angular pinta la pantalla correspondiente a la URL.

### Que es `router-outlet`?

Es el punto donde Angular renderiza el componente de la ruta activa.

En tu proyecto:

```html
<router-outlet></router-outlet>
```

Esta en:

- `Frontend/digital-fit-frontend/src/app/app.html`

Ejemplo:

```text
/login
-> app.routes.ts encuentra LoginComponent
-> LoginComponent se pinta dentro de router-outlet
```

### Donde estan las rutas?

En `Frontend/digital-fit-frontend/src/app/app.routes.ts`.

### Como se unen los componentes en el frontend?

En Angular la aplicacion se une por rutas, paginas, servicios y componentes reutilizables.

Flujo mental:

```text
app.config.ts
-> registra app.routes.ts
-> app.routes.ts decide que page se carga
-> page.ts contiene la logica
-> page.html pinta la vista
-> page.css da estilos
-> service.ts llama al backend
-> components reutilizables se insertan dentro de pages
```

Ejemplo:

```text
/publicos
-> app.routes.ts carga PublicosComponent
-> publicos.ts pide datos al service
-> publicos-service.ts hace GET /api/lugares-publicos
-> publicos.html muestra la lista
-> mapa-selector se usa como componente reutilizable si hace falta mapa
```

Ubicaciones:

- Rutas: `Frontend/digital-fit-frontend/src/app/app.routes.ts`
- Registro de rutas: `Frontend/digital-fit-frontend/src/app/app.config.ts`
- Paginas: `Frontend/digital-fit-frontend/src/app/pages`
- Servicios HTTP: `Frontend/digital-fit-frontend/src/app/services`
- Componentes reutilizables: `Frontend/digital-fit-frontend/src/app/components`

Frase para decir:

> En Angular no tengo una pagina HTML independiente por cada URL. `app.routes.ts` decide que componente se renderiza, el componente `.ts` contiene la logica, el `.html` pinta la vista y los services separan las llamadas HTTP al backend.

### Que son los standalone components e imports?

El proyecto usa el estilo moderno de Angular, donde los componentes declaran directamente lo que necesitan en `imports`.

Ejemplo:

```ts
@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink, HeaderComponent, Footer],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
```

Significa:

- `FormsModule`: permite usar formularios con `ngModel`.
- `RouterLink`: permite navegar desde el HTML.
- `HeaderComponent` y `Footer`: se pueden usar dentro del HTML como `<app-header>` y `<app-footer>`.

Frase:

> En vez de declarar todo en un modulo grande, cada componente importa las piezas que necesita. Eso hace mas claro que depende de que.

### Como funcionan los formularios en Angular?

En los formularios se usa `FormsModule` y `ngModel`.

Ejemplo en login:

```html
<form (ngSubmit)="iniciarSesion()">
<input [(ngModel)]="username" name="usuario">
```

Esto significa:

- `[(ngModel)]="username"` sincroniza el input con la variable `username` del `.ts`.
- `(ngSubmit)="iniciarSesion()"` llama al metodo cuando se envia el formulario.
- `[disabled]="cargando"` bloquea el boton mientras se esta procesando.

Flujo:

```text
login.html
-> usuario escribe en inputs
-> ngModel actualiza username/password
-> ngSubmit llama a iniciarSesion()
-> login.ts llama a AuthService.login()
-> backend procesa /api/auth/login
```

Frase:

> El HTML recoge datos con `ngModel`, el TypeScript ejecuta la logica y el service separa la llamada HTTP al backend.

### Como se crean las rutas en el frontend?

En Angular se importan los componentes de cada pantalla y despues se crea un array `routes: Routes`.

Ubicacion exacta:

- `Frontend/digital-fit-frontend/src/app/app.routes.ts`
- `Frontend/digital-fit-frontend/src/app/app.config.ts`

Como funciona:

1. En `app.routes.ts` se importan componentes y guards.
2. Se declara `export const routes: Routes = [...]`.
3. Cada objeto tiene:
   - `path`: URL del navegador.
   - `component`: pantalla que se carga.
   - `canActivate`: guard que decide si puede entrar.
4. En `app.config.ts` se registra con `provideRouter(routes)`.
5. En las plantillas se navega con `routerLink`, por ejemplo en `components/header/header.html` o `pages/inicio/inicio.html`.

Ejemplo:

```ts
{ path: 'admin/soporte', component: AdminSoporteComponent, canActivate: [adminGuard] }
```

Frase para decir:

> Las rutas no se crean en HTML directamente. Se declaran en `app.routes.ts` como objetos de Angular Router. Luego `app.config.ts` las registra con `provideRouter(routes)` y desde las vistas se navega con `routerLink` o desde TypeScript con `router.navigate(...)`.

Si te piden una ruta con parametro:

```ts
{ path: 'mis-entrenamientos/:id', component: DetalleMiEntrenamientoComponent, canActivate: [userGuard] }
```

El `:id` indica que esa parte de la URL es dinamica.

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

### Como funcionan los services de Angular?

Los services concentran las llamadas HTTP para que los componentes no tengan URLs y logica de API mezcladas con la vista.

Ejemplo:

```text
login.ts
-> AuthService.login(username, password)
-> HttpClient.post('/api/auth/login', ...)
```

Frase:

> El componente gestiona la pantalla y el service gestiona la comunicacion HTTP.

### Por que no hay interceptor HTTP?

Porque no se esta usando JWT ni una cabecera `Authorization` comun que haya que anadir automaticamente.

En este proyecto cada service manda:

```ts
withCredentials: true
```

Eso hace que el navegador envie la cookie de sesion de Spring Security.

Frase:

> Si usara JWT, tendria sentido un interceptor para anadir el token a cada peticion. Como uso cookie de sesion, lo importante es `withCredentials: true`.

### Como se guarda el usuario actual en frontend?

En `AuthService` hay un `BehaviorSubject`.

```ts
private usuarioSubject = new BehaviorSubject<any | null>(null);
usuario$ = this.usuarioSubject.asObservable();
```

Cuando se llama a `/api/auth/me`, el service actualiza ese estado:

```ts
tap(usuario => this.usuarioSubject.next(usuario))
```

El `HeaderComponent` se suscribe a `usuario$` para mostrar opciones segun el rol.

Flujo:

```text
login correcto
-> AuthService.me()
-> usuarioSubject.next(usuario)
-> HeaderComponent recibe usuario
-> muestra menu USER o ADMIN
```

Frase:

> El estado de sesion visible en frontend se comparte con un `BehaviorSubject`, pero la sesion real sigue estando en backend con la cookie de Spring Security.

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

Si preguntan por IA:

> He usado IA como apoyo para acelerar, revisar y documentar, pero entiendo la arquitectura, las entidades, las relaciones, el flujo frontend-backend y puedo ubicar el codigo principal.

## Preguntas Teoricas y de Codigo Sobre la App

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

### Como se conecta una pantalla del frontend con el backend?

Flujo:

```text
page.html
-> page.ts
-> service.ts
-> endpoint /api/...
-> controller
-> service backend
-> repository
-> MySQL
```

Ejemplo con estadisticas:

```text
estadisticas.html
-> estadisticas.ts
-> estadisticas-service.ts
-> GET /api/estadisticas
-> EstadisticasRestController
-> EstadisticasService
-> EstadisticasRepository
-> historial_entrenamientos
```

Frase:

> Las paginas de Angular no acceden directamente a la base de datos. Siempre pasan por un service HTTP, y el backend decide que datos devuelve.

### Como se conecta una entidad Java con una tabla?

Con JPA/Hibernate.

```text
Clase Java con @Entity
-> campos de la clase
-> columnas de la tabla
-> relaciones con @ManyToOne, @OneToMany y @JoinColumn
```

Ejemplo:

```text
Usuario.java -> tabla usuarios
EntrenamientoUsuario.java -> tabla entrenamientos_usuario
@JoinColumn(name = "usuario_id") -> FK hacia usuarios.id
```

Frase:

> Las entidades del paquete `model` representan tablas. Los repositories trabajan con esas entidades y JPA se encarga de traducirlo a consultas contra MySQL.

### Como funcionan las estadisticas en el codigo?

Las estadisticas son un resumen calculado del historial del usuario. No son datos que el usuario rellene directamente, sino datos derivados de los entrenamientos que ya ha registrado.

Idea principal:

```text
El usuario registra entrenamientos en historial
-> historial_entrenamientos guarda los hechos
-> estadisticas consulta ese historial
-> se devuelve un DTO con totales y resumenes
```

Por eso la tabla importante es `historial_entrenamientos`. Estadisticas no necesita una tabla propia con datos duplicados, porque puede calcularlos desde el historial.

Flujo:

```text
estadisticas.html
-> estadisticas.ts
-> estadisticas-service.ts
GET /api/estadisticas
-> EstadisticasRestController.obtenerEstadisticas
-> authentication.getName()
-> EstadisticasService.obtenerEstadisticas(username)
-> UsuarioRepository busca el usuario
-> EstadisticasRepository consulta historial_entrenamientos
-> EstadisticaUsuarioDTO devuelve el resultado
```

Que hace cada parte:

- `estadisticas-service.ts`: llama al backend con `GET /api/estadisticas`.
- `EstadisticasRestController.java`: recibe la peticion y obtiene el usuario autenticado con `Authentication`.
- `EstadisticasService.java`: coordina la logica; busca el usuario y rellena el DTO.
- `EstadisticasRepository.java`: hace las consultas de conteo, suma, promedio y agrupacion.
- `EstadisticaUsuarioDTO.java`: es el objeto que vuelve al frontend con los resultados.

Que calcula el repository:

- Entrenamientos realizados.
- Minutos entrenados.
- Promedio de minutos.
- Centros privados visitados.
- Lugares publicos visitados.
- Entrenamiento mas realizado.

Archivos:

- `EstadisticasRestController.java`
- `EstadisticasService.java`
- `EstadisticasRepository.java`
- `EstadisticaUsuarioDTO.java`
- `historial_entrenamientos`

Ejemplo para explicarlo:

> Si un usuario registra 5 entrenamientos en historial, estadisticas cuenta esos 5 registros. Si esos entrenamientos suman 300 minutos, devuelve 300 minutos entrenados. Y si un entrenamiento aparece varias veces, puede detectarlo como entrenamiento mas realizado.

Punto importante:

El service controla valores nulos. Si una consulta no devuelve nada porque el usuario todavia no tiene historial, se devuelven valores por defecto:

```text
0 entrenamientos
0 minutos
0.0 de promedio
"N/A" como entrenamiento mas realizado
```

Frase:

> El historial es la fuente de verdad. Las estadisticas son datos derivados: el repository cuenta, suma, promedia y agrupa registros del historial del usuario autenticado.

### Por que algunas consultas de estadisticas son nativas?

Porque algunas estadisticas tienen que combinar varias columnas opcionales del historial, por ejemplo centros base y centros de usuario, o lugares base y lugares de usuario.

Ejemplo:

```text
centro_privado_base_id
centro_privado_usuario_id
lugar_publico_base_id
lugar_publico_usuario_id
```

Para contar ubicaciones distintas se usan consultas SQL nativas con `COUNT(DISTINCT ...)` y `COALESCE`.

Frase:

> Uso JPQL para consultas simples y SQL nativo cuando la consulta depende de columnas concretas y agregaciones mas especificas.

### Como funciona el soporte o chat?

Soporte funciona como un sistema de tickets con conversacion. No es un chat en tiempo real tipo WhatsApp, porque no usa WebSocket ni mensajes instantaneos. Es una conversacion guardada en base de datos: el frontend consulta y envia mensajes mediante endpoints REST.

Modelo:

```text
soporte
-> ticket principal
-> guarda asunto, mensaje inicial, estado, usuario y fecha

mensajes_soporte
-> mensajes de la conversacion
-> cada mensaje pertenece a un ticket
-> cada mensaje tiene emisor, contenido y fecha
```

Cuando el usuario crea un ticket:

```text
soporte.ts frontend
-> POST /api/soporte
-> SoporteRestController.crearTicket
-> SoporteService.crearTicket
-> SoporteRepository guarda ticket
-> MensajeSoporteRepository guarda mensaje inicial
```

Esto significa que al crear el ticket se guardan dos cosas:

1. El ticket en `soporte`.
2. El primer mensaje en `mensajes_soporte`.

Asi el mensaje inicial tambien queda dentro de la conversacion.

Para ver mensajes de un ticket:

```text
GET /api/soporte/mis-tickets/{id}/mensajes
-> SoporteRestController.verMensajes
-> SoporteService.listarMensajesTicketUsuario
-> MensajeSoporteRepository.findByTicketOrderByFechaAsc
```

Para enviar un mensaje como usuario:

```text
POST /api/soporte/mis-tickets/{id}/mensajes
-> SoporteRestController.enviarMensaje
-> SoporteService.enviarMensajeUsuario
-> MensajeSoporteRepository.save
```

Para admin:

```text
/api/admin/soporte
-> SoporteAdminRestController
-> SoporteService
```

El admin tiene endpoints separados porque puede ver todos los tickets, cambiar estados y responder como administrador.

Estados:

```text
ABIERTO      -> ticket creado por usuario
EN_PROCESO  -> admin ya ha respondido o esta gestionandolo
CERRADO     -> ticket terminado
```

Archivos:

- Frontend usuario: `services/soporte.ts`
- Frontend admin: `services/admin/admin-soporte-service.ts`
- Controller usuario: `SoporteRestController.java`
- Controller admin: `SoporteAdminRestController.java`
- Logica: `SoporteService.java`
- Entidades: `Soporte.java`, `MensajeSoporte.java`
- Repositories: `SoporteRepository.java`, `MensajeSoporteRepository.java`

Frase:

> Soporte no es un chat en tiempo real, es un sistema de tickets con mensajes persistidos. Cada conversacion queda guardada en base de datos y se consulta mediante endpoints REST.

### Que reglas tiene el soporte?

Reglas principales:

- Un usuario solo puede ver sus propios tickets.
- Admin puede ver todos los tickets.
- Un usuario no puede responder a un ticket cerrado.
- El usuario no puede responder hasta que un admin haya iniciado la conversacion.
- Si el admin responde a un ticket abierto, pasa a `EN_PROCESO`.
- Admin solo puede borrar tickets cerrados.

Donde esta:

- `SoporteService.java`
- `SoporteRestController.java`
- `SoporteAdminRestController.java`
- `Soporte.java`
- `MensajeSoporte.java`

Frase:

> El service concentra las reglas: propiedad del ticket, estado, permisos y conversion a DTO. El controller solo expone endpoints.

### Como sabe el backend que usuario esta haciendo la peticion?

Spring Security inyecta `Authentication authentication` en el controller.

Ejemplo:

```java
authentication.getName()
```

Eso devuelve el username del usuario autenticado. Luego el service lo busca en `UsuarioRepository`.

Frase:

> No envio el id del usuario desde Angular para operaciones privadas. Uso la sesion y `Authentication` para obtener el usuario real autenticado.

### Por que usas DTOs en vez de devolver entidades directamente?

Porque los DTOs controlan que datos entran y salen de la API.

Ventajas:

- No expongo campos internos como password.
- Puedo validar datos con `@NotBlank`, `@NotNull`, `@Email`, etc.
- Evito acoplar directamente la API a la estructura de la entidad.
- Puedo devolver solo lo que necesita el frontend.

Ejemplos:

- `UsuarioDTO`: registro.
- `UsuarioSesionDTO`: datos del usuario en sesion.
- `CrearSoporteDTO`: crear ticket.
- `MensajeSoporteDTO`: devolver mensajes.
- `EstadisticaUsuarioDTO`: devolver estadisticas calculadas.

### Como maneja el backend los errores de validacion de DTO?

Si un controller recibe `@Valid @RequestBody` y el DTO no cumple las anotaciones, Spring lanza `MethodArgumentNotValidException`.

El `GlobalExceptionHandler` la captura y devuelve:

```text
HTTP 400 BAD_REQUEST
{
  "campo": "mensaje de error"
}
```

Frase:

> Los DTOs validan la entrada y el handler transforma los errores en respuestas claras para el frontend.

### Que diferencia hay entre validacion de DTO y validacion de service?

DTO:

- Valida forma del dato.
- Ejemplo: campo obligatorio, email valido, longitud maxima, numero minimo.

Service:

- Valida reglas de negocio.
- Ejemplo: el recurso pertenece al usuario, el ticket no esta cerrado, el entrenamiento existe, solo puede haber una ubicacion en historial.

Frase:

> El DTO valida datos simples; el service valida reglas reales de la aplicacion.

### Como se conecta el historial con estadisticas?

Cuando el usuario registra un entrenamiento realizado, se crea una fila en `historial_entrenamientos`.

Despues estadisticas lee esa tabla:

```text
historial_entrenamientos
-> contar registros
-> sumar duracion
-> calcular promedio
-> agrupar entrenamientos
-> contar ubicaciones distintas
```

Frase:

> El historial guarda hechos reales. Estadisticas no inventa datos ni los duplica: calcula resultados a partir de esos hechos.

### Como se conecta el mapa con centros y lugares?

El mapa es un componente reutilizable del frontend:

```text
components/mapa-selector
```

Sirve para seleccionar o mostrar coordenadas. Los datos se guardan en entidades de centros/lugares:

```text
CentroPrivadoBase / CentroPrivadoUsuario
LugarPublicoBase / LugarPublicoUsuario
```

Frase:

> Leaflet solo pinta o selecciona coordenadas en frontend. La informacion persistente se guarda en MySQL a traves del backend.

### Como se conecta admin con el resto de la aplicacion?

Admin usa rutas y endpoints separados.

Frontend:

```text
/admin/soporte
/admin/crear-admin
adminGuard
```

Backend:

```text
/api/admin/**
SecurityConfig -> hasRole("ADMIN")
@PreAuthorize("hasRole('ADMIN')")
```

Frase:

> Admin no es solo un menu oculto. Aunque alguien intente llamar al endpoint manualmente, backend exige rol ADMIN.

## Seguridad Web Normal que Pueden Preguntar

### Puede alguien saltarse un guard de Angular?

Si. Un guard solo protege la navegacion del frontend. Por eso el backend tambien protege endpoints. Si alguien llama manualmente a `/api/admin/...`, Spring Security comprueba el rol.

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


### Donde esta la configuracion de MySQL?

En `application.properties` y tambien en `docker-compose.yml` para Docker.

## Calidad y Mejoras

### Que pruebas has hecho?

He hecho pruebas manuales de los flujos principales: registro, login, navegacion, CRUD, mapa, historial, valoraciones, estadisticas, soporte y admin. Hay archivos `.spec.ts` en Angular y una prueba base en Spring Boot, aunque una mejora seria ampliar tests automaticos.

### Que parte tiene mas complejidad?

Historial y valoraciones. Historial conecta usuario, entrenamiento y ubicacion con varias opciones. Valoraciones puede apuntar a muchos tipos de contenido mediante `tipo_valoracion + id_relacionado`.

### Como sabes que tu BD tiene sentido?

Porque cada tabla representa una responsabilidad clara. No guardo listas dentro de usuarios; uso relaciones. Distingo catalogo base de datos personales. Historial guarda hechos reales y estadisticas se calculan desde ahi.
