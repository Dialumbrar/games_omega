package br.senac.sp.games_omega.ui.jogos;

import br.senac.sp.games_omega.data.repository.EstudioRepository;
import br.senac.sp.games_omega.model.Estudio;
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

public class TelaEstudio {

    private final EstudioRepository repository = new EstudioRepository();
    private TextField txtId, txtNome, txtFundador, txtAno, txtPais;
    private Estudio estudioEmEdicao = null;

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
        grid.setHgap(10); grid.setVgap(12);

        txtId = new TextField(); txtId.setDisable(true); txtId.setMaxWidth(80);
        txtNome = new TextField();
        txtFundador = new TextField();
        txtAno = new TextField();
        txtPais = new TextField();

        if (estudioEmEdicao != null) {
            txtId.setText(String.valueOf(estudioEmEdicao.getId()));
            txtNome.setText(estudioEmEdicao.getNome());
            txtFundador.setText(estudioEmEdicao.getFundador());
            txtAno.setText(String.valueOf(estudioEmEdicao.getAnoFundacao()));
            txtPais.setText(estudioEmEdicao.getPaisOrigem());
        }

        grid.add(new Label("ID:"), 0, 0); grid.add(txtId, 1, 0);
        grid.add(new Label("Nome do Estúdio:"), 0, 1); grid.add(txtNome, 1, 1);
        GridPane.setHgrow(txtNome, Priority.ALWAYS);
        grid.add(new Label("Nome do Fundador:"), 0, 2); grid.add(txtFundador, 1, 2);
        grid.add(new Label("Ano de Fundação:"), 0, 3); grid.add(txtAno, 1, 3);
        grid.add(new Label("País de Origem:"), 0, 4); grid.add(txtPais, 1, 4);

        painelFormulario.getChildren().add(grid);

        HBox painelBotoes = new HBox(15);
        painelBotoes.setAlignment(Pos.CENTER_RIGHT);
        painelBotoes.setPadding(new Insets(0, 15, 15, 15));

        Button btnSalvar = criarBotaoIcone("/imagens/salvar.png");
        Button btnCancelar = criarBotaoIcone("/imagens/cancelar.png");

        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().trim().isEmpty() || txtFundador.getText().trim().isEmpty() ||
                    txtAno.getText().trim().isEmpty() || txtPais.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!").showAndWait();
                return;
            }

            if (!txtAno.getText().trim().matches("\\d{4}")) {
                new Alert(Alert.AlertType.ERROR, "Ano de fundação inválido (Use 4 dígitos).").showAndWait();
                return;
            }

            ButtonType btnSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType btnNao = new ButtonType("Não", ButtonBar.ButtonData.NO);
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "", btnSim, btnNao);
            confirmacao.setTitle("Confirmar Operação");
            confirmacao.setContentText(estudioEmEdicao != null ? "Deseja salvar as alterações?" : "Deseja cadastrar o estúdio?");

            if (confirmacao.showAndWait().get() != btnSim) return;

            String nome = txtNome.getText().trim();
            String fundador = txtFundador.getText().trim();
            int ano = Integer.parseInt(txtAno.getText().trim());
            String pais = txtPais.getText().trim();

            int idAtual = (estudioEmEdicao != null) ? estudioEmEdicao.getId() : 0;
            Estudio estObj = new Estudio(idAtual, nome, fundador, ano, pais);

            if (estudioEmEdicao != null) {
                repository.atualizar(estObj);
                stage.close();
            } else {
                repository.salvar(estObj);
                Alert perguntaNovo = new Alert(Alert.AlertType.CONFIRMATION, "Deseja cadastrar outro estúdio?", btnSim, btnNao);
                if (perguntaNovo.showAndWait().get() == btnSim) {
                    txtNome.clear(); txtFundador.clear(); txtAno.clear(); txtPais.clear(); txtNome.requestFocus();
                } else {
                    stage.close();
                }
            }
        });

        btnCancelar.setOnAction(e -> stage.close());
        painelBotoes.getChildren().addAll(btnSalvar, btnCancelar);
        raiz.getChildren().addAll(painelTitulo, painelFormulario, painelBotoes);

        Scene cena = new Scene(raiz, 550, 420);
        stage.setScene(cena);
        stage.setResizable(false);

        try {
            String icone = (estudioEmEdicao != null) ? "/imagens/editar.png" : "/imagens/cadastro.png";
            stage.getIcons().add(new Image(getClass().getResourceAsStream(icone)));
        } catch (Exception ex) {}

        stage.setTitle(estudioEmEdicao != null ? "Editar Estúdio" : "Cadastro de Estúdio");
        stage.showAndWait();
    }

    public void configurarModoEdicao(Estudio e) { this.estudioEmEdicao = e; }

    private HBox criarPainelTitulo() {
        HBox painelTitulo = new HBox(15);
        painelTitulo.setAlignment(Pos.CENTER_LEFT); painelTitulo.setPadding(new Insets(15));
        painelTitulo.setStyle("-fx-background-color: #0d3b3f;");

        try {
            String icone = (estudioEmEdicao != null) ? "/imagens/editar.png" : "/imagens/cadastro.png";
            ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(icone)));
            iv.setFitHeight(30); iv.setFitWidth(30);
            painelTitulo.getChildren().add(iv);
        } catch (Exception e) {}

        Label lbTitulo = new Label(estudioEmEdicao != null ? "Editar Estúdio" : "Cadastro de Estúdio");
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