package com.alex.helpdesk.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String texto;

    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "chamado_id", nullable = false)
    private Chamado chamado;

    @ManyToOne
    @JoinColumn(name = "autor_usuario_id", nullable = true)
    private Usuario autorUsuario;

    @ManyToOne
    @JoinColumn(name = "autor_tecnico_id", nullable = true)
    private Tecnico autorTecnico;

    public Comentario() {
    }

    public Comentario(String texto, Chamado chamado, Usuario autorUsuario, Tecnico autorTecnico) {
        this.texto = texto;
        this.chamado = chamado;
        this.autorUsuario = autorUsuario;
        this.autorTecnico = autorTecnico;
        this.dataCriacao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Chamado getChamado() {
        return chamado;
    }

    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }

    public Usuario getAutorUsuario() {
        return autorUsuario;
    }

    public void setAutorUsuario(Usuario autorUsuario) {
        this.autorUsuario = autorUsuario;
    }

    public Tecnico getAutorTecnico() {
        return autorTecnico;
    }

    public void setAutorTecnico(Tecnico autorTecnico) {
        this.autorTecnico = autorTecnico;
    }
}