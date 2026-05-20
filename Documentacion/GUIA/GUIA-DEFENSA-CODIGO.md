# 🏋️ GUÍA DE ESTUDIO PARA EL TRIBUNAL - Digital-Fit

> **Lo que NO está en tu presentación de defensa y el tribunal SÍ puede preguntar**
> 
> Autor: Manuel Casinos Pérez · 2º DAW · IES Conselleria

---

## ⚠️ IMPORTANTE: ¿Qué NO preguntará el tribunal?

Tu presentación de defensa ya cubre:
- Origen del proyecto y motivaciones
- Valor diferencial y solución propuesta
- Stack tecnológico (Angular, Spring Boot, MySQL, Leaflet)
- Arquitectura (diagrama cliente-servidor + CORS)
- Funcionalidades generales (lo que hace cada rol)
- Seguridad básica (Spring Security, BCrypt, roles)
- Calidad técnica (validaciones, pruebas manuales)
- Demo en vivo

**Por tanto, el tribunal preguntará sobre COSAS QUE NO APARECEN EN LA DEFENSA:**
1. ❓ Cómo está organizado el código (estructura de carpetas, capas)
2. ❓ Cómo se implementan las relaciones JPA (anotaciones, tipos)
3. ❓ Dónde está cada cosa (localización exacta de archivos)
4. ❓ Cómo funciona el frontend por dentro (rutas, guards, servicios)
5. ❓ **Seguridad más profunda: ¿es hackeable el frontend?** (XSS, CSRF, inyecciones)
6. ❓ Manejo de errores y excepciones
7. ❓ Detalles de la base de datos (data.sql, enums, constraints)
8. ❓ Decisiones técnicas específicas

---

## 📋 ÍNDICE

1. [Estructura del Proyecto (Backend)](#1-estructura-del-proyecto-backend)
2. [Estructura del Proyecto (Frontend)](#2-estructura-del-proyecto-frontend)
3. [Explicación del Backend por Capas](#3-explicación-del-backend-por-capas)
4. [Explicación del Frontend](#4-explicación-del-frontend)
5. [Seguridad: ¿Es hackeable el frontend?](#5-seguridad-es-hackeable-el-frontend)
6. [Base de Datos: Todo lo que no está en la defensa](#6-base-de-datos-todo-lo-que-no-está-en-la-defensa)
7. [Guía de Ubicación de Código Clave](#7-guía-de-ubicación-de-código-clave)
8. [Preguntas Frecuentes del Tribunal (con respuestas)](#8-preguntas-frecuentes-del-tribunal-con-respuestas)

---

## 1. ESTRUCTURA DEL PROYECTO (BACKEND)

### 📁 Estructura de carpetas del backend

```
Backend/digital-fit/
├── pom.xml                                    ← Dependencias Maven
└── src/main/java/com/example/digital_fit/
    ├── DigitalFitApplication.java             ← @SpringBootApplication (main)
    ├── config/
    │   ├── CorsConfig.java                    ← Configuración CORS
    │   └── SecurityConfig.java                ← Spring Security (login, logout, roles)
    ├── controller/                            ← CAPA CONTROLADOR (API REST)
    │   ├── Auth/AuthController.java           ← /api/auth/*
    │   ├── CentroPrivado/
    │   │   ├── CentroPrivadoBaseRestController.java
    │   │   ├── CentroPrivadoUsuarioRestController.java
    │   │   └── Admin/CentroPrivadoBaseAdminRestController.java
    │   ├── Entrenamientos/
    │   │   ├── EntrenamientoBaseRestController.java
    │   │   ├── EntrenamientoUsuarioRestController.java
    │   │   ├── EntrenamientoComunidadRestController.java
    │   │   ├── HistorialEntrenamientosRestController.java
    │   │   └── Admin/EntrenamientoBaseAdminRestController.java
    │   ├── Estadisticas/EstadisticasRestController.java
    │   ├── LugarPublico/
    │   │   ├── LugarPublicoBaseRestController.java
    │   │   ├── LugarPublicoUsuarioRestController.java
    │   │   └── Admin/LugarPublicoBaseAdminRestController.java
    │   ├── Soporte/
    │   │   ├── SoporteRestController.java
    │   │   └── Admin/SoporteAdminRestController.java
    │   └── Valoracion/ValoracionRestController.java
    ├── dto/                                   ← CAPA DE TRANSFERENCIA (DTOs)
    │   ├── Admin/CrearAdmin.java
    │   ├── Auth/UsuarioDTO.java, UsuarioSesionDTO.java
    │   ├── CentroPrivado/ (CrearCentroPrivadoDTO, CentroPrivadoBaseDTO, etc.)
    │   ├── Entrenamientos/ (CrearEntrenamientoBaseDTO, EntrenamientoUsuarioDTO, etc.)
    │   ├── Estadisticas/EstadisticaUsuarioDTO.java
    │   ├── LugarPublico/ (CrearLugarPublicoDTO, LugarPublicoUsuarioDTO, etc.)
    │   ├── Soporte/ (CrearSoporteDTO, MensajeSoporteDTO, SoporteDTO)
    │   └── Valoracion/ (CrearValoracionDTO, ValoracionDTO)
    ├── exception/                            ← EXCEPCIONES PERSONALIZADAS
    │   ├── EmailYaExisteException.java
    │   ├── ErrorArgumentoException.java
    │   ├── OperacionNoPermitida.java
    │   ├── RecursoNoEncontradoException.java
    │   └── UsernameYaExiste.java
    ├── handler/                              ← MANEJADOR GLOBAL DE ERRORES
    │   └── GlobalExceptionHandler.java
    ├── model/                                ← CAPA MODELO (Entidades JPA)
    │   ├── Auth/Usuario.java
    │   ├── CentroPrivado/CentroPrivadoBase.java
    │   ├── CentroPrivado/CentroPrivadoUsuario.java
    │   ├── Entrenamientos/EntrenamientoBase.java
    │   ├── Entrenamientos/EntrenamientoUsuario.java
    │   ├── Entrenamientos/EntrenamientoComunidad.java
    │   ├── Entrenamientos/HistorialEntrenamientos.java
    │   ├── Enums/ (Rol.java, TipoLugarPublico.java, etc.)
    │   ├── LugarPublico/LugarPublicoBase.java
    │   ├── LugarPublico/LugarPublicoUsuario.java
    │   ├── Soporte/Soporte.java
    │   ├── Soporte/MensajeSoporte.java
    │   └── Valoracion/Valoracion.java
    ├── repository/                           ← CAPA REPOSITORIO (JPA)
    │   ├── Auth/UsuarioRepository.java
    │   ├── CentroPrivado/CentroPrivadoBaseRepository.java
    │   ├── ... (repositorios por entidad)
    └── service/                              ← CAPA SERVICIO (Lógica negocio)
        ├── Auth/AuthService.java
        ├── ... (servicios por funcionalidad)
```

### 🧠 ¿Por qué está organizado así?

Es una **arquitectura por capas** (Controller → Service → Repository → Entity). Cada capa tiene una responsabilidad única:

| Capa | Responsabilidad | Anotación clave |
|------|----------------|-----------------|
| **Controller** | Recibir peticiones HTTP y devolver respuestas | `@RestController`, `@RequestMapping` |
| **Service** | Lógica de negocio, validaciones, operaciones | `@Service` |
| **Repository** | Acceso a base de datos (CRUD automático) | Extiende `JpaRepository` |
| **Entity / Model** | Representación de tablas de la BD | `@Entity`, `@Table`, `@Id` |
| **DTO** | Transferencia de datos entre capas | Sin anotación especial |
| **Exception** | Excepciones personalizadas | Extiende `RuntimeException` |
| **Handler** | Captura global de excepciones | `@ControllerAdvice` |

---

## 2. ESTRUCTURA DEL PROYECTO (FRONTEND)

### 📁 Estructura de carpetas del frontend

```
Frontend/digital-fit-frontend/src/app/
├── app.ts, app.html, app.css          ← Componente raíz
├── app.routes.ts                      ← Sistema de rutas (¡archivo clave!)
├── app.config.ts                      ← Configuración de la app
├── components/                        ← Componentes REUTILIZABLES
│   ├── header/                        ← Barra de navegación (cambia según rol)
│   ├── footer/                        ← Pie de página
│   └── mapa-selector/                 ← Mapa Leaflet
├── guards/                            ← PROTECCIÓN DE RUTAS
│   ├── auth-guard.ts                  ← ¿Está autenticado?
│   ├── public-guard.ts                ← ¿NO está autenticado?
│   ├── user-guard.ts                  ← ¿Rol = USER?
│   └── admin-guard.ts                 ← ¿Rol = ADMIN?
├── pages/                             ← PÁGINAS (cada carpeta = una ruta)
│   ├── login/                         ← Inicio de sesión
│   ├── register/                      ← Registro
│   ├── inicio/                        ← Pantalla principal
│   ├── privados/                      ← Ver centros privados
│   │   └── mis-centros/              ← Mis centros creados
│   ├── publicos/                      ← Ver lugares públicos
│   │   └── mis-lugares/              ← Mis lugares creados
│   ├── entrenamientos/                ← Ver entrenamientos
│   │   ├── mis-entrenamientos/       ← Mis entrenamientos
│   │   ├── comunidad/                ← Entrenamientos compartidos
│   │   └── historial/                ← Historial de actividad
│   ├── estadisticas/                  ← Estadísticas personales
│   ├── soporte/                       ← Tickets de soporte
│   ├── valoraciones/                  ← Valoraciones
│   ├── admin/                         ← PANEL ADMIN
│   │   ├── soporte/                   ← Gestionar tickets (admin)
│   │   └── crear-admin/              ← Crear nuevos admins
│   ├── detalle-privado/               ← Detalle de un centro
│   │   └── detalle-mi-centro/        ← Detalle de mi centro
│   ├── detalle-publico/               ← Detalle de un lugar
│   │   └── detalle-mis-lugares/      ← Detalle de mi lugar
│   └── detalle-entrenamientos/        ← Detalle entrenamientos
│       ├── detalle-entrenamiento-base/
│       ├── detalle-mis-entrenamientos/
│       ├── detalle-comunidad/
│       └── detalle-historial/
└── services/                          ← COMUNICACIÓN HTTP CON BACKEND
    ├── auth-service.ts
    ├── soporte.ts
    ├── centros/
    │   ├── privados-service.ts
    │   └── mis-centros-service.ts
    ├── publicos/
    │   ├── publicos-service.ts
    │   └── mis-lugares.ts
    ├── entrenamientos/
    │   ├── entrenamientos-service.ts
    │   ├── mis-entrenamientos-service.ts
    │   ├── entrenamiento-comunidad-service.ts
    │   └── historial-entrenamientos.ts
    ├── estadisticas/estadisticas-service.ts
    ├── valoraciones/valoraciones-service.ts
    └── admin/
        ├── admin-service.ts
        ├── admin-soporte-service.ts
        ├── admin-centro-base.ts
        ├── admin-entrenamiento-base.ts
        └── admin-lugarespublicos-base.ts
```

### 🧠 ¿Cómo se organiza un componente en Angular?

Cada componente tiene 4 archivos:

```
login/
├── login.ts          ← Lógica del componente (clase TypeScript)
├── login.html        ← Plantilla HTML (vista)
├── login.css         ← Estilos
└── login.spec.ts     ← Tests unitarios
```

---

## 3. EXPLICACIÓN DEL BACKEND POR CAPAS

### 3.1. Capa de Controladores (Controller)

**¿Qué hace?** Recibe peticiones HTTP y delega en los servicios.

```
Petición HTTP (GET /api/entrenamientos-base)
  → EntrenamientoBaseRestController.java
    → Llama a EntrenamientoBaseService.java
      → Devuelve List<EntrenamientoBaseDTO>
```

**Ejemplo de código** (AuthController.java):
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        authService.registrar(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuario registrado exitosamente");
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioSesionDTO> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authService.obtenerSesion(authentication.getName()));
    }
}
```

**Anotaciones importantes:**
- `@RestController` → indica que es un controlador REST
- `@RequestMapping("/api/...")` → ruta base del controlador
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` → verbos HTTP
- `@RequestBody` → recibe JSON del cuerpo de la petición
- `@Valid` → activa validaciones automáticas del DTO
- `Authentication authentication` → Spring Security inyecta el usuario autenticado

### 3.2. Capa de Servicios (Service)

**¿Qué hace?** Contiene toda la lógica de negocio. Los controladores llaman a servicios, nunca acceden directamente a repositorios.

**Ejemplo** (AuthService.java):
```java
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registrar(UsuarioDTO usuarioDTO) {
        // 1. Validar que no exista el username
        if (usuarioRepository.findByUsername(usuarioDTO.getUsername()).isPresent()) {
            throw new UsernameYaExiste("Nombre de usuario ya existe");
        }
        // 2. Validar que no exista el email
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new EmailYaExisteException("Email ya existe");
        }
        // 3. Cifrar contraseña y guardar
        Usuario usuario = Usuario.builder()
                .username(usuarioDTO.getUsername())
                .email(usuarioDTO.getEmail())
                .password(passwordEncoder.encode(usuarioDTO.getPassword()))
                .rol(Rol.USER)
                .build();
        usuarioRepository.save(usuario);
    }
}
```

**Anotaciones importantes:**
- `@Service` → marca la clase como servicio
- `@Autowired` → inyección de dependencias
- `@Transactional` → la operación se ejecuta en una transacción de BD

### 3.3. Capa de Repositorios (Repository)

**¿Qué hace?** Proporciona operaciones CRUD automáticas contra la BD.

```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
}
```

**Spring Data JPA genera automáticamente** el SQL para `findByUsername`, `findByEmail`, etc. basándose en el nombre del método. No necesitas escribir SQL.

### 3.4. Capa de Modelo (Entidades JPA)

**¿Qué hace?** Representa las tablas de la base de datos como objetos Java.

```java
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;
}
```

**Anotaciones JPA clave:**
| Anotación | Función |
|-----------|---------|
| `@Entity` | Marca la clase como entidad de BD |
| `@Table(name = "x")` | Nombre de la tabla en MySQL |
| `@Id` | Clave primaria |
| `@GeneratedValue(strategy = IDENTITY)` | Auto-incremental |
| `@Column(nullable = false, unique = true)` | Columna no nula y única |
| `@ManyToOne` | Relación N:1 |
| `@OneToMany(mappedBy = "x")` | Relación 1:N |
| `@JoinColumn(name = "fk_id")` | Clave foránea |
| `@Enumerated(EnumType.STRING)` | Guarda enum como texto |
| `@UniqueConstraint` | Restricción única compuesta |

**Lombok** reduce el código:
| Anotación | Genera |
|-----------|--------|
| `@Data` | Getters, setters, toString, equals, hashCode |
| `@Builder` | Patrón Builder (new Usuario().builder()...) |
| `@NoArgsConstructor` | Constructor vacío |
| `@AllArgsConstructor` | Constructor con todos los campos |

### 3.5. DTOs (Data Transfer Objects)

**¿Por qué no usar las entidades directamente?**

1. **Seguridad**: No exponemos campos internos (como la contraseña) al frontend
2. **Control**: Podemos devolver solo los campos que necesitamos
3. **Acoplamiento**: Cambios en la BD no afectan al API

**Ejemplo**:
```java
// DTO de entrada (lo que envía el frontend)
public class UsuarioDTO {
    @NotBlank private String username;
    @Email private String email;
    @Size(min = 6) private String password;
}

// DTO de salida (lo que devuelve el backend)
public class UsuarioSesionDTO {
    private String username;
    private String email;
    private Rol rol;
}
```

### 3.6. Excepciones y Manejador Global

**Excepciones personalizadas** (todas en `exception/`):
- `RecursoNoEncontradoException` → 404 Not Found
- `EmailYaExisteException` → 409 Conflict
- `UsernameYaExiste` → 409 Conflict
- `OperacionNoPermitida` → 403 Forbidden
- `ErrorArgumentoException` → 400 Bad Request

**GlobalExceptionHandler** (`handler/GlobalExceptionHandler.java`):
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    // Captura TODAS las excepciones y devuelve la respuesta HTTP adecuada
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<String> handleNotFound(RecursoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
```

---

## 4. EXPLICACIÓN DEL FRONTEND

### 4.1. Sistema de Rutas (app.routes.ts)

**Ubicación**: `Frontend/digital-fit-frontend/src/app/app.routes.ts`

El frontend tiene **4 niveles de acceso** controlados por guards:

```
🔓 PÚBLICO (publicGuard)
  /login, /register

🔐 AUTENTICADO (authGuard - cualquier rol)
  /inicio, /privados, /publicos, /entrenamientos

👤 SOLO USER (userGuard)
  /mis-entrenamientos, /mis-centros, /mis-lugares
  /entrenamientos-comunidad, /historial-entrenamientos
  /estadisticas, /soporte, /valoraciones

🛡️ SOLO ADMIN (adminGuard)
  /admin/soporte, /admin/crear-admin
```

**Código de ejemplo** (app.routes.ts):
```typescript
export const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [publicGuard] },
  { path: 'inicio', component: InicioComponent, canActivate: [authGuard] },
  { path: 'mis-entrenamientos', component: MisEntrenamientosComponent, canActivate: [userGuard] },
  { path: 'admin/soporte', component: AdminSoporteComponent, canActivate: [adminGuard] },
  { path: '**', redirectTo: 'login' }  // Ruta comodín
];
```

### 4.2. Guards de Autenticación

**Ubicación**: `Frontend/digital-fit-frontend/src/app/guards/`

Los guards deciden si un usuario puede acceder a una ruta:

```typescript
// auth-guard.ts: ¿Está autenticado? Si no → redirige a /login
export const authGuard: CanActivateFn = (route, state) => {
  // Comprueba si hay sesión activa
};

// admin-guard.ts: ¿Es ADMIN? Si no → redirige a /inicio
export const adminGuard: CanActivateFn = (route, state) => {
  // Comprueba el rol del usuario
};
```

### 4.3. Servicios HTTP

**Ubicación**: `Frontend/digital-fit-frontend/src/app/services/`

Cada servicio se comunica con el backend mediante la API REST:

```typescript
// auth-service.ts (ejemplo simplificado)
export class AuthService {
  login(username: string, password: string) {
    return fetch('/api/auth/login', { method: 'POST', body: ... });
  }
  
  register(usuario: UsuarioDTO) {
    return fetch('/api/auth/registro', { method: 'POST', body: JSON.stringify(usuario) });
  }
  
  getSesion() {
    return fetch('/api/auth/me');
  }
}
```

### 4.4. Componentes Compartidos

**Ubicación**: `Frontend/digital-fit-frontend/src/app/components/`

- **Header**: Barra de navegación que cambia según el rol. USER ve enlaces a "Mis centros", "Mis entrenamientos", etc. ADMIN ve enlaces al panel de administración.
- **Footer**: Pie de página común a todas las páginas.
- **Mapa-selector**: Componente de mapa Leaflet + OpenStreetMap para visualizar y seleccionar ubicaciones.

---

## 5. SEGURIDAD: ¿ES HACKEABLE EL FRONTEND?

Esta es una pregunta **muy frecuente** en tribunales de DAW. Te refieres a la seguridad de una aplicación web desde el punto de vista del frontend. Te explico los riesgos y cómo los prevenimos:

### 5.1. Principio fundamental: NUNCA confíes en el frontend

**El frontend (navegador) es inseguro por naturaleza**. Todo lo que se ejecuta en el navegador del usuario puede ser manipulado. Por eso **la seguridad real debe estar en el backend**.

### 5.2. Principales vulnerabilidades del frontend

#### 🔴 XSS (Cross-Site Scripting)
**¿Qué es?** Un atacante inyecta código JavaScript malicioso en la página.

**¿Cómo lo prevenimos?**
- Angular **escapa automáticamente** todo el HTML en las plantillas (usando `{{ }}` internamente)
- No usamos `innerHTML` directamente
- Los datos que vienen del backend se tratan como texto, no como código

**Ejemplo**: Si un usuario escribe como nombre `<script>alert('hackeado')</script>`, Angular lo muestra como texto, NO lo ejecuta como código.

#### 🔴 CSRF (Cross-Site Request Forgery)
**¿Qué es?** Un atacante engaña al navegador para que haga peticiones no autorizadas usando la sesión del usuario.

**¿Cómo lo prevenimos?**
- Spring Security por defecto protege contra CSRF
- Las peticiones se autentican mediante cookies de sesión (JSESSIONID)
- Aunque el CSRF está desactivado en la configuración (porque es una API REST), las peticiones requieren autenticación

#### 🔴 Inyección SQL
**¿Qué es?** Un atacante introduce comandos SQL en los campos de formulario.

**¿Cómo lo prevenimos?**
- **JPA/Hibernate usa consultas parametrizadas** por defecto, que escapan automáticamente los valores
- NO concatenamos strings para construir SQL (eso sería vulnerable)
- Las validaciones en backend (`@Valid`, `@NotBlank`, etc.) filtran datos maliciosos

**Ejemplo**: Si alguien escribe `' OR 1=1; DROP TABLE usuarios;--` en un campo, JPA lo trata como texto, no como SQL.

#### 🔴 Manipulación de Roles desde Frontend
**¿Qué es?** Un usuario intenta cambiar su rol modificando el código JavaScript.

**¿Cómo lo prevenimos?**
- **Doble capa de control**: El frontend puede ocultar botones de admin, pero el BACKEND es el que realmente valida los permisos
- En `SecurityConfig.java`: `requestMatchers("/api/admin/**").hasRole("ADMIN")`
- Aunque alguien modifique el frontend para mostrar botones de admin, el backend rechazará las peticiones

#### 🔴 Manipulación de IDs en la URL
**¿Qué es?** Un usuario cambia el ID en la URL para ver datos de otro usuario.

**¿Cómo lo prevenimos?**
- En los servicios del backend se verifica que el usuario autenticado sea el propietario de los datos
- No basta con que el ID exista, debe pertenecer al usuario que hace la petición

#### 🔴 Exposición de la API
**¿Qué es?** Alguien descubre los endpoints de la API y los llama directamente.

**¿Cómo lo prevenimos?**
- Todas las rutas excepto `/api/auth/**` requieren autenticación
- Las rutas `/api/admin/**` requieren rol ADMIN
- Las contraseñas se cifran con BCrypt

### 5.3. Resumen: ¿Es hackeable?

| Vulnerabilidad | ¿Protegido? | Cómo |
|---------------|------------|------|
| XSS | ✅ Sí | Angular escapa HTML automáticamente |
| CSRF | ✅ Sí | Cookies de sesión + autenticación |
| SQL Injection | ✅ Sí | JPA usa consultas parametrizadas |
| Manipulación de roles | ✅ Sí | Backend valida roles, no el frontend |
| Acceso no autorizado | ✅ Sí | Spring Security protege las rutas |
| Contraseñas | ✅ Sí | Cifradas con BCrypt |

**Respuesta completa para el tribunal:**
> *"El frontend en sí mismo no es hackeable en el sentido de que un atacante pueda robar datos o realizar acciones no autorizadas solo manipulando el frontend, porque toda la seguridad real está en el backend. Angular protege contra XSS escapando el HTML automáticamente. JPA protege contra inyección SQL usando consultas parametrizadas. Spring Security protege las rutas según el rol del usuario. Aunque alguien modificara el frontend, el backend rechazaría cualquier petición no autorizada. Las contraseñas se almacenan cifradas con BCrypt. La única vía de ataque real sería que alguien robara la sesión de otro usuario, pero eso requeriría acceso físico a su ordenador o interceptar la red."*

---

## 6. BASE DE DATOS: TODO LO QUE NO ESTÁ EN LA DEFENSA

### 6.1. Las 12 tablas del sistema

| # | Tabla | Propósito | ¿FK? |
|---|-------|-----------|------|
| 1 | `usuarios` | Usuarios del sistema (USER/ADMIN) | No |
| 2 | `centros_privados_base` | Centros gestionados por admin | No |
| 3 | `centros_privados_usuario` | Centros creados por usuarios | FK → usuarios |
| 4 | `lugares_publicos_base` | Lugares gestionados por admin | No |
| 5 | `lugares_publicos_usuario` | Lugares creados por usuarios | FK → usuarios |
| 6 | `entrenamientos_base` | Entrenamientos predefinidos por admin | No |
| 7 | `entrenamientos_usuario` | Entrenamientos creados por usuarios | FK → usuarios |
| 8 | `entrenamientos_comunidad` | Entrenamientos compartidos | FK → usuarios |
| 9 | `historial_entrenamientos` | Registro de actividad | FK → usuarios + 6 FK opcionales |
| 10 | `valoraciones` | Valoraciones de elementos | FK → usuarios + Unique Constraint |
| 11 | `soporte` | Tickets de soporte | FK → usuarios |
| 12 | `mensajes_soporte` | Mensajes dentro de tickets | FK → soporte + FK → usuarios |

### 6.2. Los 6 Enums

| Enum | Valores | ¿Dónde está? |
|------|---------|-------------|
| `Rol` | USER, ADMIN | `model/Enums/Rol.java` |
| `TipoLugarPublico` | PARQUE_PUBLICO, PLAYA_DEPORTIVA, PARQUE_CALISTENIA, CARRIL_BICI, ZONA_MULTIDEPORTE, RUTA_RUNNING, CIRCUITO_CICLISMO | `model/Enums/TipoLugarPublico.java` |
| `CategoriaEntrenamientoComunidad` | FUERZA_TOTAL, RUNNING, HIIT, FUERZA_TREN_INFERIOR, FUERZA_TREN_SUPERIOR, CALISTENIA_BASICA, MOVILIDAD, CICLISMO, CROSSFIT, RECUPERACION | `model/Enums/CategoriaEntrenamientoComunidad.java` |
| `NivelEntrenamiento` | PRINCIPIANTE, INTERMEDIO, AVANZADO | `model/Enums/NivelEntrenamiento.java` |
| `TipoDeValoracion` | Define qué se valora (centro, lugar, entrenamiento) | `model/Enums/TipoDeValoracion.java` |
| `EstadoSoporte` | ABIERTO, EN_PROCESO, CERRADO | `model/Enums/EstadoSoporte.java` |

### 6.3. Datos Iniciales (data.sql)

**Ubicación**: `Backend/digital-fit/src/main/resources/data.sql`

Se ejecuta al iniciar la aplicación y carga:
- **10 entrenamientos base**: Full Body, Cardio, HIIT, Piernas, Espalda, Calistenia, Movilidad, Ciclismo, Crossfit, Recuperación
- **6 centros privados**: AltaFit, DreamFit, Basic-Fit, Synergym, Fitness Park, Metropolitan
- **8 lugares públicos**: Jardín del Turia, Malvarrosa, Parque Cabecera, Zona Calistenia, Carril Bici, Polideportivo Benimaclet, Ruta Running, Circuito Ciclismo
- **2 usuarios**: admin1 (ADMIN) y usuario1 (USER) con contraseñas cifradas

### 6.4. application.properties

**Ubicación**: `Backend/digital-fit/src/main/resources/application.properties`

Configura:
- URL de conexión a MySQL
- Usuario y contraseña de BD
- Dialecto JPA
- Configuración de Hibernate (crear/actualizar tablas)

---

## 7. GUÍA DE UBICACIÓN DE CÓDIGO CLAVE

### 7.1. Backend (todos bajo `Backend/digital-fit/src/main/java/com/example/digital_fit/`)

**Configuración**
| ¿Qué buscas? | Ruta exacta |
|-------------|------------|
| Clase principal (main) | `DigitalFitApplication.java` |
| Seguridad (login, logout, roles) | `config/SecurityConfig.java` |
| CORS (comunicación Angular) | `config/CorsConfig.java` |

**Controladores**
| ¿Qué buscas? | Ruta exacta |
|-------------|------------|
| Registro y sesión | `controller/Auth/AuthController.java` |
| Centros privados base | `controller/CentroPrivado/CentroPrivadoBaseRestController.java` |
| Centros privados de usuario | `controller/CentroPrivado/CentroPrivadoUsuarioRestController.java` |
| Admin gestiona centros | `controller/CentroPrivado/Admin/CentroPrivadoBaseAdminRestController.java` |
| Lugares públicos base | `controller/LugarPublico/LugarPublicoBaseRestController.java` |
| Lugares públicos de usuario | `controller/LugarPublico/LugarPublicoUsuarioRestController.java` |
| Admin gestiona lugares | `controller/LugarPublico/Admin/LugarPublicoBaseAdminRestController.java` |
| Entrenamientos base | `controller/Entrenamientos/EntrenamientoBaseRestController.java` |
| Entrenamientos de usuario | `controller/Entrenamientos/EntrenamientoUsuarioRestController.java` |
| Entrenamientos comunidad | `controller/Entrenamientos/EntrenamientoComunidadRestController.java` |
| Historial entrenamientos | `controller/Entrenamientos/HistorialEntrenamientosRestController.java` |
| Admin gestiona entrenamientos | `controller/Entrenamientos/Admin/EntrenamientoBaseAdminRestController.java` |
| Estadísticas | `controller/Estadisticas/EstadisticasRestController.java` |
| Soporte (usuario) | `controller/Soporte/SoporteRestController.java` |
| Soporte (admin) | `controller/Soporte/Admin/SoporteAdminRestController.java` |
| Valoraciones | `controller/Valoracion/ValoracionRestController.java` |

**Modelos (Entidades JPA)**
| ¿Qué buscas? | Ruta exacta |
|-------------|------------|
| Usuario | `model/Auth/Usuario.java` |
| Centro privado base | `model/CentroPrivado/CentroPrivadoBase.java` |
| Centro privado usuario | `model/CentroPrivado/CentroPrivadoUsuario.java` |
| Lugar público base | `model/LugarPublico/LugarPublicoBase.java` |
| Lugar público usuario | `model/LugarPublico/LugarPublicoUsuario.java` |
| Entrenamiento base | `model/Entrenamientos/EntrenamientoBase.java` |
| Entrenamiento usuario | `model/Entrenamientos/EntrenamientoUsuario.java` |
| Entrenamiento comunidad | `model/Entrenamientos/EntrenamientoComunidad.java` |
| Historial (¡la compleja!) | `model/Entrenamientos/HistorialEntrenamientos.java` |
| Valoración | `model/Valoracion/Valoracion.java` |
| Soporte (ticket) | `model/Soporte/Soporte.java` |
| Mensaje soporte | `model/Soporte/MensajeSoporte.java` |

**Enumeraciones** (todas en `model/Enums/`)
| ¿Qué buscas? | Archivo |
|-------------|---------|
| Roles (USER, ADMIN) | `Rol.java` |
| Tipo lugar público | `TipoLugarPublico.java` |
| Categoría entrenamiento | `CategoriaEntrenamientoComunidad.java` |
| Nivel entrenamiento | `NivelEntrenamiento.java` |
| Tipo valoración | `TipoDeValoracion.java` |
| Estado soporte | `EstadoSoporte.java` |

**Servicios**
| ¿Qué buscas? | Ruta desde `service/` |
|-------------|----------------------|
| Auth (registro, sesión) | `Auth/AuthService.java` |
| Centros base | `CentroPrivado/CentroPrivadoBaseService.java` |
| Estadísticas | `Estadisticas/EstadisticasService.java` |
| Soporte | `Soporte/SoporteService.java` |

**Repositorios**
| ¿Qué buscas? | Ruta desde `repository/` |
|-------------|------------------------|
| Usuario | `Auth/UsuarioRepository.java` |
| Valoración | `Valoracion/ValoracionRepository.java` |

**Otros importantes**
| ¿Qué buscas? | Ruta |
|-------------|------|
| Manejador global de errores | `handler/GlobalExceptionHandler.java` |
| Excepción "recurso no encontrado" | `exception/RecursoNoEncontradoException.java` |
| Excepción "email ya existe" | `exception/EmailYaExisteException.java` |
| Excepción "username ya existe" | `exception/UsernameYaExiste.java` |
| Datos iniciales BD | `Backend/digital-fit/src/main/resources/data.sql` |
| Configuración BD | `Backend/digital-fit/src/main/resources/application.properties` |
| Dependencias Maven | `Backend/digital-fit/pom.xml` |

### 7.2. Frontend (todos bajo `Frontend/digital-fit-frontend/src/app/`)

| ¿Qué buscas? | Ruta |
|-------------|------|
| Rutas del frontend | `app.routes.ts` |
| Componente raíz | `app.ts` |
| Header (navegación) | `components/header/header.ts` |
| Footer | `components/footer/footer.ts` |
| Mapa Leaflet | `components/mapa-selector/mapa-selector.ts` |
| Login | `pages/login/login.ts` |
| Register | `pages/register/register.ts` |
| Inicio | `pages/inicio/inicio.ts` |
| Auth guard | `guards/auth-guard.ts` |
| Admin guard | `guards/admin-guard.ts` |
| User guard | `guards/user-guard.ts` |
| Auth service | `services/auth-service.ts` |
| Soporte service | `services/soporte.ts` |

---

## 8. PREGUNTAS FRECUENTES DEL TRIBUNAL (CON RESPUESTAS)

### 🗄️ PREGUNTAS SOBRE BD Y RELACIONES

**P: ¿Qué tipo de relación hay entre Usuario y CentroPrivadoUsuario?**
**R**: Relación **1:N (OneToMany/ManyToOne)**. Un usuario crea muchos centros; cada centro pertenece a un usuario. En `CentroPrivadoUsuario.java`: `@ManyToOne @JoinColumn(name = "usuario_id")`.

**P: ¿Por qué hay tablas BASE y USUARIO separadas?**
**R**: Es un **patrón de diseño** para separar el contenido gestionado por administradores (BASE) del contenido creado por usuarios (USUARIO). Las tablas BASE no tienen FK a usuario, son fijas. Las tablas USUARIO tienen FK a usuario y campo `activo` para soft-delete. Así la plataforma tiene contenido de calidad gestionado por admins pero permite contribución de usuarios.

**P: ¿Cuántas relaciones tiene HistorialEntrenamientos?**
**R**: Tiene **7 relaciones ManyToOne**: 1 obligatoria (`usuario`) y 6 opcionales (entrenamiento base, entrenamiento usuario, lugar público base, lugar público usuario, centro privado base, centro privado usuario). Las opcionales pueden ser NULL para permitir registros libres.

**P: ¿Qué restricción única tiene Valoración?**
**R**: `@UniqueConstraint(columnNames = {"usuario_id", "tipo_valoracion", "id_relacionado"})`. Un usuario no puede valorar dos veces el mismo elemento.

**P: ¿Cómo se guardan los Enums en BD?**
**R**: Con `@Enumerated(EnumType.STRING)`. Se guarda el nombre del enum como texto VARCHAR (ej: "ADMIN", "ABIERTO"). Más legible y seguro que valores ordinales.

### 🏗️ PREGUNTAS SOBRE ARQUITECTURA Y CÓDIGO

**P: ¿Qué patrón sigue el backend?**
**R**: **MVC adaptado a REST**: Controller (peticiones HTTP) → Service (lógica) → Repository (BD). Las entidades JPA son el modelo, los DTOs transfieren datos.

**P: ¿Qué es Lombok y qué anotaciones usas?**
**R**: Lombok reduce código boilerplate. Uso: `@Data` (getters/setters/toString/equals/hashCode), `@Builder` (patrón Builder), `@NoArgsConstructor`/`@AllArgsConstructor` (constructores).

**P: ¿Cómo se manejan los errores?**
**R**: Con `GlobalExceptionHandler.java` anotado con `@ControllerAdvice`. Captura excepciones personalizadas (RecursoNoEncontrado, EmailYaExiste, etc.) y devuelve la respuesta HTTP con el código adecuado.

**P: ¿Dónde se define cada cosa?** (usa la tabla de ubicación de la sección 7)

### 🔒 PREGUNTAS SOBRE SEGURIDAD (¡IMPORTANTE!)

**P: ¿El frontend es hackeable?**
**R**: No, porque la seguridad real está en el backend. El frontend (Angular) escapa HTML automáticamente (protege contra XSS). JPA usa consultas parametrizadas (protege contra SQL injection). Spring Security protege las rutas según el rol (aunque alguien modifique el frontend, el backend rechaza peticiones no autorizadas). Las contraseñas se almacenan cifradas con BCrypt. Es lo que llamamos **"defense in depth"**: múltiples capas de seguridad.

**P: ¿Cómo se autentican los usuarios?**
**R**: Spring Security con formulario HTTP. El usuario envía username+password a `/api/auth/login`. Se verifican contra la BD con BCrypt y se crea una sesión HTTP con cookie JSESSIONID.

**P: ¿Cómo se evita que un USER acceda a rutas de ADMIN?**
**R**: **Doble capa**: 1) Backend: `SecurityConfig.java` protege `/api/admin/**` con `hasRole("ADMIN")`. 2) Frontend: `adminGuard` redirige si el rol no es ADMIN.

**P: ¿Qué es CORS?**
**R**: Mecanismo que permite que el frontend (puerto 4200) se comunique con el backend (puerto 8080) aunque estén en orígenes distintos. Sin CORS, el navegador bloquearía las peticiones.

### 💻 PREGUNTAS SOBRE FUNCIONALIDADES ESPECÍFICAS

**P: ¿Cómo funciona el registro de usuarios?**
**R**: En `AuthService.java`: 1) Validar que username no exista, 2) Validar que email no exista, 3) Cifrar password con BCrypt, 4) Crear usuario con rol USER, 5) Guardar en BD.

**P: ¿Cómo funciona el módulo de soporte?**
**R**: El usuario crea un ticket (Soporte) con asunto y mensaje. El admin ve todos los tickets, cambia el estado (ABIERTO/EN_PROCESO/CERRADO) y responde con mensajes (MensajeSoporte). Relación: Soporte 1:N MensajeSoporte.

**P: ¿Qué diferencia hay entre EntrenamientoUsuario y EntrenamientoComunidad?**
**R**: EntrenamientoUsuario es **privado** (solo lo ve su creador). EntrenamientoComunidad es **público** (lo ve toda la comunidad, tiene fecha de publicación).

**P: ¿Para qué sirve el campo `activo`?**
**R**: Implementa **soft-delete** (borrado lógico). Cuando el usuario "elimina" un elemento, se marca como `activo = false` en lugar de borrarlo físicamente. Así los datos no se pierden y las referencias existentes no se rompen.

### ⚙️ PREGUNTAS TÉCNICAS GENERALES

**P: ¿Por qué JPA/Hibernate y no SQL directamente?**
**R**: JPA mapea objetos Java a tablas de BD automáticamente (ORM). Elimina la necesidad de escribir SQL manual, las relaciones se definen con anotaciones y Hibernate genera automáticamente las tablas.

**P: ¿Qué validaciones tiene la aplicación?**
**R**: **Frontend**: validaciones en formularios Angular. **Backend**: validaciones con Jakarta Validation (`@Valid`, `@NotBlank`, `@Email`, `@Size`) en DTOs + lógica en servicios (unicidad de username/email, etc.).

**P: ¿Qué mejoras futuras tiene el proyecto?**
**R**: App móvil, despliegue en producción, estadísticas avanzadas, recomendaciones con IA, expansión a otras ciudades, colaboraciones con gimnasios.

---

## 🎯 CONSEJOS FINALES

1. **El tribunal preguntará sobre lo que NO está en tu defensa**: estructura del código, JPA, DTOs, excepciones, seguridad profunda.
2. **Para la pregunta "¿es hackeable el frontend?"** usa la respuesta de la sección 5.
3. **Para ubicación de código** usa la tabla de la sección 7.
4. **Para relaciones BD** tienes el documento específico `relaciones-bd-explicadas.md`.
5. **Para el diagrama ER** tienes `diagrama-er.md`.

**¡Mucha suerte el martes! 🚀**