package dao;

import model.Genero;
import model.Livro;
import model.Usuario;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LivroDAO {
    private final DataSource dataSource;

    public LivroDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Livro> listarPorUsuario(int usuarioId) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = """
                select l.id, l.titulo, l.autor, l.ano, l.lido, l.resumo, l.nota, l.capa_url,
                       g.id as genero_id, g.nome as genero_nome
                  from livro l
                  join genero g on g.id = l.genero_id
                 where l.usuario_id = ?
                 order by l.titulo
                """;
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    livros.add(mapear(rs, usuarioId));
                }
            }
        }
        return livros;
    }

    public List<Livro> listarPorUsuarioEBusca(int usuarioId, String busca) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = """
                select l.id, l.titulo, l.autor, l.ano, l.lido, l.resumo, l.nota, l.capa_url,
                       g.id as genero_id, g.nome as genero_nome
                  from livro l
                  join genero g on g.id = l.genero_id
                 where l.usuario_id = ?
                   and (l.titulo ILIKE ? OR l.autor ILIKE ?)
                 order by l.titulo
                """;
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, "%" + busca + "%");
            ps.setString(3, "%" + busca + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    livros.add(mapear(rs, usuarioId));
                }
            }
        }
        return livros;
    }

    public Livro buscarPorIdEUsuario(int id, int usuarioId) throws SQLException {
        String sql = """
                select l.id, l.titulo, l.autor, l.ano, l.lido, l.resumo, l.nota, l.capa_url,
                       g.id as genero_id, g.nome as genero_nome
                  from livro l
                  join genero g on g.id = l.genero_id
                 where l.id = ? and l.usuario_id = ?
                """;
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs, usuarioId);
                }
            }
        }
        return null;
    }

    public void inserir(Livro livro) throws SQLException {
        String sql = """
                insert into livro (titulo, autor, ano, lido, resumo, nota, capa_url, usuario_id, genero_id)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            preencherStatement(ps, livro);
            ps.executeUpdate();
        }
    }

    public void atualizar(Livro livro) throws SQLException {
        String sql = """
                update livro
                   set titulo = ?, autor = ?, ano = ?, lido = ?, resumo = ?, nota = ?, capa_url = ?, usuario_id = ?, genero_id = ?
                 where id = ? and usuario_id = ?
                """;
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            preencherStatement(ps, livro);
            ps.setInt(10, livro.getId());
            ps.setInt(11, livro.getUsuario().getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id, int usuarioId) throws SQLException {
        String sql = "delete from livro where id = ? and usuario_id = ?";
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        }
    }

    public Map<String, Number> estatisticasPorUsuario(int usuarioId) throws SQLException {
        Map<String, Number> estatisticas = new HashMap<>();
        String sql = """
                select count(*) as total,
                       coalesce(sum(case when lido then 1 else 0 end), 0) as lidos,
                       coalesce(sum(case when not lido then 1 else 0 end), 0) as nao_lidos,
                       coalesce(avg(case when lido then nota else null end), 0) as media_notas
                  from livro
                 where usuario_id = ?
                """;
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estatisticas.put("total", rs.getInt("total"));
                    estatisticas.put("lidos", rs.getInt("lidos"));
                    estatisticas.put("naoLidos", rs.getInt("nao_lidos"));
                    estatisticas.put("mediaNotas", rs.getDouble("media_notas"));
                }
            }
        }
        return estatisticas;
    }

    private void preencherStatement(PreparedStatement ps, Livro livro) throws SQLException {
        ps.setString(1, livro.getTitulo());
        ps.setString(2, livro.getAutor());
        ps.setInt(3, livro.getAno());
        ps.setBoolean(4, livro.isLido());
        ps.setString(5, livro.getResumo());
        if (livro.getNota() != null) {
            ps.setInt(6, livro.getNota());
        } else {
            ps.setNull(6, java.sql.Types.INTEGER);
        }
        ps.setString(7, livro.getCapaUrl());
        ps.setInt(8, livro.getUsuario().getId());
        ps.setInt(9, livro.getGenero().getId());
    }

    private Livro mapear(ResultSet rs, int usuarioId) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Genero genero = new Genero();
        genero.setId(rs.getInt("genero_id"));
        genero.setNome(rs.getString("genero_nome"));

        Livro livro = new Livro();
        livro.setId(rs.getInt("id"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setAutor(rs.getString("autor"));
        livro.setAno(rs.getInt("ano"));
        livro.setLido(rs.getBoolean("lido"));
        livro.setResumo(rs.getString("resumo"));
        int nota = rs.getInt("nota");
        livro.setNota(rs.wasNull() ? null : nota);
        livro.setCapaUrl(rs.getString("capa_url"));
        livro.setUsuario(usuario);
        livro.setGenero(genero);
        return livro;
    }
}
