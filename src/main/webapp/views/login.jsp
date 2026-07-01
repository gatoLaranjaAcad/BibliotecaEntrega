<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Biblioteca Virtual - Login</title>
  <link href="https://fonts.googleapis.com/css2?family=Lora:wght@400;500;600&family=Source+Sans+3:wght@400;500&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20260531-visual">
</head>
<body class="login-page">
<main class="auth-shell">
  <section class="auth-panel">
    <div class="brand-block">
      <h1>Biblioteca Virtual</h1>
      <p>Organize suas leituras, notas e gêneros em um só lugar.</p>
    </div>

    <c:if test="${not empty erros}">
      <div class="alert alert-error">
        <c:forEach var="erro" items="${erros}">
          <p>${erro}</p>
        </c:forEach>
      </div>
    </c:if>

    <c:if test="${not empty sucesso}">
      <div class="alert alert-success">${sucesso}</div>
    </c:if>

    <form id="loginForm" class="form-card" action="${pageContext.request.contextPath}/login" method="post" novalidate>
      <input type="hidden" name="acao" value="entrar">
      <label>
        Usuário
        <input type="text" name="usuario" required minlength="3" autocomplete="username">
      </label>
      <label>
        Senha
        <input type="password" name="senha" required autocomplete="current-password">
      </label>
      <button type="submit" class="btn btn-primary">Entrar</button>
      <button type="button" class="btn btn-ghost" id="abrirCadastro">Criar conta</button>
    </form>
  </section>
</main>

<div class="modal-backdrop" id="cadastroModal" aria-hidden="true">
  <section class="modal" role="dialog" aria-modal="true" aria-labelledby="cadastroTitulo">
    <div class="modal-header">
      <h2 id="cadastroTitulo">Cadastro</h2>
      <button type="button" class="icon-btn" id="fecharCadastro" aria-label="Fechar">x</button>
    </div>
    <form id="cadastroForm" action="${pageContext.request.contextPath}/login" method="post" novalidate>
      <input type="hidden" name="acao" value="cadastrar">
      <label>
        Nome
        <input type="text" name="nomeCadastro" required minlength="3">
      </label>
      <label>
        Usuário
        <input type="text" name="usuarioCadastro" required minlength="3" autocomplete="username">
      </label>
      <label>
        Senha
        <input type="password" name="senhaCadastro" required minlength="6" autocomplete="new-password">
      </label>
      <div class="modal-actions">
        <button type="button" class="btn btn-ghost" id="cancelarCadastro">Cancelar</button>
        <button type="submit" class="btn btn-primary">Cadastrar</button>
      </div>
    </form>
  </section>
</div>

<script>
  const modal = document.getElementById('cadastroModal');
  const abrir = document.getElementById('abrirCadastro');
  const fechar = document.getElementById('fecharCadastro');
  const cancelar = document.getElementById('cancelarCadastro');

  function abrirModal() {
    modal.classList.add('is-open');
    modal.setAttribute('aria-hidden', 'false');
  }

  function fecharModal() {
    modal.classList.remove('is-open');
    modal.setAttribute('aria-hidden', 'true');
  }

  abrir.addEventListener('click', abrirModal);
  fechar.addEventListener('click', fecharModal);
  cancelar.addEventListener('click', fecharModal);
  modal.addEventListener('click', (event) => {
    if (event.target === modal) {
      fecharModal();
    }
  });

  document.querySelectorAll('form[novalidate]').forEach((form) => {
    form.addEventListener('submit', (event) => {
      if (!form.checkValidity()) {
        event.preventDefault();
        form.classList.add('was-validated');
        form.reportValidity();
      }
    });
  });
</script>
</body>
</html>
