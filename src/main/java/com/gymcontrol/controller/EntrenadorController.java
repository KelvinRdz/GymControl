package com.gymcontrol.controller;

import com.gymcontrol.domain.Entrenador;
import com.gymcontrol.domain.Usuario;
import com.gymcontrol.service.EntrenadorService;
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
@RequestMapping("/entrenadores")
public class EntrenadorController {

    @Autowired
    private EntrenadorService entrenadorService;

    @GetMapping("")
    public String listar(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!esAdmin(usuario)) {
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para acceder al módulo de entrenadores."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "entrenadores",
                entrenadorService.listarTodos()
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        return "entrenadores/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!esAdmin(usuario)) {
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para registrar entrenadores."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "entrenador",
                new Entrenador()
        );

        return "entrenadores/form";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Entrenador entrenador,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!esAdmin(usuario)) {
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para guardar entrenadores."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        if (result.hasErrors()) {
            return "entrenadores/form";
        }

        entrenadorService.guardar(entrenador);

        return "redirect:/entrenadores";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!esAdmin(usuario)) {
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para editar entrenadores."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "entrenador",
                entrenadorService.buscarPorId(id)
        );

        return "entrenadores/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!esAdmin(usuario)) {
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para eliminar entrenadores."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        entrenadorService.eliminar(id);

        return "redirect:/entrenadores";
    }

    private Usuario obtenerUsuario(HttpSession session) {
        return (Usuario) session.getAttribute("usuarioEnSesion");
    }

    private boolean esAdmin(Usuario usuario) {
        return usuario != null
                && usuario.getRol() != null
                && "ADMIN".equalsIgnoreCase(
                        usuario.getRol().getNombre()
                );
    }
}