package org.example;

import org.example.dao.ProdutoDAO;
import org.example.model.Produto;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ProdutoDAO produtoDAO = new ProdutoDAO();

        try {
            produtoDAO.conectar();

            // Cadastro dos produtos
            produtoDAO.cadastrarProdutos();

            // Buscar todos os produtos
            List<Produto> produtos = produtoDAO.buscarTodos();
            System.out.println("Todos os produtos:");
            for (Produto produto : produtos) {
                System.out.println(produto.getId() + " - " + produto.getNome() + " - " + produto.getPreco());
            }

            // Busca de produtos de uma categoria específica
            String categoria = "Categoria 1";
            List<Produto> produtosCategoria = produtoDAO.buscarPorCategoria(categoria);
            System.out.println("\nProdutos da categoria " + categoria + ":");
            for (Produto produto : produtosCategoria) {
                System.out.println(produto.getId() + " - " + produto.getNome() + " - " + produto.getPreco());
            }

            // Faz uma busca pelo produto com o maior preço
            Produto produtoMaiorPreco = produtoDAO.buscarProdutoMaiorPreco();
            System.out.println("\nProduto com maior preço:");
            System.out.println(produtoMaiorPreco.getId() + " - " + produtoMaiorPreco.getNome() + " - " + produtoMaiorPreco.getPreco());

            // Buscar produto com data de vencimento mais próxima
            Produto produtoProximoVencimento = produtoDAO.buscarProdutoProximoVencimento();
            System.out.println("\nProduto com data de vencimento mais próxima:");
            System.out.println(produtoProximoVencimento.getId() + " - " + produtoProximoVencimento.getNome() + " - " + produtoProximoVencimento.getDataVencimento());

            // Atualiza o preço de um produto pelo nome
            String nomeProduto = "Produto 1";
            double novoPreco = 15.99;
            produtoDAO.atualizarPrecoPorNome(nomeProduto, novoPreco);

            // Remove um produto pelo nome
            String nomeProdutoRemover = "Produto 5";
            produtoDAO.removerProdutoPorNome(nomeProdutoRemover);

            // Desconectar do banco de dados
            produtoDAO.desconectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
