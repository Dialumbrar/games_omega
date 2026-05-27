package br.senac.sp.games_omega.ui.jogos;

import br.senac.sp.games_omega.data.repository.PlataformaRepository;
import br.senac.sp.games_omega.model.Plataforma;
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

public class PainelPlataforma {

    private final PlataformaRepository repository = new PlataformaRepository();

    public VBox criarPainelPlataformas() {
        VBox painelPlataformas = new VBox();
        painelPlataformas.setSpacing(10);
        painelPlataformas.setPadding(new Insets(20));
        painelPlataformas.setStyle("-fx-background-color: #2F3336;");

        Label lbTitulo = new Label("Gerenciamento de Plataformas");
        lbTitulo.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: #1563cd;");

        Separator linha = new Separator();

        TableView<Plataforma> tabelaPlataformas = new TableView<>();
        tabelaPlataformas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabelaPlataformas, Priority.ALWAYS);

        TableColumn<Plataforma, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(60);

        TableColumn<Plataforma, String> colNome = new TableColumn<>("Plataforma");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Plataforma, String> colFabricante = new TableColumn<>("Fabricante");
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));

        TableColumn<Plataforma, Double> colValor = new TableColumn<>("Preço Hardware");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        TableColumn<Plataforma, LocalDate> colLancamento = new TableColumn<>("Lançamento");
        colLancamento.setCellValueFactory(new PropertyValueFactory<>("dataLancamento"));

        tabelaPlataformas.getColumns().addAll(colId, colNome, colFabricante, colValor, colLancamento);
        tabelaPlataformas.setItems(repository.getPlataformas());

        // Botões
        HBox painelBotoes = new HBox();
        painelBotoes.setSpacing(15);
        Button btnAdicionar = criarBotao("Adicionar", "/imagens/adicionar.png");
        Button btnEditar = criarBotao("Editar", "/imagens/editar.png");
        Button btnExcluir = criarBotao("Excluir", "/imagens/excluir.png");

        // AÇÕES DOS BOTÕES
        btnAdicionar.setOnAction(e -> {
            TelaPlataforma tela = new TelaPlataforma();
            tela.criarTela((Stage) painelPlataformas.getScene().getWindow());
            tabelaPlataformas.setItems(repository.getPlataformas());
        });

        btnEditar.setOnAction(e -> {
            Plataforma selecionada = tabelaPlataformas.getSelectionModel().getSelectedItem();
            if (selecionada == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Selecione uma plataforma na tabela para editar.");
                alerta.showAndWait();
                return;
            }
            TelaPlataforma tela = new TelaPlataforma();
            tela.configurarModoEdicao(selecionada);
            tela.criarTela((Stage) painelPlataformas.getScene().getWindow());
            tabelaPlataformas.setItems(repository.getPlataformas());
        });

        btnExcluir.setOnAction(e -> {
            Plataforma selecionada = tabelaPlataformas.getSelectionModel().getSelectedItem();
            if (selecionada == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Selecione uma plataforma para excluir.");
                alerta.showAndWait();
                return;
            }

            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText(null);
            confirmacao.setContentText("Tem certeza que deseja excluir a plataforma '" + selecionada.getNome() + "'?");

            if (confirmacao.showAndWait().get() == ButtonType.OK) {
                repository.excluir(selecionada.getId());
                tabelaPlataformas.setItems(repository.getPlataformas());
            }
        });

        painelBotoes.getChildren().addAll(btnAdicionar, btnEditar, btnExcluir);
        painelPlataformas.getChildren().addAll(lbTitulo, linha, painelBotoes, tabelaPlataformas);

        return painelPlataformas;
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
        btn.setStyle("-fx-cursor: hand; -fx-background-color: #e1e1e1; -fx-font-weight: bold;");
        return btn;
    }
}