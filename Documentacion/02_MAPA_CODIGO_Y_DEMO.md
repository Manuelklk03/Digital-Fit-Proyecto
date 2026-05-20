# 02 - Mapa de Codigo y Demo

Este documento sirve para ubicar rapido cada parte del proyecto si el tribunal te pide "donde esta X".

## Estructura General

```text
Digital-Fit-Proyecto/
├─ Backend/digital-fit/              Spring Boot
├─ Frontend/digital-fit-frontend/    Angular
├─ Documentacion/                    Documentacion y guias
└─ docker-compose.yml                MySQL + Backend + Frontend
```

## Backend

Ruta base:

`Backend/digital-fit/src/main/java/com/example/digital_fit`

| Carpeta | Que contiene | Como explicarlo |
|---|---|---|
| `model` | Entidades JPA | Tablas de la base de datos |
| `controller` | Endpoints REST | Entrada HTTP de la API |
| `service` | Logica de negocio | Validaciones, permisos y conversiones |
| `repository` | Acceso a BD | Consultas JPA/SQL |
| `dto` | Objetos de entrada/salida | Evitan exponer entidades directamente |
| `config` | Seguridad y CORS | Spring Security y comunicacion Angular |
| `handler` | Errores globales | Respuestas HTTP ordenadas |
| `exception` | Excepciones propias | Errores de dominio |

## Archivos Clave Backend

| Tema | Archivo |
|---|---|
| Arranque app | `DigitalFitApplication.java` |
| Dependencias Maven | `pom.xml` |
| BD y logs | `application.properties` |
| Datos iniciales | `data.sql` |
| Seguridad | `SecurityConfig.java` |
| CORS | `CorsConfig.java` |
| Errores | `GlobalExceptionHandler.java` |

## Frontend

Ruta base:

`Frontend/digital-fit-frontend/src/app`

| Carpeta/archivo | Que contiene |
|---|---|
| `app.routes.ts` | Rutas de la SPA |
| `pages` | Pantallas |
| `services` | Llamadas HTTP al backend |
| `guards` | Proteccion de rutas |
| `components` | Componentes reutilizables |
| `components/mapa-selector` | Mapa con Leaflet |

## Rutas Angular Importantes

Archivo:

`Frontend/digital-fit-frontend/src/app/app.routes.ts`

Como leerlo:

- Arriba se importan componentes y guards.
- `export const routes: Routes = [` declara la lista de rutas.
- Cada entrada une una URL (`path`) con una pantalla (`component`).
- `canActivate` indica el guard que protege la ruta.
- Las rutas con `:id` son rutas dinamicas de detalle.
- `**` es el comodin final para redirigir si la ruta no existe.

| Ruta | Uso | Guard |
|---|---|---|
| `/login` | Login | `publicGuard` |
| `/register` | Registro | `publicGuard` |
| `/inicio` | Inicio | `authGuard` |
| `/privados` | Centros privados base | `authGuard` |
| `/publicos` | Lugares publicos base | `authGuard` |
| `/entrenamientos` | Entrenamientos base | `authGuard` |
| `/entrenamientos-comunidad` | Comunidad | `userGuard` |
| `/mis-entrenamientos` | Entrenamientos del usuario | `userGuard` |
| `/mis-centros` | Centros guardados | `userGuard` |
| `/mis-lugares` | Lugares guardados | `userGuard` |
| `/historial-entrenamientos` | Historial | `userGuard` |
| `/estadisticas` | Estadisticas | `userGuard` |
| `/soporte` | Soporte usuario | `userGuard` |
| `/valoraciones` | Valoraciones | `userGuard` |
| `/admin/soporte` | Soporte admin | `adminGuard` |
| `/admin/crear-admin` | Crear admin | `adminGuard` |

## Flujo de Demo y Codigo Asociado

## Preguntas de "Abre el Fichero y Enseñalo"

### Donde se envia el token?

Respuesta corta:

> No se envia token JWT. Se usa sesion de Spring Security con cookie. Angular envia la cookie en las peticiones con `withCredentials: true`.

Abrir:

```text
Frontend/digital-fit-frontend/src/app/services/auth-service.ts
```

Que mirar:

- `login(...)`: hace `POST` a `/api/auth/login`.
- En las opciones HTTP aparece `withCredentials: true`.
- No hay `Authorization: Bearer ...`.
- No hay guardado de token en `localStorage` ni `sessionStorage`.

Backend que lo confirma:

```text
Backend/digital-fit/src/main/java/com/example/digital_fit/config/SecurityConfig.java
```

Que mirar:

- `formLogin(...)`
- `loginProcessingUrl("/api/auth/login")`
- `.anyRequest().authenticated()`
- `/api/admin/**` con `hasRole("ADMIN")`

CORS/cookies:

```text
Backend/digital-fit/src/main/java/com/example/digital_fit/config/CorsConfig.java
```

Que mirar:

- `configuration.setAllowCredentials(true)`

### Como se crean las rutas en el frontend?

Abrir:

```text
Frontend/digital-fit-frontend/src/app/app.routes.ts
Frontend/digital-fit-frontend/src/app/app.config.ts
```

Que mirar en `app.routes.ts`:

```ts
export const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [publicGuard] },
  { path: 'inicio', component: InicioComponent, canActivate: [authGuard] },
  { path: 'mis-entrenamientos/:id', component: DetalleMiEntrenamientoComponent, canActivate: [userGuard] },
  { path: 'admin/soporte', component: AdminSoporteComponent, canActivate: [adminGuard] },
  { path: '**', redirectTo: 'login' }
];
```

Que mirar en `app.config.ts`:

```ts
provideRouter(routes)
```

Frase:

> Angular Router recibe el array `routes`. Cada ruta define una URL, el componente que se renderiza y opcionalmente un guard. Despues `app.config.ts` registra esas rutas con `provideRouter(routes)`.

### Donde se navega desde HTML?

Abrir:

```text
Frontend/digital-fit-frontend/src/app/components/header/header.html
Frontend/digital-fit-frontend/src/app/pages/inicio/inicio.html
```

Buscar:

```text
routerLink
```

Ejemplos:

- `routerLink="/inicio"`
- `[routerLink]="['/soporte', ticket.id]"`

### Donde redirige desde TypeScript?

Buscar:

```text
router.navigate
```

Ejemplos:

- Guards: redirigen a `/login` o `/inicio`.
- Login: al entrar correctamente navega a `/inicio`.
- Admin/user pages: redirigen si el rol no corresponde.

### 1. Registro e Inicio de Sesion

Frontend:

- `pages/login`
- `pages/register`
- `services/auth-service.ts`

Backend:

- `controller/Auth/AuthController.java`
- `service/Auth/AuthService.java`
- `service/Auth/CustomUserDetailsService.java`
- `config/SecurityConfig.java`
- `model/Auth/Usuario.java`

Que decir:

> Login y registro pasan por Spring Security. El registro cifra password con BCrypt y el login crea una sesion.

### 2. Pantalla Principal y Navegacion

Frontend:

- `app.routes.ts`
- `components/header`
- `components/footer`
- `pages/inicio`
- `guards`

Que decir:

> Angular controla la navegacion con rutas y guards, pero los permisos reales se verifican en backend.

### 3. Centros Privados Base y Mis Centros

Frontend:

- `pages/privados`
- `pages/privados/mis-centros`
- `pages/detalle-privado`
- `services/centros/privados-service.ts`
- `services/centros/mis-centros-service.ts`

Backend:

- `model/CentroPrivado/CentroPrivadoBase.java`
- `model/CentroPrivado/CentroPrivadoUsuario.java`
- `controller/CentroPrivado/CentroPrivadoBaseRestController.java`
- `controller/CentroPrivado/CentroPrivadoUsuarioRestController.java`
- `service/CentroPrivado/CentroPrivadoBaseService.java`
- `service/CentroPrivado/CentroPrivadoUsuarioService.java`

Que decir:

> Los centros base son catalogo. Mis centros son copias o centros guardados por usuario con `usuario_id`.

### 4. Lugares Publicos Base y Mis Lugares

Frontend:

- `pages/publicos`
- `pages/publicos/mis-lugares`
- `pages/detalle-publico`
- `services/publicos/publicos-service.ts`
- `services/publicos/mis-lugares.ts`

Backend:

- `model/LugarPublico/LugarPublicoBase.java`
- `model/LugarPublico/LugarPublicoUsuario.java`
- `controller/LugarPublico/LugarPublicoBaseRestController.java`
- `controller/LugarPublico/LugarPublicoUsuarioRestController.java`
- `service/LugarPublico/LugarPublicoBaseService.java`
- `service/LugarPublico/LugarPublicoUsuarioService.java`

Que decir:

> Igual que centros, pero para espacios publicos: parques, rutas, pistas, playas.

### 5. Mapa Interactivo

Frontend:

- `components/mapa-selector`

Backend relacionado:

- `AutocompletadoCentroPrivadoService.java`
- `AutocompletadoLugarPublicoService.java`
- DTOs de autocompletado.

Que decir:

> El mapa permite seleccionar ubicaciones y guardar lugares o centros con coordenadas. Leaflet trabaja en frontend y backend guarda los datos.

### 6. Entrenamientos Base, Mis Entrenamientos y Comunidad

Frontend:

- `pages/entrenamientos`
- `pages/entrenamientos/mis-entrenamientos`
- `pages/entrenamientos/comunidad`
- `services/entrenamientos`

Backend:

- `EntrenamientoBase.java`
- `EntrenamientoUsuario.java`
- `EntrenamientoComunidad.java`
- `EntrenamientoBaseRestController.java`
- `EntrenamientoUsuarioRestController.java`
- `EntrenamientoComunidadRestController.java`
- `EntrenamientoBaseService.java`
- `EntrenamientoUsuarioService.java`
- `EntrenamientoComunidadService.java`

Que decir:

> Base es catalogo, usuario es privado, comunidad es compartido. Cuando copias, se crea tu propia version.

### 7. Historial

Frontend:

- `pages/entrenamientos/historial`
- `pages/detalle-entrenamientos/detalle-historial`
- `services/entrenamientos/historial-entrenamientos.ts`

Backend:

- `HistorialEntrenamientos.java`
- `HistorialEntrenamientosRestController.java`
- `HistorialEntrenamientosService.java`
- `HistorialEntrenamientosRepository.java`

Que decir:

> Historial guarda actividad real y conecta usuario, entrenamiento, fecha, duracion y ubicacion opcional.

### 8. Valoraciones

Frontend:

- `pages/valoraciones`
- `services/valoraciones/valoraciones-service.ts`

Backend:

- `Valoracion.java`
- `ValoracionRestController.java`
- `ValoracionService.java`
- `ValoracionRepository.java`
- `TipoDeValoracion.java`

Que decir:

> Valoraciones usa `tipo_valoracion + id_relacionado` para poder valorar muchos tipos de contenido con una sola tabla.

### 9. Estadisticas

Frontend:

- `pages/estadisticas`
- `services/estadisticas/estadisticas-service.ts`

Backend:

- `EstadisticasRestController.java`
- `EstadisticasService.java`
- `EstadisticasRepository.java`
- `EstadisticaUsuarioDTO.java`

Que decir:

> Las estadisticas son datos derivados del historial, no se guardan duplicadas.

### 10. Soporte y Admin

Frontend:

- `pages/soporte`
- `pages/admin/soporte`
- `services/soporte.ts`
- `services/admin/admin-soporte-service.ts`

Backend:

- `Soporte.java`
- `MensajeSoporte.java`
- `SoporteRestController.java`
- `SoporteAdminRestController.java`
- `SoporteService.java`
- `SoporteRepository.java`
- `MensajeSoporteRepository.java`

Que decir:

> Soporte se modela como ticket y mensajes. Admin puede revisar y cambiar estado.

## Endpoints Principales

| Modulo | Endpoint |
|---|---|
| Auth | `/api/auth/login`, `/api/auth/registro`, `/api/auth/me`, `/api/auth/logout` |
| Entrenamientos base | `/api/entrenamientos` |
| Mis entrenamientos | `/api/mis-entrenamientos` |
| Comunidad | `/api/entrenamientos-comunidad` |
| Historial | `/api/entrenamientos/mi-historial` |
| Centros base | `/api/centros-privados` |
| Mis centros | `/api/mis-centros-privados` |
| Lugares base | `/api/lugares-publicos` |
| Mis lugares | `/api/mis-lugares-publicos` |
| Valoraciones | `/api/valoraciones` |
| Estadisticas | `/api/estadisticas` |
| Soporte | `/api/soporte` |
| Admin | `/api/admin/**` |

## Si Te Piden Encontrar Algo

Si preguntan "donde esta...":

- Entidad/tabla: `model`.
- Endpoint: `controller`.
- Regla de negocio: `service`.
- Consulta: `repository`.
- Datos formulario: `dto`.
- Ruta visual: `app.routes.ts`.
- Llamada HTTP: `services`.
- Permiso frontend: `guards`.
- Permiso backend: `SecurityConfig`.

## Flujos Concretos Para Defender

### Flujo 1: Registro

```text
register.html/register.ts
-> auth-service.ts
-> POST /api/auth/registro
-> AuthController.registrar
-> AuthService.registrar
-> UsuarioRepository
-> usuarios
```

Puntos que decir:

- Se valida con `UsuarioDTO`.
- Se comprueba username/email duplicado.
- Se cifra password con BCrypt.
- Se guarda con rol `USER`.

### Flujo 2: Login

```text
login.html/login.ts
-> auth-service.ts
-> POST /api/auth/login
-> SecurityConfig.formLogin
-> CustomUserDetailsService
-> UsuarioRepository
-> sesion Spring Security
```

Puntos que decir:

- Login lo procesa Spring Security.
- El usuario puede entrar con username o email.
- La cookie de sesion se manda luego con `withCredentials`.

### Flujo 3: Copiar Entrenamiento Base a Mis Entrenamientos

```text
entrenamientos page
-> entrenamientos-service.ts
-> mis-entrenamientos-service.ts
-> POST /api/mis-entrenamientos/desde-base/{idBase}
-> EntrenamientoUsuarioRestController
-> EntrenamientoUsuarioService.anadirDesdeBase
-> EntrenamientoBaseRepository
-> EntrenamientoUsuarioRepository
-> entrenamientos_usuario
```

No se modifica el entrenamiento base. Se crea una copia asociada al usuario.

### Flujo 4: Guardar Lugar o Centro Desde Mapa

```text
mapa-selector
-> service de mis centros/mis lugares
-> controller usuario
-> service usuario
-> repository
-> tabla *_usuario
```

Leaflet ayuda a seleccionar coordenadas y backend guarda datos asociados al usuario autenticado.

### Flujo 5: Registrar Historial

```text
historial-entrenamientos page
-> historial-entrenamientos.ts service
-> POST /api/entrenamientos/mi-historial
-> HistorialEntrenamientosRestController
-> HistorialEntrenamientosService.crearHistorialEntrenamiento
-> repositories de entrenamiento/ubicacion
-> historial_entrenamientos
```

Validaciones:

- Debe haber un entrenamiento.
- Solo un entrenamiento: base o usuario.
- Como maximo una ubicacion.
- Si el recurso es privado, se comprueba que sea del usuario.

### Flujo 6: Ver Estadisticas

```text
estadisticas page
-> estadisticas-service.ts
-> GET /api/estadisticas
-> EstadisticasRestController
-> EstadisticasService
-> EstadisticasRepository
-> historial_entrenamientos
```

Todo sale del historial: contar, sumar, promediar y agrupar.

### Flujo 7: Crear Valoracion

```text
valoraciones-service.ts
-> POST /api/valoraciones/{tipoContenido}/{idContenido}
-> ValoracionRestController
-> ValoracionService.crearOActualizar
-> validarContenidoExisteYPermisos
-> ValoracionRepository
-> valoraciones
```

Si ya existe valoracion del mismo usuario para ese contenido, se actualiza.

### Flujo 8: Soporte Usuario y Admin

Usuario:

```text
soporte page -> soporte.ts -> /api/soporte -> SoporteRestController -> SoporteService
```

Admin:

```text
admin/soporte page -> admin-soporte-service.ts -> /api/admin/soporte -> SoporteAdminRestController
```

Usuario abre tickets y admin puede revisar/cambiar estado.

## Que Abrir en VS Code si te Preguntan

```text
Seguridad:
  Backend/.../config/SecurityConfig.java

Usuario:
  Backend/.../model/Auth/Usuario.java
  Backend/.../service/Auth/AuthService.java

Rutas frontend:
  Frontend/.../src/app/app.routes.ts

Historial:
  Backend/.../model/Entrenamientos/HistorialEntrenamientos.java
  Backend/.../service/Entrenamientos/HistorialEntrenamientosService.java

Valoraciones:
  Backend/.../model/Valoracion/Valoracion.java
  Backend/.../service/Valoracion/ValoracionService.java

Estadisticas:
  Backend/.../repository/Estadisticas/EstadisticasRepository.java
```

## Explicacion de Carpetas en 20 Segundos

> El backend esta dividido en `model`, `controller`, `service`, `repository`, `dto` y `config`. El frontend esta dividido en `pages`, `services`, `guards` y `components`. Asi puedo encontrar rapido si algo es pantalla, llamada HTTP, endpoint, logica o entidad.
