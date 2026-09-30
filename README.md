# 🏋️ Digital Fit

Aplicación web full stack desarrollada como **proyecto final de 2º de Desarrollo de Aplicaciones Web (DAW)**.

Digital Fit es una plataforma orientada al entrenamiento y la actividad física. Permite a los usuarios consultar lugares donde entrenar, gestionar ubicaciones, crear y realizar entrenamientos, compartir contenido con la comunidad y consultar su progreso mediante estadísticas e historial.

El proyecto cuenta con un frontend desarrollado con **Angular y TypeScript**, un backend basado en **Java y Spring Boot**, persistencia de datos con **MySQL** y un entorno preparado para ejecutarse mediante **Docker**.

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
- Visualización detallada de cada lugar.
- Integración de mapas mediante Leaflet.

### 🌐 Comunidad

- Consulta de entrenamientos publicados por otros usuarios.
- Visualización detallada de entrenamientos de la comunidad.
- Sistema de valoraciones.

### 🛠️ Soporte

- Creación de tickets de soporte.
- Consulta del estado y detalle de los tickets.
- Panel de gestión de soporte para administradores.

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
```

El frontend se comunica con el backend, que se encarga de la lógica de negocio, autenticación y acceso a los datos almacenados en MySQL.

---

## 🐳 Ejecución con Docker

El proyecto incluye configuración de Docker para ejecutar:

- MySQL
- Backend Spring Boot
- Frontend Angular

### Requisitos

- Docker
- Docker Compose

### Iniciar el proyecto

Desde la raíz del repositorio:

```bash
docker compose up --build
```

Una vez iniciados los contenedores:

```text
Frontend:
http://localhost:4200

Backend:
http://localhost:8080

MySQL:
localhost:3307
```

Para detener los contenedores:

```bash
docker compose down
```

---

## 💻 Ejecución en desarrollo

También es posible ejecutar frontend y backend de forma independiente.

### Frontend

```bash
cd Frontend/digital-fit-frontend
npm install
npm start
```

La aplicación estará disponible normalmente en:

```text
http://localhost:4200
```

### Backend

```bash
cd Backend/digital-fit
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

En Linux/macOS:

```bash
./mvnw spring-boot:run
```

---

## 🔐 Seguridad

La aplicación utiliza **Spring Security** para controlar el acceso a las diferentes funcionalidades.

En el frontend también existen guards para limitar el acceso a determinadas rutas dependiendo del estado de autenticación y del rol del usuario.

Se diferencian principalmente:

- Usuarios no autenticados.
- Usuarios registrados.
- Administradores.

---

## 🗺️ Mapas

Digital Fit utiliza **Leaflet** para mostrar y gestionar ubicaciones relacionadas con lugares donde realizar actividad física.

También se utiliza un sistema de geocodificación para trabajar con direcciones y ubicaciones.

---

## 📚 Documentación

El repositorio incluye documentación adicional del proyecto:

- Documentación del proyecto.
- Manual de uso.
- Manual de despliegue.
- Vídeo explicativo.
- Presentación utilizada para la defensa del proyecto.

---

## 🎓 Contexto académico

Digital Fit fue desarrollado como **proyecto final de 2º de Desarrollo de Aplicaciones Web**.

El objetivo principal fue aplicar de forma conjunta los conocimientos adquiridos durante el ciclo:

- Desarrollo frontend.
- Desarrollo backend.
- Comunicación cliente-servidor.
- Persistencia de datos.
- Seguridad.
- Bases de datos relacionales.
- Despliegue de aplicaciones.
- Docker.
- Control de versiones.

---

## 👨‍💻 Autor

**Manuel Casinos Pérez**

Desarrollador Web Junior · Técnico Superior en Desarrollo de Aplicaciones Web

[![GitHub](https://img.shields.io/badge/GitHub-Manuelklk03-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Manuelklk03)

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Manuel_Casinos_Pérez-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/manuel-casinos-perez/)
