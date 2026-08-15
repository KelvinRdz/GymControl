package com.gymcontrol.config;

import com.gymcontrol.domain.Cliente;
import com.gymcontrol.domain.Pago;
import com.gymcontrol.domain.Rol;
import com.gymcontrol.domain.Rutina;
import com.gymcontrol.domain.Usuario;

import com.gymcontrol.repository.ClienteRepository;
import com.gymcontrol.repository.PagoRepository;
import com.gymcontrol.repository.RolRepository;
import com.gymcontrol.repository.RutinaRepository;
import com.gymcontrol.repository.UsuarioRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) {

        /*
         * =====================================================
         * CREACIÓN DE ROLES
         * =====================================================
         */

        Rol rolAdmin = obtenerOCrearRol(
                "ADMIN",
                "Administrador con acceso completo al sistema"
        );

        Rol rolEntrenador = obtenerOCrearRol(
                "ENTRENADOR",
                "Entrenador encargado de rutinas y seguimiento de clientes"
        );

        Rol rolRecepcionista = obtenerOCrearRol(
                "RECEPCIONISTA",
                "Recepcionista encargado de clientes, pagos, membresías y asistencias"
        );

        Rol rolCliente = obtenerOCrearRol(
                "CLIENTE",
                "Cliente con acceso únicamente a su información personal"
        );

        /*
         * =====================================================
         * USUARIO ADMINISTRADOR
         * =====================================================
         */

        Usuario admin = usuarioRepository.findByUsuario("admin");

        if (admin == null) {

            admin = new Usuario();

            admin.setUsuario("admin");
            admin.setContraseña(sha256("admin123"));
            admin.setNombre("Administrador");
            admin.setEmail("admin@gymcontrol.com");
            admin.setRol(rolAdmin);
            admin.setActivo(true);

            usuarioRepository.save(admin);

        } else {

            // Actualiza el usuario antiguo para utilizar la nueva tabla Rol
            if (admin.getRol() == null) {
                admin.setRol(rolAdmin);
                usuarioRepository.save(admin);
            }
        }

        /*
         * =====================================================
         * CLIENTE DE PRUEBA
         * =====================================================
         */

        Cliente cliente = clienteRepository.findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> {

                    Cliente nuevo = new Cliente();

                    nuevo.setNombre("Cliente de prueba");
                    nuevo.setCedula("111111111");
                    nuevo.setTelefono("8888-8888");
                    nuevo.setCorreo("cliente@gymcontrol.com");
                    nuevo.setFechaRegistro(LocalDate.now());

                    return clienteRepository.save(nuevo);
                });

        /*
         * =====================================================
         * USUARIO CLIENTE
         * =====================================================
         */

        Usuario usuarioCliente = usuarioRepository.findByUsuario("cliente1");

        if (usuarioCliente == null) {

            usuarioCliente = new Usuario();

            usuarioCliente.setUsuario("cliente1");
            usuarioCliente.setContraseña(sha256("cliente123"));
            usuarioCliente.setNombre(cliente.getNombre());
            usuarioCliente.setEmail(cliente.getCorreo());
            usuarioCliente.setRol(rolCliente);
            usuarioCliente.setActivo(true);
            usuarioCliente.setCliente(cliente);

            usuarioRepository.save(usuarioCliente);

        } else {

            boolean necesitaActualizar = false;

            if (usuarioCliente.getRol() == null) {
                usuarioCliente.setRol(rolCliente);
                necesitaActualizar = true;
            }

            if (usuarioCliente.getCliente() == null) {
                usuarioCliente.setCliente(cliente);
                necesitaActualizar = true;
            }

            if (necesitaActualizar) {
                usuarioRepository.save(usuarioCliente);
            }
        }

        /*
         * =====================================================
         * USUARIO ENTRENADOR DE PRUEBA
         * =====================================================
         */

        Usuario entrenador = usuarioRepository.findByUsuario("entrenador1");

        if (entrenador == null) {

            entrenador = new Usuario();

            entrenador.setUsuario("entrenador1");
            entrenador.setContraseña(sha256("pass123"));
            entrenador.setNombre("Entrenador de prueba");
            entrenador.setEmail("entrenador@gymcontrol.com");
            entrenador.setRol(rolEntrenador);
            entrenador.setActivo(true);

            usuarioRepository.save(entrenador);

        } else if (entrenador.getRol() == null) {

            entrenador.setRol(rolEntrenador);
            usuarioRepository.save(entrenador);
        }

        /*
         * =====================================================
         * USUARIO RECEPCIONISTA DE PRUEBA
         * =====================================================
         */

        Usuario recepcionista = usuarioRepository.findByUsuario("recepcion");

        if (recepcionista == null) {

            recepcionista = new Usuario();

            recepcionista.setUsuario("recepcion");
            recepcionista.setContraseña(sha256("pass123"));
            recepcionista.setNombre("Recepcionista de prueba");
            recepcionista.setEmail("recepcion@gymcontrol.com");
            recepcionista.setRol(rolRecepcionista);
            recepcionista.setActivo(true);

            usuarioRepository.save(recepcionista);

        } else if (recepcionista.getRol() == null) {

            recepcionista.setRol(rolRecepcionista);
            usuarioRepository.save(recepcionista);
        }

        /*
         * =====================================================
         * RUTINA DEL CLIENTE DE PRUEBA
         * =====================================================
         */

        if (rutinaRepository
                .findByClienteIdOrderByIdDesc(cliente.getId())
                .isEmpty()) {

            Rutina rutina = new Rutina();

            rutina.setNombre("Rutina personal inicial");

            rutina.setDescripcion(
                    "Calentamiento 10 minutos, sentadillas 3x12, "
                    + "press de pecho 3x10 y estiramiento final."
            );

            rutina.setCliente(cliente);

            rutinaRepository.save(rutina);
        }

        /*
         * =====================================================
         * PAGO DEL CLIENTE DE PRUEBA
         * =====================================================
         */

        if (pagoRepository
                .findByClienteIdOrderByFechaPagoDesc(cliente.getId())
                .isEmpty()) {

            Pago pago = new Pago();

            pago.setMonto(25000.0);
            pago.setFechaPago(LocalDate.now());
            pago.setCliente(cliente);

            pagoRepository.save(pago);
        }
    }

    /*
     * =========================================================
     * CREAR O BUSCAR ROL
     * =========================================================
     */

    private Rol obtenerOCrearRol(String nombre, String descripcion) {

        return rolRepository
                .findByNombre(nombre)
                .orElseGet(() -> {

                    Rol rol = new Rol();

                    rol.setNombre(nombre);
                    rol.setDescripcion(descripcion);

                    return rolRepository.save(rol);
                });
    }

    /*
     * =========================================================
     * CIFRADO SHA-256
     * =========================================================
     */

    private String sha256(String texto) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            texto.getBytes(StandardCharsets.UTF_8)
                    );

            StringBuilder resultado = new StringBuilder();

            for (byte b : hash) {
                resultado.append(
                        String.format("%02x", b)
                );
            }

            return resultado.toString();

        } catch (Exception e) {

            throw new IllegalStateException(
                    "No se pudo cifrar la contraseña",
                    e
            );
        }
    }
}