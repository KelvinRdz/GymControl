package com.gymcontrol.controller;

import com.gymcontrol.domain.Membresia;
import com.gymcontrol.service.MembresiaService;
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
@RequestMapping("/membresias")
public class MembresiaController {

    @Autowired
    private MembresiaService membresiaService;

    @GetMapping("")
    public String listar(Model model) {
        model.addAttribute("membresias", membresiaService.listarTodos());
        return "membresias/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("membresia", new Membresia());
        return "membresias/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Membresia membresia, BindingResult result) {
        if (result.hasErrors()) {
            return "membresias/form";
        }
        membresiaService.guardar(membresia);
        return "redirect:/membresias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("membresia", membresiaService.buscarPorId(id));
        return "membresias/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        membresiaService.eliminar(id);
        return "redirect:/membresias";
    }
}
