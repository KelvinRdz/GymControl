package com.gymcontrol.service.impl;

import com.gymcontrol.domain.Cliente;
import com.gymcontrol.repository.ClienteRepository;
import com.gymcontrol.service.ClienteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public List<Cliente> buscarPorNombreOCedula(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            return listarTodos();
        }
        String valor = criterio.trim();
        return clienteRepository.findByNombreContainingIgnoreCaseOrCedulaContainingIgnoreCase(valor, valor);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }
}
