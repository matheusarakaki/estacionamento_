package br.gov.sp.etec.estacionamento.service;

import br.gov.sp.etec.estacionamento.model.Usuario;

import java.util.List;

public interface UsuarioService {
     String cadastroUsuario(Usuario usuario);
     List<Usuario> listarUsuario();
     String atualizarUsuario(Usuario usuario);
     String deletarUsuario(Long id);
     Usuario buscarUsuarioPorEmail(String email);

}
