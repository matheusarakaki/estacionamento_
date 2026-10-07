package br.gov.sp.etec.estacionamento.model;

import java.time.LocalDateTime;
import java.util.List;

public record Relatorio(
        long total,
        long estacionados,
        long saidas,
        long entradasHoje,
        long saidasHoje,
        long placasDistintas,
        String permanenciaMedia,
        String maiorPermanencia,
        String horarioPico,
        List<ItemContagem> topModelos,
        List<ItemContagem> topCores,
        List<ItemContagem> topPlacas,
        List<ItemContagem> entradasPorHora,
        List<ItemContagem> entradasUltimosDias,
        LocalDateTime geradoEm) {
}
