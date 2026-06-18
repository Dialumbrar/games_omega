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
        // 1. CONTAINER PRINCIPAL (TEMA ESCURO)
        VBox painelJogos = new VBox();
        painelJogos.setSpacing(10);
        painelJogos.setPadding(new Insets(20));
        painelJogos.setStyle("-fx-background-color: #252830;"); // Cinza grafite padrão

        Label lbTitulo = new Label("Listagem de Jogos");
        lbTitulo.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

        Separator linha = new Separator();

        // 2. INSTÂNCIA DA TABELA E COMPORTAMENTO VISUAL
        TableView<Jogo> tabelaJogos = new TableView<>();
        tabelaJogos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabelaJogos, Priority.ALWAYS);

        // APLICAÇÃO DO CSS ADAPTADO PARA O "MODO GAMER" (Tabela Escura)
        tabelaJogos.setStyle(
                "-fx-background-color: #1E222B; " +
                        "-fx-border-color: #333742; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5; " +
                        ".table-header-area .filler, .table-header-area .nested-column-header { -fx-background-color: #333742; } " +
                        ".table-view .column-header .label { -fx-text-fill: #E5A93C; -fx-font-weight: bold; -fx-font-size: 13; } " +
                        ".table-row-cell { -fx-background-color: #252830; -fx-text-background-color: #FFFFFF; -fx-cell-size: 30; } " +
                        ".table-row-cell:odd { -fx-background-color: #1E222B; } " +
                        ".table-row-cell:filled:selected { -fx-background-color: #E5A93C; -fx-text-background-color: #1E222B; } " +
                        ".table-view .table-cell { -fx-border-color: transparent; -fx-padding: 5; }"
        );

        // 3. CONFIGURAÇÃO DE TODAS AS COLUNAS
        TableColumn<Jogo, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(50);

        TableColumn<Jogo, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Jogo, String> colPlat = new TableColumn<>("Plataforma");
        colPlat.setCellValueFactory(new PropertyValueFactory<>("plataforma"));

        TableColumn<Jogo, String> colCat = new TableColumn<>("Categoria");
        colCat.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Jogo, String> colEstudio = new TableColumn<>("Estúdio");
        colEstudio.setCellValueFactory(new PropertyValueFactory<>("estudio"));

        // COLUNA PREÇO: Alinhamento à direita e formatação de duas casas decimais
        TableColumn<Jogo, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colPreco.setStyle("-fx-alignment: CENTER_RIGHT;");
        colPreco.setCellFactory(tc -> new TableCell<Jogo, Double>() {
            @Override
            protected void updateItem(Double preco, boolean empty) {
                super.updateItem(preco, empty);
                if (empty || preco == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", preco));
                }
            }
        });

        TableColumn<Jogo, LocalDate> colData = new TableColumn<>("Lançamento");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataLancamento"));

        // Adicionar colunas à tabela
        tabelaJogos.getColumns().addAll(colId, colTitulo, colPlat, colCat, colEstudio, colPreco, colData);

        // Carregar os dados do repositório
        JogoRepository repository = new JogoRepository();
        javafx.collections.transformation.FilteredList<Jogo> dadosFiltrados = new javafx.collections.transformation.FilteredList<>(repository.getJogos(), j -> true);
        tabelaJogos.setItems(dadosFiltrados);

        // 4. CONFIGURAÇÃO DO PAINEL DE BOTÕES
        HBox painelBotoes = new HBox();
        painelBotoes.setSpacing(15);
        Button btnAdicionar = criarBotao("Adicionar", "/imagens/adicionar.png");
        Button btnExcluir = criarBotao("Excluir", "/imagens/excluir.png");
        Button btnEditar = criarBotao("Editar", "/imagens/editar.png");
        Button btnPesquisar = criarBotao("Pesquisar", "/imagens/pesquisar.png");
        Button btnLixeira = criarBotao("Lixeira", "/imagens/lixeira.png");

        // BOTÃO LIMPAR FILTRO (INICIA OCULTO)
        Button btnLimparFiltro = criarBotao("Limpar Filtro", "/imagens/clear-filter.png");
        btnLimparFiltro.setVisible(false);
        btnLimparFiltro.setManaged(false); // Faz o HBox ignorar o espaço dele enquanto invisível

        // --- LÓGICA: ADICIONAR JOGO ---
        btnAdicionar.setOnAction(event -> {
            TelaJogo telaCadastro = new TelaJogo();
            Stage stagePrincipal = (Stage) painelJogos.getScene().getWindow();
            telaCadastro.criarTela(stagePrincipal);
            tabelaJogos.refresh();
        });

        // --- LÓGICA: EXCLUIR JOGO ---
        btnExcluir.setOnAction(event -> {
            Jogo jogoSelecionado = tabelaJogos.getSelectionModel().getSelectedItem();

            if (jogoSelecionado == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Nenhum jogo selecionado");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, selecione um jogo na tabela para poder excluir!");

                Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
                try { alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch (Exception ex) {}
                try {
                    ImageView imagemCustomizada = new ImageView(new Image(getClass().getResourceAsStream("/imagens/atenção.png")));
                    imagemCustomizada.setFitWidth(35); imagemCustomizada.setFitHeight(35);
                    alerta.setGraphic(imagemCustomizada);
                } catch (Exception e) {}

                alerta.showAndWait();
                return;
            }

            ButtonType btnSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType btnNao = new ButtonType("Não", ButtonBar.ButtonData.NO);

            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText(null);
            confirmacao.setContentText("Tem certeza que deseja excluir o jogo '" + jogoSelecionado.getTitulo() + "'?");
            confirmacao.getButtonTypes().setAll(btnSim, btnNao);

            Stage alertStage = (Stage) confirmacao.getDialogPane().getScene().getWindow();
            try { alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/excluir.png"))); } catch (Exception ex) {}
            try {
                ImageView imagemCustomizada = new ImageView(new Image(getClass().getResourceAsStream("/imagens/lixeira.png")));
                imagemCustomizada.setFitWidth(30); imagemCustomizada.setFitHeight(30);
                confirmacao.setGraphic(imagemCustomizada);
            } catch (Exception e) {}

            java.util.Optional<ButtonType> resposta = confirmacao.showAndWait();
            if (resposta.isPresent() && resposta.get() == btnSim) {
                repository.excluir(jogoSelecionado.getId());
                tabelaJogos.refresh();
            }
        });

        // --- LÓGICA: EDITAR JOGO ---
        btnEditar.setOnAction(event -> {
            Jogo jogoSelecionado = tabelaJogos.getSelectionModel().getSelectedItem();

            if (jogoSelecionado == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Nenhum jogo selecionado");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, selecione um jogo na tabela para poder editar!");

                Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
                try { alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch (Exception ex) {}
                try {
                    ImageView imagemCustomizada = new ImageView(new Image(getClass().getResourceAsStream("/imagens/atenção.png")));
                    imagemCustomizada.setFitWidth(35); imagemCustomizada.setFitHeight(35);
                    alerta.setGraphic(imagemCustomizada);
                } catch (Exception e) {}

                alerta.showAndWait();
                return;
            }

            TelaJogo telaEdicao = new TelaJogo();
            telaEdicao.configurarModoEdicao(jogoSelecionado);
            Stage stagePrincipal = (Stage) painelJogos.getScene().getWindow();
            telaEdicao.criarTela(stagePrincipal);
            tabelaJogos.refresh();
        });

        // --- LÓGICA: TELA LIXEIRA ---
        btnLixeira.setOnAction(event -> {
            TelaLixeira telaLixeira = new TelaLixeira();
            telaLixeira.criarTela((Stage) painelJogos.getScene().getWindow());
        });

        // --- LÓGICA: PESQUISAR JOGO ---
        btnPesquisar.setOnAction(event -> {
            TelaPesquisa telaPesquisa = new TelaPesquisa();
            Stage stagePrincipal = (Stage) painelJogos.getScene().getWindow();

            boolean confirmou = telaPesquisa.exibirTela(stagePrincipal, dadosFiltrados);

            if (confirmou) {
                tabelaJogos.refresh();

                // Se o predicate for nulo ou não for mais "j -> true", significa que tem filtro ativo!
                boolean temFiltroAtivo = dadosFiltrados.getPredicate() != null;

                // Mostra o botão dinamicamente na barra
                btnLimparFiltro.setVisible(temFiltroAtivo);
                btnLimparFiltro.setManaged(temFiltroAtivo);
            }
        });

        // --- LÓGICA: LIMPAR FILTRO (RESETAR TABELA E OCULTAR BOTÃO) ---
        btnLimparFiltro.setOnAction(event -> {
            dadosFiltrados.setPredicate(jogo -> true);
            tabelaJogos.refresh();

            // Oculta o botão novamente e reorganiza o HBox
            btnLimparFiltro.setVisible(false);
            btnLimparFiltro.setManaged(false);
        });

        // Montagem final do Layout
        painelBotoes.getChildren().addAll(btnAdicionar, btnExcluir, btnEditar, btnPesquisar, btnLimparFiltro, btnLixeira);
        painelJogos.getChildren().addAll(lbTitulo, linha, painelBotoes, tabelaJogos);

        return painelJogos;
    }

    // 5. AUXILIAR DE CRIAÇÃO DE BOTÃO (ESTILO PRESET: FLAT MODERNO COM HOVER)
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

        // Configuração inicial do botão (Modo Escuro Flat)
        btn.setStyle(
                "-fx-cursor: hand; " +
                        "-fx-background-color: #333742; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 6 12 6 12;"
        );

        // Dinâmica de Hover
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #434857; -fx-text-fill: #E5A93C; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 12 6 12;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #333742; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 12 6 12;"
        ));

        return btn;
    }
}