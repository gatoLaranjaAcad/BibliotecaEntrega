<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${livro.titulo} - Biblioteca Virtual</title>
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

<main class="container">
  <article class="detail-layout">
    <div class="cover">
      <c:choose>
        <c:when test="${empty livro.capaUrl}">
          <div class="cover-placeholder">${livro.titulo}</div>
        </c:when>
        <c:otherwise>
          <img src="${livro.capaUrl}" alt="Capa de ${livro.titulo}">
        </c:otherwise>
      </c:choose>
    </div>
    <section class="detail-content">
      <span class="eyebrow">${livro.genero.nome}</span>
      <h1>${livro.titulo}</h1>
      <p class="muted">${livro.autor} - ${livro.ano}</p>
      <p><strong>Nota:</strong> ${livro.nota}/5</p>
      <p><strong>Status:</strong> ${livro.lido ? 'Lido' : 'Não lido'}</p>
      <h2>Resumo</h2>
      <p>${empty livro.resumo ? 'Sem resumo cadastrado.' : livro.resumo}</p>
      <div class="form-actions">
        <a class="btn btn-ghost" href="${pageContext.request.contextPath}/home">Voltar</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/livros?acao=editar&id=${livro.id}">Editar</a>
      </div>
    </section>
  </article>
</main>
</body>
</html>
