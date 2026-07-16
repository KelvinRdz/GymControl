package com.gymcontrol.service;

import com.gymcontrol.domain.Entrenador;
import java.util.List;

public interface EntrenadorService {
    List<Entrenador> listarTodos();
    Entrenador guardar(Entrenador entrenador);
    Entrenador buscarPorId(Long id);
    void eliminar(Long id);
}
