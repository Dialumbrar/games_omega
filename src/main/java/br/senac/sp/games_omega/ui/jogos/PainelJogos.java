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

        TableColumn<Jogo, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        TableColumn<Jogo, LocalDate> colData = new TableColumn<>("Lançamento");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataLancamento"));

        // 3. Adicionar colunas à tabela
        tabelaJogos.getColumns().addAll(
                colId, colTitulo, colPlat, colCat, colEstudio, colPreco, colData
        );

        // 4. Carregar os dados do repositório ao abrir a tela
        JogoRepository repository = new JogoRepository();
        tabelaJogos.setItems(repository.getJogos());

        // Botões
        HBox painelBotoes = new HBox();
        painelBotoes.setSpacing(15);
        Button btnAdicionar = criarBotao("Adicionar", "/imagens/adicionar.png");
        Button btnExcluir = criarBotao("Excluir", "/imagens/excluir.png");
        Button btnEditar = criarBotao("Editar", "/imagens/editar.png");
        Button btnPesquisar = criarBotao("Pesquisar", "/imagens/pesquisar.png");
        Button btnLixeira = criarBotao("Lixeira", "/imagens/lixeira.png");

        // --- LÓGICA PARA ABRIR A TELA DE ADICIONAR ---
        btnAdicionar.setOnAction(event -> {
            TelaJogo telaCadastro = new TelaJogo();
            Stage stagePrincipal = (Stage) painelJogos.getScene().getWindow();

            // Abre a janela e pausa até ela fechar
            telaCadastro.criarTela(stagePrincipal);

            // Atualiza a tabela após fechar a janela
            tabelaJogos.setItems(repository.getJogos());
        });

        // --- LÓGICA PARA O BOTÃO EXCLUIR ---
        btnExcluir.setOnAction(event -> {
            // 1. Pega o jogo selecionado na tabela
            Jogo jogoSelecionado = tabelaJogos.getSelectionModel().getSelectedItem();

            // 2. Valida se há linha selecionada
            if (jogoSelecionado == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Nenhum jogo selecionado");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, selecione um jogo na tabela para poder excluir!");

                // Configura o logo da Omega na barra de título do Alerta
                Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
                try {
                    alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
                } catch (Exception ex) {
                    System.err.println("Erro ao carregar o ícone da barra de título.");
                }

                // Configura o ícone interno
                try {
                    ImageView imagemCustomizada = new ImageView(new Image(getClass().getResourceAsStream("/imagens/atenção.png")));
                    imagemCustomizada.setFitWidth(35);
                    imagemCustomizada.setFitHeight(35);
                    alerta.setGraphic(imagemCustomizada);
                } catch (Exception e) {
                    System.err.println("Erro ao carregar o ícone interno de atenção.");
                }

                alerta.showAndWait();
                return;
            }

            // 3. Criação dos botões customizados em Português
            ButtonType btnSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType btnNao = new ButtonType("Não", ButtonBar.ButtonData.NO);

            // 4. Caixa de confirmação de segurança
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText(null);
            confirmacao.setContentText("Tem certeza que deseja excluir o jogo '" + jogoSelecionado.getTitulo() + "'?");

            // Configura os botões customizados ANTES de mostrar a tela
            confirmacao.getButtonTypes().setAll(btnSim, btnNao);

            // ALTERA O ÍCONE DA JANELA DO ALERTA
            Stage alertStage = (Stage) confirmacao.getDialogPane().getScene().getWindow();
            try {
                alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/excluir.png")));
            } catch (Exception ex) {
                System.err.println("Não foi possível carregar o ícone do alerta.");
            }

            // ALTERA O ÍCONE INTERNO GRANDE (Substitui o ponto de interrogação '?' pelo ícone de excluir)
            try {
                ImageView imagemCustomizada = new ImageView(new Image(getClass().getResourceAsStream("/imagens/lixeira.png")));
                imagemCustomizada.setFitWidth(30);
                imagemCustomizada.setFitHeight(30);
                confirmacao.setGraphic(imagemCustomizada);
            } catch (Exception e) {
                System.err.println("Não foi possível carregar o ícone interno do alerta.");
            }

            // 5. MOSTRA A TELA APENAS UMA VEZ E GUARDA A RESPOSTA
            java.util.Optional<ButtonType> resposta = confirmacao.showAndWait();

            // 6. Se confirmado, deleta e atualiza a View
            if (resposta.isPresent() && resposta.get() == btnSim) {
                repository.excluir(jogoSelecionado.getId());
                tabelaJogos.setItems(repository.getJogos());
            }
        });

        // --- LÓGICA PARA O BOTÃO EDITAR ---
        btnEditar.setOnAction(event -> {
            Jogo jogoSelecionado = tabelaJogos.getSelectionModel().getSelectedItem();

            // Valida se o utilizador selecionou uma linha com a nova identidade visual
            if (jogoSelecionado == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Nenhum jogo selecionado");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, selecione um jogo na tabela para poder editar!");

                // Configura o logo Omega na barra de título do Alerta
                Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
                try {
                    alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
                } catch (Exception ex) {
                    System.err.println("Erro ao carregar o ícone da barra de título.");
                }

                // Substitui o triângulo padrão pelo seu ícone customizado
                try {
                    ImageView imagemCustomizada = new ImageView(new Image(getClass().getResourceAsStream("/imagens/atenção.png")));
                    imagemCustomizada.setFitWidth(35);
                    imagemCustomizada.setFitHeight(35);
                    alerta.setGraphic(imagemCustomizada);
                } catch (Exception e) {
                    System.err.println("Erro ao carregar o ícone interno de atenção.");
                }

                alerta.showAndWait();
                return;
            }

            // 3. Abre a tela de cadastro, mas configurada para EDITAR
            TelaJogo telaEdicao = new TelaJogo();

            // Passa o objeto selecionado para preencher o formulário
            telaEdicao.configurarModoEdicao(jogoSelecionado);

            Stage stagePrincipal = (Stage) painelJogos.getScene().getWindow();
            telaEdicao.criarTela(stagePrincipal);

            // 4. Atualiza a tabela após fechar a janela para mostrar as alterações
            tabelaJogos.setItems(repository.getJogos());
        });

        // Tela Lixeira
        btnLixeira.setOnAction(event -> {
            TelaLixeira telaLixeira = new TelaLixeira();
            // Abre a janela passando a tela principal como parente
            telaLixeira.mostrarTela((Stage) painelJogos.getScene().getWindow());
        });

        // Organização dos elementos no layout
        painelBotoes.getChildren().addAll(btnAdicionar, btnExcluir, btnEditar, btnPesquisar, btnLixeira);
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
        btn.setStyle("-fx-cursor: hand; -fx-background-color: #e1e1e1; -fx-font-weight: bold;");
        return btn;
    }
}