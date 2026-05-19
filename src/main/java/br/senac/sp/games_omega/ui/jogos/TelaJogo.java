package br.senac.sp.games_omega.ui.jogos;

import br.senac.sp.games_omega.data.repository.JogoRepository;
import br.senac.sp.games_omega.model.Jogo;
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

import java.time.LocalDate;

public class TelaJogo {

    // Instancia o repositório para salvar no banco SQLite
    private final JogoRepository jogoRepository = new JogoRepository();

    public void criarTela(Stage stagePai) {
        Stage stage = new Stage();
        stage.initOwner(stagePai);
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox raiz = new VBox();
        raiz.setStyle("-fx-background-color: #f4f4f4;");

        // 1. Cabeçalho Azul Escuro
        HBox painelTitulo = criarPainelTitulo();

        // 2. Painel do Formulário
        VBox painelFormulario = new VBox(15);
        painelFormulario.setPadding(new Insets(20));
        VBox.setMargin(painelFormulario, new Insets(15));
        painelFormulario.setStyle("-fx-background-color: #e9e9e9; -fx-border-color: #a2d1a2; -fx-border-width: 2;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);

        // --- CAMPOS DO FORMULÁRIO ---
        TextField txtId = new TextField();
        txtId.setDisable(true);
        txtId.setPrefWidth(80);
        txtId.setMaxWidth(80);

        TextField txtTitulo = new TextField();

        // Menu para Plataforma
        ComboBox<String> cbPlataforma = new ComboBox<>();
        cbPlataforma.setItems(FXCollections.observableArrayList(
                "PC", "PlayStation 5", "Xbox Series X", "Nintendo Switch", "Mobile"
        ));
        cbPlataforma.setPromptText("Selecione a plataforma...");
        cbPlataforma.setMaxWidth(Double.MAX_VALUE);

        // Menu para Estúdio
        ComboBox<String> cbEstudio = new ComboBox<>();
        cbEstudio.setItems(FXCollections.observableArrayList(
                "CD Projekt Red", "ConcernedApe", "FromSoftware", "Insomniac Games",
                "Nintendo", "Playground Games", "Rockstar Games", "Santa Monica Studio",
                "Supergiant Games", "Team Cherry"
        ));
        cbEstudio.setPromptText("Selecione o estúdio...");
        cbEstudio.setMaxWidth(Double.MAX_VALUE);

        // Menu Categoria
        ComboBox<String> cbCategoria = new ComboBox<>();
        cbCategoria.setItems(FXCollections.observableArrayList(
                "Ação/Aventura", "Metroidvania", "RPG", "Ação", "Corrida",
                "Simulação", "Ação/Mundo Aberto", "Roguelike"
        ));
        cbCategoria.setPromptText("Selecione a categoria...");
        cbCategoria.setMaxWidth(Double.MAX_VALUE);

        TextField txtPreco = new TextField();
        DatePicker dpLancamento = new DatePicker();
        dpLancamento.setPrefWidth(200);
        CheckBox cbFinalizado = new CheckBox();

        // Adicionando ao Grid
        grid.add(new Label("ID:"), 0, 0);
        grid.add(txtId, 1, 0);

        grid.add(new Label("Título:"), 0, 1);
        grid.add(txtTitulo, 1, 1);
        GridPane.setHgrow(txtTitulo, Priority.ALWAYS);

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

        // 3. Painel de Botões Inferiores
        HBox painelBotoes = new HBox(15);
        painelBotoes.setAlignment(Pos.CENTER_RIGHT);
        painelBotoes.setPadding(new Insets(0, 15, 15, 15));

        Button btnSalvar = criarBotaoIcone("/imagens/salvar.png");
        Button btnCancelar = criarBotaoIcone("/imagens/cancelar.png");

        // --- AÇÃO DO BOTÃO SALVAR (ATUALIZADA SEM FECHAR A TELA) ---
        btnSalvar.setOnAction(e -> {
            if (txtTitulo.getText().trim().isEmpty() || cbPlataforma.getValue() == null ||
                    cbCategoria.getValue() == null || cbEstudio.getValue() == null || txtPreco.getText().trim().isEmpty()) {

                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Campos Vazios");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, preencha todos os campos antes de salvar!");
                alerta.showAndWait();
                return;
            }

            try {
                String titulo = txtTitulo.getText().trim();
                String plataforma = cbPlataforma.getValue();
                String categoria = cbCategoria.getValue();
                String estudio = cbEstudio.getValue();
                double preco = Double.parseDouble(txtPreco.getText().trim().replace(",", "."));
                LocalDate dataLancamento = dpLancamento.getValue();
                boolean finalizado = cbFinalizado.isSelected();

                Jogo novoJogo = new Jogo(0, titulo, plataforma, categoria, estudio, preco, dataLancamento, finalizado);

                jogoRepository.salvar(novoJogo);

                // ALERTA INFORMATIVO DE SUCESSO
                Alert alertaSucesso = new Alert(Alert.AlertType.INFORMATION);
                alertaSucesso.setTitle("Sucesso!");
                alertaSucesso.setHeaderText(null);
                alertaSucesso.setContentText("Jogo '" + titulo + "' cadastrado com sucesso!");
                alertaSucesso.showAndWait();

                // LIMPA OS CAMPOS PARA PODER CADASTRAR OUTRO EM SEGUIDA
                txtTitulo.clear();
                cbPlataforma.setValue(null);
                cbCategoria.setValue(null);
                cbEstudio.setValue(null);
                txtPreco.clear();
                dpLancamento.setValue(null);
                cbFinalizado.setSelected(false);

                txtTitulo.requestFocus();

            } catch (NumberFormatException ex) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro no Preço");
                alerta.setHeaderText(null);
                alerta.setContentText("O preço digitado é inválido. Digite apenas números e pontos.");
                alerta.showAndWait();
            }
        });

        // AÇÃO DO BOTÃO CANCELAR
        btnCancelar.setOnAction(e -> stage.close());

        // ADICIONA OS BOTÕES AO PAINEL INFERIOR
        painelBotoes.getChildren().addAll(btnSalvar, btnCancelar);

        // 4. MONTAGEM FINAL DA JANELA (O QUE HAVIA SUMIDO)
        raiz.getChildren().addAll(painelTitulo, painelFormulario, painelBotoes);

        Scene cena = new Scene(raiz, 600, 550);
        stage.setScene(cena);

        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
        } catch (Exception ex) {
            System.err.println("Erro ao carregar ícone da aplicação");
        }

        stage.setTitle("Cadastro de Jogo");
        stage.setResizable(false);
        stage.showAndWait(); // Segura a execução aqui
    }

    private HBox criarPainelTitulo() {
        HBox painelTitulo = new HBox(15);
        painelTitulo.setAlignment(Pos.CENTER_LEFT);
        painelTitulo.setPadding(new Insets(15));
        painelTitulo.setStyle("-fx-background-color: #0d3b3f;");

        try {
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
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        return btn;
    }


}