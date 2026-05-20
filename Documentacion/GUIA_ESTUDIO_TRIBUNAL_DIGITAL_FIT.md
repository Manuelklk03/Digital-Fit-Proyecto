# Guia de estudio para tribunal - Digital Fit

Esta guia esta alineada con la presentacion `Defensa - Digital-Fit.pdf`. Sirve para preparar el discurso tecnico de cada diapositiva y conectar la presentacion con el codigo.

Importante: como el tribunal suele preguntar despues sobre cosas que no han salido en la defensa, usa tambien `GUIA_PREGUNTAS_POST_DEFENSA_DIGITAL_FIT.md`. Ese documento esta centrado en preguntas de codigo, base de datos, seguridad, decisiones tecnicas y puntos debiles.

## 1. Idea que tienes que defender

Frase corta:

Digital Fit es una aplicacion web deportiva que centraliza entrenamientos, lugares donde entrenar, seguimiento personal, valoraciones, estadisticas y soporte en una sola plataforma enfocada a Valencia y Comunidad Valenciana.

Si te preguntan que problema resuelve:

Muchas personas quieren entrenar, pero tienen la informacion repartida entre videos, redes, mapas, notas y apps distintas. Digital Fit intenta juntar en una sola app: centros privados, lugares publicos, entrenamientos, historial, valoraciones y estadisticas.

Si te preguntan el valor diferencial:

No es solo una app de rutinas ni solo un mapa. Combina localizacion deportiva, gestion de entrenamientos y seguimiento personal. Ademas separa contenido base de contenido personalizado del usuario.

## 2. Guion tecnico por diapositiva

### Diapositiva 2 - Origen del proyecto

Que dices:

- Nace de unir deporte y programacion.
- Detecta una necesidad real: informacion dispersa.
- Busca una solucion util, practica y realista.

Que pueden preguntar:

- Por que elegiste este proyecto?
- Que necesidad concreta resuelve?
- Que diferencia hay frente a mirar Google Maps o YouTube?

Respuesta buena:

Google Maps resuelve ubicaciones, YouTube resuelve contenido, pero Digital Fit junta ubicaciones, entrenamientos, historial y estadisticas en una sola aplicacion. El objetivo no es competir con plataformas grandes, sino crear una herramienta integrada y local.

Codigo relacionado:

- Funciones de centros/lugares: `controller/CentroPrivado`, `controller/LugarPublico`.
- Funciones de entrenamientos: `controller/Entrenamientos`.
- Seguimiento: `model/Entrenamientos/HistorialEntrenamientos.java`.

### Diapositiva 3 - Solucion y valor diferencial

Que dices:

- Digital Fit centraliza actividad fisica.
- Tiene tres bloques: localizacion deportiva, entrenamientos y seguimiento.
- El enfoque local y la union de funcionalidades es el valor diferencial.

Que pueden preguntar:

- Que funcionalidades son principales?
- Que parte del proyecto consideras mas importante?
- Por que separas centros privados y lugares publicos?

Respuesta buena:

Los centros privados representan gimnasios o instalaciones de pago. Los lugares publicos representan espacios abiertos o municipales como parques, pistas, rutas o playas. Separarlos permite tener campos y filtros mas claros, y tambien ayuda a explicar mejor el dominio.

Codigo relacionado:

- `model/CentroPrivado/CentroPrivadoBase.java`
- `model/CentroPrivado/CentroPrivadoUsuario.java`
- `model/LugarPublico/LugarPublicoBase.java`
- `model/LugarPublico/LugarPublicoUsuario.java`

### Diapositiva 4 - Stack tecnologico

Que dices:

- Angular se usa para frontend SPA modular.
- Spring Boot se usa para API REST organizada por capas.
- JPA + MySQL se usa porque hay relaciones claras entre usuarios, entrenamientos, centros, lugares, historial y soporte.
- Leaflet + OpenStreetMap se usa para mapas por ser libre, ligero y facil de integrar.

Que pueden preguntar:

- Por que Angular y no React?
- Por que MySQL y no MongoDB?
- Que aporta JPA?
- Que es una SPA?

Respuestas cortas:

- Angular: estructura guiada, TypeScript, rutas, servicios y guards.
- MySQL: el proyecto tiene relaciones entre entidades, por eso una base relacional encaja mejor.
- JPA: permite mapear clases Java a tablas y trabajar con repositories.
- SPA: una aplicacion de una sola pagina donde Angular cambia vistas sin recargar toda la web.

Codigo relacionado:

- Angular: `Frontend/digital-fit-frontend/src/app`.
- Dependencias Angular: `Frontend/digital-fit-frontend/package.json`.
- Spring Boot/Maven: `Backend/digital-fit/pom.xml`.
- JPA entidades: `Backend/digital-fit/src/main/java/com/example/digital_fit/model`.

### Diapositiva 5 - Arquitectura

Que dices:

La arquitectura es cliente-servidor:

1. Angular muestra la interfaz.
2. Angular llama a servicios HTTP.
3. Los servicios llaman a endpoints REST.
4. Spring Boot recibe la peticion en controllers.
5. Los services aplican la logica.
6. Los repositories acceden a MySQL con JPA.
7. La respuesta vuelve al frontend.

Que pueden preguntar:

- Donde estan los controllers?
- Donde esta la logica de negocio?
- Donde estan las consultas?
- Como se conecta frontend con backend?
- Donde esta CORS?

Ubicacion:

- Controllers: `Backend/digital-fit/src/main/java/com/example/digital_fit/controller`.
- Services: `Backend/digital-fit/src/main/java/com/example/digital_fit/service`.
- Repositories: `Backend/digital-fit/src/main/java/com/example/digital_fit/repository`.
- CORS: `Backend/digital-fit/src/main/java/com/example/digital_fit/config/CorsConfig.java`.
- Seguridad: `Backend/digital-fit/src/main/java/com/example/digital_fit/config/SecurityConfig.java`.
- Servicios Angular: `Frontend/digital-fit-frontend/src/app/services`.

Respuesta buena sobre CORS:

CORS permite que Angular en `localhost:4200` pueda comunicarse con Spring Boot en otro puerto. En `CorsConfig.java` se permite el origen `http://localhost:4200`, los metodos principales y credenciales, porque el login funciona con sesion/cookie.

### Diapositiva 6 - Funcionalidades

Que dices:

Usuario:

- Consulta centros privados y lugares publicos.
- Usa mapa interactivo.
- Gestiona entrenamientos base, personales y comunidad.
- Registra historial.
- Consulta valoraciones y estadisticas.
- Usa soporte.

Administrador:

- Gestiona contenido base.
- Gestiona soporte.
- Puede crear administradores.

Que pueden preguntar:

- Que diferencia hay entre contenido base y contenido de usuario?
- Como evitas que un usuario modifique datos de otro?
- Donde esta el panel admin?
- Donde se protegen las rutas?

Respuesta buena:

El contenido base es comun para todos. El contenido de usuario pertenece a una cuenta concreta. En backend se comprueba el usuario autenticado y se compara con el propietario del recurso. Si no coincide, se lanza `OperacionNoPermitida`.

Codigo relacionado:

- Rutas Angular: `Frontend/digital-fit-frontend/src/app/app.routes.ts`.
- Guards: `Frontend/digital-fit-frontend/src/app/guards`.
- Admin backend: `controller/*/Admin`.
- Usuario autenticado: se recibe con `Authentication authentication` en controllers.

### Diapositiva 7 - Seguridad y despliegue

Que dices:

- Spring Security gestiona autenticacion.
- BCrypt cifra passwords.
- Hay roles `USER` y `ADMIN`.
- Hay validaciones en frontend y backend.
- La app se puede ejecutar en local con BD, backend y frontend.

Que pueden preguntar:

- Usas JWT?
- Donde se cifra la password?
- Donde se definen los roles?
- Como se protege admin?
- Como se inicializan datos?

Respuestas:

- No uso JWT; uso sesion gestionada por Spring Security.
- La password se cifra en `AuthService` usando `PasswordEncoder`, configurado como `BCryptPasswordEncoder` en `SecurityConfig`.
- Los roles estan en `model/Enums/Rol.java`.
- Backend protege `/api/admin/**` en `SecurityConfig.java`; frontend protege rutas con `admin-guard.ts`.
- Los datos iniciales estan en `Backend/digital-fit/src/main/resources/data.sql`.

Codigo relacionado:

- `SecurityConfig.java`
- `AuthService.java`
- `CustomUserDetailsService.java`
- `Usuario.java`
- `Rol.java`
- `data.sql`
- `docker-compose.yml`

### Diapositiva 8 - Demo en vivo

Tu demo seguramente sigue este flujo:

1. Registro e inicio de sesion.
2. Pantalla principal.
3. Centros privados base y mis centros.
4. Lugares publicos base y mis lugares.
5. Mapa interactivo y guardado de ubicaciones.
6. Entrenamientos base, mis entrenamientos y comunidad.
7. Historial, valoraciones y estadisticas.
8. Soporte y panel de administracion.

Codigo para ubicar cada parte:

- Login/registro frontend: `pages/login`, `pages/register`, `services/auth-service.ts`.
- Login/registro backend: `AuthController.java`, `AuthService.java`, `SecurityConfig.java`.
- Centros privados: `pages/privados`, `pages/privados/mis-centros`, `services/centros`.
- Lugares publicos: `pages/publicos`, `pages/publicos/mis-lugares`, `services/publicos`.
- Mapa: `components/mapa-selector`.
- Entrenamientos: `pages/entrenamientos`, `services/entrenamientos`.
- Historial: `pages/entrenamientos/historial`, `HistorialEntrenamientosService.java`.
- Valoraciones: `pages/valoraciones`, `ValoracionService.java`.
- Estadisticas: `pages/estadisticas`, `EstadisticasService.java`, `EstadisticasRepository.java`.
- Soporte: `pages/soporte`, `SoporteService.java`.
- Admin: `pages/admin`, controllers bajo `controller/*/Admin`.

Pregunta probable:

"Si en la demo guardas un centro desde el mapa, donde se procesa?"

Respuesta:

En frontend se usa el componente de mapa y el servicio `mis-centros-service.ts`. En backend llega al controller `CentroPrivadoUsuarioRestController`, que llama a `CentroPrivadoUsuarioService`. Si viene de mapa, el metodo relevante es `AñdirCentroPrivadoMaps`, que crea un `CentroPrivadoUsuario` asociado al usuario autenticado.

### Diapositiva 9 - Conclusion y mejoras

Que dices:

- Se ha conseguido una app funcional.
- Hay arquitectura cliente-servidor clara.
- Hay autenticacion, roles, mapas, gestion de datos y seguimiento.
- Mejoras futuras: movil, despliegue produccion, estadisticas avanzadas, recomendaciones IA, expansion a otras ciudades.

Que pueden preguntar:

- Que mejorarias si tuvieras mas tiempo?
- Que parte te ha costado mas?
- Que limitaciones tiene?

Respuesta buena:

Mejoraria el despliegue en produccion, anadiria tests mas completos, documentaria la API con Swagger/OpenAPI y usaria migraciones con Flyway o Liquibase para controlar mejor los cambios de BD. Tambien podria mejorar estadisticas y recomendaciones.

## 3. Ubicacion rapida de codigo

| Si te preguntan... | Mira aqui |
|---|---|
| Donde arranca Spring Boot | `Backend/digital-fit/src/main/java/com/example/digital_fit/DigitalFitApplication.java` |
| Dependencias backend | `Backend/digital-fit/pom.xml` |
| Configuracion BD | `Backend/digital-fit/src/main/resources/application.properties` |
| Datos iniciales | `Backend/digital-fit/src/main/resources/data.sql` |
| Seguridad | `Backend/digital-fit/src/main/java/com/example/digital_fit/config/SecurityConfig.java` |
| CORS | `Backend/digital-fit/src/main/java/com/example/digital_fit/config/CorsConfig.java` |
| Entidades | `Backend/digital-fit/src/main/java/com/example/digital_fit/model` |
| DTOs | `Backend/digital-fit/src/main/java/com/example/digital_fit/dto` |
| Controllers REST | `Backend/digital-fit/src/main/java/com/example/digital_fit/controller` |
| Logica de negocio | `Backend/digital-fit/src/main/java/com/example/digital_fit/service` |
| Repositories | `Backend/digital-fit/src/main/java/com/example/digital_fit/repository` |
| Manejo de errores | `Backend/digital-fit/src/main/java/com/example/digital_fit/handler/GlobalExceptionHandler.java` |
| Rutas Angular | `Frontend/digital-fit-frontend/src/app/app.routes.ts` |
| Guards Angular | `Frontend/digital-fit-frontend/src/app/guards` |
| Servicios Angular | `Frontend/digital-fit-frontend/src/app/services` |
| Paginas Angular | `Frontend/digital-fit-frontend/src/app/pages` |
| Mapa | `Frontend/digital-fit-frontend/src/app/components/mapa-selector` |
| Docker | `docker-compose.yml` |
| Nginx frontend | `Frontend/digital-fit-frontend/nginx.conf` |

## 4. Preguntas de codigo y respuestas preparadas

### Como esta organizado el backend?

Esta organizado por capas:

- `controller`: recibe peticiones HTTP.
- `service`: contiene la logica de negocio.
- `repository`: accede a la base de datos.
- `model`: define entidades JPA.
- `dto`: define datos de entrada/salida.
- `config`: seguridad y CORS.

### Que es una entidad JPA?

Es una clase Java marcada con `@Entity` que representa una tabla de la base de datos. Por ejemplo, `Usuario.java` representa la tabla `usuarios`.

### Que es un repository?

Es una interfaz que extiende `JpaRepository` o `Repository` y permite hacer consultas sin escribir todo el SQL manualmente. Spring Data genera consultas a partir de nombres como `findByUsername`.

### Por que usas DTOs?

Para separar lo que se expone por la API de las entidades internas. Tambien sirven para validar datos de entrada con anotaciones como `@NotBlank`, `@NotNull`, `@Email`, `@Size`, `@Min` y `@Max`.

### Donde se validan datos?

En DTOs dentro de `Backend/digital-fit/src/main/java/com/example/digital_fit/dto`. Los errores de validacion se gestionan en `GlobalExceptionHandler.java`.

### Como funciona el borrado de elementos de usuario?

En varios elementos propios del usuario se usa borrado logico con `activo = false`, por ejemplo entrenamientos, centros y lugares de usuario. Asi no se rompen referencias antiguas del historial.

### Que es el historial?

Es la entidad que registra entrenamientos realmente realizados por el usuario. Relaciona usuario, entrenamiento, posible ubicacion, fecha, duracion y notas. Es la base para las estadisticas.

### Por que historial tiene varias FK opcionales?

Porque un registro puede usar un entrenamiento base o uno personal, y puede tener una ubicacion de varios tipos: lugar publico base, lugar publico de usuario, centro privado base o centro privado de usuario. La regla del service evita que se seleccionen varias opciones incompatibles.

### Como funcionan las valoraciones?

La tabla `valoraciones` guarda usuario, puntuacion, comentario, fecha, tipo de contenido e id relacionado. Como se pueden valorar muchos tipos de contenido, se usa una relacion logica con `tipo_valoracion + id_relacionado`. La validacion se hace en `ValoracionService`.

### Como se calculan las estadisticas?

Desde `EstadisticasService`, usando `EstadisticasRepository`. Se consulta la tabla `historial_entrenamientos` para contar entrenamientos, sumar minutos, calcular promedio, contar ubicaciones visitadas y obtener el entrenamiento mas realizado.

### Como esta protegido el panel admin?

En backend, `SecurityConfig` obliga a que `/api/admin/**` tenga rol `ADMIN`. En frontend, `admin-guard.ts` consulta la sesion actual y comprueba que el rol sea `ADMIN`.

### Como se mantiene la sesion?

Spring Security crea una sesion de servidor y el navegador mantiene la cookie. En Angular, las peticiones usan `withCredentials: true` para enviar esa cookie.

## 5. Preguntas teoricas probables

### Que es arquitectura cliente-servidor?

El cliente es Angular, que muestra la interfaz y lanza peticiones. El servidor es Spring Boot, que procesa la logica y accede a MySQL. Estan separados y se comunican mediante HTTP/REST.

### Que es REST?

Es un estilo de API donde los recursos se exponen mediante URLs y metodos HTTP: `GET` para leer, `POST` para crear, `PUT/PATCH` para actualizar y `DELETE` para eliminar.

### Que es CORS?

Es una politica del navegador que controla si una web puede llamar a una API de otro origen. En desarrollo, Angular y Spring Boot pueden estar en puertos distintos, por eso se configura CORS.

### Que es BCrypt?

Es un algoritmo de hashing para almacenar contrasenas de forma segura. No se guarda la contrasena real, sino su hash.

### Que es una relacion 1:N?

Que un registro de una tabla puede relacionarse con muchos registros de otra. Ejemplo: un usuario puede tener muchos entrenamientos personales.

### Que es una clave foranea?

Es un campo que apunta a la clave primaria de otra tabla. Ejemplo: `usuario_id` en `entrenamientos_usuario` apunta a `usuarios.id`.

### Que significa `@ManyToOne`?

En JPA indica que muchos registros de esta entidad apuntan a un registro de otra entidad. Ejemplo: muchos tickets de soporte pertenecen a un usuario.

### Que significa `@OneToMany`?

Indica que un registro tiene una coleccion de otros registros. Ejemplo: un ticket de soporte tiene muchos mensajes.

## 6. Cosas que debes saber ubicar sin dudar

Autenticacion:

- Backend: `SecurityConfig`, `AuthController`, `AuthService`, `CustomUserDetailsService`.
- Frontend: `auth-service.ts`, `login`, `register`, guards.

Base de datos:

- Entidades: `model`.
- Relaciones: anotaciones `@ManyToOne`, `@OneToMany`, `@JoinColumn`.
- Datos iniciales: `data.sql`.

Demo:

- Rutas: `app.routes.ts`.
- Cada pantalla: `pages`.
- Cada llamada API: `services`.

Admin:

- Frontend: `pages/admin`.
- Backend: controllers bajo `controller/*/Admin`.
- Seguridad: `/api/admin/**` en `SecurityConfig`.

## 7. Respuesta si preguntan por IA

Puedes responder con naturalidad:

He usado IA como apoyo para acelerar partes de desarrollo, documentacion o resolucion de dudas, pero el proyecto esta organizado por mi y entiendo la arquitectura, las entidades, las relaciones, el flujo frontend-backend y las decisiones tecnicas principales. Puedo ubicar donde esta cada parte y explicar como funciona.

## 8. Puntos delicados que conviene reconocer bien

- La app usa sesiones, no JWT.
- `Valoracion` usa relacion logica con varios tipos de contenido, no FK directa.
- `HistorialEntrenamientos` tiene varias FK opcionales y el service controla que se usen bien.
- Algunas eliminaciones son logicas con `activo = false`.
- `data.sql` usa `INSERT IGNORE` porque se ejecuta siempre.
- Para produccion seria mejor usar migraciones con Flyway/Liquibase y Swagger/OpenAPI para documentar API.
