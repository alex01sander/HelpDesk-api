package com.alex.helpdesk.repository;

import com.alex.helpdesk.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByChamadoId(Long chamadoId);
}