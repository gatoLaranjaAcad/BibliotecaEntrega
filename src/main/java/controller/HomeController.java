package controller;

import dao.LivroDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class HomeController {
    private final LivroDAO livroDAO;

    public HomeController(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;
    }

    @GetMapping("/home")
    public String home(@RequestParam(required = false) String q, HttpServletRequest request, Model model) throws SQLException {
        Usuario usuario = obterUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("estatisticas", livroDAO.estatisticasPorUsuario(usuario.getId()));
        if (q != null && !q.isBlank()) {
            model.addAttribute("livros", livroDAO.listarPorUsuarioEBusca(usuario.getId(), q.trim()));
            model.addAttribute("busca", q.trim());
        } else {
            model.addAttribute("livros", livroDAO.listarPorUsuario(usuario.getId()));
        }
        return "home";
    }

    private Usuario obterUsuarioLogado(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao == null || sessao.getAttribute("usuarioLogado") == null) {
            return null;
        }
        return (Usuario) sessao.getAttribute("usuarioLogado");
    }
}
