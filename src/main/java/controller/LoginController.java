package controller;

import dao.UsuarioDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import util.SenhaUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    private final UsuarioDAO usuarioDAO;

    public LoginController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @GetMapping({"/", "/login"})
    public String login(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute("usuarioLogado") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam(defaultValue = "entrar") String acao,
                                 HttpServletRequest request,
                                 Model model) throws SQLException {
        if ("cadastrar".equals(acao)) {
            return cadastrar(request, model);
        }
        return autenticar(request, model);
    }

    private String autenticar(HttpServletRequest request, Model model) throws SQLException {
        String usuario = texto(request, "usuario");
        String senha = texto(request, "senha");
        List<String> erros = new ArrayList<>();

        if (usuario.isBlank()) {
            erros.add("Informe o usuário.");
        }
        if (senha.isBlank()) {
            erros.add("Informe a senha.");
        }
        if (!erros.isEmpty()) {
            model.addAttribute("erros", erros);
            return "login";
        }

        Usuario usuarioLogado = usuarioDAO.autenticar(usuario, SenhaUtil.criptografar(senha));
        if (usuarioLogado == null) {
            erros.add("Usuário ou senha inválidos.");
            model.addAttribute("erros", erros);
            return "login";
        }

        request.getSession(true).setAttribute("usuarioLogado", usuarioLogado);
        return "redirect:/home";
    }

    private String cadastrar(HttpServletRequest request, Model model) throws SQLException {
        String nome = texto(request, "nomeCadastro");
        String usuario = texto(request, "usuarioCadastro");
        String senha = texto(request, "senhaCadastro");
        List<String> erros = new ArrayList<>();

        if (nome.length() < 3) {
            erros.add("Nome deve ter pelo menos 3 caracteres.");
        }
        if (usuario.length() < 3) {
            erros.add("Usuário deve ter pelo menos 3 caracteres.");
        }
        if (senha.length() < 6) {
            erros.add("Senha deve ter pelo menos 6 caracteres.");
        }
        if (erros.isEmpty() && usuarioDAO.usuarioExiste(usuario)) {
            erros.add("Usuário já cadastrado.");
        }
        if (!erros.isEmpty()) {
            model.addAttribute("erros", erros);
            return "login";
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setUsuario(usuario);
        novoUsuario.setSenha(SenhaUtil.criptografar(senha));
        usuarioDAO.inserir(novoUsuario);
        model.addAttribute("sucesso", "Cadastro realizado. Entre com seu usuário e senha.");
        return "login";
    }

    private String texto(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);
        return valor == null ? "" : valor.trim();
    }
}
