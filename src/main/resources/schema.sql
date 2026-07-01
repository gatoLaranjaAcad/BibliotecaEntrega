CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS genero (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS livro (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    ano INT NOT NULL,
    lido BOOLEAN NOT NULL DEFAULT FALSE,
    resumo TEXT,
    nota INT,
    capa_url VARCHAR(255),
    usuario_id INT NOT NULL,
    genero_id INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (genero_id) REFERENCES genero(id) ON DELETE CASCADE
);

-- Inserir gêneros iniciais se não existirem
INSERT INTO genero (nome) VALUES ('Ficção Científica') ON CONFLICT (nome) DO NOTHING;
INSERT INTO genero (nome) VALUES ('Romance') ON CONFLICT (nome) DO NOTHING;
INSERT INTO genero (nome) VALUES ('Fantasia') ON CONFLICT (nome) DO NOTHING;
INSERT INTO genero (nome) VALUES ('Suspense / Mistério') ON CONFLICT (nome) DO NOTHING;
INSERT INTO genero (nome) VALUES ('Biografia') ON CONFLICT (nome) DO NOTHING;
INSERT INTO genero (nome) VALUES ('Não-Ficção') ON CONFLICT (nome) DO NOTHING;

-- Migração caso a tabela já exista com a coluna antiga
ALTER TABLE livro ALTER COLUMN nota DROP NOT NULL;
ALTER TABLE livro ALTER COLUMN nota DROP DEFAULT;
