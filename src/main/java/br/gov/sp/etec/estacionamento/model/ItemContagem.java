package br.gov.sp.etec.estacionamento.model;

/** Item de ranking/gráfico de barras do relatório. percentual vai de 0 a 100 (relativo ao maior item). */
public record ItemContagem(String rotulo, long quantidade, int percentual) {
}
