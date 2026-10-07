package br.gov.sp.etec.estacionamento.controller;

import br.gov.sp.etec.estacionamento.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PainelController {

    @Autowired
    VeiculoService service;

    @GetMapping("/painel")
    public String painel(Model model) {
        model.addAttribute("veiculos", service.listaVeiculo());
        return "painel";
    }

    @GetMapping("/registros")
    public String registros(Model model) {
        model.addAttribute("registros", service.listaRegistros());
        return "registros";
    }

    @GetMapping("/relatorio")
    public String relatorio(Model model) {
        model.addAttribute("relatorio", service.gerarRelatorio());
        return "relatorio";
    }
}
