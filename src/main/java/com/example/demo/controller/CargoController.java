package com.example.demo.controller;

import com.example.demo.model.Cargo;
import com.example.demo.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/cargos")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cargos", cargoService.listarTodos());
        return "cargo/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cargo", new Cargo());
        return "cargo/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Cargo cargo, RedirectAttributes redirectAttributes) {
        try {
            cargoService.criar(cargo);
            redirectAttributes.addFlashAttribute("mensagem", "Cargo criado com sucesso!");
            return "redirect:/cargos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cargos/novo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Cargo> cargo = cargoService.buscarPorId(id);
        if (cargo.isPresent()) {
            model.addAttribute("cargo", cargo.get());
            return "cargo/form";
        }
        return "redirect:/cargos";
    }

    @PostMapping("/{id}/atualizar")
    public String atualizar(@PathVariable Long id, @ModelAttribute Cargo cargo, RedirectAttributes redirectAttributes) {
        try {
            cargoService.atualizar(id, cargo);
            redirectAttributes.addFlashAttribute("mensagem", "Cargo atualizado com sucesso!");
            return "redirect:/cargos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cargos/" + id + "/editar";
        }
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cargoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagem", "Cargo excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/cargos";
    }

    @GetMapping("/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        Optional<Cargo> cargo = cargoService.buscarPorId(id);
        if (cargo.isPresent()) {
            model.addAttribute("cargo", cargo.get());
            return "cargo/visualizar";
        }
        return "redirect:/cargos";
    }
}
