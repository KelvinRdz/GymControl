package com.gymcontrol.service;

import com.gymcontrol.domain.Membresia;
import java.util.List;

public interface MembresiaService {
    List<Membresia> listarTodos();
    Membresia guardar(Membresia membresia);
    Membresia buscarPorId(Long id);
    void eliminar(Long id);
}
