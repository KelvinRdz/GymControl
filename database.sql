CREATE DATABASE IF NOT EXISTS gymcontrol
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE gymcontrol;

-- Las tablas son creadas y actualizadas automáticamente
-- por Hibernate mediante:
-- spring.jpa.hibernate.ddl-auto=update

-- Después de crear la base de datos:
-- 1. Configurar usuario y contraseña de MySQL en application.properties.
-- 2. Ejecutar GymControl.
-- 3. Hibernate generará las tablas necesarias.
-- 4. DataInitializer creará los roles y usuarios de prueba.

-- Usuarios iniciales creados automáticamente por DataInitializer:
--
-- ADMIN
-- usuario: admin
-- contraseña: admin123
--
-- ENTRENADOR
-- usuario: entrenador1
-- contraseña: pass123
--
-- RECEPCIONISTA
-- usuario: recepcion
-- contraseña: pass123
--
-- CLIENTE
-- usuario: cliente1
-- contraseña: cliente123
