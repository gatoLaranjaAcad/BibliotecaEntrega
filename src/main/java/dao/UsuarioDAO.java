package dao;

import model.Usuario;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UsuarioDAO {
    private final DataSource dataSource;

    public UsuarioDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Usuario autenticar(String usuario, String senhaCriptografada) throws SQLException {
        String sql = "select id, nome, usuario, senha from usuario where usuario = ? and senha = ?";
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, senhaCriptografada);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public boolean usuarioExiste(String usuario) throws SQLException {
        String sql = "select 1 from usuario where usuario = ?";
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void inserir(Usuario usuario) throws SQLException {
        String sql = "insert into usuario (nome, usuario, senha) values (?, ?, ?)";
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getSenha());
            ps.executeUpdate();
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setUsuario(rs.getString("usuario"));
        usuario.setSenha(rs.getString("senha"));
        return usuario;
    }
}
