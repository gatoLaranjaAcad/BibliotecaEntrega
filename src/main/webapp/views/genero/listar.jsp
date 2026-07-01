<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Gêneros - Biblioteca Virtual</title>
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
    <h1>Gêneros</h1>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/generos?acao=novo">Novo gênero</a>
  </div>

  <section class="table-section">
    <c:choose>
      <c:when test="${empty generos}">
        <div class="empty-state">
          <p>Nenhum gênero cadastrado.</p>
        </div>
      </c:when>
      <c:otherwise>
        <div class="table-wrap">
          <table>
            <thead>
            <tr>
              <th>Nome</th>
              <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="genero" items="${generos}">
              <tr>
                <td>${genero.nome}</td>
                <td class="actions">
                  <a href="${pageContext.request.contextPath}/generos?acao=editar&id=${genero.id}">Editar</a>
                  <form action="${pageContext.request.contextPath}/generos" method="post" onsubmit="return confirm('Excluir este gênero?');">
                    <input type="hidden" name="acao" value="excluir">
                    <input type="hidden" name="id" value="${genero.id}">
                    <button type="submit" class="link-button danger">Excluir</button>
                  </form>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </c:otherwise>
    </c:choose>
  </section>
</main>
</body>
</html>
