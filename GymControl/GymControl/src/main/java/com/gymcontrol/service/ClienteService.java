package com.gymcontrol.service;

import com.gymcontrol.domain.Cliente;
import java.util.List;

public interface ClienteService {
    List<Cliente> listarTodos();
    List<Cliente> buscarPorNombreOCedula(String criterio);
    Cliente guardar(Cliente cliente);
    Cliente buscarPorId(Long id);
    void eliminar(Long id);
}
