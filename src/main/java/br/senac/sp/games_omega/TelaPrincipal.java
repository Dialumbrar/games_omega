package br.senac.sp.games_omega;

import br.senac.sp.games_omega.ui.home.PainelHome;
import br.senac.sp.games_omega.ui.jogos.PainelJogos;
import br.senac.sp.games_omega.ui.jogos.PainelPlataforma; // IMPORTAÇÃO DA NOVA CLASSE ADICIONADA
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

    // Constantes de estilo para manter o padrão visual
    private static final String COR_PADRAO = "-fx-background-color: transparent; -fx-text-fill: #ffffff;";
    private static final String COR_HOVER = "-fx-background-color: #ffffff; -fx-text-fill: #1563cd;";

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane raiz = new BorderPane();

        // Configuração da barra lateral azul
        VBox painelLateral = new VBox();
        painelLateral.setSpacing(10);
        painelLateral.setPrefWidth(150);
        painelLateral.setStyle("-fx-background-color:#1563cd");
        painelLateral.setPadding(new Insets(15));
        painelLateral.setAlignment(Pos.TOP_CENTER);

        // --- ÍCONE OMEGA NA BARRA LATERAL ---
        Image imgOmega = new Image(getClass().getResourceAsStream("/imagens/omega.png"));
        ImageView ivOmega = new ImageView(imgOmega);
        ivOmega.setFitWidth(80);
        ivOmega.setPreserveRatio(true);
        VBox.setMargin(ivOmega, new Insets(0, 0, 20, 0)); // Espaço abaixo do ícone

        // Criação dos botões usando o metodo auxiliar
        Button btnHome = criarBotaoMenu("Home");
        Button btnJogos = criarBotaoMenu("Jogos");
        Button btnPlataformas = criarBotaoMenu("Plataforma");
        Button btnEstudios = criarBotaoMenu("Estúdios");

        // --- AÇÕES DOS BOTÕES (EVENTOS DE CLIQUE) ---

        btnHome.setOnAction(event -> {
            PainelHome painelHome = new PainelHome();
            raiz.setCenter(painelHome.criarPainelHome());
        });

        btnJogos.setOnAction(event -> {
            PainelJogos painelJogos = new PainelJogos();
            raiz.setCenter(painelJogos.criarPainelJogos());
        });

        // --- BOTÃO PLATAFORMA ---
        btnPlataformas.setOnAction(event -> {
            // Instancia a classe de gerenciamento de hardware
            PainelPlataforma painelPlataforma = new PainelPlataforma();

            // Renderiza e joga o novo painel cinza com a tabela no centro da tela
            raiz.setCenter(painelPlataforma.criarPainelPlataformas());
        });

        // Adicionando elementos ao menu lateral
        painelLateral.getChildren().addAll(
                ivOmega,
                btnHome,
                btnJogos,
                btnPlataformas,
                btnEstudios
        );

        raiz.setLeft(painelLateral);

        // Define o conteúdo inicial da tela (Home)
        PainelHome painelHome = new PainelHome();
        raiz.setCenter(painelHome.criarPainelHome());

        // Configurações da Janela Principal
        Scene cena = new Scene(raiz, 900, 600);

        // Ícone da barra de título (janela)
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagens/omega.png")));

        stage.setScene(cena);
        stage.setTitle("Games Omega");
        stage.show();

        // Ativa os efeitos visuais de seleção (Hover)
        aplicarEfeitoHover(btnHome, btnJogos, btnPlataformas, btnEstudios);
    }

    private Button criarBotaoMenu(String texto) {
        Button botao = new Button(texto);
        botao.setPrefWidth(Double.MAX_VALUE);
        botao.setPadding(new Insets(10));
        botao.setStyle(COR_PADRAO + "-fx-cursor: hand; -fx-alignment: center;");
        return botao;
    }

    private void aplicarEfeitoHover(Button... botoes) {
        for (Button btn : botoes) {
            btn.setOnMouseEntered(event -> btn.setStyle(COR_HOVER + "-fx-cursor: hand;"));
            btn.setOnMouseExited(event -> btn.setStyle(COR_PADRAO + "-fx-cursor: hand;"));
        }
    }
}