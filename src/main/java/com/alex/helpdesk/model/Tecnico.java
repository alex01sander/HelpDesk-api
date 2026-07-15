package com.alex.helpdesk.model;

import com.alex.helpdesk.dto.TecnicoRequestDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "tecnicos")
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    public Tecnico() {
    }

    public Tecnico(TecnicoRequestDTO dto) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.especialidade = dto.especialidade();
    }

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

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }
}