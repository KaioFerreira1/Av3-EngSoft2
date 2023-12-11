package org.example.dao;

import org.example.model.Produto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection conexao;

    public void conectar() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/EngSoft";
        String usuario = "postgres";
        String senha = "root";

        conexao = DriverManager.getConnection(url, usuario, senha);

        // Criação da tabela produtos se ela não existir
        try (Statement stmt = conexao.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS produtos (" +
                    "id SERIAL PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL," +
                    "preco NUMERIC(10, 2) NOT NULL," +
                    "categoria VARCHAR(50) NOT NULL," +
                    "data_vencimento DATE NOT NULL" +
                    ")";
            stmt.executeUpdate(sql);
        }
    }

    public void desconectar() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }

    public void cadastrarProdutos() throws SQLException {
        Produto produto1 = new Produto(1, "Produto 1", 10.50, "Categoria 1", LocalDate.now().plusDays(10));
        Produto produto2 = new Produto(2, "Produto 2", 15.75, "Categoria 2", LocalDate.now().plusDays(15));
        Produto produto3 = new Produto(3, "Produto 3", 20.00, "Categoria 1", LocalDate.now().plusDays(20));
        Produto produto4 = new Produto(4, "Produto 4", 8.99, "Categoria 3", LocalDate.now().plusDays(5));
        Produto produto5 = new Produto(5, "Produto 5", 12.30, "Categoria 4", LocalDate.now().plusDays(25));

        inserirProduto(produto1);
        inserirProduto(produto2);
        inserirProduto(produto3);
        inserirProduto(produto4);
        inserirProduto(produto5);
    }

    public void inserirProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, preco, categoria, data_vencimento) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getCategoria());
            stmt.setObject(4, produto.getDataVencimento());

            stmt.executeUpdate();
        }
    }

    public List<Produto> buscarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("categoria"),
                        rs.getObject("data_vencimento", LocalDate.class)
                );
                produtos.add(produto);
            }
        }

        return produtos;
    }

    public List<Produto> buscarPorCategoria(String categoria) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE categoria = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("categoria"),
                        rs.getObject("data_vencimento", LocalDate.class)
                );
                produtos.add(produto);
            }
        }

        return produtos;
    }

    public Produto buscarProdutoMaiorPreco() throws SQLException {
        String sql = "SELECT * FROM produtos ORDER BY preco DESC LIMIT 1";
        Produto produto = null;

        try (Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("categoria"),
                        rs.getObject("data_vencimento", LocalDate.class)
                );
            }
        }

        return produto;
    }

    public Produto buscarProdutoProximoVencimento() throws SQLException {
        String sql = "SELECT * FROM produtos WHERE data_vencimento >= CURRENT_DATE ORDER BY data_vencimento ASC LIMIT 1";
        Produto produto = null;

        try (Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("categoria"),
                        rs.getObject("data_vencimento", LocalDate.class)
                );
            }
        }

        return produto;
    }

    public void atualizarPrecoPorNome(String nomeProduto, double novoPreco) throws SQLException {
        String sql = "UPDATE produtos SET preco = ? WHERE nome = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setDouble(1, novoPreco);
            stmt.setString(2, nomeProduto);

            stmt.executeUpdate();
        }
    }

    public void removerProdutoPorNome(String nomeProduto) throws SQLException {
        String sql = "DELETE FROM produtos WHERE nome = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nomeProduto);
            stmt.executeUpdate();
        }
    }
}
