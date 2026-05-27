package br.senac.sp.games_omega.ui.jogos;

import br.senac.sp.games_omega.data.repository.JogoRepository;
import br.senac.sp.games_omega.model.Jogo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TelaLixeira {

    private final JogoRepository repository = new JogoRepository();

    public void mostrarTela(Stage stagePai) {
        Stage stage = new Stage();
        stage.initOwner(stagePai);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Lixeira");

        VBox raiz = new VBox(10);
        raiz.setPadding(new Insets(20));
        raiz.setStyle("-fx-background-color: #2F3336;");

        Label lbTitulo = new Label("Itens excluídos");
        lbTitulo.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: #1563cd;");

        // Configuração da Tabela da Lixeira
        TableView<Jogo> tabelaLixeira = new TableView<>();
        tabelaLixeira.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabelaLixeira, Priority.ALWAYS);

        // 1. Coluna ID
        TableColumn<Jogo, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(50);

        // 2. Coluna Título
        TableColumn<Jogo, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTitulo.setPrefWidth(150);

        // 3. Coluna Plataforma
        TableColumn<Jogo, String> colPlataforma = new TableColumn<>("Plataforma");
        colPlataforma.setCellValueFactory(new PropertyValueFactory<>("plataforma"));

        // 4. Coluna Estúdio (ADICIONADA)
        TableColumn<Jogo, String> colEstudio = new TableColumn<>("Estúdio");
        colEstudio.setCellValueFactory(new PropertyValueFactory<>("estudio"));

        // 5. Coluna Categoria
        TableColumn<Jogo, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // 6. Coluna Preço (ADICIONADA)
        TableColumn<Jogo, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        // 7. Coluna Lançamento (ADICIONADA)
        TableColumn<Jogo, LocalDate> colLancamento = new TableColumn<>("Lançamento");
        colLancamento.setCellValueFactory(new PropertyValueFactory<>("dataLancamento"));

        // Adiciona todas as colunas na tabela de forma organizada
        tabelaLixeira.getColumns().addAll(colId, colTitulo, colPlataforma, colEstudio, colCategoria, colPreco, colLancamento);

        // Liga os dados do repositório à tabela
        tabelaLixeira.setItems(repository.getJogosExcluidos());

        // Botão para fechar a janela
        Button btnFechar = new Button("Fechar Lixeira");
        btnFechar.setStyle("-fx-cursor: hand; -fx-font-weight: bold;");
        btnFechar.setOnAction(e -> stage.close());

        VBox painelBotao = new VBox(btnFechar);
        painelBotao.setAlignment(Pos.CENTER_RIGHT);

        // Organização dos elementos no layout
        raiz.getChildren().addAll(lbTitulo, tabelaLixeira, painelBotao);

        // Largura da janela para acomodar todas as colunas sem amassar o texto
        Scene cena = new Scene(raiz, 800, 450);
        stage.setScene(cena);
        stage.setResizable(false);

        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
        } catch (Exception ex) {
            System.err.println("Não foi possível carregar o ícone da janela da lixeira.");
        }

        stage.showAndWait();
    }
}