package br.senac.sp.games_omega.ui.jogos;

import br.senac.sp.games_omega.data.repository.JogoRepository;
import br.senac.sp.games_omega.model.Jogo;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class PainelJogos {

    public VBox criarPainelJogos() {
        VBox painelJogos = new VBox();
        painelJogos.setSpacing(10);
        painelJogos.setPadding(new Insets(20));
        painelJogos.setStyle("-fx-background-color: #2F3336;");

        Label lbTitulo = new Label("Listagem de Jogos");
        lbTitulo.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: #1563cd;");

        Separator linha = new Separator();

        // 1. Instância da Tabela
        TableView<Jogo> tabelaJogos = new TableView<>();
        tabelaJogos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabelaJogos, Priority.ALWAYS);

        // 2. Configuração de TODAS as colunas

        // Coluna ID
        TableColumn<Jogo, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(50);

        // Coluna Título
        TableColumn<Jogo, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        // Coluna Plataforma
        TableColumn<Jogo, String> colPlat = new TableColumn<>("Plataforma");
        colPlat.setCellValueFactory(new PropertyValueFactory<>("plataforma"));

        // Coluna Categoria
        TableColumn<Jogo, String> colCat = new TableColumn<>("Categoria");
        colCat.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // Coluna Estúdio
        TableColumn<Jogo, String> colEstudio = new TableColumn<>("Estúdio");
        colEstudio.setCellValueFactory(new PropertyValueFactory<>("estudio"));

        // Coluna Preço
        TableColumn<Jogo, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        // Coluna Lançamento
        TableColumn<Jogo, LocalDate> colData = new TableColumn<>("Lançamento");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataLancamento"));

        // 3. Adicionar colunas à tabela na ordem desejada
        tabelaJogos.getColumns().addAll(
                colId,
                colTitulo,
                colPlat,
                colCat,
                colEstudio,
                colPreco,
                colData
        );

        // 4. Carregar os dados do repositório
        JogoRepository repository = new JogoRepository();
        tabelaJogos.setItems(repository.getJogos());

        // Botões
        HBox painelBotoes = new HBox(10);
        Button btnAdicionar = criarBotao("Adicionar", "/imagens/adicionar.png");
        Button btnExcluir = criarBotao("Excluir", "/imagens/excluir.png");
        Button btnEditar = criarBotao("Editar", "/imagens/editar.png");
        Button btnLixeira = criarBotao("Lixeira", "/imagens/lixeira.png");

        // --- LÓGICA PARA ABRIR A TELA ---
        btnAdicionar.setOnAction(event -> {
            TelaJogo telaCadastro = new TelaJogo();
            // Captura a janela atual para ser a "mãe" da nova janela
            Stage stagePrincipal = (Stage) painelJogos.getScene().getWindow();
            telaCadastro.criarTela(stagePrincipal);
        });

        painelBotoes.getChildren().addAll(btnAdicionar, btnExcluir, btnEditar, btnLixeira);

        // Adicionar componentes ao painel
        painelJogos.getChildren().addAll(lbTitulo, linha, painelBotoes, tabelaJogos);

        return painelJogos;
    }

    private Button criarBotao(String texto, String urlImagem) {
        Button btn = new Button(texto);
        try {
            Image img = new Image(getClass().getResourceAsStream(urlImagem));
            ImageView iv = new ImageView(img);
            iv.setFitHeight(16);
            iv.setFitWidth(16);
            btn.setGraphic(iv);
        } catch (Exception e) {
            System.out.println("Erro ao carregar ícone: " + urlImagem);
        }
        btn.setStyle("-fx-cursor: hand;");
        return btn;
    }
}