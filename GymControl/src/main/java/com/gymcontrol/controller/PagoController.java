package com.gymcontrol.controller;

import com.gymcontrol.domain.Pago;
import com.gymcontrol.service.ClienteService;
import com.gymcontrol.service.PagoService;
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
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("")
    public String listar(Model model) {
        model.addAttribute("pagos", pagoService.listarTodos());
        return "pagos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("pago", new Pago());
        model.addAttribute("clientes", clienteService.listarTodos());
        return "pagos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Pago pago, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            return "pagos/form";
        }
        pagoService.guardar(pago);
        return "redirect:/pagos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("pago", pagoService.buscarPorId(id));
        model.addAttribute("clientes", clienteService.listarTodos());
        return "pagos/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return "redirect:/pagos";
    }
}
