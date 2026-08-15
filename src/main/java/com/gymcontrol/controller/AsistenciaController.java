package com.gymcontrol.controller;

import com.gymcontrol.domain.Asistencia;
import com.gymcontrol.domain.Usuario;
import com.gymcontrol.service.AsistenciaService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("")
    public String listar(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarAsistencias(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para acceder al módulo de asistencias."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "asistencias",
                asistenciaService.listarTodos()
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        return "asistencias/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarAsistencias(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para registrar asistencias."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "asistencia",
                new Asistencia()
        );

        model.addAttribute(
                "clientes",
                clienteService.listarTodos()
        );

        return "asistencias/form";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Asistencia asistencia,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarAsistencias(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para guardar asistencias."
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

            return "asistencias/form";
        }

        asistenciaService.guardar(asistencia);

        return "redirect:/asistencias";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarAsistencias(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para editar asistencias."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "asistencia",
                asistenciaService.buscarPorId(id)
        );

        model.addAttribute(
                "clientes",
                clienteService.listarTodos()
        );

        return "asistencias/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarAsistencias(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para eliminar asistencias."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        asistenciaService.eliminar(id);

        return "redirect:/asistencias";
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

    private boolean puedeGestionarAsistencias(
            Usuario usuario) {

        return tieneRol(usuario, "ADMIN")
                || tieneRol(usuario, "RECEPCIONISTA");
    }
}