package br.gov.sp.etec.estacionamento.controller;

import br.gov.sp.etec.estacionamento.config.AutenticacaoInterceptor;
import br.gov.sp.etec.estacionamento.model.Usuario;
import br.gov.sp.etec.estacionamento.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UsuarioService service;

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute(AutenticacaoInterceptor.ATRIBUTO_USUARIO) != null) {
            return "redirect:/painel";
        }
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastrar() {
        return "tela-cadastro";
    }

    @PostMapping("/efetuar-cadastro")
    public String efetuarCadastro(Usuario usuario, Model model) {
        log.info(usuario.toString());
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            model.addAttribute("erro", "Informe um e-mail.");
            return "tela-cadastro";
        }
        if (service.buscarUsuarioPorEmail(usuario.getEmail()) != null) {
            model.addAttribute("erro", "Já existe um cadastro com esse e-mail.");
            return "tela-cadastro";
        }
        service.cadastroUsuario(usuario);
        return "cadastro-sucess";
    }

    @PostMapping("/autenticar")
    public String autenticar(String email, String senha, HttpSession session) {
        if (email == null || senha == null) {
            return "erro";
        }
        Usuario usuario = service.buscarUsuarioPorEmail(email);
        if (usuario != null && senha.equals(usuario.getSenha())) {
            String nome = (usuario.getNome() == null || usuario.getNome().isBlank()) ? email : usuario.getNome();
            session.setAttribute(AutenticacaoInterceptor.ATRIBUTO_USUARIO, nome);
            return "redirect:/painel";
        }
        return "erro";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
