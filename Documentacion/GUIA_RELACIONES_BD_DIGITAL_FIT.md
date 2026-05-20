# Guia para aprender las relaciones de base de datos - Digital Fit

Esta guia complementa el diagrama `DIAGRAMA_ENTIDAD_RELACION_DIGITAL_FIT.md`. El objetivo no es memorizar tablas sueltas, sino entender por que existen y como se relacionan.

## 1. Idea principal

La base de datos gira alrededor de `usuarios`.

Casi todo lo importante responde a una pregunta:

"Este dato es comun para todos o pertenece a un usuario concreto?"

De ahi salen dos tipos de tablas:

- Tablas `*_base`: catalogo general de la aplicacion.
- Tablas `*_usuario`: datos guardados o creados por un usuario.

Ejemplos:

- `entrenamientos_base`: rutinas generales disponibles para todos.
- `entrenamientos_usuario`: rutinas propias guardadas por un usuario.
- `centros_privados_base`: centros del catalogo general.
- `centros_privados_usuario`: centros guardados por un usuario.
- `lugares_publicos_base`: lugares publicos del catalogo general.
- `lugares_publicos_usuario`: lugares guardados por un usuario.

## 2. Relacion central: usuarios

Tabla:

- `usuarios`

Entidad:

- `model/Auth/Usuario.java`

Campos:

- `id`
- `username`
- `email`
- `password`
- `rol`

Relaciones:

- Un usuario puede tener muchos entrenamientos propios.
- Un usuario puede publicar muchos entrenamientos de comunidad.
- Un usuario puede guardar muchos centros privados.
- Un usuario puede guardar muchos lugares publicos.
- Un usuario puede registrar muchos entrenamientos en el historial.
- Un usuario puede escribir muchas valoraciones.
- Un usuario puede abrir muchos tickets de soporte.
- Un usuario puede enviar muchos mensajes de soporte.

Forma de decirlo:

`usuarios` es la entidad principal de identidad. A partir de ella se asocian todos los datos privados o generados por la persona que usa la aplicacion.

## 3. Catalogo base frente a datos del usuario

### Por que existen tablas base?

Porque hay informacion comun que todos los usuarios pueden consultar.

Ejemplo:

Un entrenamiento base como "Full Body Principiante" no pertenece a un usuario concreto. Es parte del catalogo de la app.

### Por que existen tablas de usuario?

Porque un usuario puede guardar, copiar o crear informacion propia.

Ejemplo:

Si un usuario copia un entrenamiento base a "mis entrenamientos", se crea un registro en `entrenamientos_usuario` asociado a su `usuario_id`.

### Ventaja de esta separacion

- El catalogo general se mantiene limpio.
- Cada usuario puede personalizar su informacion.
- Los datos privados quedan asociados a `usuario_id`.
- Es mas facil proteger permisos.

Frase para tribunal:

Separe contenido base y contenido de usuario para diferenciar lo comun de lo personalizado. Asi el catalogo puede ser gestionado por administracion, mientras que cada usuario tiene sus propios elementos sin modificar los datos generales.

## 4. Entrenamientos

### EntrenamientoBase

Tabla:

- `entrenamientos_base`

Entidad:

- `model/Entrenamientos/EntrenamientoBase.java`

Relacion:

- No depende de usuario.
- Puede aparecer en muchos registros de historial.

Cardinalidad:

- `entrenamientos_base 1:N historial_entrenamientos`

Ejemplo:

El entrenamiento base "HIIT 20 minutos" puede haber sido realizado muchas veces por distintos usuarios.

### EntrenamientoUsuario

Tabla:

- `entrenamientos_usuario`

Entidad:

- `model/Entrenamientos/EntrenamientoUsuario.java`

Relacion:

- Muchos entrenamientos de usuario pertenecen a un usuario.
- Puede aparecer en muchos registros de historial.

Cardinalidad:

- `usuarios 1:N entrenamientos_usuario`
- `entrenamientos_usuario 1:N historial_entrenamientos`

En codigo:

`EntrenamientoUsuario.java` tiene:

```java
@ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
private Usuario usuario;
```

Explicacion:

Muchos entrenamientos personalizados pueden apuntar al mismo usuario propietario.

### EntrenamientoComunidad

Tabla:

- `entrenamientos_comunidad`

Entidad:

- `model/Entrenamientos/EntrenamientoComunidad.java`

Relacion:

- Muchos entrenamientos de comunidad pertenecen a un usuario autor.

Cardinalidad:

- `usuarios 1:N entrenamientos_comunidad`

Ejemplo:

Un usuario puede publicar varias rutinas para la comunidad. Otros usuarios pueden copiarlas a sus entrenamientos personales.

## 5. Centros privados

### CentroPrivadoBase

Tabla:

- `centros_privados_base`

Entidad:

- `model/CentroPrivado/CentroPrivadoBase.java`

Relacion:

- Es catalogo general.
- Puede usarse en muchos registros de historial.

Cardinalidad:

- `centros_privados_base 1:N historial_entrenamientos`

### CentroPrivadoUsuario

Tabla:

- `centros_privados_usuario`

Entidad:

- `model/CentroPrivado/CentroPrivadoUsuario.java`

Relacion:

- Muchos centros guardados pertenecen a un usuario.
- Puede usarse en muchos registros de historial.

Cardinalidad:

- `usuarios 1:N centros_privados_usuario`
- `centros_privados_usuario 1:N historial_entrenamientos`

Ejemplo:

Un usuario guarda un gimnasio desde el mapa. Ese registro no modifica el catalogo general, sino que crea un centro propio asociado a su usuario.

## 6. Lugares publicos

### LugarPublicoBase

Tabla:

- `lugares_publicos_base`

Entidad:

- `model/LugarPublico/LugarPublicoBase.java`

Relacion:

- Es catalogo general.
- Puede usarse en muchos registros de historial.

Cardinalidad:

- `lugares_publicos_base 1:N historial_entrenamientos`

### LugarPublicoUsuario

Tabla:

- `lugares_publicos_usuario`

Entidad:

- `model/LugarPublico/LugarPublicoUsuario.java`

Relacion:

- Muchos lugares guardados pertenecen a un usuario.
- Puede usarse en muchos registros de historial.

Cardinalidad:

- `usuarios 1:N lugares_publicos_usuario`
- `lugares_publicos_usuario 1:N historial_entrenamientos`

Ejemplo:

Un usuario guarda una zona de entrenamiento desde el mapa. Ese lugar queda en su zona personal.

## 7. Historial: la tabla mas importante para seguimiento

Tabla:

- `historial_entrenamientos`

Entidad:

- `model/Entrenamientos/HistorialEntrenamientos.java`

Para que sirve:

Registra una actividad realizada. Es como decir:

"El usuario X hizo el entrenamiento Y en la fecha Z, durante N minutos, y opcionalmente en la ubicacion U".

Relaciones:

- Siempre pertenece a un usuario.
- Puede apuntar a un entrenamiento base o a un entrenamiento de usuario.
- Puede apuntar a una ubicacion base o de usuario.

Cardinalidades:

- `usuarios 1:N historial_entrenamientos`
- `entrenamientos_base 1:N historial_entrenamientos`
- `entrenamientos_usuario 1:N historial_entrenamientos`
- `centros_privados_base 1:N historial_entrenamientos`
- `centros_privados_usuario 1:N historial_entrenamientos`
- `lugares_publicos_base 1:N historial_entrenamientos`
- `lugares_publicos_usuario 1:N historial_entrenamientos`

### Por que tiene tantas columnas opcionales?

Porque el historial debe ser flexible.

Un registro puede ser:

- Entrenamiento base + centro privado base.
- Entrenamiento base + lugar publico usuario.
- Entrenamiento usuario + centro privado usuario.
- Entrenamiento usuario sin ubicacion.

Pero no debe permitir incoherencias como:

- Dos entrenamientos a la vez.
- Tres ubicaciones a la vez.
- Un entrenamiento privado de otro usuario.

Esa regla se controla en:

- `service/Entrenamientos/HistorialEntrenamientosService.java`

Frase para tribunal:

El historial funciona como tabla de hechos: guarda lo que realmente ha pasado. Por eso se conecta con usuario, entrenamiento y ubicacion. Las FK opcionales permiten distintos origenes, pero la logica de negocio obliga a seleccionar solo una opcion valida.

## 8. Estadisticas salen del historial

Archivos:

- `service/Estadisticas/EstadisticasService.java`
- `repository/Estadisticas/EstadisticasRepository.java`

La estadistica no necesita guardar otra tabla propia porque se calcula desde `historial_entrenamientos`.

Ejemplos:

- Entrenamientos realizados: contar registros del historial.
- Minutos entrenados: sumar `duracion_minutos`.
- Promedio: media de `duracion_minutos`.
- Centros visitados: contar centros distintos usados en historial.
- Lugares visitados: contar lugares distintos usados en historial.
- Entrenamiento mas realizado: agrupar por entrenamiento y contar.

Frase para tribunal:

Las estadisticas son datos derivados. No se guardan duplicadas, se calculan a partir del historial porque el historial es la fuente de verdad.

## 9. Valoraciones: relacion polimorfica/logica

Tabla:

- `valoraciones`

Entidad:

- `model/Valoracion/Valoracion.java`

Relacion directa:

- Muchas valoraciones pertenecen a un usuario.

Cardinalidad directa:

- `usuarios 1:N valoraciones`

Relacion con el contenido valorado:

- No tiene una FK fisica a cada tabla.
- Usa `tipo_valoracion` + `id_relacionado`.

Ejemplo:

Si una valoracion tiene:

- `tipo_valoracion = ENTRENAMIENTO_BASE`
- `id_relacionado = 3`

Significa que esta valorando el entrenamiento base con id 3.

Si tiene:

- `tipo_valoracion = CENTRO_PRIVADO_USUARIO`
- `id_relacionado = 7`

Significa que esta valorando el centro privado de usuario con id 7.

Por que se hizo asi:

Una unica tabla de valoraciones puede servir para muchos tipos de contenido. Si se hiciera con FK fisicas, habria que tener muchas columnas o muchas tablas distintas de valoraciones.

Donde se comprueba que existe el contenido:

- `service/Valoracion/ValoracionService.java`
- Metodo importante: `validarContenidoExisteYPermisos`

Restriccion importante:

En `Valoracion.java` hay una restriccion unica:

```java
@UniqueConstraint(columnNames = { "usuario_id", "tipo_valoracion", "id_relacionado" })
```

Significa:

Un usuario no puede crear dos valoraciones distintas sobre el mismo contenido del mismo tipo. Si vuelve a valorar, se actualiza.

Frase para tribunal:

Valoraciones usa una relacion polimorfica. La base de datos guarda el tipo de contenido y el id, y el service valida que ese contenido exista y que el usuario tenga permiso.

## 10. Soporte y mensajes

### Soporte

Tabla:

- `soporte`

Entidad:

- `model/Soporte/Soporte.java`

Relacion:

- Muchos tickets pertenecen a un usuario.

Cardinalidad:

- `usuarios 1:N soporte`

### MensajeSoporte

Tabla:

- `mensajes_soporte`

Entidad:

- `model/Soporte/MensajeSoporte.java`

Relaciones:

- Muchos mensajes pertenecen a un ticket.
- Muchos mensajes tienen un usuario emisor.

Cardinalidades:

- `soporte 1:N mensajes_soporte`
- `usuarios 1:N mensajes_soporte`

En codigo:

`Soporte.java` tiene:

```java
@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
private List<MensajeSoporte> mensajes;
```

Explicacion:

Un ticket contiene varios mensajes. Si se elimina el ticket, tambien se eliminan sus mensajes por cascada.

## 11. Como responder si te piden explicar el diagrama

Respuesta de 1 minuto:

El diagrama se organiza alrededor de usuarios. A partir de usuario salen las relaciones privadas: entrenamientos personales, centros guardados, lugares guardados, historial, valoraciones y soporte. Luego hay tablas base, que son catalogos generales gestionados por la aplicacion o por administracion. El historial une usuario, entrenamiento y ubicacion, y es la fuente de las estadisticas. Valoraciones es especial porque puede valorar varios tipos de contenido usando `tipo_valoracion` e `id_relacionado`. Soporte se divide en tickets y mensajes, con una relacion uno a muchos.

## 12. Tabla resumen para memorizar

| Tabla | Tipo | Relacion principal |
|---|---|---|
| `usuarios` | Central | Tiene muchos datos asociados |
| `entrenamientos_base` | Catalogo | Puede aparecer en historial |
| `entrenamientos_usuario` | Usuario | Pertenece a usuario y puede aparecer en historial |
| `entrenamientos_comunidad` | Comunidad | Pertenece a usuario autor |
| `centros_privados_base` | Catalogo | Puede aparecer en historial |
| `centros_privados_usuario` | Usuario | Pertenece a usuario y puede aparecer en historial |
| `lugares_publicos_base` | Catalogo | Puede aparecer en historial |
| `lugares_publicos_usuario` | Usuario | Pertenece a usuario y puede aparecer en historial |
| `historial_entrenamientos` | Seguimiento | Pertenece a usuario y conecta entrenamiento + ubicacion |
| `valoraciones` | Opinion | Pertenece a usuario y apunta logicamente a un contenido |
| `soporte` | Ticket | Pertenece a usuario |
| `mensajes_soporte` | Conversacion | Pertenece a ticket y tiene emisor |

## 13. Preguntas sobre relaciones y respuestas

### Por que `usuarios` tiene tantas relaciones?

Porque la mayoria de acciones de la app son personales: guardar entrenamientos, guardar lugares, registrar historial, valorar y abrir soporte.

### Por que no mezclas base y usuario en una sola tabla?

Porque tienen responsabilidades distintas. Base es contenido comun; usuario es contenido privado. Separarlo simplifica permisos y evita que un usuario modifique el catalogo general.

### Por que historial no usa una sola columna `ubicacion_id`?

Porque las ubicaciones pueden venir de varias tablas: centro base, centro usuario, lugar base o lugar usuario. Se eligieron columnas separadas y el service controla que solo una este informada.

### Por que valoraciones no tiene FK directa?

Porque puede valorar muchos tipos de contenido. La relacion se resuelve con `tipo_valoracion` e `id_relacionado`, y el service valida la existencia.

### Que relacion tiene soporte con mensajes?

Un ticket de soporte puede tener varios mensajes. Es una relacion uno a muchos.

### Que relacion alimenta estadisticas?

La relacion principal es `usuarios 1:N historial_entrenamientos`. Las estadisticas se calculan recorriendo el historial de cada usuario.

