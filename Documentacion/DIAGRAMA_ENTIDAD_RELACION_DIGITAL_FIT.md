# Diagrama entidad-relacion - Digital Fit

Este diagrama resume las tablas principales y sus relaciones. Esta escrito en Mermaid, por lo que se puede visualizar en editores compatibles con Markdown Mermaid.

```mermaid
erDiagram
    USUARIOS {
        bigint id PK
        string username UK
        string email UK
        string password
        enum rol
    }

    ENTRENAMIENTOS_BASE {
        bigint id PK
        string nombre UK
        string descripcion
        enum categoria
        enum nivel
        int duracion_en_minutos
    }

    ENTRENAMIENTOS_USUARIO {
        bigint id PK
        string nombre
        string descripcion
        enum categoria
        enum nivel
        int duracion_en_minutos
        boolean activo
        bigint usuario_id FK
    }

    ENTRENAMIENTOS_COMUNIDAD {
        bigint id PK
        string nombre
        string descripcion
        enum categoria
        enum nivel
        int duracion_en_minutos
        datetime fecha_publicacion
        bigint usuario_id FK
    }

    CENTROS_PRIVADOS_BASE {
        bigint id PK
        string nombre UK
        string direccion
        string telefono
        string horario
        double precio_mensual
        string descripcion
        double latitud
        double longitud
    }

    CENTROS_PRIVADOS_USUARIO {
        bigint id PK
        string nombre
        string direccion
        string telefono
        string horario
        double precio_mensual
        string descripcion
        double latitud
        double longitud
        boolean activo
        bigint usuario_id FK
    }

    LUGARES_PUBLICOS_BASE {
        bigint id PK
        string nombre UK
        string direccion
        string descripcion
        string telefono
        string horario
        double latitud
        double longitud
        enum tipo
    }

    LUGARES_PUBLICOS_USUARIO {
        bigint id PK
        string nombre
        string direccion
        string descripcion
        string telefono
        string horario
        double latitud
        double longitud
        enum tipo
        boolean activo
        bigint usuario_id FK
    }

    HISTORIAL_ENTRENAMIENTOS {
        bigint id PK
        bigint usuario_id FK
        bigint entrenamiento_base_id FK
        bigint entrenamiento_usuario_id FK
        bigint lugar_publico_base_id FK
        bigint lugar_publico_usuario_id FK
        bigint centro_privado_base_id FK
        bigint centro_privado_usuario_id FK
        datetime fecha_hora
        int duracion_minutos
        string notas
    }

    VALORACIONES {
        bigint id PK
        int puntuacion
        string comentario
        datetime fecha
        enum tipo_valoracion
        bigint id_relacionado
        bigint usuario_id FK
    }

    SOPORTE {
        bigint id PK
        string asunto
        string mensaje
        datetime fecha
        enum estado
        bigint usuario_id FK
    }

    MENSAJES_SOPORTE {
        bigint id PK
        string contenido
        datetime fecha
        bigint ticket_id FK
        bigint emisor_id FK
    }

    USUARIOS ||--o{ ENTRENAMIENTOS_USUARIO : crea
    USUARIOS ||--o{ ENTRENAMIENTOS_COMUNIDAD : publica
    USUARIOS ||--o{ CENTROS_PRIVADOS_USUARIO : guarda
    USUARIOS ||--o{ LUGARES_PUBLICOS_USUARIO : guarda
    USUARIOS ||--o{ HISTORIAL_ENTRENAMIENTOS : registra
    USUARIOS ||--o{ VALORACIONES : escribe
    USUARIOS ||--o{ SOPORTE : abre
    USUARIOS ||--o{ MENSAJES_SOPORTE : envia

    ENTRENAMIENTOS_BASE ||--o{ HISTORIAL_ENTRENAMIENTOS : puede_usarse_en
    ENTRENAMIENTOS_USUARIO ||--o{ HISTORIAL_ENTRENAMIENTOS : puede_usarse_en
    LUGARES_PUBLICOS_BASE ||--o{ HISTORIAL_ENTRENAMIENTOS : ubicacion_base
    LUGARES_PUBLICOS_USUARIO ||--o{ HISTORIAL_ENTRENAMIENTOS : ubicacion_usuario
    CENTROS_PRIVADOS_BASE ||--o{ HISTORIAL_ENTRENAMIENTOS : ubicacion_base
    CENTROS_PRIVADOS_USUARIO ||--o{ HISTORIAL_ENTRENAMIENTOS : ubicacion_usuario

    SOPORTE ||--o{ MENSAJES_SOPORTE : contiene
```

## Notas para defender el diagrama

- `USUARIOS` es la tabla central: casi todo lo privado pertenece a un usuario.
- Las tablas `*_BASE` son catalogos generales de la aplicacion.
- Las tablas `*_USUARIO` son elementos guardados o creados por un usuario concreto.
- `HISTORIAL_ENTRENAMIENTOS` concentra la actividad real del usuario y por eso alimenta las estadisticas.
- En historial, las FK hacia entrenamientos y ubicaciones son opcionales porque solo se selecciona una opcion de cada grupo.
- `VALORACIONES` tiene una relacion directa con `USUARIOS`, pero con el contenido valorado usa una relacion logica mediante `tipo_valoracion + id_relacionado`.
- La restriccion unica importante de `VALORACIONES` es: un usuario no puede valorar dos veces el mismo contenido del mismo tipo; si vuelve a valorar, se actualiza.

## Relacion logica de valoraciones

`VALORACIONES` puede apuntar conceptualmente a cualquiera de estas tablas:

- `ENTRENAMIENTOS_BASE`
- `ENTRENAMIENTOS_USUARIO`
- `ENTRENAMIENTOS_COMUNIDAD`
- `HISTORIAL_ENTRENAMIENTOS`
- `CENTROS_PRIVADOS_BASE`
- `CENTROS_PRIVADOS_USUARIO`
- `LUGARES_PUBLICOS_BASE`
- `LUGARES_PUBLICOS_USUARIO`

No aparece como FK fisica en el diagrama porque en codigo se resuelve con:

- `tipo_valoracion`: indica que tipo de contenido es.
- `id_relacionado`: guarda el id del contenido.
- `ValoracionService.validarContenidoExisteYPermisos`: comprueba existencia y permisos.

