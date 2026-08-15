package com.gymcontrol.controller;

import com.gymcontrol.domain.Membresia;
import com.gymcontrol.domain.Usuario;
import com.gymcontrol.service.MembresiaService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/membresias")
public class MembresiaController {

    @Autowired
    private MembresiaService membresiaService;

    @GetMapping("")
    public String listar(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarMembresias(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para acceder al módulo de membresías."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "membresias",
                membresiaService.listarTodos()
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        return "membresias/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarMembresias(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para registrar membresías."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "membresia",
                new Membresia()
        );

        return "membresias/form";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Membresia membresia,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarMembresias(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para guardar membresías."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        if (result.hasErrors()) {
            return "membresias/form";
        }

        membresiaService.guardar(membresia);

        return "redirect:/membresias";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarMembresias(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para editar membresías."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "membresia",
                membresiaService.buscarPorId(id)
        );

        return "membresias/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarMembresias(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para eliminar membresías."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        membresiaService.eliminar(id);

        return "redirect:/membresias";
    }

    private Usuario obtenerUsuario(HttpSession session) {

        return (Usuario) session.getAttribute("usuarioEnSesion");
    }

    private boolean tieneRol(
            Usuario usuario,
            String rol) {

        return usuario != null
                && usuario.getRol() != null
                && rol.equalsIgnoreCase(
                        usuario.getRol().getNombre()
                );
    }

    private boolean puedeGestionarMembresias(
            Usuario usuario) {

        return tieneRol(usuario, "ADMIN")
                || tieneRol(usuario, "RECEPCIONISTA");
    }
}