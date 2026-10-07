package br.gov.sp.etec.estacionamento.service;

import br.gov.sp.etec.estacionamento.entity.VeiculoEntity;
import br.gov.sp.etec.estacionamento.model.Relatorio;
import br.gov.sp.etec.estacionamento.model.Veiculo;

import java.util.List;

public interface VeiculoService {
    void cadastrarVeiculo(Veiculo veiculo);
    /** Veículos que estão no estacionamento agora. */
    List<Veiculo> listaVeiculo();
    /** Todos os veículos que já passaram pelo estacionamento (do mais recente ao mais antigo). */
    List<Veiculo> listaRegistros();
    boolean deletarVeiculo(Long id);
    Veiculo atualizarVeiculo(Veiculo v);
    VeiculoEntity buscaVeiculoPorId(Long id);
    void registrarSaida(Long id);
    Relatorio gerarRelatorio();
}
