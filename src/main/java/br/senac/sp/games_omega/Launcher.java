package br.senac.sp.games_omega;

import br.senac.sp.games_omega.data.ConexaoSQLite;
import br.senac.sp.games_omega.data.repository.JogoRepository;
import br.senac.sp.games_omega.model.Jogo;
import javafx.application.Application;
import java.sql.Connection;
import java.time.LocalDate;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("--- Iniciando testes de sistema ---");

        // 1. Testa a conexão com o banco SQLite antes de abrir a tela
        Connection conexao = ConexaoSQLite.getConexao();

        if (conexao != null) {
            System.out.println("=> [SUCESSO] Conexão com o banco estabelecida perfeitamente!");
            try {
                conexao.close(); // Fecha a conexão de teste de forma limpa
            } catch (Exception e) {
                System.err.println("Erro ao fechar conexão de teste: " + e.getMessage());
            }

            // 2. Se a conexão deu certo, inicia a aplicação JavaFX normalmente
            System.out.println("=> Abrindo a interface gráfica...");
            Application.launch(TelaPrincipal.class, args);

        } else {
            System.err.println("=> [FALHA] Não foi possível conectar ao banco de dados.");
            System.err.println("Verifique se o caminho do arquivo .db na classe ConexaoSQLite está correto.");
            System.err.println("A aplicação não foi iniciada para evitar erros em cascata.");
        }
    }
}