package br.senac.sp.games_omega.ui.jogos;

import br.senac.sp.games_omega.data.repository.EstudioRepository;
import br.senac.sp.games_omega.model.Estudio;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PainelEstudio {

    private final EstudioRepository repository = new EstudioRepository();

    public VBox criarPainelEstudios() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(20));
        painel.setStyle("-fx-background-color: #252830;");

        Label lbTitulo = new Label("Gerenciamento de Estúdios");
        lbTitulo.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

        Separator linha = new Separator();

        TableView<Estudio> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        TableColumn<Estudio, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(60);

        TableColumn<Estudio, String> colNome = new TableColumn<>("Nome do Estúdio");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Estudio, String> colFundador = new TableColumn<>("Fundador");
        colFundador.setCellValueFactory(new PropertyValueFactory<>("fundador"));

        TableColumn<Estudio, Integer> colAno = new TableColumn<>("Ano Fundação");
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoFundacao"));

        TableColumn<Estudio, String> colPais = new TableColumn<>("País de Origem");
        colPais.setCellValueFactory(new PropertyValueFactory<>("paisOrigem"));

        tabela.getColumns().addAll(colId, colNome, colFundador, colAno, colPais);
        tabela.setItems(repository.getEstudios());

        HBox painelBotoes = new HBox(15);
        Button btnAdicionar = criarBotao("Adicionar", "/imagens/adicionar.png");
        Button btnEditar = criarBotao("Editar", "/imagens/editar.png");
        Button btnExcluir = criarBotao("Excluir", "/imagens/excluir.png");

        btnAdicionar.setOnAction(e -> {
            TelaEstudio tela = new TelaEstudio();
            tela.criarTela((Stage) painel.getScene().getWindow());
            tabela.setItems(repository.getEstudios());
        });

        // --- LÓGICA PARA O BOTÃO EDITAR ---
        btnEditar.setOnAction(e -> {
            Estudio selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Nenhum estúdio selecionado");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, selecione um estúdio na tabela para poder editar!");

                // Configura o logo Omega na barra de título
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
                } catch (Exception ex) {
                    System.err.println("Erro ao carregar o ícone interno de atenção.");
                }

                alerta.showAndWait();
                return;
            }
            TelaEstudio tela = new TelaEstudio();
            tela.configurarModoEdicao(selecionado);
            tela.criarTela((Stage) painel.getScene().getWindow());
            tabela.setItems(repository.getEstudios());
        });

        btnExcluir.setOnAction(e -> {
            Estudio selecionado = tabela.getSelectionModel().getSelectedItem();
            // 1. Valida se há linha selecionada
            if (selecionado == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Nenhum estúdio selecionado");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor, selecione um estúdio na tabela para poder excluir!");

                // 2. Configura o logo da Omega na barra de título
                Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
                try {
                    alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
                } catch (Exception ex) {
                    System.err.println("Erro ao carregar o ícone da barra de título.");
                }

                // 3. Configura o ícone interno
                try {
                    ImageView imagemCustomizada = new ImageView(new Image(getClass().getResourceAsStream("/imagens/atenção.png")));
                    imagemCustomizada.setFitWidth(35);
                    imagemCustomizada.setFitHeight(35);
                    alerta.setGraphic(imagemCustomizada);
                } catch (Exception ex) {
                    System.err.println("Erro ao carregar o ícone interno de atenção.");
                }

                alerta.showAndWait();
                return;
            }

            // Botões customizados
            ButtonType btnSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType btnNao = new ButtonType("Não", ButtonBar.ButtonData.NO);

            // Caixa de confirmação de segurança estruturada
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText(null);
            confirmacao.setContentText("Tem certeza que deseja excluir o estúdio '" + selecionado.getNome() + "'?");
            confirmacao.getButtonTypes().setAll(btnSim, btnNao);

            // Altera o ícone da janela do alerta
            Stage alertStage = (Stage) confirmacao.getDialogPane().getScene().getWindow();
            try {
                alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/excluir.png")));
            } catch (Exception ex) {
                System.err.println("Não foi possível carregar o ícone do alerta.");
            }

            // Altera o ícone interno grande (Muda o '?' por uma lixeira)
            try {
                ImageView imagemCustomizada = new ImageView(new Image(getClass().getResourceAsStream("/imagens/lixeira.png")));
                imagemCustomizada.setFitWidth(30);
                imagemCustomizada.setFitHeight(30);
                confirmacao.setGraphic(imagemCustomizada);
            } catch (Exception ex) {
                System.err.println("Não foi possível carregar o ícone interno do alerta.");
            }

            // Exibe e captura a resposta do usuário
            java.util.Optional<ButtonType> resposta = confirmacao.showAndWait();
            if (resposta.isPresent() && resposta.get() == btnSim) {
                repository.excluir(selecionado.getId());
                tabela.setItems(repository.getEstudios());
            }
        });

        painelBotoes.getChildren().addAll(btnAdicionar, btnEditar, btnExcluir);
        painel.getChildren().addAll(lbTitulo, linha, painelBotoes, tabela);
        return painel;
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

        // Configuração inicial do botão (Modo Escuro Flat)
        btn.setStyle(
                "-fx-cursor: hand; " +
                        "-fx-background-color: #333742; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 6 12 6 12;"
        );

        // Dinâmica de Hover (Efeito ao passar e retirar o ponteiro do mouse)
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #434857; -fx-text-fill: #E5A93C; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 12 6 12;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #333742; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 12 6 12;"
        ));

        return btn;
    }
}