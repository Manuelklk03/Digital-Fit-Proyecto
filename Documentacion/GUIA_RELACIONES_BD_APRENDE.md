# Guía: Relaciones de Base de Datos en Digital-Fit

**Propósito**: Entender cómo se relacionan las entidades y por qué están diseñadas así. Preparación para preguntas como: "¿Cómo se conectan los entrenamientos con el historial?", "¿Por qué Valoraciones tiene esa estructura tan rara?", etc.

---

## 🎯 Concepto Central: Usuario es el Eje

Imagina que tu base de datos tiene UN punto central: el usuario. Todo lo que sucede en Digital-Fit gira alrededor de un usuario autenticado.

```
                        ┌─────────────┐
                        │   USUARIO   │ ← Centro de todo
                        └──────┬──────┘
             ┌──────┬──────┬────┼────┬─────┬──────┐
             │      │      │    │    │     │      │
             ▼      ▼      ▼    ▼    ▼     ▼      ▼
          ENT.*   CENTRO.*  LUG.* HIST. VAL.   SOPOR.
          
* = Versiones Usuario (privadas) y Base (catálogo)
HIST. = Historial de entrenamientos (actividad real)
VAL. = Valoraciones
SOPOR. = Soporte/Tickets
```

**Regla de oro**: Si algo es de un usuario, hay una FK `usuario_id` que lo apunta.

---

## 1. Entrenamientos: Base vs Usuario vs Comunidad

### 🏗️ Problema que resuelven estos 3 tipos

Tu app necesita:
- **Entrenamientos predefinidos** (ej: "Full Body Principiante") → Catálogo para todos
- **Entrenamientos personalizados** (ej: El usuario crea "Mi rutina de lunes") → Privados del usuario
- **Entrenamientos compartidos** (ej: El usuario publica "Mi programa HIIT") → Para que otros copien

**Solución**: 3 tablas separadas

### 📊 ENTRENAMIENTOS_BASE

```sql
CREATE TABLE entrenamientos_base (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(255) UNIQUE,        -- "Full Body Principiante"
    descripcion TEXT,                  -- Detalles
    categoria ENUM(...),               -- FUERZA_TOTAL, RUNNING, etc.
    nivel ENUM(...),                   -- PRINCIPIANTE, INTERMEDIO, etc.
    duracion_en_minutos INT            -- 60, 30, etc.
);
```

**Características**:
- NO tiene `usuario_id` → Es de la app, visible para todos
- Nombre ÚNICO → No puedes tener 2 entrenamientos "Full Body" iguales
- Solo el ADMIN puede crear/editar
- Endpoint: `GET /api/entrenamientos` (sin auth, pero con sesión)

**En código**:
- Entidad: `Backend/digital-fit/src/main/java/com/example/digital_fit/model/Entrenamientos/EntrenamientoBase.java`
- Servicio: `Backend/digital-fit/src/main/java/com/example/digital_fit/service/Entrenamientos/EntrenamientoBaseService.java`
- Controlador: `Backend/digital-fit/src/main/java/com/example/digital_fit/controller/Entrenamientos/EntrenamientoBaseRestController.java`

### 📋 ENTRENAMIENTOS_USUARIO

```sql
CREATE TABLE entrenamientos_usuario (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(255),               -- NO unique (cada usuario puede tener "Mi rutina")
    descripcion TEXT,
    categoria ENUM(...),
    nivel ENUM(...),
    duracion_en_minutos INT,
    activo BOOLEAN,                    -- Borrado lógico (no se elimina realmente)
    usuario_id BIGINT NOT NULL,        -- ← FK: pertenece a este usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
```

**Características**:
- Tiene `usuario_id` → Privado del usuario
- Nombre NO único → Varias personas pueden tener "Mi rutina"
- `activo = false` → Es borrado lógico, no se elimina (para no romper historial)
- El usuario crea, edita, borra sus propios entrenamientos

**Origen**:
1. Usuario crea uno desde cero
2. Usuario copia uno de `ENTRENAMIENTOS_BASE`
3. Usuario copia uno de `ENTRENAMIENTOS_COMUNIDAD`

**En código**:
- Entidad: `Backend/digital-fit/src/main/java/com/example/digital_fit/model/Entrenamientos/EntrenamientoUsuario.java`
- Servicio: `Backend/digital-fit/src/main/java/com/example/digital_fit/service/Entrenamientos/EntrenamientoUsuarioService.java`

### 👥 ENTRENAMIENTOS_COMUNIDAD

```sql
CREATE TABLE entrenamientos_comunidad (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(255),
    descripcion TEXT,
    categoria ENUM(...),
    nivel ENUM(...),
    duracion_en_minutos INT,
    fecha_publicacion TIMESTAMP,
    usuario_id BIGINT NOT NULL,        -- ← FK: quién lo publicó
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
```

**Características**:
- Es un `ENTRENAMIENTOS_USUARIO` que el usuario decidió publicar
- Tiene `usuario_id` → Apunta al usuario que lo compartió
- Otros usuarios pueden verlo y copiarlo (crea una copia en su tabla `ENTRENAMIENTOS_USUARIO`)
- No es un borrado lógico: si lo borras, se elimina completamente (otros ya lo copiaron)

**En código**:
- Controlador: `Backend/digital-fit/src/main/java/com/example/digital_fit/controller/Entrenamientos/EntrenamientoComunidadRestController.java`

### 🔄 Flujo Completo

```
1. App carga 10 entrenamientos en ENTRENAMIENTOS_BASE (admin, al inicio)
   ↓
2. Usuario Manuel ve "Full Body Principiante" (es BASE)
   ↓
3. Manuel copia → se crea entrada en ENTRENAMIENTOS_USUARIO con su nombre, su usuario_id
   ↓
4. Manuel lo modifica a su gusto → actualiza su ENTRENAMIENTOS_USUARIO
   ↓
5. Manuel quiere compartirlo → se crea entrada en ENTRENAMIENTOS_COMUNIDAD
   ↓
6. Otro usuario Laura ve la comunidad
   ↓
7. Laura copia → se crea nueva entrada en su ENTRENAMIENTOS_USUARIO
   ↓
8. Laura tiene su copia personal (independiente de la de Manuel)
```

**Pregunta típica del tribunal**: "¿Qué pasa si Manuel borra su entrenamiento de comunidad?"
- **Respuesta**: Se elimina de `ENTRENAMIENTOS_COMUNIDAD`, pero las copias en `ENTRENAMIENTOS_USUARIO` de otros usuarios permanecen.

**Pregunta típica**: "¿Por qué `ENTRENAMIENTOS_USUARIO` tiene borrado lógico con `activo = false`?"
- **Respuesta**: Para no romper el historial. Si borrásemos realmente, los registros en `HISTORIAL_ENTRENAMIENTOS` quedarían sin FK válida.

---

## 2. Ubicaciones: Centro Privado vs Lugar Público

### 🏋️ CENTROS_PRIVADOS_BASE (Catálogo de Gimnasios)

```sql
CREATE TABLE centros_privados_base (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(255) UNIQUE,        -- "AltaFit Valencia"
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    horario VARCHAR(255),
    precio_mensual DECIMAL(10,2),      -- 50.00
    descripcion TEXT,
    latitud DOUBLE,                    -- Para mapa
    longitud DOUBLE,
    -- Solo lectura para usuarios, solo admin puede editar
);
```

**Uso**: El usuario ve el catálogo de gimnasios, puede guardar los que le interesan.

### ⭐ CENTROS_PRIVADOS_USUARIO (Mis Gimnasios Guardados)

```sql
CREATE TABLE centros_privados_usuario (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(255),
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    horario VARCHAR(255),
    precio_mensual DECIMAL(10,2),
    descripcion TEXT,
    latitud DOUBLE,
    longitud DOUBLE,
    activo BOOLEAN,                    -- Borrado lógico
    usuario_id BIGINT NOT NULL,        -- ← FK: mis centros
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
```

**Origen**:
1. Usuario ve un centro en `CENTROS_PRIVADOS_BASE`
2. Usuario pincha "Guardar" → se crea entrada en `CENTROS_PRIVADOS_USUARIO` con sus datos (copia)
3. O usuario crea uno manualmente desde el mapa

**Diferencia importante**: 
- El centro BASE es de lectura (solo admin lo modifica)
- El centro USUARIO es editable (el usuario puede cambiar precios, horarios, notas locales)

### 🏞️ LUGARES_PUBLICOS_BASE vs LUGARES_PUBLICOS_USUARIO

**Igual concepto que centros privados**:
- BASE = Catálogo: "Parque Turia", "Playa Malvarrosa"
- USUARIO = Mis lugares: Si yo guardo "Parque Turia", se crea copia en mi zona privada

```
LUGARES_PUBLICOS_BASE tiene tipo = {PARQUE_PUBLICO, PLAYA_DEPORTIVA}
```

---

## 3. El Centro del Sistema: HISTORIAL_ENTRENAMIENTOS

### 📊 ¿Qué es el Historial?

"El registro de que yo hice X entrenamiento en Y fecha durante Z minutos en U ubicación"

```sql
CREATE TABLE historial_entrenamientos (
    id BIGINT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,                    -- ← Quién lo hizo
    
    -- Exactamente UNO de estos debe estar lleno:
    entrenamiento_base_id BIGINT,                 -- Hice el "Full Body" de la app
    entrenamiento_usuario_id BIGINT,              -- Hice "Mi rutina personal"
    
    -- Opcionalmente UNO de estos (puede ser NULL si no registré ubicación):
    lugar_publico_base_id BIGINT,                 -- Entrenar en "Parque Turia"
    lugar_publico_usuario_id BIGINT,              -- O en "Mi parque favorito"
    centro_privado_base_id BIGINT,                -- O en "AltaFit"
    centro_privado_usuario_id BIGINT,             -- O en "Mi gym"
    
    fecha_hora TIMESTAMP,                          -- Cuándo
    duracion_minutos INT,                          -- Cuánto tiempo
    notas TEXT,                                    -- Notas libres
    
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (entrenamiento_base_id) REFERENCES entrenamientos_base(id),
    FOREIGN KEY (entrenamiento_usuario_id) REFERENCES entrenamientos_usuario(id),
    FOREIGN KEY (lugar_publico_base_id) REFERENCES lugares_publicos_base(id),
    -- ... etc
);
```

### 🔑 Reglas de Negocio (Validadas en Código)

Están en: `Backend/digital-fit/src/main/java/com/example/digital_fit/service/Entrenamientos/HistorialEntrenamientosService.java`

**Regla 1**: Exactamente 1 entrenamiento
```java
// Error si no hay entrenamiento
if (entrenamientoBaseId == null && entrenamientoUsuarioId == null) {
    throw new exception("Debes indicar un entrenamiento");
}

// Error si hay ambos (es OR, no AND)
if (entrenamientoBaseId != null && entrenamientoUsuarioId != null) {
    throw new exception("Solo 1 entrenamiento por registro");
}
```

**Regla 2**: Máximo 1 ubicación
```java
int ubicacionesContadas = 0;
if (lugarPublicoBaseId != null) ubicacionesContadas++;
if (lugarPublicoUsuarioId != null) ubicacionesContadas++;
if (centroPrivadoBaseId != null) ubicacionesContadas++;
if (centroPrivadoUsuarioId != null) ubicacionesContadas++;

if (ubicacionesContadas > 1) {
    throw new exception("Max 1 ubicación");
}
// Si ubicacionesContadas == 0, es OK (entrenar sin registrar lugar)
```

**Regla 3**: Si usas un recurso privado tuyo, debe existir y ser tuyo
```java
if (entrenamiento_usuario_id != null) {
    EntrenamientoUsuario ent = repo.findById(entrenamientoUsuarioId);
    if (!ent.getUsuario().getId().equals(usuarioActual.getId())) {
        throw new Exception("No puedes usar entrenamientos de otro usuario");
    }
}
// Lo mismo para centros y lugares privados del usuario
```

### 📈 ¿Por qué es importante?

El historial alimenta las ESTADÍSTICAS. Todas las métricas se calculan consultando esta tabla:

```sql
-- ¿Cuántos entrenamientos hice?
SELECT COUNT(*) FROM historial_entrenamientos WHERE usuario_id = ?

-- ¿Cuántos minutos totales?
SELECT SUM(duracion_minutos) FROM historial_entrenamientos WHERE usuario_id = ?

-- ¿Qué centro visité más?
SELECT centro_privado_base_id, COUNT(*) 
FROM historial_entrenamientos 
WHERE usuario_id = ? AND centro_privado_base_id IS NOT NULL
GROUP BY centro_privado_base_id
ORDER BY COUNT(*) DESC LIMIT 1
```

---

## 4. Valoraciones: La Relación Polimorfica (COMPLEJA)

### ⭐ ¿Qué es una Valoración?

"Yo (usuario) digo que este entrenamiento/centro/lugar es de 4 estrellas porque es muy bueno"

### ❌ Primer intento (Malo)

```sql
-- Opción A: Crear 8 tablas de valoraciones (una por tipo)
valoraciones_entrenamientos_base (usuario_id, entrenamiento_id, puntuacion)
valoraciones_entrenamientos_usuario (usuario_id, entrenamiento_id, puntuacion)
valoraciones_centros_base (usuario_id, centro_id, puntuacion)
-- ... etc 5 tablas más

-- PROBLEMA: Duplicación, confusión, no escalable
```

### ✅ Segunda intención (La que se usa)

Una única tabla que puede apuntar a cualquier tipo:

```sql
CREATE TABLE valoraciones (
    id BIGINT PRIMARY KEY,
    puntuacion INT CHECK (puntuacion >= 1 AND puntuacion <= 5),
    comentario TEXT,
    fecha TIMESTAMP,
    
    -- Qué tipo de cosa estoy valorando
    tipo_valoracion ENUM (
        'CENTRO_PRIVADO_BASE',
        'CENTRO_PRIVADO_USUARIO',
        'LUGAR_PUBLICO_BASE',
        'LUGAR_PUBLICO_USUARIO',
        'ENTRENAMIENTO_BASE',
        'ENTRENAMIENTO_USUARIO',
        'ENTRENAMIENTO_COMUNIDAD',
        'HISTORIAL_ENTRENAMIENTO'
    ),
    
    -- El ID de esa cosa (en su tabla original)
    id_relacionado BIGINT,              -- ← Nota: no es FK física, sino lógica
    
    usuario_id BIGINT NOT NULL,         -- ← Quien valora (FK física)
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    
    -- Restricción: Un usuario solo 1 valoración por contenido
    UNIQUE (usuario_id, tipo_valoracion, id_relacionado)
);
```

### 🔍 Ejemplo de Uso

```
Yo (usuario 42) quiero valorar el entrenamiento base "Full Body" (id 10)

INSERT INTO valoraciones (
    puntuacion = 5,
    comentario = "Muy buen entrenamiento",
    tipo_valoracion = 'ENTRENAMIENTO_BASE',
    id_relacionado = 10,
    usuario_id = 42
)

Después, para obtener valoraciones del "Full Body":
SELECT * FROM valoraciones 
WHERE tipo_valoracion = 'ENTRENAMIENTO_BASE' 
AND id_relacionado = 10
```

### ⚠️ El Problema: Integridad

**¿Qué pasa si alguien elimina el entrenamiento base?**

```
Entrenamiento "Full Body" (id 10) tiene 5 valoraciones.
Admin borra "Full Body"

Ahora hay valoraciones huérfanas:
- id_relacionado = 10
- tipo_valoracion = 'ENTRENAMIENTO_BASE'
- Pero no existe ese entrenamiento en su tabla
```

**Solución**: Validar en código (no en BD)

En `Backend/digital-fit/src/main/java/com/example/digital_fit/service/Valoracion/ValoracionService.java`:

```java
public void crear(ValoracionRequest req, Usuario usuario) {
    // Validar que el contenido exista
    if (req.getTipoValoracion().equals(TipoDeValoracion.ENTRENAMIENTO_BASE)) {
        EntrenamientoBase ent = entrenamientoBaseRepo.findById(req.getIdRelacionado())
            .orElseThrow(() -> new Exception("Entrenamiento no existe"));
    }
    
    // Validar que el usuario tenga permisos
    if (req.getTipoValoracion().equals(TipoDeValoracion.ENTRENAMIENTO_USUARIO)) {
        EntrenamientoUsuario ent = entrenamientoUsuarioRepo.findById(req.getIdRelacionado())
            .orElseThrow(() -> new Exception("No existe"));
        
        // Si es usuario privado, solo el dueño puede verlo (pero otros SÍ pueden valorarlo)
        // Depende de la lógica del negocio
    }
    
    // Crear o actualizar (UPSERT)
    Valoracion val = valoracionRepo.findByUsuarioAndTipoAndId(usuario, req.getTipoValoracion(), req.getIdRelacionado())
        .orElse(new Valoracion());
    val.setPuntuacion(req.getPuntuacion());
    val.setComentario(req.getComentario());
    // ... etc
    valoracionRepo.save(val);
}
```

### 🎯 Ventajas de este Diseño

✅ Una sola tabla para todo  
✅ Fácil de escalar (agregar nuevos tipos)  
✅ Lógica centralizada  

### ⚠️ Desventajas

❌ No hay FK física (integridad en código)  
❌ Necesita validaciones manuales  
❌ Las queries son menos eficientes  

---

## 5. Soporte: Tickets y Mensajes

### 🆘 SOPORTE (Tickets)

```sql
CREATE TABLE soporte (
    id BIGINT PRIMARY KEY,
    asunto VARCHAR(255),               -- "La app se cuelga"
    mensaje TEXT,                       -- Descripción del problema
    fecha TIMESTAMP,                    -- Cuándo abrió el ticket
    estado ENUM ('ABIERTO', 'EN_PROCESO', 'CERRADO'),
    usuario_id BIGINT NOT NULL,        -- Quién abrió el ticket
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
```

### 💬 MENSAJES_SOPORTE (Conversaciones en Tickets)

```sql
CREATE TABLE mensajes_soporte (
    id BIGINT PRIMARY KEY,
    contenido TEXT,                    -- El mensaje
    fecha TIMESTAMP,
    ticket_id BIGINT NOT NULL,         -- A qué ticket pertenece
    emisor_id BIGINT NOT NULL,         -- Quién lo envió (usuario o admin)
    FOREIGN KEY (ticket_id) REFERENCES soporte(id),
    FOREIGN KEY (emisor_id) REFERENCES usuarios(id),
    -- CASCADE: Si borro el ticket, se borran sus mensajes
);
```

### 🔄 Relación OneToMany con CASCADE

En código (entidad Soporte):

```java
@Entity
public class Soporte {
    @Id
    private Long id;
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MensajeSoporte> mensajes;
}
```

**¿Qué significa?**
- `cascade = CascadeType.ALL` → Si borro un ticket, se borran automáticamente sus mensajes
- `orphanRemoval = true` → Si saco un mensaje de la lista, se elimina de BD

---

## 6. Resumen Visual: Todas las Relaciones

```
                                ┌────────────────────────────────────┐
                                │        USUARIOS (Central)          │
                                │ id, username, email, password, rol │
                                └────────────┬───────────────────────┘
                                             │
                    ┌────────────┬───────────┼────────┬──────┬─────────┐
                    │            │           │        │      │         │
                    ▼            ▼           ▼        ▼      ▼         ▼
        ┌──────────────────┐  ┌─────────┐  ┌─────────────────────┐   ┌──────────┐
        │ ENT_USUARIO (1:N)│  │CENTRO_* │  │HISTORIAL (1:N)      │   │SOPORTE   │
        │ id, nombre, ..   │  │ (1:N)   │  │id, usuario_id, ...  │   │(1:N)     │
        └──────────────────┘  └─────────┘  │fechaHora, duracion  │   └────┬─────┘
                                           │                     │        │
        ┌──────────────────┐  ┌─────────┐  │ Apunta a:          │        │
        │ENT_COMUNIDAD(1:N)│  │LUGAR_*  │  │  - ENT_BASE        │        │
        │id, nombre, ..    │  │(1:N)    │  │  - ENT_USUARIO     │        ▼
        └──────────────────┘  └─────────┘  │  - CENTRO_BASE     │   ┌──────────────┐
                                           │  - CENTRO_USUARIO  │   │MENSAJES_SOPORTE│
        ┌──────────────────┐               │  - LUGAR_BASE      │   │id, contenido,..│
        │VALORACIONES      │               │  - LUGAR_USUARIO   │   │(1:N)           │
        │(1 usuario, múlt. │               │                    │   └────────────────┘
        │tipos contenido)  │               └────────────────────┘
        └──────────────────┘               

        Catálogos (sin usuario_id):
        ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
        │ENT_BASE      │    │CENTRO_BASE   │    │LUGAR_BASE    │
        │(readonly)    │    │(readonly)    │    │(readonly)    │
        └──────────────┘    └──────────────┘    └──────────────┘
              ▲                    ▲                     ▲
              │ referencia         │ referencia         │ referencia
              └────────────────────┴─────────────────────┘
                  (pueden estar en HISTORIAL)
```

---

## 7. Patrones y Buenas Prácticas

### ✅ Borrado Lógico vs Borrado Físico

**Tablas que usan borrado lógico** (`activo = false`):
- `ENTRENAMIENTOS_USUARIO` → Porque otros registros en HISTORIAL la referencian
- `CENTROS_PRIVADOS_USUARIO` → Mismo motivo
- `LUGARES_PUBLICOS_USUARIO` → Mismo motivo

**Tablas que usan borrado físico** (se eliminan realmente):
- `ENTRENAMIENTOS_COMUNIDAD` → Ya está copiada por otros usuarios
- `SOPORTE` (cuando se autoriza) → Temporal, no afecta otros registros
- `VALORACIONES` → Puede eliminarse sin afectar integridad

**Por qué borrado lógico en entrenamientos de usuario:**
```
Si borro físicamente "Mi rutina" (id 50), pero tengo 10 registros en HISTORIAL que apuntan a id 50, 
esos registros quedan huérfanos (FK rota).

Con borrado lógico: El entrenamiento "sigue existiendo" en BD para consultas de historial,
pero el usuario no lo ve en su lista.
```

### 🔑 Restricciones UNIQUE

| Tabla | Restricción | Razón |
|-------|-------------|-------|
| `USUARIOS` | `username` UNIQUE | Autenticación: cada user debe tener username único |
| `USUARIOS` | `email` UNIQUE | No duplicar cuentas por email |
| `ENT_BASE` | `nombre` UNIQUE | Catálogo: no puede haber 2 "Full Body" iguales |
| `CENTRO_BASE` | `nombre` UNIQUE | Catálogo: no puede haber 2 "AltaFit" iguales |
| `LUGAR_BASE` | `nombre` UNIQUE | Catálogo: no puede haber 2 "Parque Turia" iguales |
| `VALORACIONES` | `(usuario_id, tipo, id_relacionado)` UNIQUE | Un usuario solo 1 valoración por contenido |

---

## 📝 Preguntas de Defensa Sobre Relaciones

### Pregunta 1: "¿Cómo se diferencia un entrenamiento base de uno de usuario?"

**Respuesta**:
- **Base**: En tabla `entrenamientos_base`, SIN usuario_id, es el catálogo que ve todo el mundo
- **Usuario**: En tabla `entrenamientos_usuario`, CON usuario_id, es privado del usuario
- El usuario copia uno de base → se genera nuevo en tabla usuario
- El usuario modifica su copia sin afectar la base

### Pregunta 2: "¿Por qué el historial apunta a 4 tablas de entrenamientos y ubicaciones?"

**Respuesta**:
- Porque puede ser: entrenamiento base O usuario
- Y puede ser en: centro base O centro usuario O lugar base O lugar usuario
- Así con 1 tabla capturamos todas las combinaciones
- Las FK son opcionales (ubicación puede ser null)

### Pregunta 3: "¿Cómo funciona la valoración polimorfica? ¿No es arriesgado?"

**Respuesta**:
- Sí, es compleja. Usamos `tipo_valoracion` + `id_relacionado` para apuntar a cualquier tipo
- Riesgo: No hay FK física, así que pueden quedar valoraciones huérfanas
- Solución: Validamos en código (`ValoracionService`) que el contenido exista
- Ventaja: UNA tabla para TODO en lugar de 8 tablas separadas

### Pregunta 4: "¿Qué pasa si elimino un entrenamiento que está en el historial?"

**Respuesta**:
- Si es `entrenamientos_usuario`: Usamos borrado lógico (`activo = false`), no se elimina físicamente
- El historial sigue apuntando a ese id, pero el entrenamiento no se ve en la lista del usuario
- Si es `entrenamientos_base`: Solo admin puede eliminarlo, y afectaría a muchos historiales

### Pregunta 5: "¿Por qué hay 2 tipos de centros/lugares (base y usuario)?"

**Respuesta**:
- Base = Catálogo general (gimnasios reales, parques públicos)
- Usuario = Mis favoritos/personalizados (uno que vi y guardé, o uno que creé)
- Permite que cada usuario tenga su zona privada sin duplicar el catálogo

---

## 🎓 Conclusión

La BD de Digital-Fit sigue estos principios:

1. **Usuario es central**: Todo privado pertenece a un usuario (FK usuario_id)
2. **Separación base/usuario**: Catálogo vs zona privada
3. **Integridad mediante código**: Validaciones en servicios, no solo en BD
4. **Borrado lógico para historial**: No romper registros históricos
5. **Flexibilidad con polimorfismo**: Valoraciones pueden apuntar a múltiples tipos

Estos patrones son comunes en aplicaciones modernas y muestran buenas prácticas de diseño.
