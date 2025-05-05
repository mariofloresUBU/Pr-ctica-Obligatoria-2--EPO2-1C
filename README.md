# Práctica 2

Aplicación de gestión de partidos de baloncesto con arquitectura dividida en dos módulos:

- **Frontend**: Aplicación web en Java (Spring Boot)
- **Backend**: API REST en Python (Flask) con base de datos SQLite

---

## Estructura del proyecto

```
Practica-Obligatoria-2--EPO2-1C/
│
├── api/                     # Backend en Flask
│   ├── instance/
│   │   └── baloncesto.db
│   ├── __init__.py
│   ├── app.py
│   ├── config.py
│   ├── init_db.py
│   ├── models.py
│   ├── requirements.txt
│   ├── routes.py
│   └── run.py
│
├── frontend/                # Aplicación Spring Boot
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── es/ubu/baloncesto/
│           │       ├── Application.java
│           │       ├── config/
│           │       │   └── SecurityConfig.java
│           │       ├── controller/
│           │       │   ├── ApiController.java
│           │       │   ├── HomeController.java
│           │       │   ├── LoginController.java
│           │       │   └── PartidoController.java
│           │       ├── exception/
│           │       │   ├── ApiException.java
│           │       │   ├── DatabaseException.java
│           │       │   ├── FileException.java
│           │       │   └── GlobalExceptionHandler.java
│           │       ├── model/
│           │       │   ├── Equipo.java
│           │       │   └── Partido.java
│           │       ├── repository/
│           │       │   ├── EquipoRepository.java
│           │       │   └── PartidoRepository.java
│           │       └── service/
│           │           ├── EquipoService.java
│           │           ├── EquipoServiceImpl.java
│           │           ├── PartidoService.java
│           │           └── PartidoServiceImpl.java
│           └── resources/
│               ├── static/
│               │   ├── css/styles.css
│               │   └── js/scripts.js
│               ├── templates/
│               │   ├── api-test.html
│               │   ├── error.html
│               │   ├── form-partido.html
│               │   ├── form-resultaldo.html
│               │   ├── index.html
│               │   ├── login.html
│               │   └── partidos.html
│               └── application.properties
│
└── README.md
```

---

## Requisitos

### Backend

- Python 3.10 o superior
- `pip` (gestor de paquetes de Python)
- Entorno virtual recomendado

### Frontend

- Java 11 o superior
- Maven 3.6+
- IntelliJ IDEA (u otro IDE compatible)

---

## Clonar el repositorio

```bash
git clone https://github.com/mariofloresUBU/Pr-ctica-Obligatoria-2--EPO2-1C.git
```

---

## Ejecución del backend (API Flask)

1. Abre una terminal y ve al directorio `api/`:

```bash
cd Practica-Obligatoria-2--EPO2-1C/api
```

2. Crea y activa un entorno virtual (opcional pero recomendado):

```bash
python -m venv venv
venv\Scripts\activate   # en Windows
```

3. Instala las dependencias:

```bash
pip install -r requirements.txt
```

4. Ejecuta la API:

```bash
python run.py
```

Esto iniciará el backend en:

```
http://localhost:5000
```

Puedes probar directamente estas rutas:

- `http://localhost:5000/api/equipos`
- `http://localhost:5000/api/partidos`

---

## Ejecución del frontend (Spring Boot)

1. Abre IntelliJ IDEA
2. Selecciona `File > Open` y abre la carpeta `frontend`
3. Espera a que cargue el proyecto y reconozca `pom.xml`
4. Ejecuta el método `main` de la clase `Application.java`, que se encuentra en el paquete `es.ubu.baloncesto`, como una aplicación Java estándar

Esto iniciará el frontend en:

```
http://localhost:8080
```

Abre esa URL en tu navegador para acceder a la interfaz.

---

## Flujo de trabajo sugerido

1. Ejecuta primero el backend Flask (`python run.py`)
2. Luego abre y ejecuta el frontend Spring (`Application.java`) desde IntelliJ
3. Navega por la aplicación y crea/consulta partidos desde la interfaz

---

## Sobre `requirements.txt`

Este archivo incluye todas las dependencias del backend. Se instala con:

```
pip install -r requirements.txt
```

Incluye librerías como:

- `Flask` y `Flask-SQLAlchemy` para la API y base de datos
- `Flask-Cors` para permitir peticiones desde el frontend
- `requests`, `python-dateutil`, `pytest`, `gunicorn`...

Todo preparado para desarrollo y producción.

---

## Notas adicionales

- El archivo `application.properties` está en `frontend/src/main/resources/`
- El sistema permite comunicación cruzada entre backend y frontend
- Se implementan excepciones personalizadas, rutas REST, controladores, servicios, repositorios y seguridad en Spring
- La base de datos SQLite (`baloncesto.db`) se encuentra en `api/instance/` y se consulta automáticamente desde Flask

---

© Mario Flores – Universidad de Burgos
