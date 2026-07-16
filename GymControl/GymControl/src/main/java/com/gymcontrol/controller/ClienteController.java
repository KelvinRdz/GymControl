package com.gymcontrol.controller;

import com.gymcontrol.domain.Cliente;
import com.gymcontrol.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("")
    public String listar(@RequestParam(required = false) String criterio, Model model) {
        model.addAttribute("clientes", clienteService.buscarPorNombreOCedula(criterio));
        model.addAttribute("criterio", criterio == null ? "" : criterio);
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "clientes/form";
        }
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("cliente", cliente);
        return "clientes/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }
}
