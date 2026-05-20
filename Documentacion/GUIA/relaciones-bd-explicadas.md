# 📚 Guía Didáctica: Relaciones de la Base de Datos - Digital-Fit

> Documento para aprender y memorizar todas las relaciones entre las tablas de Digital-Fit, explicadas de forma clara y visual.

---

## 📊 Mapa Conceptual de las Relaciones

```
                    ┌──────────────────────────────────────────────┐
                    │                                              │
                    │                  USUARIO                     │
                    │           (Tabla Principal)                  │
                    │                                              │
                    └──┬──┬──┬──┬──┬──┬──┬──┬──┬──┬──┬──┬──────┘
                       │  │  │  │  │  │  │  │  │  │  │  │
         ┌─────────────┘  │  │  │  │  │  │  │  │  │  │  └──────────────┐
         ▼                ▼  │  │  │  │  │  │  │  │  ▼                 ▼
   ┌──────────┐    ┌────────┐ │  │  │  │  │  │  │  ┌─────────┐  ┌──────────┐
   │ SOPORTE  │    │MENSAJE │ │  │  │  │  │  │  │  │CENTRO   │  │ VALORACIÓN│
   │(1 ticket)│◄──►│SOPORTE │ │  │  │  │  │  │  │  │PRIVADO  │  │           │
   └──────────┘    └────────┘ │  │  │  │  │  │  │  │USUARIO  │  └──────────┘
                              │  │  │  │  │  │  │  └─────────┘
           ┌──────────────────┘  │  │  │  │  │  └──────────────────┐
           ▼                     ▼  │  │  │  ▼                     ▼
    ┌──────────────┐    ┌──────────┐ │  │  ┌──────────────┐  ┌────────────┐
    │ CENTRO       │    │ LUGAR    │ │  │  │ ENTRENAMIENTO │  │ENTRENAMIENTO│
    │ PRIVADO      │    │ PÚBLICO  │ │  │  │ USUARIO      │  │ COMUNIDAD   │
    │ USUARIO      │    │ USUARIO  │ │  │  │              │  │             │
    └──────────────┘    └──────────┘ │  │  └──────────────┘  └────────────┘
                                     │  │
                    ┌────────────────┘  └──────────────────┐
                    ▼                                       ▼
             ┌─────────────────────────────────────────────────────────┐
             │                  HISTORIAL ENTRENAMIENTOS               │
             │   (Relaciona USUARIO + ENTRENAMIENTO + LUGAR + CENTRO)  │
             └─────────────────────────────────────────────────────────┘
```

---

## 1️⃣ La Tabla CENTRAL: USUARIO

Usuario es la tabla más importante porque **casi todas las demás tablas la referencian**.

### 📝 Estructura de la tabla:
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long (PK) | Identificador único |
| `username` | String (UNIQUE) | Nombre de usuario para login |
| `email` | String (UNIQUE) | Correo electrónico |
| `password` | String | Contraseña cifrada con BCrypt |
| `rol` | Enum (USER/ADMIN) | Rol del usuario |

### 🎯 Regla mnemotécnica:
> **"Usuario es como el DNI de la app: todo está vinculado a un usuario"**

---

## 2️⃣ Las Relaciones 1:N (Un usuario → Muchos elementos)

Estas son las relaciones más comunes. Un usuario puede crear/varios elementos de cada tipo.

### 🔗 Relación 1: Usuario → CentroPrivadoUsuario

**Explicación**: Un usuario puede crear **muchos** centros privados. Cada centro privado de usuario pertenece a **un solo** usuario.

**Ejemplo real**: Mario (usuario) descubre un gimnasio pequeño en su barrio que no está en la app. Lo añade como "CentroPrivadoUsuario". Luego descubre otro y lo añade también.

**En código** (`CentroPrivadoUsuario.java`):
```java
@ManyToOne                          // Muchos centros → Un usuario
@JoinColumn(name = "usuario_id", nullable = false)
private Usuario usuario;
```

**En BD**: Tabla `centros_privados_usuario` tiene columna `usuario_id` (FK → `usuarios.id`)

---

### 🔗 Relación 2: Usuario → LugarPublicoUsuario

**Explicación**: Un usuario puede crear **muchos** lugares públicos. Cada lugar público de usuario pertenece a **un solo** usuario.

**Ejemplo real**: Mario encuentra un parque con barras de calistenia y lo añade. Luego añade una pista de atletismo.

**En código** (`LugarPublicoUsuario.java`):
```java
@ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
private Usuario usuario;
```

---

### 🔗 Relación 3: Usuario → EntrenamientoUsuario

**Explicación**: Un usuario puede crear **muchos** entrenamientos personales. Cada entrenamiento personal pertenece a **un solo** usuario.

**Ejemplo real**: Mario crea su rutina "Full Body Martes" y "Cardio Jueves". Solo él las ve.

**En código** (`EntrenamientoUsuario.java`):
```java
@ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
private Usuario usuario;
```

---

### 🔗 Relación 4: Usuario → EntrenamientoComunidad

**Explicación**: Un usuario puede compartir **muchos** entrenamientos en la comunidad. Cada entrenamiento compartido fue publicado por **un solo** usuario.

**Ejemplo real**: Mario crea una rutina "HIIT Quema Grasa" y la comparte con la comunidad para que otros usuarios puedan verla y usarla.

**En código** (`EntrenamientoComunidad.java`):
```java
@ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
private Usuario usuario;
```

---

### 🔗 Relación 5: Usuario → HistorialEntrenamientos

**Explicación**: Un usuario puede registrar **muchas** entradas en su historial de entrenamientos. Cada entrada pertenece a **un solo** usuario.

**Ejemplo real**: Mario entrena hoy y registra "30 min de carrera por el Turia". Mañana registra "1h en el gimnasio". Ambas entradas son de Mario.

**En código** (`HistorialEntrenamientos.java`):
```java
@ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
private Usuario usuario;
```

---

### 🔗 Relación 6: Usuario → Valoracion

**Explicación**: Un usuario puede hacer **muchas** valoraciones. Cada valoración fue hecha por **un solo** usuario.

**Importante**: Un usuario **no puede valorar dos veces el mismo elemento** (restricción unique: usuario_id + tipo_valoracion + id_relacionado).

**Ejemplo real**: Mario valora el gimnasio AltaFit con 5 estrellas. Luego valora el entrenamiento "Full Body" con 4 estrellas. Pero NO puede valorar AltaFit otra vez.

**En código** (`Valoracion.java`):
```java
@ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
private Usuario usuario;
```

---

### 🔗 Relación 7: Usuario → Soporte

**Explicación**: Un usuario puede abrir **muchos** tickets de soporte. Cada ticket fue abierto por **un solo** usuario.

**Ejemplo real**: Mario tiene un problema con el mapa y abre un ticket "El mapa no carga". Otro día tiene una sugerencia y abre otro ticket "Añadir más ejercicios".

**En código** (`Soporte.java`):
```java
@ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
private Usuario usuario;
```

---

### 🔗 Relación 8: Usuario → MensajeSoporte

**Explicación**: Un usuario puede enviar **muchos** mensajes en los tickets de soporte. Cada mensaje fue enviado por **un solo** usuario.

**Ejemplo real**: Mario envía un mensaje en su ticket explicando el problema. Luego envía otro mensaje con una captura de pantalla.

**En código** (`MensajeSoporte.java`):
```java
@ManyToOne
@JoinColumn(name = "emisor_id", nullable = false)
private Usuario emisor;
```

---

### 📋 RESUMEN: Las 8 relaciones 1:N de Usuario

```
USUARIO (1) ────── (N) CENTRO_PRIVADO_USUARIO    → "crea centros"
USUARIO (1) ────── (N) LUGAR_PUBLICO_USUARIO     → "crea lugares"
USUARIO (1) ────── (N) ENTRENAMIENTO_USUARIO     → "crea entrenamientos"
USUARIO (1) ────── (N) ENTRENAMIENTO_COMUNIDAD   → "comparte entrenamientos"
USUARIO (1) ────── (N) HISTORIAL_ENTRENAMIENTOS  → "registra actividad"
USUARIO (1) ────── (N) VALORACION                → "valora elementos"
USUARIO (1) ────── (N) SOPORTE                   → "abre tickets"
USUARIO (1) ────── (N) MENSAJE_SOPORTE           → "envía mensajes"
```

---

## 3️⃣ El Patrón BASE vs USUARIO (Concepto Clave)

Este es el **patrón de diseño más importante** y que más preguntan en las defensas.

### ❓ ¿Por qué hay dos tablas para lo mismo?

```
CENTRO_PRIVADO_BASE (admin)     CENTRO_PRIVADO_USUARIO (usuarios)
LUGAR_PUBLICO_BASE (admin)      LUGAR_PUBLICO_USUARIO (usuarios)
ENTRENAMIENTO_BASE (admin)      ENTRENAMIENTO_USUARIO (usuarios)
```

### 🏢 Entidades BASE
- Creadas y gestionadas por **ADMINISTRADORES**
- Son fijas, visibles para todos
- Sin FK a usuario (no pertenecen a nadie en concreto)
- Se cargan inicialmente desde `data.sql`
- Ejemplo: AltaFit Valencia Centro (CentroPrivadoBase)

### 👤 Entidades USUARIO
- Creadas por **USUARIOS NORMALES**
- Tienen campo `activo` (boolean) para soft-delete
- Tienen FK → Usuario (pertenecen a quien las creó)
- Ejemplo: "Gym de mi barrio" creado por usuario1

### 🧠 Mnemotécnica:
> **"Los admins ponen la BASE, los usuarios aportan lo suYO"**

---

## 4️⃣ La Relación COMPLEJA: HistorialEntrenamientos

Esta es la tabla **más preguntada** en defensas porque tiene 7 relaciones.

### 🗺️ Diagrama visual de HistorialEntrenamientos

```
                    ┌─────────────────────────────────────────────────────────────────┐
                    │                     HISTORIAL ENTRENAMIENTOS                    │
                    │                                                                 │
                    │  ● usuario_id ───────────── (OBLIGATORIO) ────→ USUARIO         │
                    │                                                                 │
                    │  ○ entrenamiento_base_id ── (OPCIONAL) ───────→ ENTRENAMIENTO   │
                    │  ○ entrenamiento_usuario_id (OPCIONAL) ───────→ ENTRENAMIENTO   │
                    │  ○ lugar_publico_base_id ─── (OPCIONAL) ───────→ LUGAR PUBLICO  │
                    │  ○ lugar_publico_usuario_id (OPCIONAL) ───────→ LUGAR PUBLICO  │
                    │  ○ centro_privado_base_id ── (OPCIONAL) ───────→ CENTRO PRIVADO │
                    │  ○ centro_privado_usuario_id (OPCIONAL) ───────→ CENTRO PRIVADO │
                    │                                                                 │
                    │  Además: fechaHora, duracionMinutos, notas                       │
                    └─────────────────────────────────────────────────────────────────┘

    ● = OBLIGATORIO (siempre tiene valor)
    ○ = OPCIONAL (puede ser NULL)
```

### 🎯 ¿Para qué sirve?
Para que el usuario pueda registrar **qué entrenó, dónde entrenó y durante cuánto tiempo**.

### 💡 Ejemplos de uso:

**Ejemplo 1**: "Hoy fui a AltaFit (centro base) e hice el entrenamiento Full Body (entrenamiento base)"
→ `usuario_id = 1`, `centro_privado_base_id = 1`, `entrenamiento_base_id = 1`

**Ejemplo 2**: "Corrí por el Turia (lugar público base) y no seguí ningún entrenamiento concreto"
→ `usuario_id = 1`, `lugar_publico_base_id = 1`, los demás NULL

**Ejemplo 3**: "Hice mi rutina personal 'Full Body Martes' en mi centro 'Gym Barrio'"
→ `usuario_id = 1`, `entrenamiento_usuario_id = 1`, `centro_privado_usuario_id = 1`

**Ejemplo 4**: "Solo quiero registrar que entrené 30 min, sin más detalles"
→ `usuario_id = 1`, todos los demás NULL, solo `fechaHora`, `duracionMinutos` y `notas`

### 🧠 Mnemotécnica:
> **"El historial es como un DIARIO: puedes detallar mucho (qué entrenamiento + dónde) o poco (solo tiempo y notas)"**

---

## 5️⃣ Relación Interna: Soporte → MensajeSoporte

Esta es la **única relación 1:N que NO involucra a Usuario** directamente.

### 🗺️ Diagrama:

```
SOPORTE (1) ──────── (N) MENSAJE_SOPORTE
  │                          │
  │                          └── emisor_id → USUARIO
  └── usuario_id → USUARIO
```

### 💡 Explicación:

**Soporte** es un ticket (un problema, una duda, una sugerencia).
- Tiene: asunto, mensaje inicial, fecha, estado (ABIERTO/EN_PROCESO/CERRADO)
- FK → Usuario (quién abrió el ticket)

**MensajeSoporte** son las respuestas dentro de ese ticket.
- Tiene: contenido, fecha
- FK → Soporte (a qué ticket pertenece)
- FK → Usuario (quién envió el mensaje, puede ser el usuario o el admin)

### 💡 Ejemplo:
1. Mario abre ticket: "No puedo guardar mi entrenamiento" → Soporte (estado: ABIERTO)
2. Admin responde: "¿Qué error te aparece?" → MensajeSoporte (emisor: admin)
3. Mario responde: "Me dice 'Error de conexión'" → MensajeSoporte (emisor: usuario1)
4. Admin cambia estado a CERRADO y dice: "Solucionado" → MensajeSoporte

### 🧠 Mnemotécnica:
> **"Un TICKET de soporte es como un hilo de WhatsApp: tiene muchos MENSAJES dentro"**

---

## 6️⃣ La Restricción Única en Valoracion

### ❓ ¿Qué hace especial a Valoracion?

Además de la FK a Usuario, tiene una **restricción única compuesta**:

```java
@Table(name = "valoraciones", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "usuario_id", "tipo_valoracion", "id_relacionado" })
})
```

Esto significa que **un usuario no puede valorar dos veces el mismo elemento**.

### 💡 Ejemplo:
- ✅ usuario1 valora AltaFit (tipo_valoracion = "CENTRO", id_relacionado = 1) → PERMITIDO
- ✅ usuario1 valora Full Body (tipo_valoracion = "ENTRENAMIENTO", id_relacionado = 1) → PERMITIDO
- ❌ usuario1 valora AltaFit OTRA VEZ → **BLOQUEADO** por la constraint

### 🧠 Mnemotécnica:
> **"Como en Google Maps: solo puedes dejar UNA reseña por sitio"**

---

## 7️⃣ Tabla Resumen: TODAS LAS RELACIONES

| # | Tabla A | Relación | Tabla B | Tipo | Cardinalidad | FK en |
|---|---------|----------|---------|------|-------------|-------|
| 1 | Usuario | → | CentroPrivadoUsuario | 1:N | Un usuario → muchos centros | CentroPrivadoUsuario |
| 2 | Usuario | → | LugarPublicoUsuario | 1:N | Un usuario → muchos lugares | LugarPublicoUsuario |
| 3 | Usuario | → | EntrenamientoUsuario | 1:N | Un usuario → muchos entrenamientos | EntrenamientoUsuario |
| 4 | Usuario | → | EntrenamientoComunidad | 1:N | Un usuario → muchos compartidos | EntrenamientoComunidad |
| 5 | Usuario | → | HistorialEntrenamientos | 1:N | Un usuario → muchos registros | HistorialEntrenamientos |
| 6 | Usuario | → | Valoracion | 1:N | Un usuario → muchas valoraciones | Valoracion |
| 7 | Usuario | → | Soporte | 1:N | Un usuario → muchos tickets | Soporte |
| 8 | Usuario | → | MensajeSoporte | 1:N | Un usuario → muchos mensajes | MensajeSoporte |
| 9 | Soporte | → | MensajeSoporte | 1:N | Un ticket → muchos mensajes | MensajeSoporte |
| 10 | Historial | → | EntrenamientoBase | N:1 opcional | Varios historiales → un entrenamiento | Historial |
| 11 | Historial | → | EntrenamientoUsuario | N:1 opcional | Varios historiales → un entrenamiento | Historial |
| 12 | Historial | → | LugarPublicoBase | N:1 opcional | Varios historiales → un lugar | Historial |
| 13 | Historial | → | LugarPublicoUsuario | N:1 opcional | Varios historiales → un lugar | Historial |
| 14 | Historial | → | CentroPrivadoBase | N:1 opcional | Varios historiales → un centro | Historial |
| 15 | Historial | → | CentroPrivadoUsuario | N:1 opcional | Varios historiales → un centro | Historial |

**Total: 15 relaciones entre 11 tablas**

---

## 8️⃣ Preguntas Tipo Test para Auto-evaluación

**P1**: ¿Qué tipo de relación hay entre Usuario y Soporte?
- a) 1:1
- b) 1:N ✅
- c) N:M
- d) No hay relación

**P2**: ¿Cuántas claves foráneas tiene HistorialEntrenamientos?
- a) 1
- b) 3
- c) 7 ✅ (una obligatoria + 6 opcionales)
- d) Ninguna

**P3**: ¿Un usuario puede valorar dos veces el mismo centro privado?
- a) Sí
- b) No ✅ (unique constraint lo impide)
- c) Sí, si cambia la puntuación
- d) Depende del rol

**P4**: ¿Qué entidades tienen el campo `activo`?
- a) Solo CentroPrivadoBase
- b) CentroPrivadoUsuario, LugarPublicoUsuario, EntrenamientoUsuario ✅
- c) Todas las entidades
- d) Ninguna

**P5**: ¿Cuál es la principal diferencia entre EntrenamientoUsuario y EntrenamientoComunidad?
- a) El nivel de entrenamiento
- b) EntrenamientoUsuario es privado, EntrenamientoComunidad es público ✅
- c) EntrenamientoComunidad no tiene FK a Usuario
- d) Son iguales

**P6**: ¿Por qué hay tablas BASE y tablas USUARIO para centros/lugares/entrenamientos?
- a) Porque son tecnologías diferentes
- b) Para separar contenido de admin (base) del contenido de usuarios ✅
- c) Por error de diseño
- d) Para duplicar datos

**P7**: ¿Qué hace la anotación `@Enumerated(EnumType.STRING)`?
- a) Guarda el enum como número
- b) Guarda el enum como texto VARCHAR ✅
- c) Ignora el enum
- d) Cifra el enum

**P8**: ¿Qué pasa si se intenta borrar un usuario que tiene centros creados?
- a) Se borra todo en cascada
- b) La BD lanza error por violación de FK ✅
- c) Se borra solo el usuario
- d) Se desactiva el usuario

**Respuestas**: 1-b, 2-c, 3-b, 4-b, 5-b, 6-b, 7-b, 8-b

---

## 9️⃣ Flashcards para Memorizar Rápido

Corta esta sección en tarjetas físicas o digitales:

**CARA 1** → **CARA 2**

---

**Usuario → CentroPrivadoUsuario** = 1:N (un usuario crea muchos centros)

**Usuario → Historial** = 1:N (un usuario registra muchas actividades)

**Soporte → MensajeSoporte** = 1:N (un ticket tiene muchos mensajes)

**Entidad BASE** = Gestionada por ADMIN (AltaFit, Jardín del Turia)

**Entidad USUARIO** = Creada por USER (Mi gym de barrio, con campo `activo`)

**Historial tiene 7 FKs** = 1 obligatoria + 6 opcionales

**Valoracion NO duplica** = UniqueConstraint (usuario + tipo + idRelacionado)

**Enums como texto** = `@Enumerated(EnumType.STRING)` → VARCHAR legible

---

> **📌 Consejo final**: Apréndete bien la tabla de las 15 relaciones de la sección 7. Si te preguntan "¿cómo se relacionan las entidades en Digital-Fit?", puedes nombrar las relaciones clave: Usuario con sus 8 tablas hijas, Historial con sus 7 relaciones, y la interna Soporte-MensajeSoporte.