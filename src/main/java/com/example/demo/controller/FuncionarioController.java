package com.example.demo.controller;

import com.example.demo.model.Funcionario;
import com.example.demo.model.Departamento;
import com.example.demo.model.Cargo;
import com.example.demo.service.FuncionarioService;
import com.example.demo.service.DepartamentoService;
import com.example.demo.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public String listar(
            @RequestParam(required = false) Long departamentoId,
            @RequestParam(required = false) Long cargoId,
            @RequestParam(required = false) Long chefeId,
            @RequestParam(required = false) String search,
            Model model) {
        List<Funcionario> funcionarios;

        if (departamentoId != null) {
            Optional<Departamento> departamento = departamentoService.buscarPorId(departamentoId);
            if (departamento.isPresent()) {
                funcionarios = funcionarioService.listarPorDepartamento(departamento.get());
                model.addAttribute("filtro", "Departamento: " + departamento.get().getNome());
            } else {
                funcionarios = funcionarioService.listarTodos();
            }
        } else if (cargoId != null) {
            Optional<Cargo> cargo = cargoService.buscarPorId(cargoId);
            if (cargo.isPresent()) {
                funcionarios = funcionarioService.listarPorCargo(cargo.get());
                model.addAttribute("filtro", "Cargo: " + cargo.get().getNome());
            } else {
                funcionarios = funcionarioService.listarTodos();
            }
        } else if (chefeId != null) {
            Optional<Funcionario> chefe = funcionarioService.buscarPorId(chefeId);
            if (chefe.isPresent()) {
                funcionarios = funcionarioService.listarPorChefe(chefe.get());
                model.addAttribute("filtro", "Chefe: " + chefe.get().getNome());
            } else {
                funcionarios = funcionarioService.listarTodos();
            }
        } else {
            funcionarios = funcionarioService.listarTodos();
        }

        // Apply fuzzy search if search parameter is provided
        if (search != null && !search.trim().isEmpty()) {
            funcionarios = funcionarioService.fuzzySearch(funcionarios, search);
            model.addAttribute("searchTerm", search);
        }

        model.addAttribute("funcionarios", funcionarios);
        model.addAttribute("departamentos", departamentoService.listarTodos());
        model.addAttribute("cargos", cargoService.listarTodos());
        model.addAttribute("funcionarios", funcionarios);
        return "funcionario/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        model.addAttribute("departamentos", departamentoService.listarTodos());
        model.addAttribute("cargos", cargoService.listarTodos());
        model.addAttribute("funcionarios", funcionarioService.listarTodos());
        return "funcionario/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Funcionario funcionario, RedirectAttributes redirectAttributes) {
        try {
            funcionarioService.criar(funcionario);
            redirectAttributes.addFlashAttribute("mensagem", "Funcionário criado com sucesso!");
            return "redirect:/funcionarios";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/funcionarios/novo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Funcionario> funcionario = funcionarioService.buscarPorId(id);
        if (funcionario.isPresent()) {
            model.addAttribute("funcionario", funcionario.get());
            model.addAttribute("departamentos", departamentoService.listarTodos());
            model.addAttribute("cargos", cargoService.listarTodos());
            model.addAttribute("funcionarios", funcionarioService.listarTodos());
            return "funcionario/form";
        }
        return "redirect:/funcionarios";
    }

    @PostMapping("/{id}/atualizar")
    public String atualizar(@PathVariable Long id, @ModelAttribute Funcionario funcionario,
            RedirectAttributes redirectAttributes) {
        try {
            funcionarioService.atualizar(id, funcionario);
            redirectAttributes.addFlashAttribute("mensagem", "Funcionário atualizado com sucesso!");
            return "redirect:/funcionarios";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/funcionarios/" + id + "/editar";
        }
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            funcionarioService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagem", "Funcionário excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/funcionarios";
    }

    @GetMapping("/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        Optional<Funcionario> funcionario = funcionarioService.buscarPorId(id);
        if (funcionario.isPresent()) {
            model.addAttribute("funcionario", funcionario.get());
            return "funcionario/visualizar";
        }
        return "redirect:/funcionarios";
    }
}
