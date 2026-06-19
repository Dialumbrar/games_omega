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
        txtId.setMaxWidth(80);
        txtId.setStyle("-fx-background-color: #333742; -fx-text-fill: #A0A5B5; -fx-opacity: 0.8;");

        txtNome = new TextField();
        txtNome.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");

        txtFundador = new TextField();
        txtFundador.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");

        txtAno = new TextField();
        txtAno.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");

        txtPais = new TextField();
        txtPais.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");

        if (estudioEmEdicao != null) {
            txtId.setText(String.valueOf(estudioEmEdicao.getId()));
            txtNome.setText(estudioEmEdicao.getNome());
            txtFundador.setText(estudioEmEdicao.getFundador());
            txtAno.setText(String.valueOf(estudioEmEdicao.getAnoFundacao()));
            txtPais.setText(estudioEmEdicao.getPaisOrigem());
        }

        // Mapeamento dos Labels usando o metodo customizado para cor branca estável
        grid.add(criarLabelFormulario("ID:"), 0, 0);
        grid.add(txtId, 1, 0);

        grid.add(criarLabelFormulario("Nome do Estúdio:"), 0, 1);
        grid.add(txtNome, 1, 1);
        GridPane.setHgrow(txtNome, Priority.ALWAYS);

        grid.add(criarLabelFormulario("Nome do Fundador:"), 0, 2);
        grid.add(txtFundador, 1, 2);

        grid.add(criarLabelFormulario("Ano de Fundação:"), 0, 3);
        grid.add(txtAno, 1, 3);

        grid.add(criarLabelFormulario("País de Origem:"), 0, 4);
        grid.add(txtPais, 1, 4);

        painelFormulario.getChildren().add(grid);

        // 3. PAINEL DE BOTÕES INFERIORES PADRONIZADOS (FLAT COM HOVER)
        HBox painelBotoes = new HBox(15);
        painelBotoes.setAlignment(Pos.CENTER_RIGHT);
        painelBotoes.setPadding(new Insets(0, 15, 15, 15));

        Button btnSalvar = criarBotaoFormulario("Salvar", "/imagens/salvar.png");
        Button btnCancelar = criarBotaoFormulario("Cancelar", "/imagens/cancelar.png");

        // --- AÇÃO DO BOTÃO SALVAR ---
        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().trim().isEmpty() || txtFundador.getText().trim().isEmpty() ||
                    txtAno.getText().trim().isEmpty() || txtPais.getText().trim().isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Por favor, preencha todos os campos!");
                try { ((Stage) alerta.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch(Exception ex){}
                alerta.showAndWait();
                return;
            }

            if (!txtAno.getText().trim().matches("\\d{4}")) {
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Ano de fundação inválido (Use 4 dígitos).");
                try { ((Stage) alerta.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch(Exception ex){}
                alerta.showAndWait();
                return;
            }

            ButtonType btnSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType btnNao = new ButtonType("Não", ButtonBar.ButtonData.NO);
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "", btnSim, btnNao);
            confirmacao.setTitle("Confirmar Operação");
            confirmacao.setContentText(estudioEmEdicao != null ? "Deseja salvar as alterações deste estúdio?" : "Deseja cadastrar o estúdio?");

            try { ((Stage) confirmacao.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch(Exception ex){}

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
                try { ((Stage) perguntaNovo.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png"))); } catch(Exception ex){}

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

        Scene cena = new Scene(raiz, 550, 430);
        stage.setScene(cena);
        stage.setResizable(false);

        // Barra de título externa mantém a logo oficial estável
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
        } catch (Exception ex) {}

        stage.setTitle(estudioEmEdicao != null ? "Editar Estúdio" : "Cadastro de Estúdio");
        stage.showAndWait();
    }

    public void configurarModoEdicao(Estudio e) { this.estudioEmEdicao = e; }

    // CABEÇALHO ATUALIZADO: ALTERAÇÃO DINÂMICA DE ÍCONE E CORREÇÃO DO TEXTO DO JOGO PARA ESTÚDIO
    private HBox criarPainelTitulo() {
        HBox painelTitulo = new HBox(15);
        painelTitulo.setAlignment(Pos.CENTER_LEFT);
        painelTitulo.setPadding(new Insets(15));
        painelTitulo.setStyle("-fx-background-color: #1E222B; -fx-border-color: #333742; -fx-border-width: 0 0 1 0;");

        try {
            // Mapeia dinamicamente o ícone correto de acordo com a operação sendo executada
            String iconeInterno = (estudioEmEdicao != null) ? "/imagens/editar.png" : "/imagens/cadastro.png";

            Image image = new Image(getClass().getResourceAsStream(iconeInterno));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            painelTitulo.getChildren().add(imageView);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone dinâmico do título do cabeçalho");
        }

        Label lbTitulo = new Label(estudioEmEdicao != null ? "Editar Estúdio Selecionado" : "Cadastro de Estúdio");
        lbTitulo.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
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