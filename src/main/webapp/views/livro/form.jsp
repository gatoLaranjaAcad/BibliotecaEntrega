<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${empty livro.id || livro.id == 0 ? 'Novo livro' : 'Editar livro'} - Biblioteca Virtual</title>
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
    <h1>${empty livro.id || livro.id == 0 ? 'Novo livro' : 'Editar livro'}</h1>
    <a class="btn btn-ghost" href="${pageContext.request.contextPath}/home">Voltar</a>
  </div>

  <c:if test="${not empty erros}">
    <div class="alert alert-error">
      <c:forEach var="erro" items="${erros}">
        <p>${erro}</p>
      </c:forEach>
    </div>
  </c:if>

  <form id="livroForm" class="form-card" action="${pageContext.request.contextPath}/livros" method="post" novalidate>
    <input type="hidden" name="id" value="${livro.id}">
    <label>
      Título
      <input type="text" name="titulo" value="${livro.titulo}" required minlength="2">
    </label>
    <label>
      Autor
      <input type="text" name="autor" value="${livro.autor}" required minlength="2">
    </label>
    <div class="form-row">
      <label>
        Ano
        <input type="number" name="ano" value="${livro.ano == 0 ? '' : livro.ano}" required min="1" max="${anoAtual}">
      </label>
      <label id="labelNota">
        Nota
        <input type="number" name="nota" id="inputNota" value="${livro.nota}" required min="0" max="5">
      </label>
    </div>
    <label>
      Gênero
      <select name="generoId" required>
        <option value="">Selecione</option>
        <c:forEach var="genero" items="${generos}">
          <option value="${genero.id}" ${livro.genero.id == genero.id ? 'selected' : ''}>${genero.nome}</option>
        </c:forEach>
      </select>
    </label>
    <label>
      URL da capa
      <input type="url" name="capaUrl" value="${livro.capaUrl}" placeholder="https://exemplo.com/capa.jpg">
    </label>
    <label>
      Resumo
      <textarea name="resumo" rows="5">${livro.resumo}</textarea>
    </label>
    <label class="check-line">
      <input type="checkbox" name="lido" id="checkboxLido" value="true" ${livro.lido ? 'checked' : ''}>
      Livro lido
    </label>
    <div class="form-actions">
      <a class="btn btn-ghost" href="${pageContext.request.contextPath}/home">Cancelar</a>
      <button type="submit" class="btn btn-primary">Salvar</button>
    </div>
  </form>
</main>

<script>
  const form = document.getElementById('livroForm');
  const checkboxLido = document.getElementById('checkboxLido');
  const labelNota = document.getElementById('labelNota');
  const inputNota = document.getElementById('inputNota');

  function toggleNota() {
    if (checkboxLido.checked) {
      labelNota.style.display = 'grid';
      inputNota.setAttribute('required', 'required');
    } else {
      labelNota.style.display = 'none';
      inputNota.removeAttribute('required');
    }
  }

  checkboxLido.addEventListener('change', toggleNota);
  toggleNota();

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
