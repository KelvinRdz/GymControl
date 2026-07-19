package com.gymcontrol.controller;

import com.gymcontrol.domain.Entrenador;
import com.gymcontrol.service.EntrenadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/entrenadores")
public class EntrenadorController {

    @Autowired
    private EntrenadorService entrenadorService;

    @GetMapping("")
    public String listar(Model model) {
        model.addAttribute("entrenadores", entrenadorService.listarTodos());
        return "entrenadores/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("entrenador", new Entrenador());
        return "entrenadores/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Entrenador entrenador, BindingResult result) {
        if (result.hasErrors()) {
            return "entrenadores/form";
        }
        entrenadorService.guardar(entrenador);
        return "redirect:/entrenadores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("entrenador", entrenadorService.buscarPorId(id));
        return "entrenadores/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        entrenadorService.eliminar(id);
        return "redirect:/entrenadores";
    }
}
