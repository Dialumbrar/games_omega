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

    private final JogoRepository jogoRepository = new JogoRepository();

    // ATRIBUTOS GLOBAIS DA CLASSE
    private TextField txtId;
    private TextField txtTitulo;
    private ComboBox<String> cbPlataforma;
    private ComboBox<String> cbEstudio;
    private ComboBox<String> cbCategoria;
    private TextField txtPreco;
    private DatePicker dpLancamento;
    private CheckBox cbFinalizado;

    // Guardará os dados do jogo caso a tela seja aberta para Editar
    private Jogo jogoEmEdicao = null;

    public void criarTela(Stage stagePai) {
        Stage stage = new Stage();
        stage.initOwner(stagePai);
        stage.initModality(Modality.APPLICATION_MODAL);

        // 1. CONFIGURAÇÃO DO CONTAINER RAIZ (DARK THEME)
        VBox raiz = new VBox();
        raiz.setStyle("-fx-background-color: #252830;"); // Mesmo cinza grafite do sistema

        // Cabeçalho Dinâmico (Ícone adaptativo configurado abaixo)
        HBox painelTitulo = criarPainelTitulo();

        // 2. PAINEL DO FORMULÁRIO (INTEGRADO AO MODO ESCURO)
        VBox painelFormulario = new VBox(15);
        painelFormulario.setPadding(new Insets(20));
        VBox.setMargin(painelFormulario, new Insets(15));

        // Estilo escuro sem a borda esmeralda antiga
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

        // --- INICIALIZAÇÃO DOS CAMPOS ---
        txtId = new TextField();
        txtId.setDisable(true);
        txtId.setPrefWidth(80);
        txtId.setMaxWidth(80);
        txtId.setStyle("-fx-background-color: #333742; -fx-text-fill: #A0A5B5; -fx-opacity: 0.8;");

        txtTitulo = new TextField();
        txtTitulo.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");

        cbPlataforma = new ComboBox<>();
        cbPlataforma.setItems(FXCollections.observableArrayList(
                "PC", "PlayStation 5", "Xbox Series X", "Nintendo Switch", "Mobile"
        ));
        cbPlataforma.setPromptText("Selecione a plataforma...");
        cbPlataforma.setMaxWidth(Double.MAX_VALUE);

        cbEstudio = new ComboBox<>();
        cbEstudio.setItems(FXCollections.observableArrayList(
                "CD Projekt Red", "ConcernedApe", "FromSoftware", "Insomniac Games",
                "Nintendo", "Playground Games", "Rockstar Games", "Santa Monica Studio",
                "Supergiant Games", "Team Cherry"
        ));
        cbEstudio.setPromptText("Selecione o estúdio...");
        cbEstudio.setMaxWidth(Double.MAX_VALUE);

        cbCategoria = new ComboBox<>();
        cbCategoria.setItems(FXCollections.observableArrayList(
                "Ação/Aventura", "Metroidvania", "RPG", "Ação", "Corrida",
                "Simulação", "Ação/Mundo Aberto", "Roguelike"
        ));
        cbCategoria.setPromptText("Selecione a categoria...");
        cbCategoria.setMaxWidth(Double.MAX_VALUE);

        txtPreco = new TextField();
        txtPreco.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");

        dpLancamento = new DatePicker();
        dpLancamento.setPrefWidth(200);
        dpLancamento.getEditor().setDisable(true);
        dpLancamento.getEditor().setStyle("-fx-opacity: 1; -fx-text-fill: black; -fx-background-color: #ffffff;");

        cbFinalizado = new CheckBox();
        cbFinalizado.setStyle("-fx-text-fill: #FFFFFF;");

        if (jogoEmEdicao != null) {
            preencherCamposFormulario(jogoEmEdicao);
        }

        // Adicionando elementos ao Grid com Labels estilizados em Branco para contraste
        grid.add(criarLabelFormulario("ID:"), 0, 0);
        grid.add(txtId, 1, 0);

        grid.add(criarLabelFormulario("Título:"), 0, 1);
        grid.add(txtTitulo, 1, 1);
        GridPane.setHgrow(txtTitulo, Priority.ALWAYS);

        grid.add(criarLabelFormulario("Plataforma:"), 0, 2);
        grid.add(cbPlataforma, 1, 2);

        grid.add(criarLabelFormulario("Estúdio:"), 0, 3);
        grid.add(cbEstudio, 1, 3);

        grid.add(criarLabelFormulario("Categoria:"), 0, 4);
        grid.add(cbCategoria, 1, 4);

        grid.add(criarLabelFormulario("Preço:"), 0, 5);
        grid.add(txtPreco, 1, 5);

        grid.add(criarLabelFormulario("Lançamento:"), 0, 6);
        grid.add(dpLancamento, 1, 6);

        grid.add(criarLabelFormulario("Finalizado?:"), 0, 7);
        grid.add(cbFinalizado, 1, 7);

        painelFormulario.getChildren().add(grid);

        // 3. PAINEL DE BOTÕES INFERIORES PADRONIZADOS (FLAT COM HOVER)
        HBox painelBotoes = new HBox(15);
        painelBotoes.setAlignment(Pos.CENTER_RIGHT);
        painelBotoes.setPadding(new Insets(0, 15, 15, 15));

        Button btnSalvar = criarBotaoFormulario("Salvar", "/imagens/salvar.png");
        Button btnCancelar = criarBotaoFormulario("Cancelar", "/imagens/cancelar.png");

        // --- AÇÃO DO BOTÃO SALVAR ---
        btnSalvar.setOnAction(e -> {
            if (txtTitulo.getText().trim().isEmpty() ||
                    cbPlataforma.getValue() == null ||
                    cbCategoria.getValue() == null ||
                    cbEstudio.getValue() == null ||
                    txtPreco.getText().trim().isEmpty() ||
                    dpLancamento.getValue() == null) {

                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Campos Obrigatórios");
                alerta.setHeaderText("Não foi possível salvar");
                alerta.setContentText("Por favor, preencha todos os campos, incluindo uma data de lançamento válida!");

                try { ((Stage) alerta.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch(Exception ex){}
                alerta.showAndWait();
                return;
            }

            String precoTexto = txtPreco.getText().trim().replace(",", ".");
            if (!precoTexto.matches("\\d+(\\.\\d{1,2})?")) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Preço Inválido");
                alerta.setHeaderText("Erro no formato do valor");
                alerta.setContentText("O preço deve ser um número válido positivo (Ex: 199.90).\nNão utilize letras ou caracteres especiais.");

                try { ((Stage) alerta.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch(Exception ex){}
                alerta.showAndWait();
                txtPreco.requestFocus();
                return;
            }

            ButtonType btnSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType btnNao = new ButtonType("Não", ButtonBar.ButtonData.NO);

            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Operação");
            confirmacao.setHeaderText(null);
            confirmacao.getButtonTypes().setAll(btnSim, btnNao);

            if (jogoEmEdicao != null) {
                confirmacao.setContentText("Deseja realmente salvar as alterações deste jogo?");
            } else {
                confirmacao.setContentText("Deseja realmente cadastrar este novo jogo?");
            }

            Stage alertStage = (Stage) confirmacao.getDialogPane().getScene().getWindow();
            try {
                alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
            } catch (Exception ex) {
                System.err.println("Não foi possível carregar o ícone na barra do alerta.");
            }

            try {
                ImageView imagemCustomizada = new ImageView(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
                imagemCustomizada.setFitWidth(32);
                imagemCustomizada.setFitHeight(32);
                confirmacao.setGraphic(imagemCustomizada);
            } catch (Exception ex) {
                System.err.println("Não foi possível carregar o ícone interno do alerta.");
            }

            java.util.Optional<ButtonType> resposta = confirmacao.showAndWait();

            if (!resposta.isPresent() || resposta.get() != btnSim) {
                return;
            }

            try {
                String titulo = txtTitulo.getText().trim();
                String plataforma = cbPlataforma.getValue();
                String categoria = cbCategoria.getValue();
                String estudio = cbEstudio.getValue();
                double preco = Double.parseDouble(precoTexto);
                LocalDate dataLancamento = dpLancamento.getValue();
                boolean finalizado = cbFinalizado.isSelected();

                btnSalvar.setDisable(true);

                int idAtual = (jogoEmEdicao != null) ? jogoEmEdicao.getId() : 0;
                Jogo jogoObj = new Jogo(idAtual, titulo, plataforma, categoria, estudio, preco, dataLancamento, finalizado);

                if (jogoEmEdicao != null) {
                    // MODO EDIÇÃO
                    jogoRepository.atualizar(jogoObj);

                    Alert alertaSucesso = new Alert(Alert.AlertType.INFORMATION);
                    alertaSucesso.setTitle("Jogo Atualizado!");
                    alertaSucesso.setHeaderText(null);
                    alertaSucesso.setContentText("O jogo '" + titulo + "' foi updated com sucesso!");
                    try { ((Stage) alertaSucesso.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch(Exception ex){}
                    alertaSucesso.showAndWait();

                    stage.close();
                } else {
                    // MODO INSERÇÃO
                    jogoRepository.salvar(jogoObj);

                    Alert alertaSucesso = new Alert(Alert.AlertType.INFORMATION);
                    alertaSucesso.setTitle("Jogo Cadastrado!");
                    alertaSucesso.setHeaderText(null);
                    alertaSucesso.setContentText("O jogo '" + titulo + "' foi salvo com sucesso!");
                    try { ((Stage) alertaSucesso.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch(Exception ex){}
                    alertaSucesso.showAndWait();

                    ButtonType btnSimNovo = new ButtonType("Sim", ButtonBar.ButtonData.YES);
                    ButtonType btnNaoNovo = new ButtonType("Não", ButtonBar.ButtonData.NO);

                    Alert perguntaNovoCadastro = new Alert(Alert.AlertType.CONFIRMATION);
                    perguntaNovoCadastro.setTitle("Continuar Cadastrando?");
                    perguntaNovoCadastro.setHeaderText(null);
                    perguntaNovoCadastro.setContentText("Deseja cadastrar um novo jogo?");
                    perguntaNovoCadastro.getButtonTypes().setAll(btnSimNovo, btnNaoNovo);

                    Stage novoStage = (Stage) perguntaNovoCadastro.getDialogPane().getScene().getWindow();
                    try {
                        novoStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/editar.png")));
                    } catch (Exception ex) {
                        System.err.println("Não foi possível carregar o ícone.");
                    }

                    java.util.Optional<ButtonType> respostaNova = perguntaNovoCadastro.showAndWait();

                    if (respostaNova.isPresent() && respostaNova.get() == btnSimNovo) {
                        txtTitulo.clear();
                        cbPlataforma.setValue(null);
                        cbCategoria.setValue(null);
                        cbEstudio.setValue(null);
                        txtPreco.clear();
                        dpLancamento.setValue(null);
                        cbFinalizado.setSelected(false);
                        txtTitulo.requestFocus();
                    } else {
                        stage.close();
                    }
                }

            } catch (Exception ex) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro inesperado");
                alerta.setContentText("Ocorreu um problema ao tentar salvar o registro: " + ex.getMessage());
                alerta.showAndWait();
            } finally {
                btnSalvar.setDisable(false);
            }
        });

        btnCancelar.setOnAction(e -> stage.close());

        painelBotoes.getChildren().addAll(btnSalvar, btnCancelar);
        raiz.getChildren().addAll(painelTitulo, painelFormulario, painelBotoes);

        Scene cena = new Scene(raiz, 600, 560);
        stage.setScene(cena);

        // Barra de título externa da janela mantém a assinatura visual estável do sistema
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
        } catch (Exception ex) {
            System.err.println("Erro ao carregar ícone omega da janela principal");
        }

        stage.setTitle(jogoEmEdicao != null ? "Editar Jogo" : "Cadastro de Jogo");
        stage.setResizable(false);
        stage.showAndWait();
    }

    public void configurarModoEdicao(Jogo jogo) {
        this.jogoEmEdicao = jogo;
    }

    private void preencherCamposFormulario(Jogo jogo) {
        txtId.setText(String.valueOf(jogo.getId()));
        txtTitulo.setText(jogo.getTitulo());
        cbPlataforma.setValue(jogo.getPlataforma());
        cbEstudio.setValue(jogo.getEstudio());
        cbCategoria.setValue(jogo.getCategoria());
        txtPreco.setText(String.valueOf(jogo.getPreco()));
        dpLancamento.setValue(jogo.getDataLancamento());
        cbFinalizado.setSelected(jogo.isFinalizado());
    }

    // CABEÇALHO ATUALIZADO PARA SUPORTAR ALTERAÇÃO DINÂMICA DE ÍCONE (CADASTRO VS EDIÇÃO)
    private HBox criarPainelTitulo() {
        HBox painelTitulo = new HBox(15);
        painelTitulo.setAlignment(Pos.CENTER_LEFT);
        painelTitulo.setPadding(new Insets(15));
        painelTitulo.setStyle("-fx-background-color: #1E222B; -fx-border-color: #333742; -fx-border-width: 0 0 1 0;");

        try {
            // Se o jogoEmEdicao for diferente de nulo ele busca editar, senão puxa o cadastro.
            String iconeInterno = (jogoEmEdicao != null) ? "/imagens/editar.png" : "/imagens/cadastro.png";

            Image image = new Image(getClass().getResourceAsStream(iconeInterno));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            painelTitulo.getChildren().add(imageView);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone dinâmico do título do cabeçalho");
        }

        Label lbTitulo = new Label(jogoEmEdicao != null ? "Editar Jogo" : "Cadastro de Jogo");
        lbTitulo.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        painelTitulo.getChildren().add(lbTitulo);

        return painelTitulo;
    }

    // Metodo auxiliar para criar labels do formulário com cor branca estável
    private Label criarLabelFormulario(String texto) {
        Label lb = new Label(texto);
        lb.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 13;");
        return lb;
    }

    // Metodo para criar Botões planos padronizados
    private Button criarBotaoFormulario(String texto, String urlImagem) {
        Button btn = new Button(texto);
        try {
            Image img = new Image(getClass().getResourceAsStream(urlImagem));
            ImageView iv = new ImageView(img);
            iv.setFitHeight(16);
            iv.setFitWidth(16);
            btn.setGraphic(iv);
        } catch (Exception e) {
            System.out.println("Erro ao carregar ícone no botão do formulário: " + urlImagem);
        }

        btn.setStyle(
                "-fx-cursor: hand; " +
                        "-fx-background-color: #333742; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 6 16 6 16;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #434857; -fx-text-fill: #E5A93C; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 16 6 16;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #333742; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 16 6 16;"
        ));

        return btn;
    }
}