package com.gymcontrol.service;

import com.gymcontrol.domain.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario guardar(Usuario usuario);
    Usuario buscarPorId(Long id);
    Usuario buscarPorUsuario(String usuario);
    List<Usuario> listar();
    void eliminar(Long id);
    Usuario actualizar(Usuario usuario);
    boolean validarCredenciales(String usuario, String contraseña);
}
