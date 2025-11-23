package com.example.demo.controller;

import com.example.demo.model.Departamento;
import com.example.demo.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String search) {
        List<Departamento> departamentos = departamentoService.listarTodos();

        if (search != null && !search.trim().isEmpty()) {
            departamentos = departamentoService.fuzzySearch(departamentos, search);
            model.addAttribute("searchTerm", search);
        }

        model.addAttribute("departamentos", departamentos);
        return "departamento/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("departamento", new Departamento());
        return "departamento/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Departamento departamento, RedirectAttributes redirectAttributes) {
        try {
            departamentoService.criar(departamento);
            redirectAttributes.addFlashAttribute("mensagem", "Departamento criado com sucesso!");
            return "redirect:/departamentos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/departamentos/novo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Departamento> departamento = departamentoService.buscarPorId(id);
        if (departamento.isPresent()) {
            model.addAttribute("departamento", departamento.get());
            return "departamento/form";
        }
        return "redirect:/departamentos";
    }

    @PostMapping("/{id}/atualizar")
    public String atualizar(@PathVariable Long id, @ModelAttribute Departamento departamento,
            RedirectAttributes redirectAttributes) {
        try {
            departamentoService.atualizar(id, departamento);
            redirectAttributes.addFlashAttribute("mensagem", "Departamento atualizado com sucesso!");
            return "redirect:/departamentos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/departamentos/" + id + "/editar";
        }
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            departamentoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagem", "Departamento excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/departamentos";
    }

    @GetMapping("/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        Optional<Departamento> departamento = departamentoService.buscarPorId(id);
        if (departamento.isPresent()) {
            model.addAttribute("departamento", departamento.get());
            return "departamento/visualizar";
        }
        return "redirect:/departamentos";
    }
}
