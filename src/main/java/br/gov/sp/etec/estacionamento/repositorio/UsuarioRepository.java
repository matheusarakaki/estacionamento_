package br.gov.sp.etec.estacionamento.repositorio;

import br.gov.sp.etec.estacionamento.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    UsuarioEntity findFirstByEmail(String email);
}
