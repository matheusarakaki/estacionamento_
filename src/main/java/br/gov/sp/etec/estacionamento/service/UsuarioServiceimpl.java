package br.gov.sp.etec.estacionamento.service;

import br.gov.sp.etec.estacionamento.model.Usuario;
import br.gov.sp.etec.estacionamento.entity.UsuarioEntity;
import br.gov.sp.etec.estacionamento.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceimpl implements UsuarioService {
    @Autowired
    UsuarioRepository repository;

    @Override
    public String cadastroUsuario(Usuario usuario) {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setNome(usuario.getNome());
        usuarioEntity.setCpf(usuario.getCpf());
        usuarioEntity.setEmail(usuario.getEmail());
        usuarioEntity.setDatadenasc(usuario.getDatadenasc());
        usuarioEntity.setSenha(usuario.getSenha());
        usuarioEntity.setTelefone(usuario.getTelefone());

        repository.save(usuarioEntity);
        return "";
    }

    @Override
    public List<Usuario> listarUsuario() {
        return List.of();
    }

    @Override
    public String atualizarUsuario(Usuario usuario) {
        return "";
    }

    @Override
    public String deletarUsuario(Long id) {
        return "";
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        UsuarioEntity entity = repository.findFirstByEmail(email);
        if (entity == null) {
            return null;
        }
        return toUsuario(entity);
    }

    private Usuario toUsuario(UsuarioEntity entity ){
        Usuario usuario = new Usuario();
        usuario.setEmail(entity.getEmail());
        usuario.setNome(entity.getNome());
        usuario.setSenha(entity.getSenha());
        usuario.setTelefone(entity.getTelefone());
        usuario.setCpf(entity.getCpf());
        usuario.setDatadenasc(entity.getDatadenasc());
        return usuario;
    }
}