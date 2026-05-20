# Defensa tecnica - Digital Fit

Documento de estudio para la defensa del proyecto. La idea es que puedas explicar que hace la aplicacion, donde esta cada parte del codigo y responder preguntas habituales del tribunal.

Nota: este documento es la guia tecnica amplia. Para estudiar de forma mas directa segun tu presentacion, usa tambien `GUIA_ESTUDIO_TRIBUNAL_DIGITAL_FIT.md`. Para aprender las relaciones de base de datos paso a paso, usa `GUIA_RELACIONES_BD_DIGITAL_FIT.md`.

## 1. Resumen del proyecto

Digital Fit es una aplicacion web para gestionar actividad fisica. Permite a un usuario:

- Registrarse e iniciar sesion.
- Consultar entrenamientos base creados por la aplicacion.
- Crear entrenamientos propios.
- Publicar entrenamientos para la comunidad.
- Consultar centros privados y lugares publicos donde entrenar.
- Guardar centros/lugares personalizados en su zona privada.
- Registrar entrenamientos realizados en un historial.
- Ver estadisticas personales.
- Crear valoraciones sobre entrenamientos, centros y lugares.
- Abrir tickets de soporte.

Tambien existe un rol administrador, que puede gestionar contenido base y soporte.

## 2. Stack tecnico

Backend:

- Spring Boot, en `Backend/digital-fit`.
- Java 25, configurado en `Backend/digital-fit/pom.xml`.
- Spring Web MVC para API REST.
- Spring Data JPA para acceso a base de datos.
- Spring Security para login, sesiones y roles.
- MySQL como base de datos.
- Lombok para reducir getters, setters, constructores y builders.
- Bean Validation para validar DTOs.

Frontend:

- Angular, en `Frontend/digital-fit-frontend`.
- Rutas definidas en `src/app/app.routes.ts`.
- Servicios HTTP en `src/app/services`.
- Guards de navegacion en `src/app/guards`.
- Leaflet para mapas.

Despliegue:

- `docker-compose.yml` levanta MySQL, backend y frontend.
- Backend escucha en `8080`.
- Frontend se sirve con Nginx en `4200`.
- Nginx reenvia `/api/` al backend mediante `Frontend/digital-fit-frontend/nginx.conf`.

## 3. Estructura del repositorio

- `Backend/digital-fit/src/main/java/com/example/digital_fit/model`: entidades JPA.
- `Backend/digital-fit/src/main/java/com/example/digital_fit/controller`: endpoints REST.
- `Backend/digital-fit/src/main/java/com/example/digital_fit/service`: logica de negocio.
- `Backend/digital-fit/src/main/java/com/example/digital_fit/repository`: repositorios de acceso a datos.
- `Backend/digital-fit/src/main/java/com/example/digital_fit/dto`: objetos que entran o salen por la API.
- `Backend/digital-fit/src/main/java/com/example/digital_fit/config`: seguridad y CORS.
- `Backend/digital-fit/src/main/resources/application.properties`: configuracion de BD, JPA y logs.
- `Backend/digital-fit/src/main/resources/data.sql`: datos iniciales.
- `Frontend/digital-fit-frontend/src/app/pages`: pantallas Angular.
- `Frontend/digital-fit-frontend/src/app/services`: llamadas al backend.
- `Frontend/digital-fit-frontend/src/app/components`: componentes compartidos.
- `Frontend/digital-fit-frontend/src/app/guards`: proteccion de rutas.

## 4. Arquitectura general

El flujo normal es:

1. El usuario interactua con una pagina Angular.
2. La pagina llama a un servicio Angular.
3. El servicio usa `HttpClient` contra una URL `/api/...`.
4. Nginx, en Docker, redirige esa peticion al backend Spring Boot.
5. Un controller recibe la peticion.
6. El controller delega en un service.
7. El service aplica reglas de negocio, valida permisos y usa repositories.
8. Los repositories consultan o modifican MySQL mediante JPA.
9. El backend devuelve DTOs al frontend.

Este patron se repite en casi todo el proyecto: Controller -> Service -> Repository -> Entity.

## 5. Seguridad y autenticacion

La seguridad esta en `Backend/digital-fit/src/main/java/com/example/digital_fit/config/SecurityConfig.java`.

Puntos clave:

- Se usa Spring Security con login basado en formulario, no JWT.
- El endpoint real de login es `/api/auth/login`.
- El logout es `/api/auth/logout`.
- El registro y consultas de autenticacion estan bajo `/api/auth/**`.
- Los endpoints `/api/admin/**` requieren rol `ADMIN`.
- El resto de endpoints requieren sesion iniciada.
- CSRF esta desactivado para facilitar consumo desde Angular.
- Las passwords se cifran con `BCryptPasswordEncoder`.

La carga de usuarios para Spring Security esta en:

- `service/Auth/CustomUserDetailsService.java`.

Ese servicio busca primero por username y, si no existe, por email. Despues devuelve un `UserDetails` con el rol del usuario.

El registro esta en:

- `controller/Auth/AuthController.java`.
- `service/Auth/AuthService.java`.

En `AuthService.registrar` se valida que username y email no existan, se cifra la password y se guarda el usuario con rol `USER`. La creacion de administradores usa `registrarAdmin` y asigna rol `ADMIN`.

En Angular, la proteccion de pantallas esta en:

- `guards/auth-guard.ts`: deja pasar a cualquier usuario autenticado.
- `guards/user-guard.ts`: deja pasar solo a `USER`.
- `guards/admin-guard.ts`: deja pasar solo a `ADMIN`.
- `guards/public-guard.ts`: evita que un usuario ya logueado vuelva a login/register.

## 6. Entidades principales

Todas las entidades estan en `Backend/digital-fit/src/main/java/com/example/digital_fit/model`.

### Usuario

Archivo: `model/Auth/Usuario.java`

Tabla: `usuarios`

Campos importantes:

- `id`: clave primaria.
- `username`: unico y obligatorio.
- `email`: unico y obligatorio.
- `password`: obligatoria y cifrada.
- `rol`: enum `USER` o `ADMIN`.

Es la entidad central. Muchas tablas tienen relacion `ManyToOne` hacia usuario.

### EntrenamientoBase

Archivo: `model/Entrenamientos/EntrenamientoBase.java`

Tabla: `entrenamientos_base`

Representa entrenamientos creados por la aplicacion o el administrador. Tiene nombre unico, descripcion, categoria, nivel y duracion.

Se consulta desde:

- `controller/Entrenamientos/EntrenamientoBaseRestController.java`.
- `service/Entrenamientos/EntrenamientoBaseService.java`.

El admin lo gestiona desde:

- `controller/Entrenamientos/Admin/EntrenamientoBaseAdminRestController.java`.

### EntrenamientoUsuario

Archivo: `model/Entrenamientos/EntrenamientoUsuario.java`

Tabla: `entrenamientos_usuario`

Representa entrenamientos guardados por un usuario. Puede nacer de:

- Un entrenamiento creado manualmente.
- Una copia desde entrenamiento base.
- Una copia desde entrenamiento de comunidad.

Relacion:

- Muchos entrenamientos de usuario pertenecen a un usuario.

El borrado es logico mediante `activo = false`, para no romper historiales antiguos.

### EntrenamientoComunidad

Archivo: `model/Entrenamientos/EntrenamientoComunidad.java`

Tabla: `entrenamientos_comunidad`

Representa entrenamientos publicados por usuarios para que otros puedan verlos o copiarlos.

Relacion:

- Muchos entrenamientos de comunidad pertenecen a un usuario autor.

La logica esta en:

- `controller/Entrenamientos/EntrenamientoComunidadRestController.java`.
- `service/Entrenamientos/EntrenamientoComunidadService.java`.

### HistorialEntrenamientos

Archivo: `model/Entrenamientos/HistorialEntrenamientos.java`

Tabla: `historial_entrenamientos`

Registra que un usuario ha hecho un entrenamiento en una fecha, durante X minutos y opcionalmente en una ubicacion.

Relaciones:

- Pertenece siempre a un usuario.
- Debe apuntar a un unico entrenamiento: base o de usuario.
- Puede apuntar a una unica ubicacion: lugar publico base, lugar publico de usuario, centro privado base o centro privado de usuario.

La regla de negocio esta en `service/Entrenamientos/HistorialEntrenamientosService.java`:

- Si se mandan entrenamiento base y entrenamiento usuario a la vez, lanza error.
- Si no se manda ninguno de los dos, lanza error.
- Si se manda mas de una ubicacion, lanza error.
- Si se usa un recurso privado del usuario, comprueba que pertenezca al usuario autenticado.

Esta entidad es clave para explicar estadisticas, porque las estadisticas se calculan sobre el historial.

### CentroPrivadoBase

Archivo: `model/CentroPrivado/CentroPrivadoBase.java`

Tabla: `centros_privados_base`

Representa gimnasios o centros cargados en el catalogo general. Tiene nombre, direccion, telefono, horario, precio, descripcion y coordenadas.

Gestion normal:

- `controller/CentroPrivado/CentroPrivadoBaseRestController.java`.
- `service/CentroPrivado/CentroPrivadoBaseService.java`.

Gestion admin:

- `controller/CentroPrivado/Admin/CentroPrivadoBaseAdminRestController.java`.

### CentroPrivadoUsuario

Archivo: `model/CentroPrivado/CentroPrivadoUsuario.java`

Tabla: `centros_privados_usuario`

Representa centros guardados por un usuario en su zona personal. Pueden venir de un centro base o de datos seleccionados desde mapa.

Relacion:

- Muchos centros privados de usuario pertenecen a un usuario.

Tiene `activo` para borrado logico.

### LugarPublicoBase

Archivo: `model/LugarPublico/LugarPublicoBase.java`

Tabla: `lugares_publicos_base`

Representa espacios publicos del catalogo: parques, rutas, pistas, playas, etc. Usa el enum `TipoLugarPublico`.

### LugarPublicoUsuario

Archivo: `model/LugarPublico/LugarPublicoUsuario.java`

Tabla: `lugares_publicos_usuario`

Representa lugares publicos guardados o personalizados por un usuario.

Relacion:

- Muchos lugares publicos de usuario pertenecen a un usuario.

Tambien tiene `activo` para borrado logico.

### Valoracion

Archivo: `model/Valoracion/Valoracion.java`

Tabla: `valoraciones`

Sirve para valorar distintos tipos de contenido: entrenamientos, centros, lugares e historial.

Campos clave:

- `puntuacion`: entre 1 y 5.
- `comentario`.
- `fecha`.
- `tipoDeValoracion`: enum con el tipo de contenido.
- `idRelacionado`: id del contenido valorado.
- `usuario`: usuario que valora.

Relacion importante:

- Hay FK real con `usuarios`.
- La relacion con el contenido valorado es logica: `tipo_valoracion + id_relacionado`.

Esto se puede explicar asi: se eligio una relacion polimorfica para que una sola tabla de valoraciones pueda servir para varios tipos de contenido. La integridad se comprueba en `ValoracionService.validarContenidoExisteYPermisos`.

Tambien hay una restriccion unica:

- Un usuario solo puede tener una valoracion por cada combinacion de tipo de contenido e id relacionado.

### Soporte

Archivo: `model/Soporte/Soporte.java`

Tabla: `soporte`

Representa un ticket de soporte. Tiene asunto, mensaje inicial, fecha, estado y usuario propietario.

Relacion:

- Muchos tickets pertenecen a un usuario.
- Un ticket tiene muchos mensajes.

### MensajeSoporte

Archivo: `model/Soporte/MensajeSoporte.java`

Tabla: `mensajes_soporte`

Representa mensajes dentro de un ticket.

Relaciones:

- Muchos mensajes pertenecen a un ticket.
- Muchos mensajes tienen un usuario emisor.

En `Soporte.java`, la relacion con mensajes usa `cascade = CascadeType.ALL` y `orphanRemoval = true`, por lo que al eliminar un ticket se eliminan sus mensajes asociados.

## 7. Relaciones de base de datos

Relaciones directas:

- `usuarios 1:N entrenamientos_usuario`.
- `usuarios 1:N entrenamientos_comunidad`.
- `usuarios 1:N centros_privados_usuario`.
- `usuarios 1:N lugares_publicos_usuario`.
- `usuarios 1:N historial_entrenamientos`.
- `usuarios 1:N valoraciones`.
- `usuarios 1:N soporte`.
- `usuarios 1:N mensajes_soporte` como emisor.
- `soporte 1:N mensajes_soporte`.
- `entrenamientos_base 1:N historial_entrenamientos`.
- `entrenamientos_usuario 1:N historial_entrenamientos`.
- `lugares_publicos_base 1:N historial_entrenamientos`.
- `lugares_publicos_usuario 1:N historial_entrenamientos`.
- `centros_privados_base 1:N historial_entrenamientos`.
- `centros_privados_usuario 1:N historial_entrenamientos`.

Relacion logica:

- `valoraciones.tipo_valoracion + valoraciones.id_relacionado` apunta a uno de varios tipos posibles de contenido.

## 8. Endpoints principales del backend

Autenticacion:

- `POST /api/auth/login`: login gestionado por Spring Security.
- `POST /api/auth/registro`: registro de usuario.
- `GET /api/auth/yo`: devuelve el username si hay sesion.
- `GET /api/auth/me`: devuelve username, email y rol.
- `POST /api/auth/logout`: cierre de sesion.

Catalogos:

- `GET /api/entrenamientos`: lista o filtra entrenamientos base.
- `GET /api/entrenamientos/{id}`: detalle de entrenamiento base.
- `GET /api/centros-privados`: lista o filtra centros privados base.
- `GET /api/centros-privados/{id}`: detalle de centro privado base.
- `GET /api/lugares-publicos`: lista o filtra lugares publicos base.
- `GET /api/lugares-publicos/{id}`: detalle de lugar publico base.

Zona del usuario:

- `GET /api/mis-entrenamientos`: lista entrenamientos del usuario.
- `POST /api/mis-entrenamientos`: crea entrenamiento propio.
- `POST /api/mis-entrenamientos/desde-base/{idBase}`: copia entrenamiento base.
- `POST /api/mis-entrenamientos/desde-comunidad/{idComunidad}`: copia entrenamiento de comunidad.
- `DELETE /api/mis-entrenamientos/{id}`: borrado logico.
- `GET /api/mis-centros-privados`: lista centros guardados.
- `POST /api/mis-centros-privados/centros-app/{id}`: guarda centro base.
- `POST /api/mis-centros-privados/centros-maps`: guarda centro desde mapa.
- `GET /api/mis-lugares-publicos`: lista lugares guardados.
- `POST /api/mis-lugares-publicos`: crea lugar publico personalizado.
- `POST /api/mis-lugares-publicos/base/{idBase}`: guarda lugar base.

Comunidad e historial:

- `GET /api/entrenamientos-comunidad`: lista entrenamientos publicados.
- `POST /api/entrenamientos-comunidad`: publica entrenamiento.
- `PUT /api/entrenamientos-comunidad/{id}`: actualiza entrenamiento propio publicado.
- `DELETE /api/entrenamientos-comunidad/{id}`: elimina entrenamiento propio publicado.
- `GET /api/entrenamientos/mi-historial`: lista historial.
- `POST /api/entrenamientos/mi-historial`: registra entrenamiento realizado.
- `DELETE /api/entrenamientos/mi-historial/{id}`: elimina registro.

Valoraciones y estadisticas:

- `GET /api/valoraciones/mis-valoraciones`: valoraciones del usuario.
- `GET /api/valoraciones/publicas`: valoraciones publicas.
- `GET /api/valoraciones/{tipoContenido}/{idContenido}`: valoraciones de un contenido.
- `POST /api/valoraciones/{tipoContenido}/{idContenido}`: crea o actualiza valoracion.
- `DELETE /api/valoraciones/{tipoContenido}/{idContenido}`: borra valoracion propia.
- `GET /api/estadisticas`: obtiene estadisticas del usuario autenticado.

Soporte:

- `POST /api/soporte`: crea ticket.
- `GET /api/soporte/mis-tickets`: lista tickets del usuario.
- `GET /api/soporte/mis-tickets/{id}`: detalle de ticket.
- `GET /api/soporte/mis-tickets/{id}/mensajes`: mensajes del ticket.
- `POST /api/soporte/mis-tickets/{id}/mensajes`: responde a un ticket.

Admin:

- `POST /api/admin/crear-admin`: crea otro admin.
- `POST/PUT/DELETE /api/admin/entrenamientos-base`: gestiona entrenamientos base.
- `POST/PUT/DELETE /api/admin/lugares-publicos-base`: gestiona lugares publicos base.
- `POST/PUT/DELETE /api/admin/centros-privados-base`: gestiona centros privados base.
- `GET/PATCH/DELETE /api/admin/soporte`: gestiona tickets.

## 9. Frontend Angular

Rutas principales:

Archivo: `Frontend/digital-fit-frontend/src/app/app.routes.ts`

- `/login` y `/register`: pantallas publicas.
- `/inicio`: pantalla principal tras login.
- `/privados` y `/privados/:id`: catalogo y detalle de centros privados.
- `/publicos` y `/publicos/:id`: catalogo y detalle de lugares publicos.
- `/entrenamientos` y `/entrenamientos/:id`: catalogo de entrenamientos base.
- `/entrenamientos-comunidad`: entrenamientos compartidos por usuarios.
- `/mis-entrenamientos`: entrenamientos guardados/creados por el usuario.
- `/mis-centros`: centros privados guardados.
- `/mis-lugares`: lugares publicos guardados.
- `/historial-entrenamientos`: historial del usuario.
- `/estadisticas`: estadisticas personales.
- `/soporte`: tickets del usuario.
- `/valoraciones`: valoraciones.
- `/admin/soporte` y `/admin/crear-admin`: zona admin.

Servicios Angular:

- `services/auth-service.ts`: login, registro, sesion y logout.
- `services/entrenamientos/entrenamientos-service.ts`: entrenamientos base.
- `services/entrenamientos/mis-entrenamientos-service.ts`: entrenamientos del usuario.
- `services/entrenamientos/entrenamiento-comunidad-service.ts`: comunidad.
- `services/entrenamientos/historial-entrenamientos.ts`: historial.
- `services/centros/privados-service.ts`: centros privados base.
- `services/centros/mis-centros-service.ts`: centros del usuario.
- `services/publicos/publicos-service.ts`: lugares publicos base.
- `services/publicos/mis-lugares.ts`: lugares del usuario.
- `services/valoraciones/valoraciones-service.ts`: valoraciones.
- `services/estadisticas/estadisticas-service.ts`: estadisticas.
- `services/soporte.ts`: soporte del usuario.
- `services/admin/*`: llamadas de administrador.

Detalle importante:

- Las llamadas usan `withCredentials: true` para conservar la sesion de Spring Security mediante cookie.
- `provideHttpClient()` esta en `src/app/app.config.ts`.

## 10. Base de datos y datos iniciales

Configuracion:

- `Backend/digital-fit/src/main/resources/application.properties`.

Puntos clave:

- Base de datos: `digital_fit`.
- En local usa `jdbc:mysql://localhost:3306/digital_fit`.
- En Docker usa la variable `DB_URL` apuntando al servicio `mysql`.
- `spring.jpa.hibernate.ddl-auto=update` hace que Hibernate actualice el esquema segun entidades.
- `spring.sql.init.mode=always` ejecuta `data.sql`.
- `spring.jpa.defer-datasource-initialization=true` espera a que JPA cree tablas antes de insertar datos.

Datos iniciales:

- `Backend/digital-fit/src/main/resources/data.sql`.
- Inserta entrenamientos base.
- Inserta centros privados base.
- Inserta lugares publicos base.
- Inserta un admin inicial.
- Inserta un usuario normal inicial.

Los inserts usan `INSERT IGNORE` para evitar duplicados si el script se ejecuta mas de una vez.

## 11. Estadisticas

Las estadisticas se basan en el historial de entrenamientos.

Archivos:

- `controller/Estadisticas/EstadisticasRestController.java`.
- `service/Estadisticas/EstadisticasService.java`.
- `repository/Estadisticas/EstadisticasRepository.java`.

Calcula:

- Total de entrenamientos realizados.
- Suma de minutos entrenados.
- Promedio de duracion.
- Centros privados distintos visitados.
- Lugares publicos distintos visitados.
- Entrenamiento mas realizado.

Usa consultas JPQL y consultas SQL nativas cuando necesita combinar columnas distintas del historial.

---

## 12. Preguntas Teóricas Esperadas del Tribunal

### 🎓 ARQUITECTURA Y DISEÑO

**P1: ¿Por qué elegiste Spring Boot en lugar de otro framework Java?**

**R1**: 
- Spring Boot automatiza la configuración (spring-boot-starter-web, spring-boot-starter-data-jpa)
- Proporciona Spring Security lista para usar sin código boilerplate
- Tiene excelente integración con MySQL mediante Hibernate
- La comunidad es muy grande, hay mucha documentación
- Para un proyecto DAW, permite que me enfoque en la lógica, no en configuración

Código de ejemplo (`Backend/digital-fit/pom.xml`):
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>4.0.6</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

---

**P2: ¿Por qué dividiste el frontend y backend en contenedores Docker separados?**

**R2**:
- Permite que cada componente escale independientemente
- El backend puede servir múltiples frontends (web, mobile)
- Facilita despliegue: cambiar backend sin tocar frontend
- En producción, frontend (Nginx) es muy ligero, backend tiene más recursos
- Simula un entorno real: frontend en CDN, backend en servidor

Configuración (`docker-compose.yml`):
```yaml
services:
  frontend:
    image: nginx:latest
    ports: ["4200:80"]
    depends_on: [backend]
    
  backend:
    image: openjdk:25
    ports: ["8080:8080"]
    depends_on: [mysql]
    
  mysql:
    image: mysql:8.0
    ports: ["3307:3306"]
```

---

**P3: ¿Cuáles son los patrones de diseño principales en tu proyecto?**

**R3**:
1. **MVC** (Model-View-Controller):
   - Model: Entidades JPA (`Backend/.../model`)
   - View: Componentes Angular (`Frontend/.../src/app`)
   - Controller: REST Controllers (`Backend/.../controller`)

2. **DAO/Repository**: Spring Data JPA (`Backend/.../repository`)
   
3. **Service Layer**: Lógica centralizada (`Backend/.../service`)
   
4. **DTO**: Separar modelos internos de API (`Backend/.../dto`)
   
5. **Guard Pattern**: Proteger rutas Angular (`Frontend/.../guards`)

---

### 🔐 SEGURIDAD

**P4: ¿Cómo está implementada la autenticación? ¿Por qué no JWT?**

**R4**:
- Usé **sesiones basadas en cookies** (Spring Security estándar)
- El navegador guarda la cookie de sesión automáticamente
- Cada petición incluye la cookie → servidor valida la sesión
- Ventaja: No necesito validar token en cada petición (es stateful)
- Para este proyecto DAW está bien; en producción sería JWT

Código (`Backend/.../config/SecurityConfig.java`):
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Desactivo CSRF para consumo desde Angular
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin()
            .loginProcessingUrl("/api/auth/login")
            .defaultSuccessUrl("/api/auth/me");
        return http.build();
    }
}
```

---

**P5: ¿Cómo cifras las contraseñas?**

**R5**:
- Uso `BCryptPasswordEncoder` de Spring Security
- Es un hash adaptativo (cuesta computacional variable)
- No es reversible: si alguien roba la BD, no puede recuperar passwords
- Para validar, calcula hash del input y compara con el guardado

Código (`Backend/.../service/Auth/AuthService.java`):
```java
@Service
public class AuthService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public void registrar(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));  // Encriptado
        usuario.setRol(Rol.USER);
        usuarioRepo.save(usuario);
    }
}
```

---

**P6: ¿Cómo proteges las rutas en Angular?**

**R6**:
- Uso **Guards**: archivos que validan si el usuario puede acceder a una ruta
- Tipos:
  - `publicGuard`: Solo redirige a /inicio si ya está logueado
  - `authGuard`: Requiere cualquier usuario autenticado
  - `userGuard`: Solo rol USER
  - `adminGuard`: Solo rol ADMIN

Código (`Frontend/.../guards/auth-guard.ts`):
```typescript
export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);
    
    return authService.yo().pipe(
        map(response => {
            if (response) return true;  // Si hay sesión, permite
            else {
                router.navigate(['/login']);
                return false;
            }
        }),
        catchError(() => {
            router.navigate(['/login']);
            return of(false);
        })
    );
};
```

---

### 📊 BASE DE DATOS Y RELACIONES

**P7: ¿Cómo organizaste las tablas de entrenamientos? ¿Por qué 3 tipos?**

**R7**:
- `entrenamientos_base`: Catálogo de la app (todos ven lo mismo)
- `entrenamientos_usuario`: Privados del usuario (copias personalizadas)
- `entrenamientos_comunidad`: Publicados por usuarios (para compartir)

Esto permite:
1. Mantener catálogo limpio (admin actualiza base, no afecta copias de usuarios)
2. Que cada usuario personalice sin afectar a otros
3. Que usuarios compartan entrenamientos

**Pregunta de seguimiento**: "¿Qué pasa si alguien copia un entrenamiento y luego lo borro?"
- **Respuesta**: Usamos borrado lógico (`activo = false`), no eliminación física. Así el historial no se rompe.

---

**P8: ¿Cómo funciona el historial? ¿Por qué tiene tantas FKs?**

**R8**:
El historial es el registro de que un usuario hizo X entrenamiento en Y ubicación en Z fecha.

```
1 usuario + (entrenamiento base O usuario) + (ubicación base O usuario O nada) + fecha + duración
```

Tiene muchas FK porque cada parte puede venir de tablas diferentes (base o usuario).

Reglas validadas en `HistorialEntrenamientosService`:
1. Exactamente 1 entrenamiento (base O usuario, no ambos)
2. Como máximo 1 ubicación (puede ser null)
3. Si usas recurso privado, debe ser tuyo

Código de validación:
```java
public void validar(HistorialDTO dto) {
    boolean tieneBase = dto.getEntrenamientoBaseId() != null;
    boolean tieneUsuario = dto.getEntrenamientoUsuarioId() != null;
    
    if ((tieneBase && tieneUsuario) || (!tieneBase && !tieneUsuario)) {
        throw new IllegalArgumentException("Debe haber exactamente 1 entrenamiento");
    }
    
    int ubicaciones = 0;
    if (dto.getLugarPublicoBaseId() != null) ubicaciones++;
    if (dto.getLugarPublicoUsuarioId() != null) ubicaciones++;
    if (dto.getCentroPrivadoBaseId() != null) ubicaciones++;
    if (dto.getCentroPrivadoUsuarioId() != null) ubicaciones++;
    
    if (ubicaciones > 1) {
        throw new IllegalArgumentException("Máximo 1 ubicación");
    }
}
```

---

**P9: ¿Cómo gestionas las valoraciones? ¿Por qué esa estructura?**

**R9**:
Las valoraciones son complejas porque un usuario puede valorar:
- Entrenamientos base, usuario, comunidad
- Centros privados base, usuario
- Lugares públicos base, usuario
- Registros de historial

Opciones:
1. Crear 8 tablas separadas (duplicación)
2. Una tabla polimorfica (lo que hago)

Usé tabla única con:
```
tipo_valoracion (ENUM) = tipo de contenido
id_relacionado = id en esa tabla
usuario_id = quien valora
```

Validación en código (`ValoracionService`):
```java
public void validar(ValoracionRequest req, Long usuarioId) {
    // Según el tipo, busca en tabla correcta
    switch (req.getTipoValoracion()) {
        case ENTRENAMIENTO_BASE:
            entrenamientoBaseRepo.findById(req.getIdRelacionado())
                .orElseThrow(() -> new Exception("No existe"));
            break;
        case CENTRO_PRIVADO_USUARIO:
            CentroPrivadoUsuario centro = centroRepo.findById(req.getIdRelacionado());
            if (!centro.getUsuario().getId().equals(usuarioId)) {
                throw new Exception("No es tu centro");
            }
            break;
        // ... etc
    }
    
    // Restricción UNIQUE: un usuario solo 1 valoración por contenido
    valoracionRepo.findByUsuarioAndTipoAndId(usuarioId, req.getTipoValoracion(), req.getIdRelacionado())
        .ifPresent(v -> {
            v.setPuntuacion(req.getPuntuacion());  // Actualizar, no crear
            valoracionRepo.save(v);
            return;
        });
}
```

---

### 🔄 FLUJO DE DATOS

**P10: Describe el flujo completo desde que el usuario ve un entrenamiento hasta que se registra en el historial.**

**R10**:
```
1. Usuario en Angular ve lista de entrenamientos (/entrenamientos)
   ↓ Componente llama: EntrenamientosService.listar()
   
2. Angular hace: GET /api/entrenamientos
   ↓ Nginx redirige a Backend:8080
   
3. Backend recibe en: EntrenamientoBaseRestController.listar()
   ↓ Delega a: EntrenamientoBaseService.listar()
   
4. Service usa: EntrenamientoBaseRepository.findAll()
   ↓ Repository ejecuta: SELECT * FROM entrenamientos_base
   ↓ Hibernate mapea a entidades Java
   
5. Service devuelve List<EntrenamientoBase>
   ↓ Controller lo convierte a DTOs y devuelve JSON
   
6. Angular recibe JSON, renderiza lista
   
7. Usuario pincha "Realizar" en "Full Body"
   ↓ Navega a /historial-entrenamientos
   
8. Usuario rellena:
   - Entrenamiento: Full Body (entrenamientoBaseId = 1)
   - Ubicación: AltaFit (centroPrivadoBaseId = 5)
   - Duración: 60 minutos
   - Fecha: 2026-05-20
   
9. Angular hace: POST /api/entrenamientos/mi-historial
   Con JSON: {
     "entrenamientoBaseId": 1,
     "centroPrivadoBaseId": 5,
     "duracionMinutos": 60,
     "fechaHora": "2026-05-20T18:00"
   }
   
10. Backend recibe en: HistorialEntrenamientosRestController.crear()
    ↓ Valida (exactamente 1 entrenamiento, max 1 ubicación)
    ↓ Comprueba que el usuario tenga permisos
    ↓ Crea registro en HISTORIAL_ENTRENAMIENTOS
    
11. Se inserta: INSERT INTO historial_entrenamientos 
    (usuario_id=42, entrenamiento_base_id=1, centro_privado_base_id=5, ...)
    
12. Angular recibe confirmación, muestra "Registrado"
```

---

**P11: ¿Cómo obtiene Angular la información del usuario autenticado?**

**R11**:
```
1. Usuario hace login en /login
   ↓ AuthService.login(username, password)
   ↓ POST /api/auth/login (Spring Security lo procesa)
   ↓ Servidor devuelve cookie de sesión
   ↓ Angular guarda la cookie (navegador)

2. AuthService llama a yo() (GET /api/auth/yo)
   ↓ Spring Security lee la cookie
   ↓ Identifica al usuario, devuelve el username

3. AuthService llama a me() (GET /api/auth/me)
   ↓ Devuelve: {username: "manuel", email: "m@...", rol: "USER"}

4. AuthService actualiza BehaviorSubject usuario$
   ↓ Componentes suscritos reciben actualización

5. Header detecta que hay usuario
   ↓ Muestra nombre, rol, ícono admin si aplica
```

Código (`Frontend/.../services/auth-service.ts`):
```typescript
@Injectable({ providedIn: 'root' })
export class AuthService {
    private usuarioSubject = new BehaviorSubject(null);
    usuario$ = this.usuarioSubject.asObservable();
    
    login(username: string, password: string) {
        return this.http.post('/api/auth/login', { username, password }).pipe(
            tap(() => this.me())  // Obtener datos tras login exitoso
        );
    }
    
    me() {
        return this.http.get('/api/auth/me', { withCredentials: true }).pipe(
            tap(usuario => this.usuarioSubject.next(usuario))
        );
    }
}
```

---

### 🛠️ DESARROLLO Y TESTING

**P12: ¿Cómo probaste que todo funciona? ¿Usas tests unitarios?**

**R12**:
- Tests unitarios en Angular: `*.spec.ts` en cada componente
- Tests en Spring Boot: `src/test/java`
- Testing manual durante desarrollo con Postman/curl
- Docker Compose para probar todo junto

Ejemplo de test Angular (`Frontend/.../guards/auth-guard.spec.ts`):
```typescript
describe('authGuard', () => {
    it('debería permitir si hay sesión', () => {
        const authService = {
            yo: () => of("manuel")
        };
        const result = authGuard({} as any, {} as any);
        expect(result).toBe(true);
    });
});
```

---

**P13: ¿Cómo manejas errores en el backend?**

**R13**:
- Controlador global: `Backend/.../handler/GlobalExceptionHandler.java`
- Captura excepciones y devuelve respuestas HTTP consistentes
- Ejemplo: Si el usuario intenta acceder a datos de otro usuario, lanza `AccessDeniedException`

Código:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(403).body(
            new ErrorResponse("No tienes permisos", 403)
        );
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(
            new ErrorResponse(ex.getMessage(), 500)
        );
    }
}
```

---

### 🚀 DESPLIEGUE

**P14: ¿Cómo se despliega la aplicación? ¿Qué es Docker Compose?**

**R14**:
- **Docker**: Empaqueta app en contenedores (aislados, reproducibles)
- **Docker Compose**: Orquesta múltiples contenedores (frontend, backend, BD)

Archivo `docker-compose.yml`:
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: digital_fit
      MYSQL_ROOT_PASSWORD: root
    ports: ["3307:3306"]
    volumes:
      - digital_fit_mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s

  backend:
    image: openjdk:25
    build: ./Backend/digital-fit
    ports: ["8080:8080"]
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      DB_URL: jdbc:mysql://mysql:3306/digital_fit

  frontend:
    image: nginx:latest
    ports: ["4200:80"]
    volumes:
      - ./Frontend/digital-fit-frontend/nginx.conf:/etc/nginx/nginx.conf
    depends_on: [backend]
```

Comando para ejecutar:
```bash
docker-compose up -d
```

---

### 💡 DECISIONES DE DISEÑO

**P15: ¿Por qué incluiste un sistema de soporte con tickets?**

**R15**:
- Es requisito habitual en apps reales
- Muestra capacidad de gestionar relaciones complejas (1:N mensajes)
- Permite que admins gestionen problemas de usuarios
- Es simple pero educativo: muestra cascade en JPA

Características:
- Usuario abre ticket (asunto + mensaje)
- Usuario y admin pueden intercambiar mensajes
- Admin puede cambiar estado (ABIERTO → EN_PROCESO → CERRADO)
- Al eliminar ticket, sus mensajes se eliminan (cascade)

---

**P16: ¿Qué fue lo más complicado de implementar?**

**R16**:
1. **Valoraciones polimórficas**: Hacer que una tabla valide múltiples tipos fue complejo
2. **Historial**: Validar reglas de negocio (exactamente 1 entrenamiento, max 1 ubicación)
3. **CORS**: Configurar que Angular (puerto 4200) pueda llamar al backend (8080)
4. **Mapas**: Integrar Leaflet en Angular requirió investigación

La solución fue:
- Validación exhaustiva en ServiceLayer
- Tests unitarios
- Documentación clara

---

**P17: ¿Qué mejorarías si tuvieras más tiempo?**

**R17**:
1. **JWT en lugar de sesiones**: Mejor para producción y móviles
2. **Notificaciones en tiempo real**: WebSockets para mensajes de soporte
3. **Imágenes en BD**: Guardar fotos de entrenamientos
4. **Caché**: Redis para acelerar búsquedas frecuentes
5. **Tests automatizados**: Mayor cobertura de tests
6. **Paginación**: En listas grandes (actualmente sin paginar)
7. **Filtros avanzados**: Más opciones de búsqueda

---

## 13. Glosario Técnico

| Término | Significado |
|---------|------------|
| **API REST** | Interfaz de comunicación entre cliente y servidor usando HTTP |
| **DTO** | Data Transfer Object, objeto que viaja en la API (separado de Entity) |
| **JPA** | Java Persistence API, estándar para ORM (mapeo BD a objetos) |
| **Hibernate** | Implementación de JPA (lo que realmente hace el mapeo) |
| **FK (Foreign Key)** | Clave foránea, referencia a otra tabla |
| **CORS** | Cross-Origin Resource Sharing, permite peticiones desde otro dominio |
| **CSRF** | Cross-Site Request Forgery, ataque que prevenimos desactivándolo |
| **BCrypt** | Algoritmo de hash de passwords adaptativo |
| **BehaviorSubject** | Observable de RxJS que emite última emisión a nuevos suscriptores |
| **Guard** | Función que valida si puedes acceder a una ruta (Angular) |
| **Cascade** | Si borras un registro, borra automáticamente sus dependientes |
| **Borrado lógico** | Marcar como inactivo en lugar de eliminar (preserva FK) |
| **Borrado físico** | Eliminar el registro completamente de BD |

---

## 📋 Checklist para la Defensa

- [ ] Practicar el flujo de login → crear entrenamiento → registrar en historial → ver estadísticas
- [ ] Tener abierta la estructura de carpetas (mostrar dónde está cada archivo si preguntan)
- [ ] Conocer los principales endpoints (`/api/auth/login`, `/api/entrenamientos`, etc.)
- [ ] Entender las 3 reglas del historial (validation)
- [ ] Explicar por qué valoraciones es polimorfica
- [ ] Mostrar cómo funciona borrado lógico (`activo = false`)
- [ ] Demostrar Docker Compose levantando la app
- [ ] Estar listo para preguntas sobre seguridad (BCrypt, sesiones, CORS)

---



## 12. Validaciones y errores

Validaciones:

- Estan en DTOs con anotaciones como `@NotBlank`, `@NotNull`, `@Email`, `@Size`, `@Min`, `@Max`.
- Ejemplo: `UsuarioDTO`, `CrearValoracionDTO`, `CrearEntrenamientoUsuario`, `CrearSoporteDTO`.

Manejo global de errores:

- `Backend/digital-fit/src/main/java/com/example/digital_fit/handler/GlobalExceptionHandler.java`.

Excepciones propias:

- `UsernameYaExiste`.
- `EmailYaExisteException`.
- `RecursoNoEncontradoException`.
- `OperacionNoPermitida`.
- `ErrorArgumentoException`.

Esto permite responder con codigos HTTP claros:

- `400 Bad Request` para datos incorrectos o duplicados.
- `403 Forbidden` para acciones sin permiso.
- `404 Not Found` para recursos inexistentes.

## 13. Logs

Configuracion:

- `application.properties`.

Ruta:

- `logs/digitalfit.log`.

El proyecto usa `Logger` en servicios para registrar acciones relevantes: registro de usuarios, altas, borrados, estadisticas, etc.

## 14. Preguntas tipicas del tribunal y respuestas

### Donde estan las entidades de base de datos?

En `Backend/digital-fit/src/main/java/com/example/digital_fit/model`. Cada clase con `@Entity` representa una tabla.

### Donde se define la relacion entre usuario y sus entrenamientos?

En `EntrenamientoUsuario.java`, con `@ManyToOne` y `@JoinColumn(name = "usuario_id")`. Eso significa que muchos entrenamientos pueden pertenecer a un mismo usuario.

### Por que hay tablas base y tablas de usuario?

Porque el proyecto separa el catalogo general del contenido personalizado. Por ejemplo, `entrenamientos_base` contiene rutinas generales de la app, mientras `entrenamientos_usuario` contiene rutinas que pertenecen a un usuario concreto. Lo mismo ocurre con centros y lugares.

### Como se protege que un usuario no vea datos de otro?

En los services se obtiene el usuario autenticado por username y se compara el `id` del propietario del recurso. Si no coincide se lanza `OperacionNoPermitida`. Ejemplos claros: `HistorialEntrenamientosService`, `EntrenamientoUsuarioService`, `CentroPrivadoUsuarioService` y `LugarPublicoUsuarioService`.

### Donde se configura que solo admin entre al panel admin?

En backend: `SecurityConfig.java`, con `.requestMatchers("/api/admin/**").hasRole("ADMIN")`.

En frontend: `admin-guard.ts`, que consulta `/api/auth/me` y revisa `usuario.rol === 'ADMIN'`.

### Como funciona el login?

Angular manda usuario y password a `/api/auth/login`. Ese endpoint lo procesa Spring Security, usando `CustomUserDetailsService` para buscar el usuario y `BCryptPasswordEncoder` para comparar la password cifrada. Si es correcto, queda una sesion de servidor y el navegador conserva la cookie.

### Por que no usas JWT?

Este proyecto usa autenticacion por sesion, que es suficiente y coherente para una aplicacion web servida desde el mismo dominio/proxy. La sesion la gestiona Spring Security y el frontend envia cookies con `withCredentials`.

### Como se calculan las estadisticas?

Se calculan desde `historial_entrenamientos`, porque es la tabla que registra entrenamientos realmente realizados. `EstadisticasRepository` contiene consultas para contar registros, sumar minutos y obtener elementos mas usados.

### Que es el historial?

Es la tabla que une un usuario, un entrenamiento realizado y opcionalmente una ubicacion. Es flexible porque puede guardar entrenamientos base o personalizados, y lugares/centros base o personalizados.

### Que significa `@ManyToOne`?

Que muchos registros de una tabla pueden apuntar a un registro de otra tabla. Por ejemplo, muchos tickets de soporte pueden pertenecer a un mismo usuario.

### Que significa `@OneToMany` en soporte?

Un ticket puede tener muchos mensajes. En `Soporte.java`, `mensajes` esta mapeado por el campo `ticket` de `MensajeSoporte.java`.

### Por que `Valoracion` no tiene una FK directa a cada tabla?

Porque una valoracion puede apuntar a distintos tipos de contenido. Se usa `tipoDeValoracion` para saber la tabla conceptual y `idRelacionado` para guardar el id. La comprobacion de existencia y permisos se hace en `ValoracionService`.

### Donde estan las consultas a base de datos?

En los repositories de `Backend/digital-fit/src/main/java/com/example/digital_fit/repository`. Algunas son metodos derivados de Spring Data, como `findByUsername`, y otras usan `@Query`.

### Donde estan los endpoints REST?

En los controllers de `Backend/digital-fit/src/main/java/com/example/digital_fit/controller`. Cada clase suele tener `@RequestMapping("/api/...")` y metodos con `@GetMapping`, `@PostMapping`, `@PutMapping`, etc.

### Donde estan las llamadas del frontend al backend?

En `Frontend/digital-fit-frontend/src/app/services`. Cada servicio define un `apiUrl` y usa `HttpClient`.

### Donde se inicializan los datos de prueba?

En `Backend/digital-fit/src/main/resources/data.sql`.

### Donde se configura Docker?

En `docker-compose.yml`. Define tres servicios: `mysql`, `backend` y `frontend`.

### Que hace Nginx?

Sirve los archivos compilados de Angular y redirige las peticiones `/api/` al backend. Esta configuracion esta en `Frontend/digital-fit-frontend/nginx.conf`.

### Que mejoras reconoceria si te preguntan?

Buenas respuestas:

- Anadir mas tests unitarios y de integracion.
- Revisar algunos textos con codificacion rara en comentarios/mensajes.
- Usar migraciones con Flyway o Liquibase en vez de depender solo de `ddl-auto=update`.
- Documentar la API con OpenAPI/Swagger.
- Revisar que todos los servicios admin tengan la URL correcta antes de la entrega.
- Mejorar autorizacion con anotaciones mas finas o tests de seguridad.

## 15. Posible explicacion en 1 minuto

Digital Fit es una aplicacion web full stack para organizar entrenamientos y lugares donde entrenar. El frontend esta hecho en Angular y consume una API REST desarrollada en Spring Boot. La base de datos es MySQL y se gestiona con JPA. La aplicacion distingue entre contenido base, que funciona como catalogo general, y contenido del usuario, que es personalizado y privado. El usuario puede guardar entrenamientos, centros y lugares, registrar sesiones realizadas en un historial y consultar estadisticas calculadas desde ese historial. Tambien puede valorar contenido y abrir tickets de soporte. La seguridad se basa en Spring Security con sesiones y roles, separando usuarios normales y administradores.

## 16. Cosas delicadas que conviene llevar preparadas

- `Valoracion` tiene una relacion logica con varios contenidos, no una FK fisica por cada posible tabla.
- `HistorialEntrenamientos` tiene varias FK opcionales porque puede registrar diferentes combinaciones de entrenamiento y ubicacion.
- El borrado de algunos recursos de usuario es logico con `activo = false`.
- La app usa sesion de Spring Security, no token JWT.
- Los endpoints admin se protegen tanto en frontend con guards como en backend con Spring Security.
- `data.sql` se ejecuta siempre y por eso usa `INSERT IGNORE`.
