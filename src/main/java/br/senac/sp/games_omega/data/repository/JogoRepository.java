package br.senac.sp.games_omega.data.repository;

import br.senac.sp.games_omega.data.ConexaoSQLite;
import br.senac.sp.games_omega.model.Jogo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class JogoRepository {

    // 1. METODO PARA LISTAR OS JOGOS NA TABELA (SELECT)
    public ObservableList<Jogo> getJogos() {
        ObservableList<Jogo> listaJogos = FXCollections.observableArrayList();

        // Query que junta as tabelas para trazer os NOMES em vez dos IDs numéricos
        String sql = "SELECT g.id, g.titulo, p.nome AS plataforma, c.nome AS categoria, " +
                "e.nome AS estudio, g.preco, g.data_lancamento, g.finalizado " +
                "FROM games g " +
                "INNER JOIN plataformas p ON g.id_plataforma = p.id " +
                "INNER JOIN categorias c ON g.id_categoria = c.id " +
                "INNER JOIN estudios e ON g.id_estudio = e.id";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String plataforma = rs.getString("plataforma");
                String categoria = rs.getString("categoria");
                String estudio = rs.getString("estudio");
                double preco = rs.getDouble("preco");

                String dataStr = rs.getString("data_lancamento");
                LocalDate dataLancamento = (dataStr != null) ? LocalDate.parse(dataStr) : null;

                // Mapeia o campo finalizado (1 = true, 0 = false)
                boolean finalizado = rs.getInt("finalizado") == 1;

                // Cria o objeto com os dados reais e adiciona na lista da View
                Jogo jogo = new Jogo(id, titulo, plataforma, categoria, estudio, preco, dataLancamento, finalizado);
                listaJogos.add(jogo);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar jogos no banco de dados:");
            e.printStackTrace();
        }

        return listaJogos;
    }

    // 2. METODO PARA SALVAR UM NOVO JOGO (INSERT)
    public void salvar(Jogo jogo) {
        String sql = "INSERT INTO games (titulo, id_plataforma, id_categoria, id_estudio, preco, data_lancamento, finalizado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Descobre dinamicamente os IDs baseados nos nomes que vieram do formulário
            int idPlataforma = buscarIdPorNome(conn, "plataformas", jogo.getPlataforma());
            int idCategoria = buscarIdPorNome(conn, "categorias", jogo.getCategoria());
            int idEstudio = buscarIdPorNome(conn, "estudios", jogo.getEstudio());

            stmt.setString(1, jogo.getTitulo());
            stmt.setInt(2, idPlataforma);
            stmt.setInt(3, idCategoria);
            stmt.setInt(4, idEstudio);
            stmt.setDouble(5, jogo.getPreco());

            if (jogo.getDataLancamento() != null) {
                stmt.setString(6, jogo.getDataLancamento().toString());
            } else {
                stmt.setString(6, null);
            }

            stmt.setInt(7, jogo.isFinalizado() ? 1 : 0);

            stmt.executeUpdate();
            System.out.println("Jogo '" + jogo.getTitulo() + "' salvo com sucesso no SQLite!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o jogo no banco de dados:");
            e.printStackTrace();
        }
    }

    // 3. METODO AUXILIAR PARA PEGAR O ID DA TABELA ESTRANGEIRA PELO NOME
    private int buscarIdPorNome(Connection conn, String tabela, String nome) throws SQLException {
        // Query genérica dinâmica para reaproveitar nas 3 tabelas
        String sql = "SELECT id FROM " + tabela + " WHERE nome = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        // Se por acaso digitar um nome que não existe nas tabelas, retorna 1 por segurança (ou lance uma exceção)
        return 1;
    }

    // 4. METODO PARA ATUALIZAR UM JOGO EXISTENTE (UPDATE)
    public void atualizar(Jogo jogo) {
        String sql = "UPDATE games SET titulo = ?, id_plataforma = ?, id_categoria = ?, " +
                "id_estudio = ?, preco = ?, data_lancamento = ?, finalizado = ? WHERE id = ?";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Descobre os IDs estrangeiros com base no texto selecionado
            int idPlataforma = buscarIdPorNome(conn, "plataformas", jogo.getPlataforma());
            int idCategoria = buscarIdPorNome(conn, "categorias", jogo.getCategoria());
            int idEstudio = buscarIdPorNome(conn, "estudios", jogo.getEstudio());

            stmt.setString(1, jogo.getTitulo());
            stmt.setInt(2, idPlataforma);
            stmt.setInt(3, idCategoria);
            stmt.setInt(4, idEstudio);
            stmt.setDouble(5, jogo.getPreco());

            stmt.setString(6, (jogo.getDataLancamento() != null) ? jogo.getDataLancamento().toString() : null);
            stmt.setInt(7, jogo.isFinalizado() ? 1 : 0);
            stmt.setInt(8, jogo.getId()); // Onde id bate com o jogo sendo editado

            stmt.executeUpdate();
            System.out.println("Jogo ID " + jogo.getId() + " atualizado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o jogo no banco de dados:");
            e.printStackTrace();
        }
    }


    // 5. Metodo para deletar um jogo do banco pelo ID
    public void excluir(int id) {
        // 1. Query para copiar os dados para a lixeira antes de deletar
        String sqlCopiar = "INSERT INTO games_lixeira (id, titulo, plataforma, categoria, estudio, preco, data_lancamento, finalizado, data_exclusao) "
                + "SELECT g.id, g.titulo, p.nome, c.nome, e.nome, g.preco, g.data_lancamento, g.finalizado, datetime('now', 'localtime') "
                + "FROM games g "
                + "INNER JOIN plataformas p ON g.id_plataforma = p.id "
                + "INNER JOIN categorias c ON g.id_categoria = c.id "
                + "INNER JOIN estudios e ON g.id_estudio = e.id "
                + "WHERE g.id = ?";

        // 2. Query de exclusão real
        String sqlDeletar = "DELETE FROM games WHERE id = ?";

        try (Connection conn = ConexaoSQLite.getConexao()) {
            conn.setAutoCommit(false); // Transação segura para garantir as duas operações

            try (PreparedStatement stmtCopiar = conn.prepareStatement(sqlCopiar);
                 PreparedStatement stmtDeletar = conn.prepareStatement(sqlDeletar)) {

                // Executa a cópia
                stmtCopiar.setInt(1, id);
                stmtCopiar.executeUpdate();

                // Executa a exclusão
                stmtDeletar.setInt(1, id);
                stmtDeletar.executeUpdate();

                conn.commit(); // Salva as alterações
                System.out.println("Jogo movido para o histórico da lixeira com sucesso!");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao mover jogo para a lixeira: " + e.getMessage());
        }
    }

    // 6. METODO PARA LIXEIRA
    public ObservableList<Jogo> getJogosExcluidos() {
        ObservableList<Jogo> listaExcluidos = FXCollections.observableArrayList();

        // O SELECT deve trazer TODOS os campos necessários para preencher o objeto Jogo
        String sql = "SELECT id, titulo, plataforma, categoria, estudio, preco, data_lancamento, finalizado FROM games_lixeira";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String dataStr = rs.getString("data_lancamento");
                LocalDate dataLancamento = (dataStr != null) ? LocalDate.parse(dataStr) : null;
                boolean finalizado = rs.getInt("finalizado") == 1;

                // Cria o objeto completo com todas as variáveis
                Jogo jogo = new Jogo(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("plataforma"),
                        rs.getString("categoria"),
                        rs.getString("estudio"),
                        rs.getDouble("preco"),
                        dataLancamento,
                        finalizado
                );
                listaExcluidos.add(jogo);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar histórico da lixeira: " + e.getMessage());
        }
        return listaExcluidos;
    }
}