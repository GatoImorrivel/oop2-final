package com.example.demo.repository;

import com.example.demo.model.Funcionario;
import com.example.demo.model.Cargo;
import com.example.demo.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByEmail(String email);

    List<Funcionario> findByDepartamento(Departamento departamento);

    List<Funcionario> findByCargo(Cargo cargo);

    List<Funcionario> findByChefe(Funcionario chefe);
}
