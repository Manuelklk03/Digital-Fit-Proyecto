# ⚡ Puntos Clave para la Defensa - Digital-Fit

**Formato**: Guía rápida de consulta durante la defensa. Leer antes de subir.

---

## 🎯 TL;DR (30 segundos)

**Digital-Fit** es una app web para gestionar entrenamientos personales. Usuarios pueden:
- Crear y compartir entrenamientos
- Registrar actividad en un historial
- Ver estadísticas personales
- Valorar entrenamientos/lugares/centros
- Reportar problemas en soporte

**Stack**: Spring Boot (Java) + Angular + MySQL + Docker

---

## 📊 Estructura Rápida

| Carpeta | Qué contiene |
|---------|-------------|
| `Backend/digital-fit/src/main/java/.../model` | Entidades BD (12 tablas) |
| `Backend/digital-fit/src/main/java/.../controller` | Endpoints REST (`/api/...`) |
| `Backend/digital-fit/src/main/java/.../service` | Lógica de negocio y validaciones |
| `Backend/digital-fit/src/main/java/.../repository` | Acceso a BD (JPA) |
| `Frontend/digital-fit-frontend/src/app/pages` | Pantallas Angular |
| `Frontend/digital-fit-frontend/src/app/guards` | Protección de rutas (auth) |
| `Frontend/digital-fit-frontend/src/app/services` | Llamadas HTTP al backend |

---

## 🔐 Autenticación (Lo que SIEMPRE preguntan)

**Flujo**:
```
1. Usuario → Login (/login página Angular)
2. POST /api/auth/login → Spring Security
3. Si ok: Devuelve cookie de sesión
4. GET /api/auth/me → Obtiene rol y datos
5. Header actualiza: muestra usuario + rol
```

**Seguridad**:
- ✅ Passwords cifradas con BCrypt (no reversible)
- ✅ CSRF desactivado (facilita consumo desde Angular)
- ✅ Sesiones basadas en cookies (Spring Security estándar)
- ✅ Guards en Angular: `authGuard`, `userGuard`, `adminGuard`, `publicGuard`

**Ubicación**:
- Backend: `Backend/digital-fit/src/main/java/com/example/digital_fit/config/SecurityConfig.java`
- Angular: `Frontend/digital-fit-frontend/src/app/guards/`

---

## 🎯 Las 3 Cosas Que Mostrar en Demo

### 1️⃣ Login + Crear Entrenamiento
```
Usuario Manuel → Login (username: manuel, password: 123456)
→ Ve página /inicio
→ Va a /mis-entrenamientos
→ Crea "Mi rutina de lunes"
→ Guarda
```

Archivos implicados:
- Frontend: `pages/login`, `pages/mis-entrenamientos`, `services/auth-service.ts`
- Backend: `controller/Auth/AuthController.java`, `controller/Entrenamientos/EntrenamientoUsuarioRestController.java`

### 2️⃣ Registrar Entrenamiento en Historial
```
Manuel →  /historial-entrenamientos
→ "Registrar entrenamiento realizado"
→ Selecciona: "Mi rutina de lunes" + "AltaFit" + 60 min + hoy
→ Confirma
```

Archivos implicados:
- Backend: `model/Entrenamientos/HistorialEntrenamientos.java`, `service/Entrenamientos/HistorialEntrenamientosService.java` (validaciones)

### 3️⃣ Ver Estadísticas
```
Manuel → /estadisticas
→ Ve: "60 minutos totales", "1 entrenamiento realizado", etc.
```

Archivos implicados:
- Backend: `service/Estadisticas/EstadisticasService.java` (calcula desde historial)

---

## 🗂️ Las 12 Entidades (Memoriza Nombres)

| # | Tabla | "Qué es" | FK usuario | Special |
|----|-------|----------|-----------|---------|
| 1 | `usuarios` | Central | N/A | Rol: USER, ADMIN |
| 2 | `entrenamientos_base` | Catálogo | NO | Solo lectura |
| 3 | `entrenamientos_usuario` | Mis entrenamientos | SÍ | Borrado lógico |
| 4 | `entrenamientos_comunidad` | Entrenamientos publicados | SÍ | Lo que comparto |
| 5 | `centros_privados_base` | Catálogo gimnasios | NO | Solo lectura |
| 6 | `centros_privados_usuario` | Mis gimnasios | SÍ | Borrado lógico |
| 7 | `lugares_publicos_base` | Catálogo parques | NO | Solo lectura |
| 8 | `lugares_publicos_usuario` | Mis lugares | SÍ | Borrado lógico |
| 9 | `historial_entrenamientos` | Actividad real | SÍ | **Hub central** |
| 10 | `valoraciones` | Reseñas (5 estrellas) | SÍ | Polimorfica |
| 11 | `soporte` | Tickets | SÍ | Est: ABIERTO/CERRADO |
| 12 | `mensajes_soporte` | Chats en tickets | SÍ | Cascade |

---

## 🚨 Preguntas Más Probables (Prepárate)

### P1: "¿Cómo funciona el historial?"
**R**: Es un registro de lo que hice. Apunta a:
- 1 entrenamiento (base O usuario)
- Opcionalmente 1 ubicación (centro O lugar)
- Fecha, duración, notas

Las reglas están en `HistorialEntrenamientosService` (Java). Si pasas 2 entrenamientos, lanza error.

**Dónde mostrar**: `Backend/.../model/Entrenamientos/HistorialEntrenamientos.java`

---

### P2: "¿Por qué 3 tablas de entrenamientos?"
**R**:
- `BASE`: Catálogo para todos (ej: "Full Body")
- `USUARIO`: Mi copia personal (la puedo modificar)
- `COMUNIDAD`: Que publiqué para otros (la pueden copiar)

Si otro usuario copia la mía, se crea una entrada en SU tabla usuario (son independientes).

---

### P3: "¿Cómo están protegidas las rutas?"
**R**: Con Guards:
- `publicGuard` → Solo si NO estás logueado (login, register)
- `authGuard` → Cualquier usuario logueado
- `userGuard` → Solo rol USER
- `adminGuard` → Solo rol ADMIN

**Dónde**: `Frontend/.../guards/`

---

### P4: "¿Cómo la BD está normalizada?"
**R**:
- 1NF: Cada atributo es atómico (no hay arrays en columnas)
- 2NF: Sin dependencias parciales (todos dependen de PK completa)
- 3NF: Sin dependencias transitivas

Ejemplo: No metí "lista de entrenamientos" en usuarios (sería 1NF). Uso tabla separada `entrenamientos_usuario` con FK.

---

### P5: "¿Cómo obtiene Angular la lista de entrenamientos?"
**R**:
1. Componente Angular: `ngOnInit() → this.service.listar()`
2. Service: `GET /api/entrenamientos` (HttpClient)
3. Nginx redirige a Backend:8080
4. Backend: Controller → Service → Repository → Entity
5. JPA ejecuta `SELECT * FROM entrenamientos_base`
6. Devuelve JSON al frontend
7. Angular renderiza en HTML

**Flujo**: Component → Service → HttpClient → Network → Backend Controller → Service → Repository → MySQL → Response → JSON → Template

---

### P6: "¿Por qué bcrypt y no MD5?"
**R**: 
- MD5 es rápido, pero hoy es "crackeable" (fuerza bruta)
- Bcrypt es lento a propósito (adaptativo, se ajusta con parámetro cost)
- Si roban la BD, ni siquiera MD5 te protege (hay rainbow tables)
- Bcrypt + salt: imposible de invertir, resistente a ataques

---

### P7: "¿Cómo funciona Docker Compose?"
**R**: Levanta 3 servicios en contenedores:
- MySQL (BD en puerto 3307)
- Backend Java (API en puerto 8080)
- Nginx (Frontend en puerto 4200)

Comando: `docker-compose up -d`

El backend espera a que MySQL esté listo (healthcheck). Nginx redirige `/api/*` al backend.

---

### P8: "¿Las valoraciones qué son?"
**R**: Un usuario puede dejar reseña (1-5 estrellas + comentario) en:
- Entrenamientos (base, usuario, comunidad)
- Centros y lugares
- Registros de historial

Problema: ¿Cómo una sola tabla valida múltiples tipos?
Solución: `tipo_valoracion` (ENUM) + `id_relacionado` (ID)

No hay FK física, pero hay validación en código.

---

### P9: "¿Qué es borrado lógico?"
**R**: No eliminar realmente, solo marcar `activo = false`

Razón: Si borro un entrenamiento físicamente, los registros en historial quedan huérfanos (FK rota).

Con borrado lógico: El entrenamiento "sigue existiendo" para queries de historial, pero el usuario no lo ve en su lista.

---

### P10: "¿Cómo validas que el usuario solo vea sus datos?"
**R**: En cada endpoint:

```java
@GetMapping("/mis-entrenamientos")
public List<EntrenamientoUsuario> misEntrenamientos() {
    Usuario usuarioActual = securityContext.getAuthentication().getPrincipal();
    return repo.findAllByUsuario(usuarioActual);  // Filtra por usuario
}
```

Spring Security proporciona usuario autenticado. Siempre filtro por `usuario_id == usuarioActual.id`.

---

## 📍 Ubicación de Código Crítico

**Si te preguntan "¿dónde está X?":**

```
¿Dónde está el login?
→ Backend/digital-fit/src/main/java/com/example/digital_fit/controller/Auth/AuthController.java
→ Backend/digital-fit/src/main/java/com/example/digital_fit/service/Auth/AuthService.java

¿Dónde están las entidades?
→ Backend/digital-fit/src/main/java/com/example/digital_fit/model/

¿Dónde están los endpoints?
→ Backend/digital-fit/src/main/java/com/example/digital_fit/controller/

¿Dónde es la lógica?
→ Backend/digital-fit/src/main/java/com/example/digital_fit/service/

¿Dónde está la seguridad?
→ Backend/digital-fit/src/main/java/com/example/digital_fit/config/SecurityConfig.java

¿Dónde está el historial?
→ Backend/digital-fit/src/main/java/com/example/digital_fit/model/Entrenamientos/HistorialEntrenamientos.java
→ Backend/digital-fit/src/main/java/com/example/digital_fit/service/Entrenamientos/HistorialEntrenamientosService.java

¿Dónde está la validación del historial?
→ HistorialEntrenamientosService.java - método validar()

¿Dónde están los guards?
→ Frontend/digital-fit-frontend/src/app/guards/

¿Dónde están los servicios HTTP?
→ Frontend/digital-fit-frontend/src/app/services/

¿Dónde está la configuración de BD?
→ Backend/digital-fit/src/main/resources/application.properties

¿Dónde están los datos iniciales?
→ Backend/digital-fit/src/main/resources/data.sql

¿Dónde está Docker?
→ docker-compose.yml (raíz del proyecto)
```

---

## 🎬 Orden de Defensa (Sugerencia)

1. **Intro** (1 min): "Digital-Fit gestiona entrenamientos. Usuarios pueden registrar actividad y ver estadísticas"

2. **Stack** (1 min): "Backend Spring Boot, Frontend Angular, BD MySQL, Docker"

3. **Arquitectura** (2 min): "Patrón MVC, 12 entidades relacionadas, historial es el hub central"

4. **Demo** (5 min):
   - Login como usuario demo
   - Mostrar crear entrenamiento
   - Registrar en historial
   - Ver estadísticas
   - Mostrar carpetas de código en VS Code

5. **Preguntas** (2-3 min): Respon usando esta guía

---

## 🎓 Si Te Quedan Nerviosos

**Memoriza estas 3 cosas**:

1. **Entidades**: 3 entrenamientos (base, usuario, comunidad), 3 ubicaciones (idem), historial, valoraciones, soporte
2. **Flujo**: Angular → HttpClient → Nginx → Backend Controller → Service → Repository → MySQL → Response
3. **Seguridad**: BCrypt (passwords), Spring Security (sesiones), Guards (rutas Angular)

**Si no sabes responder algo**:
- "Está en Backend/.../service, donde valido las reglas de negocio"
- "Lo puedo mostrar en el código si quieres"
- "Es un patrón de diseño que usé para..."

---

## ✅ Checklist Pre-Defensa

- [ ] He practicado el flujo completo (login → crear → registrar → ver stats)
- [ ] He abierto el código en VS Code y sé navegar las carpetas
- [ ] He memorizado las 12 entidades
- [ ] Entiendo por qué historial es un hub (apunta a 4 tablas de entrenamientos/ubicaciones)
- [ ] Sé explicar borrado lógico (`activo = false`) y por qué
- [ ] He leído sobre valoraciones polimórficas
- [ ] Puedo ejecutar `docker-compose up` y levantar la app
- [ ] Tengo los archivos abiertos en VS Code para mostrar durante defensa
- [ ] He ensayado mis respuestas a las 10 preguntas probables
- [ ] Entiendo el flujo de seguridad (BCrypt, Spring Security, Guards)

---

## 🚀 Último Consejo

**No memorices todo**. El tribunal valora que:
- ✅ Entiendas qué hiciste
- ✅ Puedas mostrar dónde está cada parte
- ✅ Resuelvas dudas con lógica (aunque no recuerdes exactamente)
- ✅ Admitas si no sabes algo ("No recuerdo de memoria, pero te lo muestro en el código")

**Lo que MATA en defensa**:
- ❌ Inventar respuestas
- ❌ Decir "no sé" sin intentar
- ❌ No poder ejecutar la demo
- ❌ No encontrar el código cuando preguntan

¡Tú puedes! 💪
