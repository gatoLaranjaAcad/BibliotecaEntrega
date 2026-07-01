<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${empty genero.id || genero.id == 0 ? 'Novo gênero' : 'Editar gênero'} - Biblioteca Virtual</title>
  <link href="https://fonts.googleapis.com/css2?family=Lora:wght@400;500;600&family=Source+Sans+3:wght@400;500&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20260531-visual">
</head>
<body>
<header class="topbar">
  <a class="logo" href="${pageContext.request.contextPath}/home">Biblioteca Virtual</a>
  <nav>
    <a href="${pageContext.request.contextPath}/home">Livros</a>
    <a href="${pageContext.request.contextPath}/generos">Gêneros</a>
    <a href="${pageContext.request.contextPath}/logout">Sair</a>
  </nav>
</header>

<main class="container narrow">
  <div class="page-heading">
    <h1>${empty genero.id || genero.id == 0 ? 'Novo gênero' : 'Editar gênero'}</h1>
    <a class="btn btn-ghost" href="${pageContext.request.contextPath}/generos">Voltar</a>
  </div>

  <c:if test="${not empty erros}">
    <div class="alert alert-error">
      <c:forEach var="erro" items="${erros}">
        <p>${erro}</p>
      </c:forEach>
    </div>
  </c:if>

  <form id="generoForm" class="form-card" action="${pageContext.request.contextPath}/generos" method="post" novalidate>
    <input type="hidden" name="id" value="${genero.id}">
    <label>
      Nome
      <input type="text" name="nome" value="${genero.nome}" required minlength="2">
    </label>
    <div class="form-actions">
      <a class="btn btn-ghost" href="${pageContext.request.contextPath}/generos">Cancelar</a>
      <button type="submit" class="btn btn-primary">Salvar</button>
    </div>
  </form>
</main>

<script>
  const form = document.getElementById('generoForm');
  form.addEventListener('submit', (event) => {
    if (!form.checkValidity()) {
      event.preventDefault();
      form.classList.add('was-validated');
      form.reportValidity();
    }
  });
</script>
</body>
</html>
