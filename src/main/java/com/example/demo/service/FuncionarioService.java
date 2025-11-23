package com.example.demo.service;

import com.example.demo.model.Funcionario;
import com.example.demo.model.Departamento;
import com.example.demo.model.Cargo;
import com.example.demo.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    public Optional<Funcionario> buscarPorEmail(String email) {
        return funcionarioRepository.findByEmail(email);
    }

    public List<Funcionario> listarPorDepartamento(Departamento departamento) {
        return funcionarioRepository.findByDepartamento(departamento);
    }

    public List<Funcionario> listarPorCargo(Cargo cargo) {
        return funcionarioRepository.findByCargo(cargo);
    }

    public List<Funcionario> listarPorChefe(Funcionario chefe) {
        return funcionarioRepository.findByChefe(chefe);
    }

    public Funcionario criar(Funcionario funcionario) {
        validarFuncionario(funcionario);
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario atualizar(Long id, Funcionario funcionarioAtualizado) {
        Optional<Funcionario> funcionarioExistente = funcionarioRepository.findById(id);
        if (funcionarioExistente.isPresent()) {
            Funcionario funcionario = funcionarioExistente.get();

            if (funcionarioAtualizado.getNome() != null && !funcionarioAtualizado.getNome().trim().isEmpty()) {
                funcionario.setNome(funcionarioAtualizado.getNome());
            }

            if (funcionarioAtualizado.getEmail() != null && !funcionarioAtualizado.getEmail().trim().isEmpty()) {
                // Validar se o novo email não está sendo usado por outro funcionário
                Optional<Funcionario> outroFuncionario = funcionarioRepository
                        .findByEmail(funcionarioAtualizado.getEmail());
                if (outroFuncionario.isPresent() && !outroFuncionario.get().getId().equals(id)) {
                    throw new IllegalArgumentException("Email já está em uso");
                }
                funcionario.setEmail(funcionarioAtualizado.getEmail());
            }

            if (funcionarioAtualizado.getDataContratacao() != null) {
                validarDataContratacao(funcionarioAtualizado.getDataContratacao());
                funcionario.setDataContratacao(funcionarioAtualizado.getDataContratacao());
            }

            if (funcionarioAtualizado.getSalario() != null) {
                validarSalario(funcionarioAtualizado.getSalario());
                funcionario.setSalario(funcionarioAtualizado.getSalario());
            }

            if (funcionarioAtualizado.getDepartamento() != null) {
                funcionario.setDepartamento(funcionarioAtualizado.getDepartamento());
            }

            if (funcionarioAtualizado.getCargo() != null) {
                funcionario.setCargo(funcionarioAtualizado.getCargo());
            }

            if (funcionarioAtualizado.getChefe() != null) {
                validarChefe(funcionario, funcionarioAtualizado.getChefe());
                funcionario.setChefe(funcionarioAtualizado.getChefe());
            } else {
                funcionario.setChefe(null);
            }

            return funcionarioRepository.save(funcionario);
        }
        throw new IllegalArgumentException("Funcionário não encontrado");
    }

    public void excluir(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Funcionário não encontrado");
        }
        funcionarioRepository.deleteById(id);
    }

    private void validarFuncionario(Funcionario funcionario) {
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do funcionário é obrigatório");
        }

        if (funcionario.getEmail() == null || funcionario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        // Validar unicidade do email
        Optional<Funcionario> existente = funcionarioRepository.findByEmail(funcionario.getEmail());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        if (funcionario.getDataContratacao() == null) {
            throw new IllegalArgumentException("Data de contratação é obrigatória");
        }

        validarDataContratacao(funcionario.getDataContratacao());

        if (funcionario.getSalario() == null) {
            throw new IllegalArgumentException("Salário é obrigatório");
        }

        validarSalario(funcionario.getSalario());

        if (funcionario.getDepartamento() == null) {
            throw new IllegalArgumentException("Departamento é obrigatório");
        }

        if (funcionario.getCargo() == null) {
            throw new IllegalArgumentException("Cargo é obrigatório");
        }

        // Validar se um funcionário não é chefe de si mesmo
        if (funcionario.getChefe() != null) {
            validarChefe(funcionario, funcionario.getChefe());
        }
    }

    private void validarDataContratacao(Calendar dataContratacao) {
        Calendar hoje = Calendar.getInstance();
        if (dataContratacao.after(hoje)) {
            throw new IllegalArgumentException("Data de contratação não pode ser futura");
        }
    }

    private void validarSalario(Double salario) {
        if (salario <= 0) {
            throw new IllegalArgumentException("Salário deve ser positivo");
        }
    }

    private void validarChefe(Funcionario funcionario, Funcionario chefe) {
        if (funcionario.getId() != null && funcionario.getId().equals(chefe.getId())) {
            throw new IllegalArgumentException("Um funcionário não pode ser chefe de si mesmo");
        }
    }
}
