# GymControl

GymControl es una aplicación web desarrollada para la gestión administrativa y operativa de un gimnasio.

El sistema permite administrar clientes, entrenadores, membresías, pagos, rutinas y asistencias. Además, cuenta con autenticación de usuarios, control de acceso según roles e internacionalización en español e inglés.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Thymeleaf
- Bootstrap 5
- MySQL
- Maven
- NetBeans
- Git y GitHub

## Requisitos

Para ejecutar el proyecto se necesita:

- Java 21
- Maven
- MySQL
- NetBeans o cualquier IDE compatible con Maven
- Navegador web

## Base de datos

El proyecto utiliza una base de datos MySQL llamada:

```sql
gymcontrol
```

Antes de ejecutar el proyecto se debe crear la base de datos:

```sql
CREATE DATABASE gymcontrol;
```

Las credenciales de conexión deben configurarse en:

```text
src/main/resources/application.properties
```

Ejemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gymcontrol
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
```

No se recomienda almacenar contraseñas personales o credenciales reales en el repositorio público.

## Ejecución

### Desde NetBeans

1. Abrir el proyecto GymControl.
2. Verificar la conexión con MySQL.
3. Verificar que exista la base de datos `gymcontrol`.
4. Ejecutar el proyecto con Run Project.
5. Abrir en el navegador:

```text
http://localhost:85
```

### Desde Maven

También se puede ejecutar utilizando:

```bash
mvn spring-boot:run
```

Luego abrir:

```text
http://localhost:85
```

## Usuarios de prueba

El sistema crea usuarios iniciales para probar los diferentes roles.

### Administrador

```text
Usuario: admin
Contraseña: admin123
```

### Entrenador

```text
Usuario: entrenador1
Contraseña: pass123
```

### Recepcionista

```text
Usuario: recepcion
Contraseña: pass123
```

### Cliente

```text
Usuario: cliente1
Contraseña: cliente123
```

## Roles y permisos

### ADMIN

Tiene acceso administrativo a los principales módulos del sistema:

- Clientes
- Entrenadores
- Membresías
- Pagos
- Rutinas
- Asistencias

### ENTRENADOR

Puede acceder a:

- Clientes
- Rutinas

El entrenador puede consultar clientes y gestionar sus rutinas de entrenamiento.

### RECEPCIONISTA

Puede acceder a:

- Clientes
- Membresías
- Pagos
- Asistencias

Este rol está orientado a las operaciones administrativas y de recepción del gimnasio.

### CLIENTE

El cliente tiene acceso únicamente a su información personal:

- Mi rutina
- Mis pagos

No puede acceder a los módulos administrativos.

## Control de acceso

GymControl implementa control de acceso según el rol del usuario.

No solamente se ocultan las opciones del menú. Los controladores verifican los permisos antes de permitir el acceso a las diferentes funcionalidades.

De esta forma, un usuario no autorizado tampoco puede acceder escribiendo manualmente la URL de un módulo restringido.

## Gestión de clientes

El sistema permite:

- Registrar clientes.
- Consultar clientes.
- Buscar clientes.
- Editar información.
- Eliminar registros.
- Relacionar clientes con otros módulos del sistema.

## Gestión de entrenadores

Permite registrar, consultar, editar y eliminar entrenadores.

Este módulo está reservado para usuarios con permisos administrativos.

## Gestión de membresías

Permite:

- Crear membresías.
- Editar membresías.
- Eliminar membresías.
- Definir fecha de inicio.
- Definir fecha de finalización.
- Controlar el estado.
- Asociar una membresía con un cliente.

Cada membresía queda relacionada con un cliente mediante la base de datos.

## Gestión de pagos

Permite registrar y administrar los pagos realizados por los clientes.

Cada pago está asociado con el cliente correspondiente.

Los clientes pueden consultar su propio historial mediante la opción:

```text
Mis pagos
```

## Gestión de rutinas

Los administradores y entrenadores pueden crear y administrar rutinas.

Cada rutina puede asociarse con un cliente.

Los clientes pueden consultar únicamente su rutina mediante:

```text
Mi rutina
```

## Gestión de asistencias

Permite registrar:

- Cliente
- Fecha
- Hora de ingreso

Cada registro de asistencia queda asociado con un cliente.

## Internacionalización

GymControl cuenta con soporte para:

- Español
- Inglés

Desde la interfaz principal se puede cambiar el idioma mediante el selector:

```text
ES | EN
```

La implementación utiliza los archivos:

```text
src/main/resources/messages.properties
src/main/resources/messages_en.properties
```

## Seguridad de contraseñas

Los usuarios iniciales utilizan contraseñas procesadas mediante SHA-256 antes de almacenarse en la base de datos.

## Arquitectura

El proyecto utiliza una estructura basada en capas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Base de datos
```

También utiliza entidades JPA para representar las tablas y relaciones de la base de datos.

## Principales módulos

- Usuarios
- Roles
- Clientes
- Entrenadores
- Membresías
- Pagos
- Rutinas
- Asistencias

## Relaciones principales

El sistema maneja relaciones entre diferentes entidades, entre ellas:

```text
Usuario → Rol
Usuario → Cliente
Cliente → Membresía
Cliente → Pago
Cliente → Rutina
Cliente → Asistencia
```

Estas relaciones permiten mantener organizada la información correspondiente a cada cliente.

## Control de versiones

El proyecto utiliza Git y GitHub para el control de versiones y trabajo colaborativo.

Los integrantes pueden trabajar mediante ramas,
 commits y posteriormente integrar los cambios al repositorio principal.

## Proyecto académico

GymControl fue desarrollado como proyecto universitario 
con el objetivo de aplicar conceptos de desarrollo de aplicaciones web,
arquitectura por capas, persistencia de datos, relaciones entre entidades, 
autenticación, autorización, internacionalización y trabajo colaborativo con Git.