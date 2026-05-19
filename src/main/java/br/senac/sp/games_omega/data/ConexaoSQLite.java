package br.senac.sp.games_omega.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSQLite {

    public static Connection getConexao(){

        // String de Conexão - url do banco de dados
        String url = "jdbc:sqlite:C:/Users/thiago.jsantos12/OneDrive - SENAC - SP/_Cursos/Tecnico Desenvolvimento de Sistemas/UC7/Banco de Dados/db_game_omega.db";

        try {
            Connection conexao = DriverManager.getConnection(url);
            return  conexao;
        } catch (SQLException erro) {
            System.out.println("Ocorreu um erro durante a conexão com o banco");
            erro.printStackTrace();
            return null;

        }
    }
}
