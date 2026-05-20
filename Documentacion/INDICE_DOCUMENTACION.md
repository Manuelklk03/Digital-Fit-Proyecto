# 📚 Índice de Documentación para la Defensa

**Guía de navegación**: Usa este documento para saber qué leer según tus necesidades.

---

## 📖 Documentos Disponibles

### 1. **PUNTOS_CLAVE_DEFENSA.md** ⚡ (Empieza aquí)
**Tiempo**: 20 min de lectura
**Para**: Última revisión antes de subir a defender

Contiene:
- TL;DR del proyecto (30 segundos)
- Las 12 entidades
- 10 preguntas probables + respuestas cortas
- Ubicación de código crítico
- Checklist pre-defensa

✅ **Usa esto si**: Tienes 20 min antes de la defensa

---

### 2. **DEFENSA_TECNICA_DIGITAL_FIT.md** 📋 (Lo más completo)
**Tiempo**: 45 min de lectura
**Para**: Preparación profunda, entiender detalles

Contiene:
- Resumen del proyecto
- Stack técnico completo
- Estructura de carpetas
- Arquitectura general (flujo frontend-backend)
- Seguridad y autenticación (código real)
- Todas las entidades explicadas
- Relaciones BD
- Endpoints principales
- Frontend (rutas, servicios, guards)
- BD y datos iniciales
- Estadísticas
- **17 preguntas teóricas con respuestas detalladas y fragmentos de código**
- Glosario técnico

✅ **Usa esto si**: Quieres entender TODO en profundidad

---

### 3. **GUIA_RELACIONES_BD_APRENDE.md** 🧠 (Para aprender)
**Tiempo**: 60 min de lectura
**Para**: Entender relaciones de BD "como si fueras principiante"

Contiene:
- Concepto central: Usuario es el eje
- Entrenamientos: Por qué 3 tipos (base, usuario, comunidad)
- Ubicaciones: Por qué 2 tipos (base, usuario)
- Historial: Qué es, reglas de negocio, ejemplos
- Valoraciones: Problema, solución polimorfica, ejemplos
- Soporte: Tickets y mensajes
- Resumen visual de todas las relaciones
- Patrones y buenas prácticas
- **7 preguntas teóricas sobre relaciones**

✅ **Usa esto si**: No entiendas bien cómo se relacionan las tablas

---

### 4. **ER_DIAGRAM_PLANTUL_DIGITALFIT.puml** 📊 (Diagrama profesional)
**Formato**: PlantUML (renderizable en muchas herramientas)
**Para**: Ver todas las entidades y relaciones de un vistazo

Contiene:
- 12 entidades con atributos y tipos
- Todas las FK (Foreign Keys) destacadas
- Relaciones 1:1, 1:N visualizadas
- Notas explicativas en diagrama
- Enums embebidas
- Claves primarias destacadas en rojo

✅ **Usa esto si**: Necesitas ver la BD visualmente

**Cómo renderizar**:
- Copiar contenido del .puml
- Ir a https://www.planttext.com
- Pegar
- Ver diagrama
- O instalar extensión PlantUML en VS Code

---

### 5. **DIAGRAMA_ENTIDAD_RELACION_DIGITAL_FIT.md** 📈 (Diagrama Mermaid)
**Formato**: Mermaid (más simple que PlantUML)
**Para**: Alternativa más legible si no quieres PlantUML

Contiene:
- Mismas entidades, pero en formato Mermaid
- Más simple de leer
- Se renderiza en GitHub/MD viewers

---

## 🎯 Estrategia de Estudio Según Disponibilidad

### Si tienes 1 hora
1. Lee **PUNTOS_CLAVE_DEFENSA.md** (20 min)
2. Revisa las 10 preguntas probables (15 min)
3. Abre las carpetas de código en VS Code (15 min)
4. Practica demo (10 min)

### Si tienes 2-3 horas
1. Lee **DEFENSA_TECNICA_DIGITAL_FIT.md** completo (60 min)
2. Lee **GUIA_RELACIONES_BD_APRENDE.md** secciones 1-5 (30 min)
3. Revisa diagramas ER (10 min)
4. Practica todas las 17 preguntas (30 min)
5. Practica demo (10 min)

### Si tienes toda la noche
1. Lee todo en orden: Puntos clave → Técnica → Relaciones (120 min)
2. Estudia diagramas (20 min)
3. Contesta todas las preguntas sin ver respuestas (60 min)
4. Practica demo repetidas veces (30 min)
5. Duerme 6+ horas antes de defensa 😴

---

## 🔍 Búsqueda Rápida por Tema

### Tema: "¿Cómo funciona la autenticación?"
**Busca en**:
- DEFENSA_TECNICA: Sección "Seguridad y autenticación"
- PUNTOS_CLAVE: "P1: ¿Cómo funciona el login?"

**Ubicación código**:
- `Backend/digital-fit/src/main/java/com/example/digital_fit/config/SecurityConfig.java`
- `Backend/digital-fit/src/main/java/com/example/digital_fit/service/Auth/AuthService.java`

---

### Tema: "¿Cómo se relacionan entrenamientos y historial?"
**Busca en**:
- GUIA_RELACIONES: Sección "2. Entrenamientos" + "3. Historial"
- DEFENSA_TECNICA: Sección "Entidades principales" (EntrenamientoBase, etc.)

**Ubicación código**:
- `Backend/digital-fit/src/main/java/com/example/digital_fit/model/Entrenamientos/`

---

### Tema: "¿Por qué valoraciones es polimorfica?"
**Busca en**:
- GUIA_RELACIONES: Sección "4. Valoraciones"
- DEFENSA_TECNICA: P9, "P17: ¿Cómo gestionas las valoraciones?"

**Ubicación código**:
- `Backend/digital-fit/src/main/java/com/example/digital_fit/model/Valoracion/Valoracion.java`
- `Backend/digital-fit/src/main/java/com/example/digital_fit/service/Valoracion/ValoracionService.java`

---

### Tema: "¿Cómo están protegidas las rutas?"
**Busca en**:
- DEFENSA_TECNICA: P6, P13
- PUNTOS_CLAVE: Sección "Autenticación"

**Ubicación código**:
- `Frontend/digital-fit-frontend/src/app/guards/`

---

### Tema: "¿Cómo se valida el historial?"
**Busca en**:
- GUIA_RELACIONES: Sección "3. Historial - Reglas de negocio"
- DEFENSA_TECNICA: P8, "HistorialEntrenamientos"

**Ubicación código**:
- `Backend/digital-fit/src/main/java/com/example/digital_fit/service/Entrenamientos/HistorialEntrenamientosService.java`

---

### Tema: "¿Cómo funciona el flujo completo?"
**Busca en**:
- DEFENSA_TECNICA: P10, "Describe el flujo completo"

---

### Tema: "¿Dónde está X?"
**Busca en**:
- PUNTOS_CLAVE: Sección "📍 Ubicación de Código Crítico"

---

## 🎤 Plan de Presentación

**Seguir este orden durante la defensa**:

1. **Intro (1 min)** → Leer de **PUNTOS_CLAVE**: TL;DR
2. **Stack (1 min)** → De **DEFENSA_TECNICA**: Sección 2
3. **Arquitectura (2 min)** → De **DEFENSA_TECNICA**: Sección 4
4. **Demo (5 min)** → Mostrar app funcionando
5. **Preguntas (2-3 min)** → Usar **DEFENSA_TECNICA** para respuestas detalladas

---

## 💡 Tips de Estudio

✅ **Haz esto**:
- Abre el código en VS Code mientras lees documentación
- Cuando leas sobre `HistorialEntrenamientos`, abre ese archivo en paralelo
- Ejecuta los queries SQL en MySQL Workbench (si tienes)
- Dibuja los diagramas a mano para memorizar
- Responde preguntas SIN leer respuesta primero
- Practica demo 5+ veces

❌ **No hagas esto**:
- No memorices código línea por línea
- No intentes memorizar todos los endpoints
- No leas solo, sin abrir el código real
- No practiques la demo solo 1 vez
- No confíes solo en esta documentación (consulta el código también)

---

## 📞 Si Te Atasca Durante Defensa

**Si no recuerdas algo**:
```
Tribunal: "¿Cómo validas que el usuario solo vea sus datos?"
Tú: "Eso lo valido en el ServiceLayer. Me permites mostrar el código?"
→ Abre Backend/digital-fit/src/main/java/.../service/
→ "Ves aquí? Siempre filtro por usuario_id == usuarioActual.id"
```

**Si te preguntan algo que no esperabas**:
```
Tribunal: "¿Cómo podrías escalar la app a 1 millón de usuarios?"
Tú: "Buena pregunta. Actuallly haría... [piensa logicamente]
- Caché con Redis
- Paginación en queries
- Índices en FK (usuario_id, etc.)
- Sharding de BD
Pero eso está fuera del scope de este DAW."
```

---

## 🎯 Objetivo de Esta Documentación

✅ **Que entiendas** tu proyecto completo
✅ **Que puedas responder** preguntas teóricas AND técnicas
✅ **Que ubiques** cualquier archivo en segundos
✅ **Que hagas demo** sin hesitaciones
✅ **Que defiendas** tus decisiones de diseño

---

## 📋 Resumen: Qué Leer

| Necesidad | Lee | Tiempo |
|-----------|-----|--------|
| "Defensa en 20 min" | PUNTOS_CLAVE_DEFENSA.md | 20 min |
| "Entender todo" | DEFENSA_TECNICA_DIGITAL_FIT.md | 45 min |
| "Entender relaciones BD" | GUIA_RELACIONES_BD_APRENDE.md | 60 min |
| "Ver diagrama" | ER_DIAGRAM o DIAGRAMA_ENTIDAD_RELACION | 10 min |
| "Responder pregunta X" | Usa índice búsqueda arriba | 5 min |

---

¡Ánimo en la defensa! 🚀
