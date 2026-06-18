package br.senac.sp.games_omega.ui.jogos;

import br.senac.sp.games_omega.model.Jogo;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
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

public class TelaPesquisa {

    private TextField txtTitulo;
    private ComboBox<String> cbPlataforma;
    private ComboBox<String> cbCategoria;
    private FilteredList<Jogo> listaFiltrada = null;
    private boolean confirmouPesquisa = false;

    // Recebe a lista original para preparar os filtros
    public boolean exibirTela(Stage stagePai, FilteredList<Jogo> listaParaFiltrar) {
        this.listaFiltrada = listaParaFiltrar;

        Stage stage = new Stage();
        stage.initOwner(stagePai);
        stage.initModality(Modality.APPLICATION_MODAL);

        // 1. CONTAINER RAIZ (DARK THEME)
        VBox raiz = new VBox();
        raiz.setStyle("-fx-background-color: #252830;");

        // Cabeçalho idêntico ao padrão do sistema
        HBox painelTitulo = criarPainelTitulo();

        // 2. PAINEL DO FORMULÁRIO DE BUSCA
        VBox painelFormulario = new VBox(15);
        painelFormulario.setPadding(new Insets(20));
        VBox.setMargin(painelFormulario, new Insets(15));
        painelFormulario.setStyle(
                "-fx-background-color: #1E222B; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #333742; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8;"
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);

        // --- CAMPOS DE FILTRO ---
        txtTitulo = new TextField();
        txtTitulo.setPromptText("Digite parte do título...");
        txtTitulo.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");

        cbPlataforma = new ComboBox<>();
        cbPlataforma.setItems(FXCollections.observableArrayList(
                "TODAS", "PC", "PlayStation 5", "Xbox Series X", "Nintendo Switch", "Mobile"
        ));
        cbPlataforma.setValue("TODAS");
        cbPlataforma.setMaxWidth(Double.MAX_VALUE);

        cbCategoria = new ComboBox<>();
        cbCategoria.setItems(FXCollections.observableArrayList(
                "TODAS", "Ação/Aventura", "Metroidvania", "RPG", "Ação", "Corrida",
                "Simulação", "Ação/Mundo Aberto", "Roguelike"
        ));
        cbCategoria.setValue("TODAS");
        cbCategoria.setMaxWidth(Double.MAX_VALUE);

        // Adicionando elementos ao Grid
        grid.add(criarLabelFormulario("Título:"), 0, 0);
        grid.add(txtTitulo, 1, 0);
        GridPane.setHgrow(txtTitulo, Priority.ALWAYS);

        grid.add(criarLabelFormulario("Plataforma:"), 0, 1);
        grid.add(cbPlataforma, 1, 1);

        grid.add(criarLabelFormulario("Categoria:"), 0, 2);
        grid.add(cbCategoria, 1, 2);

        painelFormulario.getChildren().add(grid);

        // 3. PAINEL DE BOTÕES INFERIORES
        HBox painelBotoes = new HBox(15);
        painelBotoes.setAlignment(Pos.CENTER_RIGHT);
        painelBotoes.setPadding(new Insets(0, 15, 15, 15));

        Button btnFiltrar = criarBotaoFormulario("Filtrar", "/imagens/filter.png");
        Button btnLimpar = criarBotaoFormulario("Limpar", "/imagens/broom.png");
        Button btnFechar = criarBotaoFormulario("Sair", "/imagens/exit.png");

        // --- LÓGICA DE FILTRAGEM ---
        btnFiltrar.setOnAction(e -> {
            String termoTitulo = txtTitulo.getText().trim().toLowerCase();
            String plataformaSel = cbPlataforma.getValue();
            String categoriaSel = cbCategoria.getValue();

            // Aplica o Predicate combinando todos os campos preenchidos
            listaFiltrada.setPredicate(jogo -> {
                boolean bateTitulo = termoTitulo.isEmpty() || jogo.getTitulo().toLowerCase().contains(termoTitulo);
                boolean batePlat = plataformaSel.equals("TODAS") || jogo.getPlataforma().equals(plataformaSel);
                boolean bateCat = categoriaSel.equals("TODAS") || jogo.getCategoria().equals(categoriaSel);

                return bateTitulo && batePlat && bateCat;
            });

            confirmouPesquisa = true;
            stage.close();
        });

        // --- LÓGICA DO BOTÃO LIMPAR ---
        btnLimpar.setOnAction(e -> {
            // Reseta o filtro da lista ao fundo para exibir tudo novamente
            listaFiltrada.setPredicate(jogo -> true);

            // Limpa os componentes visuais da tela de pesquisa
            txtTitulo.clear();
            cbPlataforma.setValue("TODAS");
            cbCategoria.setValue("TODAS");

            // Informa que houve uma mudança na listagem
            confirmouPesquisa = true;

            // Devolve o foco para o campo de texto para facilitar uma nova digitação
            txtTitulo.requestFocus();
        });

        // --- LÓGICA DO BOTÃO SAIR ---
        btnFechar.setOnAction(e -> stage.close());

        painelBotoes.getChildren().addAll(btnFiltrar, btnLimpar, btnFechar);
        raiz.getChildren().addAll(painelTitulo, painelFormulario, painelBotoes);

        Scene cena = new Scene(raiz, 500, 320);
        stage.setScene(cena);

        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
        } catch (Exception ex) {}

        stage.setTitle("Pesquisa de Jogos");
        stage.setResizable(false);
        stage.showAndWait();

        return confirmouPesquisa;
    }

    private HBox criarPainelTitulo() {
        HBox painelTitulo = new HBox(15);
        painelTitulo.setAlignment(Pos.CENTER_LEFT);
        painelTitulo.setPadding(new Insets(15));
        painelTitulo.setStyle("-fx-background-color: #1E222B; -fx-border-color: #333742; -fx-border-width: 0 0 1 0;");

        try {
            Image image = new Image(getClass().getResourceAsStream("/imagens/pesquisar.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(28);
            imageView.setFitWidth(28);
            painelTitulo.getChildren().add(imageView);
        } catch (Exception e) {}

        Label lbTitulo = new Label("Pesquisar Jogos");
        lbTitulo.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        painelTitulo.getChildren().add(lbTitulo);

        return painelTitulo;
    }

    private Label criarLabelFormulario(String texto) {
        Label lb = new Label(texto);
        lb.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 13;");
        return lb;
    }

    private Button criarBotaoFormulario(String texto, String urlImagem) {
        Button btn = new Button(texto);
        try {
            Image img = new Image(getClass().getResourceAsStream(urlImagem));
            ImageView iv = new ImageView(img);
            iv.setFitHeight(16);
            iv.setFitWidth(16);
            btn.setGraphic(iv);
        } catch (Exception e) {}

        btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #333742; -fx-text-fill: #FFFFFF; " +
                        "-fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 16 6 16;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #434857; -fx-text-fill: #E5A93C; " +
                        "-fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 16 6 16;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #333742; -fx-text-fill: #FFFFFF; " +
                        "-fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 16 6 16;"
        ));

        return btn;
    }
}