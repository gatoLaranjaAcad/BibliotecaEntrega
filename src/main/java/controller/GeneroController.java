package controller;

import dao.GeneroDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.Genero;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GeneroController {
    private static final String SQLSTATE_UNIQUE_VIOLATION = "23505";
    private final GeneroDAO generoDAO;

    public GeneroController(GeneroDAO generoDAO) {
        this.generoDAO = generoDAO;
    }

    @GetMapping("/generos")
    public String generos(@RequestParam(required = false) String acao,
                          @RequestParam(defaultValue = "0") int id,
                          HttpServletRequest request,
                          Model model) throws SQLException {
        if (!sessaoValida(request)) {
            return "redirect:/login";
        }

        if ("novo".equals(acao)) {
            return "genero/form";
        }
        if ("editar".equals(acao)) {
            Genero genero = generoDAO.buscarPorId(id);
            if (genero == null) {
                return "redirect:/generos";
            }
            model.addAttribute("genero", genero);
            return "genero/form";
        }

        model.addAttribute("generos", generoDAO.listar());
        return "genero/listar";
    }

    @PostMapping("/generos")
    public String salvarGenero(HttpServletRequest request, Model model) throws SQLException {
        if (!sessaoValida(request)) {
            return "redirect:/login";
        }

        String acao = request.getParameter("acao");
        if ("excluir".equals(acao)) {
            generoDAO.excluir(parseInt(request.getParameter("id"), 0));
            return "redirect:/generos";
        }

        Genero genero = new Genero();
        genero.setId(parseInt(request.getParameter("id"), 0));
        genero.setNome(texto(request, "nome"));

        List<String> erros = validar(genero);
        if (erros.isEmpty() && generoDAO.existePorNome(genero.getNome(), genero.getId())) {
            erros.add("Já existe um gênero cadastrado com esse nome.");
        }
        if (!erros.isEmpty()) {
            return encaminharFormulario(model, genero, erros);
        }

        try {
            if (genero.getId() > 0) {
                generoDAO.atualizar(genero);
            } else {
                generoDAO.inserir(genero);
            }
        } catch (SQLException e) {
            if (SQLSTATE_UNIQUE_VIOLATION.equals(e.getSQLState())) {
                erros.add("Já existe um gênero cadastrado com esse nome.");
                return encaminharFormulario(model, genero, erros);
            }
            throw e;
        }

        return "redirect:/generos";
    }

    private boolean sessaoValida(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        return sessao != null && sessao.getAttribute("usuarioLogado") != null;
    }

    private List<String> validar(Genero genero) {
        List<String> erros = new ArrayList<>();
        if (genero.getNome() == null || genero.getNome().trim().length() < 2) {
            erros.add("Nome do gênero deve ter pelo menos 2 caracteres.");
        }
        return erros;
    }

    private String encaminharFormulario(Model model, Genero genero, List<String> erros) {
        model.addAttribute("erros", erros);
        model.addAttribute("genero", genero);
        return "genero/form";
    }

    private String texto(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);
        return valor == null ? "" : valor.trim();
    }

    private int parseInt(String valor, int padrao) {
        try {
            return valor == null || valor.isBlank() ? padrao : Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return padrao;
        }
    }
}
