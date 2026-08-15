package com.gymcontrol.controller;

import com.gymcontrol.domain.Rutina;
import com.gymcontrol.domain.Usuario;
import com.gymcontrol.service.ClienteService;
import com.gymcontrol.service.RutinaService;
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
@RequestMapping("/rutinas")
public class RutinaController {

    @Autowired
    private RutinaService rutinaService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("")
    public String listar(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarRutinas(usuario)) {
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para acceder al módulo de rutinas."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "rutinas",
                rutinaService.listarTodos()
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        return "rutinas/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarRutinas(usuario)) {
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para crear rutinas."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "rutina",
                new Rutina()
        );

        model.addAttribute(
                "clientes",
                clienteService.listarTodos()
        );

        return "rutinas/form";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Rutina rutina,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarRutinas(usuario)) {
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para guardar rutinas."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        if (result.hasErrors()) {
            model.addAttribute(
                    "clientes",
                    clienteService.listarTodos()
            );

            return "rutinas/form";
        }

        rutinaService.guardar(rutina);

        return "redirect:/rutinas";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarRutinas(usuario)) {
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para editar rutinas."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "rutina",
                rutinaService.buscarPorId(id)
        );

        model.addAttribute(
                "clientes",
                clienteService.listarTodos()
        );

        return "rutinas/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarRutinas(usuario)) {
            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para eliminar rutinas."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        rutinaService.eliminar(id);

        return "redirect:/rutinas";
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

    private boolean puedeGestionarRutinas(
            Usuario usuario) {

        return tieneRol(usuario, "ADMIN")
                || tieneRol(usuario, "ENTRENADOR");
    }
}