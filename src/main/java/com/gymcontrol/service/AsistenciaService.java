package com.gymcontrol.service;

import com.gymcontrol.domain.Asistencia;
import java.util.List;

public interface AsistenciaService {
    List<Asistencia> listarTodos();
    Asistencia guardar(Asistencia asistencia);
    Asistencia buscarPorId(Long id);
    void eliminar(Long id);
}
