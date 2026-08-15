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
                    "mensaje",
                    "No tienes permiso para acceder a clientes."
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

        model.addAttribute("usuario", usuario);

        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarClientes(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para registrar clientes."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute("cliente", new Cliente());

        return "clientes/form";
    }

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
                    "mensaje",
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

        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarClientes(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para editar clientes."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        Cliente cliente = clienteService.buscarPorId(id);

        model.addAttribute("cliente", cliente);

        return "clientes/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarClientes(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para eliminar clientes."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        clienteService.eliminar(id);

        return "redirect:/clientes";
    }

    private Usuario obtenerUsuario(HttpSession session) {

        return (Usuario) session.getAttribute("usuarioEnSesion");
    }

    private boolean tieneRol(Usuario usuario, String rol) {

        return usuario != null
                && usuario.getRol() != null
                && rol.equalsIgnoreCase(
                        usuario.getRol().getNombre()
                );
    }

    private boolean puedeGestionarClientes(Usuario usuario) {

        return tieneRol(usuario, "ADMIN")
                || tieneRol(usuario, "RECEPCIONISTA");
    }
}