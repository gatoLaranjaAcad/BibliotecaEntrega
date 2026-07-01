<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Biblioteca Virtual</title>
  <link href="https://fonts.googleapis.com/css2?family=Lora:wght@400;500;600&family=Source+Sans+3:wght@400;500&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20260531-shelf">
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

<main class="container library-page">
  <div class="library-hero" style="display: flex; justify-content: space-between; align-items: flex-end; flex-wrap: wrap; gap: 16px; margin-bottom: 32px;">
    <div>
      <span class="eyebrow">Olá, ${sessionScope.usuarioLogado.nome}</span>
      <h1 style="margin-bottom: 0;">Minha biblioteca</h1>
    </div>
    <div style="display: flex; gap: 16px; align-items: center; flex-wrap: wrap;">
      <form method="get" action="${pageContext.request.contextPath}/home" style="margin: 0;">
        <div class="input-group" style="display: flex; gap: 8px;">
          <input type="text" name="q" value="${busca}" class="form-control" placeholder="Buscar por título ou autor..." style="width: 280px; margin: 0;">
          <button type="submit" class="btn btn-primary">Buscar</button>
          <c:if test="${not empty busca}">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-ghost">Limpar</a>
          </c:if>
        </div>
      </form>
      <a class="btn btn-primary" href="${pageContext.request.contextPath}/livros?acao=novo">Novo livro</a>
    </div>
  </div>

  <section class="stats-grid compact-stats">
    <article class="stat-card"><span>Total</span><strong>${estatisticas.total}</strong></article>
    <article class="stat-card"><span>Lidos</span><strong>${estatisticas.lidos}</strong></article>
    <article class="stat-card"><span>Não lidos</span><strong>${estatisticas.naoLidos}</strong></article>
    <article class="stat-card"><span>Média</span><strong><fmt:formatNumber value="${estatisticas.mediaNotas}" minFractionDigits="1" maxFractionDigits="1"/></strong></article>
  </section>

  <c:choose>
    <c:when test="${empty livros}">
      <section class="empty-library">
        <p>Nenhum livro cadastrado.</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/livros?acao=novo">Adicionar primeiro livro</a>
      </section>
    </c:when>
    <c:otherwise>
      <section class="bookshelf">
        <article class="shelf">
          <div class="shelf-title">
            <h2>Lendo</h2>
            <span>${estatisticas.naoLidos} livro(s)</span>
          </div>
          <div class="books">
            <c:forEach var="livro" items="${livros}">
              <c:if test="${not livro.lido}">
                <article class="book-card">
                  <a class="book size-${livro.nota >= 4 ? '3' : livro.nota >= 2 ? '2' : '1'}" href="${pageContext.request.contextPath}/livros?acao=detalhe&id=${livro.id}" aria-label="Ver detalhes de ${livro.titulo}">
                    <c:choose>
                      <c:when test="${not empty livro.capaUrl}">
                        <img class="cover" src="${livro.capaUrl}" alt="Capa de ${livro.titulo}">
                      </c:when>
                      <c:otherwise>
                        <span class="generated-cover">
                          <strong>${livro.titulo}</strong>
                          <em>${livro.autor}</em>
                        </span>
                      </c:otherwise>
                    </c:choose>
                    <span class="info">
                      <span class="title">${livro.titulo}</span>
                      <span class="author">${livro.autor}</span>
                    </span>
                  </a>
                  <div class="book-actions">
                    <a href="${pageContext.request.contextPath}/livros?acao=editar&id=${livro.id}">Editar</a>
                    <form action="${pageContext.request.contextPath}/livros" method="post" onsubmit="return confirm('Excluir este livro?');">
                      <input type="hidden" name="acao" value="excluir">
                      <input type="hidden" name="id" value="${livro.id}">
                      <button type="submit" class="link-button danger">Excluir</button>
                    </form>
                  </div>
                </article>
              </c:if>
            </c:forEach>
          </div>
        </article>

        <article class="shelf">
          <div class="shelf-title">
            <h2>Lidos</h2>
            <span>${estatisticas.lidos} livro(s)</span>
          </div>
          <div class="books">
            <c:forEach var="livro" items="${livros}">
              <c:if test="${livro.lido}">
                <article class="book-card">
                  <a class="book size-${livro.nota >= 4 ? '3' : livro.nota >= 2 ? '2' : '1'}" href="${pageContext.request.contextPath}/livros?acao=detalhe&id=${livro.id}" aria-label="Ver detalhes de ${livro.titulo}">
                    <c:choose>
                      <c:when test="${not empty livro.capaUrl}">
                        <img class="cover" src="${livro.capaUrl}" alt="Capa de ${livro.titulo}">
                      </c:when>
                      <c:otherwise>
                        <span class="generated-cover">
                          <strong>${livro.titulo}</strong>
                          <em>${livro.autor}</em>
                        </span>
                      </c:otherwise>
                    </c:choose>
                    <span class="info">
                      <span class="title">${livro.titulo}</span>
                      <span class="author">${livro.autor}</span>
                    </span>
                  </a>
                  <div class="book-actions">
                    <a href="${pageContext.request.contextPath}/livros?acao=editar&id=${livro.id}">Editar</a>
                    <form action="${pageContext.request.contextPath}/livros" method="post" onsubmit="return confirm('Excluir este livro?');">
                      <input type="hidden" name="acao" value="excluir">
                      <input type="hidden" name="id" value="${livro.id}">
                      <button type="submit" class="link-button danger">Excluir</button>
                    </form>
                  </div>
                </article>
              </c:if>
            </c:forEach>
          </div>
        </article>
      </section>
    </c:otherwise>
  </c:choose>
</main>
</body>
</html>
