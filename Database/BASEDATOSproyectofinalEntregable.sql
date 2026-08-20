DROP DATABASE IF EXISTS gymcontrol;

CREATE DATABASE gymcontrol
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE gymcontrol;


-- =====================================================
-- 1. TABLA ROLES
-- =====================================================

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);


-- =====================================================
-- 2. TABLA CLIENTES
-- =====================================================

CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    fecha_registro DATE NOT NULL
);


-- =====================================================
-- 3. TABLA ENTRENADORES
-- =====================================================

CREATE TABLE entrenadores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL
);


-- =====================================================
-- 4. TABLA USUARIOS
-- =====================================================

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    usuario VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    rol_id BIGINT NOT NULL,
    cliente_id BIGINT NULL,

    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (rol_id)
        REFERENCES roles(id),

    CONSTRAINT fk_usuario_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id),

    CONSTRAINT uk_usuario_cliente
        UNIQUE (cliente_id)
);


-- =====================================================
-- 5. TABLA MEMBRESIAS
-- =====================================================

CREATE TABLE membresias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    tipo VARCHAR(100) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'ACTIVA',

    cliente_id BIGINT NOT NULL,

    CONSTRAINT fk_membresia_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id)
);


-- =====================================================
-- 6. TABLA PAGOS
-- =====================================================

CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    monto DECIMAL(10,2) NOT NULL,
    fecha_pago DATE NOT NULL,

    cliente_id BIGINT NOT NULL,

    CONSTRAINT fk_pago_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id)
);


-- =====================================================
-- 7. TABLA RUTINAS
-- =====================================================

CREATE TABLE rutinas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,

    cliente_id BIGINT,

    CONSTRAINT fk_rutina_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id)
);


-- =====================================================
-- 8. TABLA ASISTENCIAS
-- =====================================================

CREATE TABLE asistencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    fecha DATE NOT NULL,
    hora_ingreso TIME NOT NULL,

    cliente_id BIGINT NOT NULL,

    CONSTRAINT fk_asistencia_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id)
);


-- =====================================================
-- DATOS DE PRUEBA - ROLES
-- =====================================================

INSERT INTO roles (nombre, descripcion)
VALUES
(
    'ADMIN',
    'Administrador con acceso completo al sistema'
),
(
    'ENTRENADOR',
    'Entrenador encargado de rutinas y seguimiento de clientes'
),
(
    'RECEPCIONISTA',
    'Recepcionista encargado de clientes, pagos, membresías y asistencias'
),
(
    'CLIENTE',
    'Cliente con acceso únicamente a su información personal'
);


-- =====================================================
-- DATOS DE PRUEBA - CLIENTES
-- =====================================================

INSERT INTO clientes
(nombre, cedula, telefono, correo, fecha_registro)
VALUES
(
    'Carlos Hernández Mora',
    '115670891',
    '8888-4521',
    'carlos.hernandez@gmail.com',
    '2026-01-15'
),
(
    'María Fernanda Rojas Solís',
    '207890456',
    '8712-3345',
    'maria.rojas@hotmail.com',
    '2026-02-03'
),
(
    'José Andrés Vargas Castro',
    '304560789',
    '8623-7812',
    'jose.vargas@yahoo.com',
    '2026-02-18'
),
(
    'Daniela González Ramírez',
    '118900234',
    '8415-9921',
    'daniela.gonzalez@gmail.com',
    '2026-03-10'
),
(
    'Luis Alberto Jiménez Araya',
    '502340678',
    '8799-1254',
    'luis.jimenez@outlook.com',
    '2026-03-28'
);


-- =====================================================
-- DATOS DE PRUEBA - ENTRENADORES
-- =====================================================

INSERT INTO entrenadores
(nombre, especialidad, telefono)
VALUES
(
    'Juan García',
    'Musculación',
    '555-0101'
),
(
    'Carlos Rodríguez',
    'Cardio',
    '555-0102'
),
(
    'Ana Martínez',
    'Pilates',
    '555-0103'
),
(
    'Miguel Fernández',
    'Crossfit',
    '555-0104'
);


-- =====================================================
-- DATOS DE PRUEBA - USUARIOS
-- =====================================================

INSERT INTO usuarios
(usuario, contraseña, nombre, email, activo, rol_id, cliente_id)
VALUES
(
    'admin',
    SHA2('admin123', 256),
    'Administrador',
    'admin@gymcontrol.com',
    TRUE,
    1,
    NULL
),
(
    'entrenador1',
    SHA2('pass123', 256),
    'Entrenador de prueba',
    'entrenador@gymcontrol.com',
    TRUE,
    2,
    NULL
),
(
    'recepcion',
    SHA2('pass123', 256),
    'Recepcionista de prueba',
    'recepcion@gymcontrol.com',
    TRUE,
    3,
    NULL
),
(
    'cliente1',
    SHA2('cliente123', 256),
    'Carlos Hernández Mora',
    'cliente1@gymcontrol.com',
    TRUE,
    4,
    1
);


-- =====================================================
-- DATOS DE PRUEBA - MEMBRESIAS
-- =====================================================

INSERT INTO membresias
(tipo, fecha_inicio, fecha_fin, estado, cliente_id)
VALUES
(
    'Básica',
    '2026-01-15',
    '2026-04-15',
    'ACTIVA',
    1
),
(
    'Premium',
    '2026-02-03',
    '2026-08-03',
    'ACTIVA',
    2
),
(
    'Gold',
    '2026-02-18',
    '2026-08-18',
    'ACTIVA',
    3
),
(
    'Básica',
    '2026-03-10',
    '2026-06-10',
    'ACTIVA',
    4
),
(
    'VIP',
    '2026-03-28',
    '2027-03-28',
    'ACTIVA',
    5
);


-- =====================================================
-- DATOS DE PRUEBA - PAGOS
-- =====================================================

INSERT INTO pagos
(monto, fecha_pago, cliente_id)
VALUES
(
    25000.00,
    '2026-01-15',
    1
),
(
    35000.00,
    '2026-02-03',
    2
),
(
    45000.00,
    '2026-02-18',
    3
),
(
    30000.00,
    '2026-03-10',
    4
),
(
    55000.00,
    '2026-03-28',
    5
);


-- =====================================================
-- DATOS DE PRUEBA - RUTINAS
-- =====================================================

INSERT INTO rutinas
(nombre, descripcion, cliente_id)
VALUES
(
    'Rutina Principiante',
    'Rutina básica para nuevos miembros. Enfocada en técnica y adaptación.',
    1
),
(
    'Rutina Intermedia',
    'Rutina de desarrollo muscular con progresión constante.',
    2
),
(
    'Rutina Avanzada',
    'Rutina de alto rendimiento para usuarios experimentados.',
    3
),
(
    'Rutina Cardio',
    'Rutina enfocada en resistencia cardiovascular y quema de calorías.',
    4
),
(
    'Rutina Personal',
    'Rutina personalizada según los objetivos del cliente.',
    5
);


-- =====================================================
-- DATOS DE PRUEBA - ASISTENCIAS
-- =====================================================

INSERT INTO asistencias
(fecha, hora_ingreso, cliente_id)
VALUES
(
    '2026-08-10',
    '06:00:00',
    1
),
(
    '2026-08-10',
    '06:15:00',
    2
),
(
    '2026-08-10',
    '07:00:00',
    3
),
(
    '2026-08-11',
    '05:45:00',
    4
),
(
    '2026-08-11',
    '06:30:00',
    5
),
(
    '2026-08-12',
    '07:15:00',
    1
),
(
    '2026-08-12',
    '08:00:00',
    2
);


-- =====================================================
-- ÍNDICES
-- =====================================================

CREATE INDEX idx_usuarios_email
ON usuarios(email);

CREATE INDEX idx_clientes_cedula
ON clientes(cedula);

CREATE INDEX idx_clientes_correo
ON clientes(correo);

CREATE INDEX idx_membresias_estado
ON membresias(estado);

CREATE INDEX idx_membresias_cliente
ON membresias(cliente_id);

CREATE INDEX idx_pagos_cliente
ON pagos(cliente_id);

CREATE INDEX idx_rutinas_cliente
ON rutinas(cliente_id);

CREATE INDEX idx_asistencias_fecha
ON asistencias(fecha);

CREATE INDEX idx_asistencias_cliente
ON asistencias(cliente_id);


-- =====================================================
-- CONSULTAS DE VERIFICACIÓN
-- =====================================================

SELECT * FROM roles;

SELECT * FROM usuarios;

SELECT * FROM clientes;

SELECT * FROM entrenadores;

SELECT * FROM membresias;

SELECT * FROM pagos;

SELECT * FROM rutinas;

SELECT * FROM asistencias;


-- =====================================================
-- VERIFICAR USUARIOS Y ROLES
-- =====================================================

SELECT
    u.id,
    u.usuario,
    u.nombre,
    u.email,
    r.nombre AS rol,
    u.activo
FROM usuarios u
INNER JOIN roles r
    ON u.rol_id = r.id;


-- =====================================================
-- VERIFICAR MEMBRESIAS Y CLIENTES
-- =====================================================

SELECT
    m.id,
    c.nombre AS cliente,
    m.tipo,
    m.fecha_inicio,
    m.fecha_fin,
    m.estado
FROM membresias m
INNER JOIN clientes c
    ON m.cliente_id = c.id;


-- =====================================================
-- VERIFICAR PAGOS Y CLIENTES
-- =====================================================

SELECT
    p.id,
    c.nombre AS cliente,
    p.monto,
    p.fecha_pago
FROM pagos p
INNER JOIN clientes c
    ON p.cliente_id = c.id;


-- =====================================================
-- VERIFICAR RUTINAS Y CLIENTES
-- =====================================================

SELECT
    r.id,
    c.nombre AS cliente,
    r.nombre AS rutina,
    r.descripcion
FROM rutinas r
INNER JOIN clientes c
    ON r.cliente_id = c.id;


-- =====================================================
-- VERIFICAR ASISTENCIAS Y CLIENTES
-- =====================================================

SELECT
    a.id,
    c.nombre AS cliente,
    a.fecha,
    a.hora_ingreso
FROM asistencias a
INNER JOIN clientes c
    ON a.cliente_id = c.id;


SELECT
    'Base de datos GymControl creada correctamente.'
    AS Mensaje;