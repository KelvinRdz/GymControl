package com.gymcontrol.controller;

import com.gymcontrol.domain.Asistencia;
import com.gymcontrol.service.AsistenciaService;
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
@RequestMapping("/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @GetMapping("")
    public String listar(Model model) {
        model.addAttribute("asistencias", asistenciaService.listarTodos());
        return "asistencias/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("asistencia", new Asistencia());
        return "asistencias/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Asistencia asistencia, BindingResult result) {
        if (result.hasErrors()) {
            return "asistencias/form";
        }
        asistenciaService.guardar(asistencia);
        return "redirect:/asistencias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("asistencia", asistenciaService.buscarPorId(id));
        return "asistencias/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        asistenciaService.eliminar(id);
        return "redirect:/asistencias";
    }
}
