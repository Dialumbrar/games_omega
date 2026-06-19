package br.senac.sp.games_omega.ui.jogos;

import br.senac.sp.games_omega.data.repository.PlataformaRepository;
import br.senac.sp.games_omega.model.Plataforma;
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

public class TelaPlataforma {

    private final PlataformaRepository repository = new PlataformaRepository();

    // ATRIBUTOS GLOBAIS DA CLASSE
    private TextField txtId;
    private TextField txtNome;
    private TextField txtFabricante;
    private TextField txtValor;
    private DatePicker dpLancamento;

    private Plataforma plataformaEmEdicao = null;

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

        // Fundo ligeiramente mais escuro
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

        txtNome = new TextField();
        txtNome.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");

        txtFabricante = new TextField();
        txtFabricante.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");

        txtValor = new TextField();
        txtValor.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");

        dpLancamento = new DatePicker();
        dpLancamento.setPrefWidth(200);
        dpLancamento.getEditor().setDisable(true);
        dpLancamento.getEditor().setStyle("-fx-opacity: 1; -fx-text-fill: black; -fx-background-color: #ffffff;");

        if (plataformaEmEdicao != null) {
            txtId.setText(String.valueOf(plataformaEmEdicao.getId()));
            txtNome.setText(plataformaEmEdicao.getNome());
            txtFabricante.setText(plataformaEmEdicao.getFabricante());
            txtValor.setText(String.valueOf(plataformaEmEdicao.getValor()));
            dpLancamento.setValue(plataformaEmEdicao.getDataLancamento());
        }

        // Mapeamento dos Labels usando o metodo customizado para cor branca estável
        grid.add(criarLabelFormulario("ID:"), 0, 0);
        grid.add(txtId, 1, 0);

        grid.add(criarLabelFormulario("Nome Comercial:"), 0, 1);
        grid.add(txtNome, 1, 1);
        GridPane.setHgrow(txtNome, Priority.ALWAYS);

        grid.add(criarLabelFormulario("Fabricante:"), 0, 2);
        grid.add(txtFabricante, 1, 2);

        grid.add(criarLabelFormulario("Valor Estimado:"), 0, 3);
        grid.add(txtValor, 1, 3);

        grid.add(criarLabelFormulario("Lançamento:"), 0, 4);
        grid.add(dpLancamento, 1, 4);

        painelFormulario.getChildren().add(grid);

        // PAINEL DE BOTÕES INFERIORES PADRONIZADOS (FLAT COM HOVER)
        HBox painelBotoes = new HBox(15);
        painelBotoes.setAlignment(Pos.CENTER_RIGHT);
        painelBotoes.setPadding(new Insets(0, 15, 15, 15));

        Button btnSalvar = criarBotaoFormulario("Salvar", "/imagens/salvar.png");
        Button btnCancelar = criarBotaoFormulario("Cancelar", "/imagens/cancelar.png");

        // --- AÇÃO DO BOTÃO SALVAR ---
        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().trim().isEmpty() || txtFabricante.getText().trim().isEmpty() ||
                    txtValor.getText().trim().isEmpty() || dpLancamento.getValue() == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Campos Obrigatórios");
                alerta.setContentText("Por favor, preencha todos os campos corretamente!");
                try { ((Stage) alerta.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch(Exception ex){}
                alerta.showAndWait();
                return;
            }

            String valorTexto = txtValor.getText().trim().replace(",", ".");
            if (!valorTexto.matches("\\d+(\\.\\d{1,2})?")) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Valor Inválido");
                alerta.setContentText("Insira um preço de hardware válido.");
                try { ((Stage) alerta.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch(Exception ex){}
                alerta.showAndWait();
                return;
            }

            ButtonType btnSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType btnNao = new ButtonType("Não", ButtonBar.ButtonData.NO);
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Salvamento");
            confirmacao.getButtonTypes().setAll(btnSim, btnNao);

            String caminhoIcone = (plataformaEmEdicao != null) ? "/imagens/editar.png" : "/imagens/salvar.png";
            confirmacao.setContentText(plataformaEmEdicao != null ? "Deseja salvar as edições?" : "Deseja cadastrar esta plataforma?");

            try {
                Stage alertStage = (Stage) confirmacao.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
                ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(caminhoIcone)));
                iv.setFitWidth(32); iv.setFitHeight(32);
                confirmacao.setGraphic(iv);
            } catch (Exception ex) {}

            if (confirmacao.showAndWait().get() != btnSim) return;

            try {
                String nome = txtNome.getText().trim();
                String fabricante = txtFabricante.getText().trim();
                double valor = Double.parseDouble(valorTexto);
                LocalDate lancamento = dpLancamento.getValue();

                int idAtual = (plataformaEmEdicao != null) ? plataformaEmEdicao.getId() : 0;
                Plataforma pObj = new Plataforma(idAtual, nome, fabricante, lancamento, valor);

                if (plataformaEmEdicao != null) {
                    repository.atualizar(pObj);
                    stage.close();
                } else {
                    repository.salvar(pObj);

                    Alert perguntaNovo = new Alert(Alert.AlertType.CONFIRMATION);
                    perguntaNovo.setTitle("Continuar?");
                    perguntaNovo.setContentText("Deseja cadastrar uma nova plataforma?");
                    perguntaNovo.getButtonTypes().setAll(btnSim, btnNao);

                    // Configura o ícone na janela do alerta usando o nome correto da variável
                    try {
                        ((Stage) perguntaNovo.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
                    } catch (Exception ex) {
                        System.err.println("Não foi possível carregar o ícone.");
                    }

                    // Trata a resposta do usuário
                    if (perguntaNovo.showAndWait().get() == btnSim) {
                        txtNome.clear();
                        txtFabricante.clear();
                        txtValor.clear();
                        dpLancamento.setValue(null);
                        txtNome.requestFocus();
                    } else {
                        stage.close();
                    }
                }
            } catch (Exception ex) {
                System.err.println("Erro: " + ex.getMessage());
            }
        });

        btnCancelar.setOnAction(e -> stage.close());
        painelBotoes.getChildren().addAll(btnSalvar, btnCancelar);
        raiz.getChildren().addAll(painelTitulo, painelFormulario, painelBotoes);

        Scene cena = new Scene(raiz, 550, 420);
        stage.setScene(cena);
        stage.setResizable(false);

        // Barra de título externa mantém a logo oficial estável
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
        } catch (Exception ex) {}

        stage.setTitle(plataformaEmEdicao != null ? "Editar Plataforma" : "Cadastro de Plataforma");
        stage.showAndWait();
    }

    public void configurarModoEdicao(Plataforma p) { this.plataformaEmEdicao = p; }

    // CABEÇALHO ADAPTATIVO: CARREGA O ÍCONE INTERNO DE ACORDO COM A OPERAÇÃO
    private HBox criarPainelTitulo() {
        HBox painelTitulo = new HBox(15);
        painelTitulo.setAlignment(Pos.CENTER_LEFT);
        painelTitulo.setPadding(new Insets(15));
        painelTitulo.setStyle("-fx-background-color: #1E222B; -fx-border-color: #333742; -fx-border-width: 0 0 1 0;");

        try {
            String icone = (plataformaEmEdicao != null) ? "/imagens/editar.png" : "/imagens/cadastro.png";
            ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(icone)));
            iv.setFitHeight(30); iv.setFitWidth(30);
            painelTitulo.getChildren().add(iv);
        } catch (Exception e) {}

        Label lbTitulo = new Label(plataformaEmEdicao != null ? "Editar Plataforma" : "Cadastro de Plataforma");
        lbTitulo.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;"); // Lindo Amarelo Ouro
        painelTitulo.getChildren().add(lbTitulo);
        return painelTitulo;
    }

    // Metodo auxiliar para criar labels do formulário com cor branca
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

        // Estilo Base plano integrado ao tema escuro
        btn.setStyle(
                "-fx-cursor: hand; " +
                        "-fx-background-color: #333742; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 6 16 6 16;"
        );

        // Comportamento dinâmico de Hover
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #434857; -fx-text-fill: #E5A93C; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 16 6 16;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-cursor: hand; -fx-background-color: #333742; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 6 16 6 16;"
        ));

        return btn;
    }
}