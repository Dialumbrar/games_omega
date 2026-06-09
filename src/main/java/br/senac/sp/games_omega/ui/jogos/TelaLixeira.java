package br.senac.sp.games_omega.ui.jogos;

import br.senac.sp.games_omega.data.repository.JogoRepository;
import br.senac.sp.games_omega.model.Jogo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TelaLixeira {

    private final JogoRepository repository = new JogoRepository();

    public void criarTela(Stage stagePai) {
        Stage stage = new Stage();
        stage.initOwner(stagePai);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Lixeira");

        // Fundo escuro integrado ao sistema
        VBox raiz = new VBox(10);
        raiz.setPadding(new Insets(20));
        raiz.setStyle("-fx-background-color: #252830;");

        Label lbTitulo = new Label("Itens excluídos");
        lbTitulo.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

        // Tabela restaurada para o padrão visual original do JavaFX
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

        // 4. Coluna Estúdio
        TableColumn<Jogo, String> colEstudio = new TableColumn<>("Estúdio");
        colEstudio.setCellValueFactory(new PropertyValueFactory<>("estudio"));

        // 5. Coluna Categoria
        TableColumn<Jogo, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // 6. Coluna Preço
        TableColumn<Jogo, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        // 7. Coluna Lançamento
        TableColumn<Jogo, LocalDate> colLancamento = new TableColumn<>("Lançamento");
        colLancamento.setCellValueFactory(new PropertyValueFactory<>("dataLancamento"));

        // Adiciona as colunas originais na tabela
        tabelaLixeira.getColumns().addAll(colId, colTitulo, colPlataforma, colEstudio, colCategoria, colPreco, colLancamento);

        // Liga os dados do repositório à tabela
        tabelaLixeira.setItems(repository.getJogosExcluidos());

        // --- BOTÃO RESTAURAR ---
        Button btnRestaurar = new Button("Restaurar");
        try {
            // Carrega e define o ícone de restauração
            Image imgRestaurar = new Image(getClass().getResourceAsStream("/imagens/restaurar.png"));
            ImageView ivRestaurar = new ImageView(imgRestaurar);
            ivRestaurar.setFitHeight(16);
            ivRestaurar.setFitWidth(16);
            btnRestaurar.setGraphic(ivRestaurar);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone do botão restaurar: " + e.getMessage());
        }

        // Estilo base do botão Restaurar (Tema Escuro Flat)
        btnRestaurar.setStyle(
                "-fx-cursor: hand; " +
                        "-fx-background-color: #333742; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 6 12 6 12;"
        );

        // Interação Hover do botão Restaurar
        btnRestaurar.setOnMouseEntered(e -> btnRestaurar.setStyle(
                "-fx-cursor: hand; -fx-background-color: #434857; -fx-text-fill: #E5A93C; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 12 6 12;"
        ));
        btnRestaurar.setOnMouseExited(e -> btnRestaurar.setStyle(
                "-fx-cursor: hand; -fx-background-color: #333742; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 12 6 12;"
        ));

        // Lógica de ação para o botão Restaurar
        btnRestaurar.setOnAction(e -> {
            Jogo selecionado = tabelaLixeira.getSelectionModel().getSelectedItem();

            if (selecionado == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Nenhum jogo selecionado");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, selecione um jogo na tabela para poder restaurar!");

                Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
                try { alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch (Exception ex) {}
                try {
                    ImageView imagemCustomizada = new ImageView(new Image(getClass().getResourceAsStream("/imagens/atenção.png")));
                    imagemCustomizada.setFitWidth(35); imagemCustomizada.setFitHeight(35);
                    alerta.setGraphic(imagemCustomizada);
                } catch (Exception ex) {}

                alerta.showAndWait();
                return;
            }

            // Executa a restauração e atualiza os itens na tela
            repository.restaurar(selecionado.getId());
            tabelaLixeira.setItems(repository.getJogosExcluidos());
        });


        // --- BOTÃO SAIR LIXEIRA ---
        Button btnFechar = new Button("Sair");
        try {
            // Carrega e define o ícone de saída (exit.png)
            Image imgExit = new Image(getClass().getResourceAsStream("/imagens/exit.png"));
            ImageView ivExit = new ImageView(imgExit);
            ivExit.setFitHeight(16);
            ivExit.setFitWidth(16);
            btnFechar.setGraphic(ivExit);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone do botão fechar: " + e.getMessage());
        }

        // Estilo base do botão Fechar
        btnFechar.setStyle(
                "-fx-cursor: hand; " +
                        "-fx-background-color: #333742; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 6 12 6 12;"
        );

        // Interação Hover do botão Fechar
        btnFechar.setOnMouseEntered(e -> btnFechar.setStyle(
                "-fx-cursor: hand; -fx-background-color: #434857; -fx-text-fill: #E5A93C; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 12 6 12;"
        ));
        btnFechar.setOnMouseExited(e -> btnFechar.setStyle(
                "-fx-cursor: hand; -fx-background-color: #333742; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 12 6 12;"
        ));

        btnFechar.setOnAction(e -> stage.close());


        // --- ALINHAMENTO DOS BOTÕES NO LAYOUT ---
        HBox painelBotoes = new HBox(15);
        painelBotoes.setAlignment(Pos.CENTER_RIGHT);
        VBox.setMargin(painelBotoes, new Insets(5, 0, 0, 0));
        painelBotoes.getChildren().addAll(btnRestaurar, btnFechar);

        raiz.getChildren().addAll(lbTitulo, tabelaLixeira, painelBotoes);

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