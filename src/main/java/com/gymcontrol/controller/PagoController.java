package com.gymcontrol.controller;

import com.gymcontrol.domain.Pago;
import com.gymcontrol.domain.Usuario;
import com.gymcontrol.service.ClienteService;
import com.gymcontrol.service.PagoService;
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
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("")
    public String listar(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarPagos(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para acceder al módulo de pagos."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "pagos",
                pagoService.listarTodos()
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        return "pagos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarPagos(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para registrar pagos."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "pago",
                new Pago()
        );

        model.addAttribute(
                "clientes",
                clienteService.listarTodos()
        );

        return "pagos/form";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Pago pago,
            BindingResult result,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarPagos(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para guardar pagos."
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

            return "pagos/form";
        }

        pagoService.guardar(pago);

        return "redirect:/pagos";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarPagos(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para editar pagos."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        model.addAttribute(
                "pago",
                pagoService.buscarPorId(id)
        );

        model.addAttribute(
                "clientes",
                clienteService.listarTodos()
        );

        return "pagos/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (!puedeGestionarPagos(usuario)) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "No tienes permiso para eliminar pagos."
            );

            return usuario == null
                    ? "redirect:/login"
                    : "redirect:/home";
        }

        pagoService.eliminar(id);

        return "redirect:/pagos";
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

    private boolean puedeGestionarPagos(
            Usuario usuario) {

        return tieneRol(usuario, "ADMIN")
                || tieneRol(usuario, "RECEPCIONISTA");
    }
}