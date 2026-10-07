package br.gov.sp.etec.estacionamento.service;

import br.gov.sp.etec.estacionamento.entity.VeiculoEntity;
import br.gov.sp.etec.estacionamento.model.ItemContagem;
import br.gov.sp.etec.estacionamento.model.Relatorio;
import br.gov.sp.etec.estacionamento.model.Veiculo;
import br.gov.sp.etec.estacionamento.repositorio.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VeiculoServiceimpl implements VeiculoService {

    @Autowired
    VeiculoRepository repository;

    @Override
    public void cadastrarVeiculo(Veiculo veiculo) {
        String placa = veiculo.getPlaca() == null ? "" : veiculo.getPlaca().trim().toUpperCase(Locale.ROOT);
        if (placa.isEmpty()) {
            throw new IllegalStateException("Informe a placa do veículo.");
        }
        if (repository.existsByPlacaIgnoreCaseAndHoraSaidaIsNull(placa)) {
            throw new IllegalStateException("O veículo de placa " + placa + " já está no estacionamento.");
        }
        veiculo.setPlaca(placa);
        repository.save(toEntity(veiculo));
    }

    @Override
    public List<Veiculo> listaVeiculo() {
        return toListVeiculo(repository.findByHoraSaidaIsNullOrderByDataHoraEntradaAsc());
    }

    @Override
    public List<Veiculo> listaRegistros() {
        return toListVeiculo(repository.findAllByOrderByDataHoraEntradaDesc());
    }

    @Override
    public boolean deletarVeiculo(Long id) {
        return false;
    }

    @Override
    public Veiculo atualizarVeiculo(Veiculo v) {
        return null;
    }

    @Override
    public VeiculoEntity buscaVeiculoPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void registrarSaida(Long id) {
        VeiculoEntity entity = buscaVeiculoPorId(id);
        if (entity.getHoraSaida() != null) {
            return; // saída já registrada
        }
        entity.setHoraSaida(LocalDateTime.now());
        entity.setEstacionado(false);
        repository.save(entity);
    }

    // ------------------------------------------------------------------
    // Relatório
    // ------------------------------------------------------------------
    @Override
    public Relatorio gerarRelatorio() {
        List<Veiculo> todos = listaRegistros();
        LocalDate hoje = LocalDate.now();

        long total = todos.size();
        long estacionados = todos.stream().filter(Veiculo::isEstacionado).count();
        long saidas = total - estacionados;
        long entradasHoje = todos.stream()
                .filter(v -> v.getDataHoraEntrada() != null && v.getDataHoraEntrada().toLocalDate().equals(hoje))
                .count();
        long saidasHoje = todos.stream()
                .filter(v -> v.getDataHoraSaida() != null && v.getDataHoraSaida().toLocalDate().equals(hoje))
                .count();
        long placasDistintas = todos.stream()
                .map(Veiculo::getPlaca).filter(Objects::nonNull)
                .map(p -> p.toUpperCase(Locale.ROOT)).distinct().count();

        // Permanência (apenas veículos que já saíram)
        List<Duration> permanencias = todos.stream()
                .filter(v -> !v.isEstacionado())
                .map(Veiculo::getDuracao)
                .toList();
        String media = "-";
        String maior = "-";
        if (!permanencias.isEmpty()) {
            long mediaSegundos = (long) permanencias.stream().mapToLong(Duration::getSeconds).average().orElse(0);
            media = Veiculo.formatarDuracao(Duration.ofSeconds(mediaSegundos));
            maior = Veiculo.formatarDuracao(permanencias.stream().max(Duration::compareTo).orElse(Duration.ZERO));
        }

        // Entradas por hora do dia
        long[] porHora = new long[24];
        for (Veiculo v : todos) {
            if (v.getDataHoraEntrada() != null) {
                porHora[v.getDataHoraEntrada().getHour()]++;
            }
        }
        long maxHora = Arrays.stream(porHora).max().orElse(0);
        List<ItemContagem> entradasPorHora = new ArrayList<>();
        String pico = "-";
        for (int h = 0; h < 24; h++) {
            if (porHora[h] > 0) {
                String rotulo = String.format("%02dh", h);
                entradasPorHora.add(new ItemContagem(rotulo, porHora[h], percentual(porHora[h], maxHora)));
                if (porHora[h] == maxHora && pico.equals("-")) {
                    pico = rotulo;
                }
            }
        }

        // Entradas nos últimos 7 dias
        DateTimeFormatter fmtDia = DateTimeFormatter.ofPattern("dd/MM");
        long[] porDia = new long[7];
        for (int i = 0; i < 7; i++) {
            LocalDate dia = hoje.minusDays(6 - i);
            porDia[i] = todos.stream()
                    .filter(v -> v.getDataHoraEntrada() != null && v.getDataHoraEntrada().toLocalDate().equals(dia))
                    .count();
        }
        long maxDia = Arrays.stream(porDia).max().orElse(0);
        List<ItemContagem> ultimosDias = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate dia = hoje.minusDays(6 - i);
            ultimosDias.add(new ItemContagem(dia.format(fmtDia), porDia[i], percentual(porDia[i], maxDia)));
        }

        return new Relatorio(
                total, estacionados, saidas, entradasHoje, saidasHoje, placasDistintas,
                media, maior, pico,
                topContagem(todos, Veiculo::getModelo, this::tituloCase, 5),
                topContagem(todos, Veiculo::getCor, this::tituloCase, 5),
                topContagem(todos, Veiculo::getPlaca, s -> s.toUpperCase(Locale.ROOT), 5),
                entradasPorHora,
                ultimosDias,
                LocalDateTime.now());
    }

    private List<ItemContagem> topContagem(List<Veiculo> veiculos,
                                           Function<Veiculo, String> campo,
                                           Function<String, String> exibir,
                                           int limite) {
        Map<String, Long> contagem = veiculos.stream()
                .map(campo)
                .filter(Objects::nonNull)
                .map(s -> s.trim().toLowerCase(Locale.ROOT))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        List<Map.Entry<String, Long>> ordenado = contagem.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(limite)
                .toList();

        long max = ordenado.isEmpty() ? 0 : ordenado.get(0).getValue();
        List<ItemContagem> itens = new ArrayList<>();
        for (Map.Entry<String, Long> e : ordenado) {
            itens.add(new ItemContagem(exibir.apply(e.getKey()), e.getValue(), percentual(e.getValue(), max)));
        }
        return itens;
    }

    private int percentual(long valor, long maximo) {
        if (maximo <= 0 || valor <= 0) {
            return 0;
        }
        return Math.max(4, (int) Math.round(valor * 100.0 / maximo));
    }

    private String tituloCase(String texto) {
        StringBuilder sb = new StringBuilder();
        for (String palavra : texto.split("\\s+")) {
            if (palavra.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(palavra.charAt(0))).append(palavra.substring(1));
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // Conversões
    // ------------------------------------------------------------------
    private VeiculoEntity toEntity(Veiculo veiculo) {
        VeiculoEntity entity = new VeiculoEntity();
        entity.setPlaca(veiculo.getPlaca());
        entity.setCor(veiculo.getCor());
        entity.setModelo(veiculo.getModelo());
        entity.setObservacoes(veiculo.getObservacao());
        entity.setDataHoraEntrada(LocalDateTime.now());
        entity.setEstacionado(true);
        return entity;
    }

    private List<Veiculo> toListVeiculo(List<VeiculoEntity> entities) {
        List<Veiculo> veiculos = new ArrayList<>();
        for (VeiculoEntity v : entities) {
            Veiculo veiculo = new Veiculo();
            veiculo.setId(v.getId());
            veiculo.setPlaca(v.getPlaca());
            veiculo.setCor(v.getCor());
            veiculo.setModelo(v.getModelo());
            veiculo.setObservacao(v.getObservacoes());
            veiculo.setDataHoraEntrada(v.getDataHoraEntrada());
            veiculo.setDataHoraSaida(v.getHoraSaida());
            veiculos.add(veiculo);
        }
        return veiculos;
    }
}
