package br.senac.sp.games_omega;

import br.senac.sp.games_omega.ui.home.PainelHome;
import br.senac.sp.games_omega.ui.jogos.PainelEstudio;
import br.senac.sp.games_omega.ui.jogos.PainelJogos;
import br.senac.sp.games_omega.ui.jogos.PainelPlataforma;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaPrincipal extends Application {

    // 1. CONSTANTES DE ESTILO ATUALIZADAS (TEMA ESCURO PREMIUM)
    private static final String COR_PADRAO = "-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;";
    private static final String COR_HOVER = "-fx-background-color: #252830; -fx-text-fill: #E5A93C; -fx-font-weight: bold;";

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane raiz = new BorderPane();

        // 2. CONFIGURAÇÃO DA BARRA LATERAL (CINZA BEM ESCURO - ESTILO STEAM)
        VBox painelLateral = new VBox();
        painelLateral.setSpacing(10);
        painelLateral.setPrefWidth(150);
        painelLateral.setStyle("-fx-background-color: #1E222B;"); // Substituído o azul vibrante por cinza profundo
        painelLateral.setPadding(new Insets(15));
        painelLateral.setAlignment(Pos.TOP_CENTER);

        // 3. ÍCONE OMEGA NA BARRA LATERAL
        Image imgOmega = new Image(getClass().getResourceAsStream("/imagens/omega.png"));
        ImageView ivOmega = new ImageView(imgOmega);
        ivOmega.setFitWidth(80);
        ivOmega.setPreserveRatio(true);
        VBox.setMargin(ivOmega, new Insets(0, 0, 20, 0));

        // 4. CRIAÇÃO DOS BOTÕES DO MENU
        Button btnHome = criarBotaoMenu("Home");
        Button btnJogos = criarBotaoMenu("Jogos");
        Button btnPlataformas = criarBotaoMenu("Plataforma");
        Button btnEstudios = criarBotaoMenu("Estúdios");

        // 5. EVENTOS DE CLIQUE (NAVEGAÇÃO DO BORDERPANE)

        // Botão Home
        btnHome.setOnAction(event -> {
            PainelHome painelHome = new PainelHome();
            raiz.setCenter(painelHome.criarPainelHome());
        });

        // Botão Jogos
        btnJogos.setOnAction(event -> {
            PainelJogos painelJogos = new PainelJogos();
            raiz.setCenter(painelJogos.criarPainelJogos());
        });

        // Botão Plataforma
        btnPlataformas.setOnAction(event -> {
            PainelPlataforma painelPlataforma = new PainelPlataforma();
            raiz.setCenter(painelPlataforma.criarPainelPlataformas());
        });

        // Botão Estúdios
        btnEstudios.setOnAction(event -> {
            PainelEstudio painelEstudio = new PainelEstudio();
            raiz.setCenter(painelEstudio.criarPainelEstudios());
        });

        // 6. MONTAGEM DA BARRA LATERAL E DA VIEW INICIAL
        painelLateral.getChildren().addAll(
                ivOmega,
                btnHome,
                btnJogos,
                btnPlataformas,
                btnEstudios
        );

        raiz.setLeft(painelLateral);

        // Define a Home como conteúdo inicial padrão
        PainelHome painelHome = new PainelHome();
        raiz.setCenter(painelHome.criarPainelHome());

        // 7. CONFIGURAÇÕES FINAIS DA JANELA DO SISTEMA
        Scene cena = new Scene(raiz, 950, 650); // Largura levemente aumentada para acomodar melhor os componentes

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));
        stage.setScene(cena);
        stage.setTitle("Games Omega");
        stage.show();

        // Ativa os novos efeitos visuais de seleção (Hover)
        aplicarEfeitoHover(btnHome, btnJogos, btnPlataformas, btnEstudios);
    }

    // 8. MÉTODOS AUXILIARES DE CONSTRUÇÃO VISUAL (BOTOES E INTERAÇÃO)
    private Button criarBotaoMenu(String texto) {
        Button botao = new Button(texto);
        botao.setPrefWidth(Double.MAX_VALUE);
        botao.setPadding(new Insets(10));
        botao.setStyle(COR_PADRAO + " -fx-cursor: hand; -fx-alignment: center;");
        return botao;
    }

    private void aplicarEfeitoHover(Button... botoes) {
        for (Button btn : botoes) {
            btn.setOnMouseEntered(event -> btn.setStyle(COR_HOVER + " -fx-cursor: hand;"));
            btn.setOnMouseExited(event -> btn.setStyle(COR_PADRAO + " -fx-cursor: hand;"));
        }
    }
}