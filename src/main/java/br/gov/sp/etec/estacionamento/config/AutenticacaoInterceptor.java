package br.gov.sp.etec.estacionamento.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/** Bloqueia o acesso às páginas internas para quem não fez login. */
public class AutenticacaoInterceptor implements HandlerInterceptor {

    public static final String ATRIBUTO_USUARIO = "usuarioNome";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(ATRIBUTO_USUARIO) != null) {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/");
        return false;
    }
}
