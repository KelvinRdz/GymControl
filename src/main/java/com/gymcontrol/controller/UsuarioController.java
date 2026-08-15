package com.gymcontrol.controller;

import com.gymcontrol.domain.Usuario;
import com.gymcontrol.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(HttpSession sesion) {

        Usuario usuarioEnSesion =
                (Usuario) sesion.getAttribute("usuarioEnSesion");

        if (usuarioEnSesion != null) {
            return "redirect:/home";
        }

        return "usuario/login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String usuario,
            @RequestParam String contraseña,
            HttpSession sesion,
            Model model) {

        if (usuarioService.validarCredenciales(usuario, contraseña)) {

            Usuario usuarioEncontrado =
                    usuarioService.buscarPorUsuario(usuario);

            if (usuarioEncontrado == null || !usuarioEncontrado.isActivo()) {
                model.addAttribute(
                        "error",
                        "El usuario se encuentra inactivo"
                );

                return "usuario/login";
            }

            sesion.setAttribute(
                    "usuarioEnSesion",
                    usuarioEncontrado
            );

            return "redirect:/home";

        } else {

            model.addAttribute(
                    "error",
                    "Usuario o contraseña inválidos"
            );

            return "usuario/login";
        }
    }

    @GetMapping("/home")
    public String home(
            HttpSession sesion,
            Model model) {

        Usuario usuarioEnSesion =
                (Usuario) sesion.getAttribute("usuarioEnSesion");

        if (usuarioEnSesion == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "usuario",
                usuarioEnSesion
        );

        if (usuarioEnSesion.getRol() != null) {

            model.addAttribute(
                    "rol",
                    usuarioEnSesion.getRol().getNombre()
            );
        }

        return "usuario/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession sesion) {

        sesion.invalidate();

        return "redirect:/login";
    }
}