package br.gov.sp.etec.estacionamento.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Veiculo {

    private Long id;
    private String placa;
    private String modelo;
    private String cor;
    private String observacao;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
        this.dataHoraEntrada = dataHoraEntrada;
    }

    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
    }

    /** Veículo ainda está no estacionamento (não tem saída registrada). */
    public boolean isEstacionado() {
        return dataHoraSaida == null;
    }

    /** Tempo entre a entrada e a saída (ou até agora, se ainda estiver estacionado). */
    public Duration getDuracao() {
        if (dataHoraEntrada == null) {
            return Duration.ZERO;
        }
        LocalDateTime fim = dataHoraSaida != null ? dataHoraSaida : LocalDateTime.now();
        return Duration.between(dataHoraEntrada, fim);
    }

    public String getPermanencia() {
        return formatarDuracao(getDuracao());
    }

    public static String formatarDuracao(Duration duracao) {
        long minutos = Math.max(0, duracao.toMinutes());
        long dias = minutos / 1440;
        long horas = (minutos % 1440) / 60;
        long min = minutos % 60;
        if (dias > 0) {
            return dias + "d " + horas + "h " + min + "min";
        }
        if (horas > 0) {
            return horas + "h " + min + "min";
        }
        return min + " min";
    }
}
