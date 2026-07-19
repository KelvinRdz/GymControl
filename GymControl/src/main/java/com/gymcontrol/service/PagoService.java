package com.gymcontrol.service;

import com.gymcontrol.domain.Pago;
import java.util.List;

public interface PagoService {
    List<Pago> listarTodos();
    List<Pago> listarPorCliente(Long clienteId);
    Pago guardar(Pago pago);
    Pago buscarPorId(Long id);
    void eliminar(Long id);
}
