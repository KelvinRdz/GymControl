package com.gymcontrol.service.impl;

import com.gymcontrol.domain.Entrenador;
import com.gymcontrol.repository.EntrenadorRepository;
import com.gymcontrol.service.EntrenadorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntrenadorServiceImpl implements EntrenadorService {

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Override
    public List<Entrenador> listarTodos() {
        return entrenadorRepository.findAll();
    }

    @Override
    public Entrenador guardar(Entrenador entrenador) {
        return entrenadorRepository.save(entrenador);
    }

    @Override
    public Entrenador buscarPorId(Long id) {
        return entrenadorRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        entrenadorRepository.deleteById(id);
    }
}
