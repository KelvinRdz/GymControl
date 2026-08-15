package com.gymcontrol.controller;

import com.gymcontrol.domain.Cliente;
import com.gymcontrol.domain.Usuario;
import com.gymcontrol.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // =====================================================
    // LISTAR CLIENTES
    // ADMIN - RECEPCIONISTA - ENTRENADOR
    // =====================================================
    @GetMapping("")
    public String listar(
            @RequestParam(required = false) String criterio,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!tieneRol(usuario, "ADMIN")
                && !tieneRol(usuario, "RECEPCIONISTA")
                && !tieneRol(usuario, "ENTRENADOR")) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No tienes permiso para acceder al módulo de clientes."
            );

            return "redirect:/home";
        }

        model.addAttribute(
                "clientes",
                clienteService.buscarPorNombreOCedula(criterio)
        );

        model.addAttribute(
                "criterio",
                criterio == null ? "" : criterio
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        return "clientes/lista";
    }

    // =====================================================
    // NUEVO CLIENTE
    // ADMIN - RECEPCIONISTA
    // =====================================================
    @GetMapping("/nuevo")
    public String nuevo(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarClientes(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No tienes permiso para registrar clientes."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "cliente",
                new Cliente()
        );

        return "clientes/form";
    }

    // =====================================================
    // GUARDAR CLIENTE
    // ADMIN - RECEPCIONISTA
    // =====================================================
    @PostMapping("/guardar")
    public String guardar(
            @Valid Cliente cliente,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarClientes(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No tienes permiso para guardar clientes."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        if (result.hasErrors()) {
            return "clientes/form";
        }

        clienteService.guardar(cliente);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Cliente guardado correctamente."
        );

        return "redirect:/clientes";
    }

    // =====================================================
    // EDITAR CLIENTE
    // ADMIN - RECEPCIONISTA
    // =====================================================
    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarClientes(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No tienes permiso para editar clientes."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        Cliente cliente = clienteService.buscarPorId(id);

        if (cliente == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "El cliente solicitado no existe."
            );

            return "redirect:/clientes";
        }

        model.addAttribute(
                "cliente",
                cliente
        );

        return "clientes/form";
    }

    // =====================================================
    // ELIMINAR CLIENTE
    // ADMIN - RECEPCIONISTA
    // =====================================================
    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarClientes(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No tienes permiso para eliminar clientes."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        try {

            clienteService.eliminar(id);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Cliente eliminado correctamente."
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se puede eliminar este cliente porque tiene "
                    + "membresías, pagos, rutinas, asistencias "
                    + "o un usuario asociado."
            );
        }

        return "redirect:/clientes";
    }

    // =====================================================
    // OBTENER USUARIO DE SESIÓN
    // =====================================================
    private Usuario obtenerUsuario(HttpSession session) {

        return (Usuario) session.getAttribute(
                "usuarioEnSesion"
        );
    }

    // =====================================================
    // VALIDAR ROL
    // =====================================================
    private boolean tieneRol(
            Usuario usuario,
            String rol) {

        return usuario != null
                && usuario.getRol() != null
                && rol.equalsIgnoreCase(
                        usuario.getRol().getNombre()
                );
    }

    // =====================================================
    // ADMIN Y RECEPCIONISTA PUEDEN MODIFICAR CLIENTES
    // =====================================================
    private boolean puedeGestionarClientes(
            Usuario usuario) {

        return tieneRol(usuario, "ADMIN")
                || tieneRol(usuario, "RECEPCIONISTA");
    }
}