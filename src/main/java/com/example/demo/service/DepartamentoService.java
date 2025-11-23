package com.example.demo.service;

import com.example.demo.model.Departamento;
import com.example.demo.repository.DepartamentoRepository;
import com.example.demo.util.FuzzyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Departamento> listarTodos() {
        return departamentoRepository.findAll();
    }

    public Optional<Departamento> buscarPorId(Long id) {
        return departamentoRepository.findById(id);
    }

    public Optional<Departamento> buscarPorNome(String nome) {
        return departamentoRepository.findByNome(nome);
    }

    public Departamento criar(Departamento departamento) {
        if (departamento.getNome() == null || departamento.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do departamento é obrigatório");
        }
        return departamentoRepository.save(departamento);
    }

    public Departamento atualizar(Long id, Departamento departamentoAtualizado) {
        Optional<Departamento> departamentoExistente = departamentoRepository.findById(id);
        if (departamentoExistente.isPresent()) {
            Departamento departamento = departamentoExistente.get();
            if (departamentoAtualizado.getNome() != null && !departamentoAtualizado.getNome().trim().isEmpty()) {
                departamento.setNome(departamentoAtualizado.getNome());
            }
            if (departamentoAtualizado.getDescricao() != null) {
                departamento.setDescricao(departamentoAtualizado.getDescricao());
            }
            return departamentoRepository.save(departamento);
        }
        throw new IllegalArgumentException("Departamento não encontrado");
    }

    public void excluir(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new IllegalArgumentException("Departamento não encontrado");
        }
        departamentoRepository.deleteById(id);
    }

    public List<Departamento> fuzzySearch(List<Departamento> departamentos, String searchTerm) {
        return departamentos.stream()
                .filter(d -> FuzzyMatcher.fuzzyMatch(d.getNome(), searchTerm) ||
                        FuzzyMatcher.fuzzyMatch(d.getDescricao() != null ? d.getDescricao() : "", searchTerm))
                .sorted((d1, d2) -> {
                    double score1 = Math.max(
                            FuzzyMatcher.fuzzyScore(d1.getNome(), searchTerm),
                            FuzzyMatcher.fuzzyScore(d1.getDescricao() != null ? d1.getDescricao() : "", searchTerm));
                    double score2 = Math.max(
                            FuzzyMatcher.fuzzyScore(d2.getNome(), searchTerm),
                            FuzzyMatcher.fuzzyScore(d2.getDescricao() != null ? d2.getDescricao() : "", searchTerm));
                    return Double.compare(score2, score1);
                })
                .collect(Collectors.toList());
    }
}
