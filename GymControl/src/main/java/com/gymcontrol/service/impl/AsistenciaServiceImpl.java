package com.gymcontrol.service.impl;

import com.gymcontrol.domain.Asistencia;
import com.gymcontrol.repository.AsistenciaRepository;
import com.gymcontrol.service.AsistenciaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsistenciaServiceImpl implements AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Override
    public List<Asistencia> listarTodos() {
        return asistenciaRepository.findAll();
    }

    @Override
    public Asistencia guardar(Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia buscarPorId(Long id) {
        return asistenciaRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }
}
