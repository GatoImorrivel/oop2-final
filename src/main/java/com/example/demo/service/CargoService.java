package com.example.demo.service;

import com.example.demo.model.Cargo;
import com.example.demo.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
}
