# 🏋️ Digital Fit

Aplicación web full stack desarrollada como **proyecto final de 2º de Desarrollo de Aplicaciones Web (DAW)**.

Digital Fit es una plataforma orientada al mundo del entrenamiento y la actividad física. Permite a los usuarios consultar lugares donde entrenar, gestionar sus propios centros y ubicaciones, crear y realizar entrenamientos, compartir contenido con la comunidad y consultar su progreso mediante estadísticas e historial.

El proyecto cuenta con un frontend desarrollado con **Angular y TypeScript**, un backend basado en **Java y Spring Boot**, persistencia de datos con **MySQL** y un entorno completo preparado para ejecutarse mediante **Docker**.

---

## 🚀 Funcionalidades

### 👤 Usuarios

- Registro e inicio de sesión.
- Sistema de autenticación.
- Control de acceso mediante roles.
- Rutas protegidas según el tipo de usuario.

### 🏋️ Entrenamientos

- Consulta de entrenamientos.
- Creación y gestión de entrenamientos propios.
- Visualización del detalle de cada entrenamiento.
- Entrenamientos compartidos por la comunidad.
- Historial de entrenamientos realizados.
- Estadísticas de actividad.

### 📍 Lugares de entrenamiento

- Consulta de centros privados.
- Consulta de lugares públicos.
- Gestión de centros propios.
- Gestión de ubicaciones propias.
- Visualización de información detallada de cada lugar.
- Integración de mapas mediante Leaflet.

### 🌐 Comunidad

- Consulta de entrenamientos publicados por otros usuarios.
- Visualización detallada de entrenamientos de la comunidad.
- Sistema de valoraciones.

### 🛠️ Soporte

- Creación de tickets de soporte.
- Consulta del estado y detalle de los tickets.
- Panel específico para la gestión de soporte por parte de administradores.

### 🔐 Administración

- Rutas exclusivas para administradores.
- Gestión de tickets de soporte.
- Creación de nuevos usuarios administradores.

---

## 🛠️ Tecnologías utilizadas

### Frontend

![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![RxJS](https://img.shields.io/badge/RxJS-B7178C?style=for-the-badge&logo=reactivex&logoColor=white)
![Leaflet](https://img.shields.io/badge/Leaflet-199900?style=for-the-badge&logo=leaflet&logoColor=white)

### Backend

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Hibernate](https://img.shields.io/badge/JPA%20%2F%20Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

### Base de datos

![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### Desarrollo y despliegue

![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)

---

## 🏗️ Arquitectura del proyecto

El repositorio está dividido principalmente en dos aplicaciones:

```text
Digital-Fit-Proyecto/
│
├── Backend/
│   └── digital-fit/
│       └── Aplicación Java + Spring Boot
│
├── Frontend/
│   └── digital-fit-frontend/
│       └── Aplicación Angular
│
├── Documentacion/
│
├── Manuales (Uso y Despliegue) y video explicativo/
│
├── Presentación - Defensa/
│
├── docker-compose.yml
│
└── README.md
