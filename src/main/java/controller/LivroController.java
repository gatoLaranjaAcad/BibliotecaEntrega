package controller;

import dao.GeneroDAO;
import dao.LivroDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.Genero;
import model.Livro;
import model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LivroController {
    private final LivroDAO livroDAO;
    private final GeneroDAO generoDAO;

    public LivroController(LivroDAO livroDAO, GeneroDAO generoDAO) {
        this.livroDAO = livroDAO;
        this.generoDAO = generoDAO;
    }

    @GetMapping("/livros")
    public String livros(@RequestParam(required = false) String acao,
                         @RequestParam(defaultValue = "0") int id,
                         HttpServletRequest request,
                         Model model) throws SQLException {
        Usuario usuario = obterUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/login";
        }

        if ("novo".equals(acao)) {
            carregarGeneros(model);
            return "livro/form";
        }
        if ("editar".equals(acao)) {
            Livro livro = livroDAO.buscarPorIdEUsuario(id, usuario.getId());
            if (livro == null) {
                return "redirect:/home";
            }
            model.addAttribute("livro", livro);
            carregarGeneros(model);
            return "livro/form";
        }
        if ("detalhe".equals(acao)) {
            Livro livro = livroDAO.buscarPorIdEUsuario(id, usuario.getId());
            if (livro == null) {
                return "redirect:/home";
            }
            model.addAttribute("livro", livro);
            return "livro/detalhe";
        }

        return "redirect:/home";
    }

    @PostMapping("/livros")
    public String salvarLivro(HttpServletRequest request, Model model) throws SQLException {
        Usuario usuario = obterUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/login";
        }

        String acao = request.getParameter("acao");
        if ("excluir".equals(acao)) {
            livroDAO.excluir(parseInt(request.getParameter("id"), 0), usuario.getId());
            return "redirect:/home";
        }

        Livro livro = montarLivro(request, usuario);
        List<String> erros = validar(livro);
        if (!erros.isEmpty()) {
            model.addAttribute("erros", erros);
            model.addAttribute("livro", livro);
            carregarGeneros(model);
            return "livro/form";
        }

        if (livro.getId() > 0) {
            livroDAO.atualizar(livro);
        } else {
            livroDAO.inserir(livro);
        }

        return "redirect:/home";
    }

    private Livro montarLivro(HttpServletRequest request, Usuario usuario) {
        Genero genero = new Genero();
        genero.setId(parseInt(request.getParameter("generoId"), 0));

        Livro livro = new Livro();
        livro.setId(parseInt(request.getParameter("id"), 0));
        livro.setTitulo(texto(request, "titulo"));
        livro.setAutor(texto(request, "autor"));
        livro.setAno(parseInt(request.getParameter("ano"), 0));
        livro.setLido("true".equals(request.getParameter("lido")));
        livro.setResumo(texto(request, "resumo"));
        livro.setNota(livro.isLido() ? parseInteger(request.getParameter("nota")) : null);
        livro.setCapaUrl(texto(request, "capaUrl"));
        livro.setUsuario(usuario);
        livro.setGenero(genero);
        return livro;
    }

    private List<String> validar(Livro livro) {
        List<String> erros = new ArrayList<>();
        int anoAtual = Year.now().getValue();
        if (livro.getTitulo().length() < 2) {
            erros.add("Título deve ter pelo menos 2 caracteres.");
        }
        if (livro.getAutor().length() < 2) {
            erros.add("Autor deve ter pelo menos 2 caracteres.");
        }
        if (livro.getAno() < 1 || livro.getAno() > anoAtual) {
            erros.add("Ano deve estar entre 1 e " + anoAtual + ".");
        }
        if (livro.isLido()) {
            if (livro.getNota() == null || livro.getNota() < 0 || livro.getNota() > 5) {
                erros.add("Nota deve estar entre 0 e 5 para livros lidos.");
            }
        }
        if (livro.getGenero() == null || livro.getGenero().getId() <= 0) {
            erros.add("Selecione um gênero.");
        }
        return erros;
    }

    private void carregarGeneros(Model model) throws SQLException {
        model.addAttribute("generos", generoDAO.listar());
        model.addAttribute("anoAtual", Year.now().getValue());
    }

    private Usuario obterUsuarioLogado(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao == null || sessao.getAttribute("usuarioLogado") == null) {
            return null;
        }
        return (Usuario) sessao.getAttribute("usuarioLogado");
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

    private Integer parseInteger(String valor) {
        try {
            return valor == null || valor.isBlank() ? null : Integer.valueOf(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
