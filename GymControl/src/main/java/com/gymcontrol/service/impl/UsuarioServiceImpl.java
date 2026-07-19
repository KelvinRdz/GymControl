package com.gymcontrol.service.impl;

import com.gymcontrol.domain.Usuario;
import com.gymcontrol.repository.UsuarioRepository;
import com.gymcontrol.service.UsuarioService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    
    @Override
    public Usuario buscarPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }
    
    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }
    
    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    @Override
    public Usuario actualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public boolean validarCredenciales(String usuario, String contraseña) {
        Usuario usuarioEncontrado = usuarioRepository.findByUsuario(usuario);
        if (usuarioEncontrado == null || !usuarioEncontrado.isActivo()) {
            return false;
        }

        String contraseñaGuardada = usuarioEncontrado.getContraseña();
        if (contraseñaGuardada == null) {
            return false;
        }

        String contraseñaIngresadaHash = sha256(contraseña);
        return contraseñaIngresadaHash.equalsIgnoreCase(contraseñaGuardada);
    }

    private String sha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No se pudo generar el hash SHA-256", e);
        }
    }
    
}
