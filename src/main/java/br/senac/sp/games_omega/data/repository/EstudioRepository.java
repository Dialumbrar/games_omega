package br.senac.sp.games_omega.data.repository;

import br.senac.sp.games_omega.data.ConexaoSQLite;
import br.senac.sp.games_omega.model.Estudio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class EstudioRepository {

    public ObservableList<Estudio> getEstudios() {
        ObservableList<Estudio> lista = FXCollections.observableArrayList();
        String sql = "SELECT id, nome, fundador, ano_fundacao, pais_origem FROM estudios";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Estudio(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("fundador"),
                        rs.getInt("ano_fundacao"),
                        rs.getString("pais_origem")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar estúdios: " + e.getMessage());
        }
        return lista;
    }

    public void salvar(Estudio e) {
        String sql = "INSERT INTO estudios (nome, fundador, ano_fundacao, pais_origem) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getNome());
            stmt.setString(2, e.getFundador());
            stmt.setInt(3, e.getAnoFundacao());
            stmt.setString(4, e.getPaisOrigem());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao salvar estúdio: " + ex.getMessage());
        }
    }

    public void atualizar(Estudio e) {
        String sql = "UPDATE estudios SET nome = ?, fundador = ?, ano_fundacao = ?, pais_origem = ? WHERE id = ?";
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getNome());
            stmt.setString(2, e.getFundador());
            stmt.setInt(3, e.getAnoFundacao());
            stmt.setString(4, e.getPaisOrigem());
            stmt.setInt(5, e.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao atualizar estúdio: " + ex.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM estudios WHERE id = ?";
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao excluir estúdio: " + ex.getMessage());
        }
    }
}