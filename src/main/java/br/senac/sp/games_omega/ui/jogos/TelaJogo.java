package br.senac.sp.games_omega.ui.jogos;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TelaJogo {

    public void criarTela(Stage stagePai) {
        Stage stage = new Stage();
        stage.initOwner(stagePai);
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox raiz = new VBox();
        raiz.setStyle("-fx-background-color: #f4f4f4;");

        // 1. Cabeçalho Azul Escuro
        HBox painelTitulo = criarPainelTitulo();

        // 2. Painel do Formulário (O "quadrado" cinza com borda verde)
        VBox painelFormulario = new VBox(15);
        painelFormulario.setPadding(new Insets(20));
        VBox.setMargin(painelFormulario, new Insets(15));
        // Estilo conforme a imagem: Fundo cinza claro e borda verde
        painelFormulario.setStyle("-fx-background-color: #e9e9e9; -fx-border-color: #a2d1a2; -fx-border-width: 2;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);

        // --- CAMPOS DO FORMULÁRIO ---
        TextField txtId = new TextField();
        txtId.setDisable(true); // ID geralmente é automático
        txtId.setPrefWidth(80);
        txtId.setMaxWidth(80);

        TextField txtTitulo = new TextField();

        // Menu para Plataforma
        ComboBox<String> cbPlataforma = new ComboBox<>();
        cbPlataforma.setItems(FXCollections.observableArrayList(
                "PC", "PlayStation 5", "Xbox Series X", "Nintendo Switch", "Mobile"
        ));
        cbPlataforma.setPromptText("Selecione a plataforma...");
        cbPlataforma.setMaxWidth(Double.MAX_VALUE); // Faz ocupar a largura disponível

        // Menu para Estúdio
        ComboBox<String> cbEstudio = new ComboBox<>();
        cbEstudio.setItems(FXCollections.observableArrayList(
                "Nintendo", "Santa Monica Studio", "Team Cherry", "Ubisoft", "Rockstar Games", "EA Sports"
        ));
        cbEstudio.setPromptText("Selecione o estúdio...");
        cbEstudio.setMaxWidth(Double.MAX_VALUE); // Faz ocupar a largura disponível

        // Menu Categoria
        ComboBox<String> cbCategoria = new ComboBox<>();
        cbCategoria.setItems(FXCollections.observableArrayList(
                "Ação", "Aventura", "RPG", "Estratégia", "Esportes", "Terror", "Metroidvania"
        ));
        cbCategoria.setPromptText("Selecione a categoria...");
        cbCategoria.setMaxWidth(Double.MAX_VALUE); // Faz ocupar a largura disponível

        TextField txtPreco = new TextField();
        DatePicker dpLancamento = new DatePicker();
        dpLancamento.setPrefWidth(200);
        CheckBox cbFinalizado = new CheckBox();

        // Adicionando ao Grid (Rótulo, Coluna, Linha)
        grid.add(new Label("ID:"), 0, 0);
        grid.add(txtId, 1, 0);

        grid.add(new Label("Título:"), 0, 1);
        grid.add(txtTitulo, 1, 1);
        GridPane.setHgrow(txtTitulo, Priority.ALWAYS); // Faz o campo esticar

        grid.add(new Label("Plataforma:"), 0, 2);
        grid.add(cbPlataforma, 1, 2);

        grid.add(new Label("Estúdio:"), 0, 3);
        grid.add(cbEstudio, 1, 3);

        grid.add(new Label("Categoria:"), 0, 4);
        grid.add(cbCategoria, 1, 4);

        grid.add(new Label("Preço:"), 0, 5);
        grid.add(txtPreco, 1, 5);

        grid.add(new Label("Lançamento:"), 0, 6);
        grid.add(dpLancamento, 1, 6);

        grid.add(new Label("Finalizado?:"), 0, 7);
        grid.add(cbFinalizado, 1, 7);

        painelFormulario.getChildren().add(grid);

        // 3. Painel de Botões Inferiores (Salvar e Cancelar)
        HBox painelBotoes = new HBox(15);
        painelBotoes.setAlignment(Pos.CENTER_RIGHT);
        painelBotoes.setPadding(new Insets(0, 15, 15, 15));

        Button btnSalvar = criarBotaoIcone("/imagens/salvar.png");
        Button btnCancelar = criarBotaoIcone("/imagens/cancelar.png");

        btnCancelar.setOnAction(e -> stage.close());

        painelBotoes.getChildren().addAll(btnSalvar, btnCancelar);

        // Montagem da estrutura
        raiz.getChildren().addAll(painelTitulo, painelFormulario, painelBotoes);

        Scene cena = new Scene(raiz, 600, 550);
        stage.setScene(cena);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
        stage.setTitle("Cadastro de Jogo");
        stage.setResizable(false);
        stage.showAndWait();

    }

    private HBox criarPainelTitulo() {
        HBox painelTitulo = new HBox(15);
        painelTitulo.setAlignment(Pos.CENTER_LEFT);
        painelTitulo.setPadding(new Insets(15));
        painelTitulo.setStyle("-fx-background-color: #0d3b3f;");

        try {
            // Reutilizando o ícone de adicionar para o título
            Image image = new Image(getClass().getResourceAsStream("/imagens/adicionar.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            painelTitulo.getChildren().add(imageView);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone do título");
        }

        Label lbTitulo = new Label("Cadastro de Jogo");
        lbTitulo.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: white;");
        painelTitulo.getChildren().add(lbTitulo);

        return painelTitulo;
    }

    private Button criarBotaoIcone(String url) {
        Button btn = new Button();
        try {
            Image img = new Image(getClass().getResourceAsStream(url));
            ImageView iv = new ImageView(img);
            iv.setFitHeight(35);
            iv.setFitWidth(35);
            btn.setGraphic(iv);
        } catch (Exception e) {
            btn.setText("Ação");
        }
        // Remove o fundo do botão para aparecer apenas o ícone, como na imagem
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        return btn;
    }
}