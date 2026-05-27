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

        VBox raiz = new VBox();
        raiz.setStyle("-fx-background-color: #f4f4f4;");

        HBox painelTitulo = criarPainelTitulo();

        VBox painelFormulario = new VBox(15);
        painelFormulario.setPadding(new Insets(20));
        VBox.setMargin(painelFormulario, new Insets(15));
        painelFormulario.setStyle("-fx-background-color: #e9e9e9; -fx-border-color: #1563cd; -fx-border-width: 2;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);

        txtId = new TextField();
        txtId.setDisable(true);
        txtId.setPrefWidth(80);
        txtId.setMaxWidth(80);

        txtNome = new TextField();
        txtFabricante = new TextField();
        txtValor = new TextField();

        dpLancamento = new DatePicker();
        dpLancamento.setPrefWidth(200);
        dpLancamento.getEditor().setDisable(true);
        dpLancamento.getEditor().setStyle("-fx-opacity: 1; -fx-text-fill: black;");

        if (plataformaEmEdicao != null) {
            txtId.setText(String.valueOf(plataformaEmEdicao.getId()));
            txtNome.setText(plataformaEmEdicao.getNome());
            txtFabricante.setText(plataformaEmEdicao.getFabricante());
            txtValor.setText(String.valueOf(plataformaEmEdicao.getValor()));
            dpLancamento.setValue(plataformaEmEdicao.getDataLancamento());
        }

        grid.add(new Label("ID:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Nome Comercial:"), 0, 1);
        grid.add(txtNome, 1, 1);
        GridPane.setHgrow(txtNome, Priority.ALWAYS);
        grid.add(new Label("Fabricante:"), 0, 2);
        grid.add(txtFabricante, 1, 2);
        grid.add(new Label("Valor Estimado:"), 0, 3);
        grid.add(txtValor, 1, 3);
        grid.add(new Label("Lançamento:"), 0, 4);
        grid.add(dpLancamento, 1, 4);

        painelFormulario.getChildren().add(grid);

        HBox painelBotoes = new HBox(15);
        painelBotoes.setAlignment(Pos.CENTER_RIGHT);
        painelBotoes.setPadding(new Insets(0, 15, 15, 15));

        Button btnSalvar = criarBotaoIcone("/imagens/salvar.png");
        Button btnCancelar = criarBotaoIcone("/imagens/cancelar.png");

        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().trim().isEmpty() || txtFabricante.getText().trim().isEmpty() ||
                    txtValor.getText().trim().isEmpty() || dpLancamento.getValue() == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Campos Obrigatórios");
                alerta.setContentText("Por favor, preencha todos os campos corretamente!");
                alerta.showAndWait();
                return;
            }

            String valorTexto = txtValor.getText().trim().replace(",", ".");
            if (!valorTexto.matches("\\d+(\\.\\d{1,2})?")) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Valor Inválido");
                alerta.setContentText("Insira um preço de hardware válido.");
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
                alertStage.getIcons().add(new Image(getClass().getResourceAsStream(caminhoIcone)));
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

                    if (perguntaNovo.showAndWait().get() == btnSim) {
                        txtNome.clear(); txtFabricante.clear(); txtValor.clear(); dpLancamento.setValue(null);
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

        try {
            String iconeJanela = (plataformaEmEdicao != null) ? "/imagens/editar.png" : "/imagens/cadastro.png";
            stage.getIcons().add(new Image(getClass().getResourceAsStream(iconeJanela)));
        } catch (Exception ex) {}

        stage.setTitle(plataformaEmEdicao != null ? "Editar Plataforma" : "Cadastro de Plataforma");
        stage.showAndWait();
    }

    public void configurarModoEdicao(Plataforma p) { this.plataformaEmEdicao = p; }

    private HBox criarPainelTitulo() {
        HBox painelTitulo = new HBox(15);
        painelTitulo.setAlignment(Pos.CENTER_LEFT);
        painelTitulo.setPadding(new Insets(15));
        painelTitulo.setStyle("-fx-background-color: #0d3b3f;");

        try {
            String icone = (plataformaEmEdicao != null) ? "/imagens/editar.png" : "/imagens/cadastro.png";
            ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(icone)));
            iv.setFitHeight(30); iv.setFitWidth(30);
            painelTitulo.getChildren().add(iv);
        } catch (Exception e) {}

        Label lbTitulo = new Label(plataformaEmEdicao != null ? "Editar Plataforma" : "Cadastro de Plataforma");
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
        btn.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: transparent; " +
                "-fx-padding: 0; " +
                "-fx-cursor: hand;");
        return btn;
    }
}