package br.senac.sp.games_omega.data.repository;

import br.senac.sp.games_omega.data.ConexaoSQLite;
import br.senac.sp.games_omega.model.Plataforma;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class PlataformaRepository {

    public ObservableList<Plataforma> getPlataformas() {
        ObservableList<Plataforma> lista = FXCollections.observableArrayList();
        String sql = "SELECT id, nome, fabricante, data_lancamento, valor FROM plataformas";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String dataStr = rs.getString("data_lancamento");
                LocalDate data = (dataStr != null) ? LocalDate.parse(dataStr) : null;

                lista.add(new Plataforma(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("fabricante"),
                        data,
                        rs.getDouble("valor")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar plataformas: " + e.getMessage());
        }
        return lista;
    }

    public void salvar(Plataforma p) {
        String sql = "INSERT INTO plataformas (nome, fabricante, data_lancamento, valor) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getFabricante());
            stmt.setString(3, p.getDataLancamento() != null ? p.getDataLancamento().toString() : null);
            stmt.setDouble(4, p.getValor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao salvar plataforma: " + e.getMessage());
        }
    }

    public void atualizar(Plataforma p) {
        String sql = "UPDATE plataformas SET nome = ?, fabricante = ?, data_lancamento = ?, valor = ? WHERE id = ?";
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getFabricante());
            stmt.setString(3, p.getDataLancamento() != null ? p.getDataLancamento().toString() : null);
            stmt.setDouble(4, p.getValor());
            stmt.setInt(5, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar plataforma: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM plataformas WHERE id = ?";
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao excluir plataforma: " + e.getMessage());
        }
    }
}