package com.example.demo.service;

import com.example.demo.model.Cargo;
import com.example.demo.repository.CargoRepository;
import com.example.demo.util.FuzzyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    public List<Cargo> listarTodos() {
        return cargoRepository.findAll();
    }

    public Optional<Cargo> buscarPorId(Long id) {
        return cargoRepository.findById(id);
    }

    public Optional<Cargo> buscarPorNome(String nome) {
        return cargoRepository.findByNome(nome);
    }

    public Cargo criar(Cargo cargo) {
        if (cargo.getNome() == null || cargo.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cargo é obrigatório");
        }
        return cargoRepository.save(cargo);
    }

    public Cargo atualizar(Long id, Cargo cargoAtualizado) {
        Optional<Cargo> cargoExistente = cargoRepository.findById(id);
        if (cargoExistente.isPresent()) {
            Cargo cargo = cargoExistente.get();
            if (cargoAtualizado.getNome() != null && !cargoAtualizado.getNome().trim().isEmpty()) {
                cargo.setNome(cargoAtualizado.getNome());
            }
            if (cargoAtualizado.getDescricao() != null) {
                cargo.setDescricao(cargoAtualizado.getDescricao());
            }
            return cargoRepository.save(cargo);
        }
        throw new IllegalArgumentException("Cargo não encontrado");
    }

    public void excluir(Long id) {
        if (!cargoRepository.existsById(id)) {
            throw new IllegalArgumentException("Cargo não encontrado");
        }
        cargoRepository.deleteById(id);
    }

    public List<Cargo> fuzzySearch(List<Cargo> cargos, String searchTerm) {
        return cargos.stream()
                .filter(c -> FuzzyMatcher.fuzzyMatch(c.getNome(), searchTerm) ||
                        FuzzyMatcher.fuzzyMatch(c.getDescricao() != null ? c.getDescricao() : "", searchTerm))
                .sorted((c1, c2) -> {
                    double score1 = Math.max(
                            FuzzyMatcher.fuzzyScore(c1.getNome(), searchTerm),
                            FuzzyMatcher.fuzzyScore(c1.getDescricao() != null ? c1.getDescricao() : "", searchTerm));
                    double score2 = Math.max(
                            FuzzyMatcher.fuzzyScore(c2.getNome(), searchTerm),
                            FuzzyMatcher.fuzzyScore(c2.getDescricao() != null ? c2.getDescricao() : "", searchTerm));
                    return Double.compare(score2, score1);
                })
                .collect(Collectors.toList());
    }
}
