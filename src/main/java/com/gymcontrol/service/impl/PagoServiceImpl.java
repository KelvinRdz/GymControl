package com.gymcontrol.service.impl;

import com.gymcontrol.domain.Pago;
import com.gymcontrol.repository.PagoRepository;
import com.gymcontrol.service.PagoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    @Override
    public List<Pago> listarPorCliente(Long clienteId) {
        return pagoRepository.findByClienteIdOrderByFechaPagoDesc(clienteId);
    }

    @Override
    public Pago guardar(Pago pago) {
        return pagoRepository.save(pago);
    }

    @Override
    public Pago buscarPorId(Long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }
}
