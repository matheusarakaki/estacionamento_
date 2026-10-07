package br.gov.sp.etec.estacionamento.controller;

import br.gov.sp.etec.estacionamento.entity.VeiculoEntity;
import br.gov.sp.etec.estacionamento.model.Veiculo;
import br.gov.sp.etec.estacionamento.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/veiculo")
public class VeiculoController {

    @Autowired
    VeiculoService service;

    @GetMapping("/entrada")
    public String entrada(Model model) {
        model.addAttribute("veiculo", new Veiculo());
        return "registrar-entrada";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(Veiculo veiculo, Model model, RedirectAttributes redirect) {
        try {
            service.cadastrarVeiculo(veiculo);
        } catch (IllegalStateException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("veiculo", veiculo);
            return "registrar-entrada";
        }
        redirect.addFlashAttribute("sucesso", "Entrada registrada: " + veiculo.getPlaca() + ".");
        return "redirect:/painel";
    }

    @GetMapping("/registrar-saida")
    public String registrarSaida(Model model) {
        model.addAttribute("veiculos", service.listaVeiculo());
        return "registrar-saida";
    }

    @PostMapping("/saida/{id}")
    public String confirmarSaida(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            VeiculoEntity veiculo = service.buscaVeiculoPorId(id);
            service.registrarSaida(id);
            redirect.addFlashAttribute("sucesso", "Saída registrada: " + veiculo.getPlaca() + ".");
        } catch (NoSuchElementException e) {
            redirect.addFlashAttribute("erro", "Veículo não encontrado.");
        }
        return "redirect:/veiculo/registrar-saida";
    }
}
