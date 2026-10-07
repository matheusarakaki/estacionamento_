package br.gov.sp.etec.estacionamento.repositorio;

import br.gov.sp.etec.estacionamento.entity.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<VeiculoEntity, Long> {
    List<VeiculoEntity> findByHoraSaidaIsNullOrderByDataHoraEntradaAsc();
    List<VeiculoEntity> findAllByOrderByDataHoraEntradaDesc();
    boolean existsByPlacaIgnoreCaseAndHoraSaidaIsNull(String placa);
}
