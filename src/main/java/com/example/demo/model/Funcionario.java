package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar dataContratacao;

    @Column(nullable = false)
    private Double salario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chefe_id")
    private Funcionario chefe;

    @OneToMany(mappedBy = "chefe", cascade = CascadeType.ALL)
    private List<Funcionario> subordinados = new ArrayList<>();

    // Constructors
    public Funcionario() {
    }

    public Funcionario(String nome, String email, Calendar dataContratacao, Double salario,
            Departamento departamento, Cargo cargo) {
        this.nome = nome;
        this.email = email;
        this.dataContratacao = dataContratacao;
        this.salario = salario;
        this.departamento = departamento;
        this.cargo = cargo;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Calendar getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(Calendar dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Funcionario getChefe() {
        return chefe;
    }

    public void setChefe(Funcionario chefe) {
        this.chefe = chefe;
    }

    public List<Funcionario> getSubordinados() {
        return subordinados;
    }

    public void setSubordinados(List<Funcionario> subordinados) {
        this.subordinados = subordinados;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", salario=" + salario +
                '}';
    }
}
