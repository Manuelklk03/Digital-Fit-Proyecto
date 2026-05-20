# 04 - Repaso Final 30 Minutos

Este es el documento para leer el dia antes o justo antes de la defensa.

## Tu Proyecto en 30 Segundos

Digital Fit es una aplicacion web deportiva que centraliza entrenamientos, lugares donde entrenar, seguimiento personal, valoraciones, estadisticas y soporte. El frontend esta hecho con Angular, el backend con Spring Boot y la base de datos con MySQL/JPA. La app separa catalogos base de datos personales del usuario, y usa Spring Security para autenticacion, sesiones y roles.

## Stack

```text
Frontend: Angular + TypeScript + Leaflet
Backend: Spring Boot + Spring Security + JPA
BD: MySQL
Despliegue: Docker Compose + Nginx
```

## Arquitectura

```text
Angular
-> services HttpClient
-> /api/...
-> Spring Controller
-> Spring Service
-> Repository JPA
-> MySQL
```

Frase:

> El proyecto esta organizado por capas para separar interfaz, API, logica de negocio y persistencia.

## 10 Respuestas que Tienes que Saber

### 1. Que problema resuelve?

Centraliza informacion deportiva dispersa: entrenamientos, centros, lugares, historial, valoraciones y estadisticas.

### 2. Por que Angular?

Por estructura modular, TypeScript, rutas, servicios y guards.

### 3. Por que Spring Boot?

Porque facilita crear una API REST organizada por capas e integrada con Java, JPA y Security.

### 4. Por que MySQL?

Porque el proyecto tiene relaciones claras entre entidades. Una base relacional encaja mejor que una no relacional.

### 5. Como funciona login?

Spring Security procesa `/api/auth/login`, busca usuario, compara password con BCrypt y crea sesion.

### 6. Es hackeable el frontend?

Si, todo frontend es manipulable. Por eso la seguridad real esta en backend: roles, DTOs, services y comprobaciones de propiedad.

### 7. Que es historial?

La tabla que registra entrenamientos realizados. Es la fuente de las estadisticas.

### 8. Por que tablas base y usuario?

Base es catalogo general; usuario es contenido privado/personalizado asociado a `usuario_id`.

### 9. Como funcionan valoraciones?

Con `tipo_valoracion + id_relacionado`, porque se pueden valorar muchos tipos de contenido con una sola tabla.

### 10. Que mejorarias?

Mas tests, Swagger/OpenAPI, Flyway/Liquibase, despliegue real, estadisticas avanzadas y recomendaciones.

## Archivos que Debes Saber Ubicar

| Tema | Ubicacion |
|---|---|
| Rutas Angular | `Frontend/digital-fit-frontend/src/app/app.routes.ts` |
| Guards | `Frontend/digital-fit-frontend/src/app/guards` |
| Servicios HTTP | `Frontend/digital-fit-frontend/src/app/services` |
| Paginas | `Frontend/digital-fit-frontend/src/app/pages` |
| Mapa | `Frontend/digital-fit-frontend/src/app/components/mapa-selector` |
| Seguridad backend | `Backend/digital-fit/src/main/java/com/example/digital_fit/config/SecurityConfig.java` |
| CORS | `Backend/digital-fit/src/main/java/com/example/digital_fit/config/CorsConfig.java` |
| Entidades | `Backend/digital-fit/src/main/java/com/example/digital_fit/model` |
| Controllers | `Backend/digital-fit/src/main/java/com/example/digital_fit/controller` |
| Services | `Backend/digital-fit/src/main/java/com/example/digital_fit/service` |
| Repositories | `Backend/digital-fit/src/main/java/com/example/digital_fit/repository` |
| DTOs | `Backend/digital-fit/src/main/java/com/example/digital_fit/dto` |
| Errores | `Backend/digital-fit/src/main/java/com/example/digital_fit/handler/GlobalExceptionHandler.java` |
| Datos iniciales | `Backend/digital-fit/src/main/resources/data.sql` |
| Config BD | `Backend/digital-fit/src/main/resources/application.properties` |
| Docker | `docker-compose.yml` |

## Entidades para Memorizar

```text
usuarios
entrenamientos_base
entrenamientos_usuario
entrenamientos_comunidad
centros_privados_base
centros_privados_usuario
lugares_publicos_base
lugares_publicos_usuario
historial_entrenamientos
valoraciones
soporte
mensajes_soporte
```

## Respuesta de Seguridad Completa

> El frontend es manipulable por naturaleza. Los guards ayudan a la experiencia de usuario, pero no son la seguridad principal. La seguridad real esta en backend: Spring Security valida sesion, `/api/admin/**` exige rol ADMIN, las passwords se cifran con BCrypt, los DTOs validan datos y los services comprueban que un recurso pertenezca al usuario antes de consultarlo o modificarlo.

## Respuesta de Relaciones Completa

> La BD se organiza alrededor de `usuarios`. Las tablas `*_base` son catalogos comunes y las `*_usuario` son datos personales. El historial conecta usuario, entrenamiento y ubicacion, y desde ahi salen las estadisticas. Valoraciones es especial porque usa `tipo_valoracion` e `id_relacionado` para apuntar logicamente a varios tipos de contenido.

## Respuesta de Flujo Completo

> Si un usuario crea algo desde Angular, el componente llama a un service, el service hace una peticion HTTP a `/api/...`, Spring Boot la recibe en un controller, delega en un service, valida permisos y datos, usa un repository JPA y finalmente guarda en MySQL.

## Si Te Preguntan Algo que No Sabes

No inventes. Di:

> No recuerdo el nombre exacto de memoria, pero por la arquitectura estaria en la capa correspondiente: controller si es endpoint, service si es logica, repository si es consulta y model si es entidad. Puedo localizarlo siguiendo ese flujo.

## Checklist Final

- Se explicar login y sesiones.
- Se explicar por que el frontend es manipulable.
- Se explicar base vs usuario.
- Se explicar historial.
- Se explicar valoraciones polimorficas.
- Se ubicar rutas, services, controllers, services backend y entities.
- Se reconocer mejoras sin tirar mi proyecto abajo.

