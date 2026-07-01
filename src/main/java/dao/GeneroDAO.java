package dao;

import model.Genero;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GeneroDAO {
    private final DataSource dataSource;

    public GeneroDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Genero> listar() throws SQLException {
        List<Genero> generos = new ArrayList<>();
        String sql = "select id, nome from genero order by nome";
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                generos.add(mapear(rs));
            }
        }
        return generos;
    }

    public Genero buscarPorId(int id) throws SQLException {
        String sql = "select id, nome from genero where id = ?";
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public boolean existePorNome(String nome, int idIgnorado) throws SQLException {
        String sql = "select 1 from genero where lower(nome) = lower(?) and id <> ?";
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setInt(2, idIgnorado);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void inserir(Genero genero) throws SQLException {
        String sql = "insert into genero (nome) values (?)";
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, genero.getNome());
            ps.executeUpdate();
        }
    }

    public void atualizar(Genero genero) throws SQLException {
        String sql = "update genero set nome = ? where id = ?";
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, genero.getNome());
            ps.setInt(2, genero.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "delete from genero where id = ?";
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Genero mapear(ResultSet rs) throws SQLException {
        Genero genero = new Genero();
        genero.setId(rs.getInt("id"));
        genero.setNome(rs.getString("nome"));
        return genero;
    }
}
