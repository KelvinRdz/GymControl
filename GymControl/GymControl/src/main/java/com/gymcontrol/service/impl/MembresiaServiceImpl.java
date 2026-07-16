package com.gymcontrol.service.impl;

import com.gymcontrol.domain.Membresia;
import com.gymcontrol.repository.MembresiaRepository;
import com.gymcontrol.service.MembresiaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembresiaServiceImpl implements MembresiaService {

    @Autowired
    private MembresiaRepository membresiaRepository;

    @Override
    public List<Membresia> listarTodos() {
        return membresiaRepository.findAll();
    }

    @Override
    public Membresia guardar(Membresia membresia) {
        return membresiaRepository.save(membresia);
    }

    @Override
    public Membresia buscarPorId(Long id) {
        return membresiaRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        membresiaRepository.deleteById(id);
    }
}
