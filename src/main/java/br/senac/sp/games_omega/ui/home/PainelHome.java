package br.senac.sp.games_omega.ui.home;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PainelHome {
    public VBox criarPainelHome(){

        // 1. CONFIGURAÇÃO DO CONTAINER PRINCIPAL (DARK THEME)
        VBox painelPrincipal = new VBox();
        painelPrincipal.setAlignment(Pos.TOP_CENTER);
        painelPrincipal.setPadding(new Insets(15, 20, 20, 20));
        painelPrincipal.setSpacing(15);
        painelPrincipal.setStyle("-fx-background-color: #252830;"); // Cinza grafite moderno

        // 2. CABEÇALHO DA TELA (TÍTULO E LINHA DIVISÓRIA)
        VBox painelTitulo = new VBox();
        painelTitulo.setSpacing(10);
        painelTitulo.setPadding(new Insets(20));
        painelTitulo.setStyle("-fx-background-color: #252830;");

        Label lblTitulo = new Label("Seja Bem-Vindo!");
        lblTitulo.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;"); // Texto branco para alto contraste

        painelTitulo.getChildren().addAll(lblTitulo, new Separator());

        // 3. IDENTIDADE VISUAL CENTRAL (LOGO E RECONHECIMENTO DO APP)
        Image imgLogo = new Image(getClass().getResourceAsStream("/imagens/arcade.png"));
        ImageView ivLogo = new ImageView(imgLogo);
        ivLogo.setFitHeight(200);
        ivLogo.setPreserveRatio(true);

        Label lblNomeApp = new Label("Games Omega");
        lblNomeApp.setStyle("-fx-font-weight: bold; -fx-font-size: 36; -fx-text-fill: #E5A93C;"); // Amarelo/Ouro do logotipo

        Label lblDisc = new Label("Gerenciamento de Jogos");
        lblDisc.setStyle("-fx-font-weight: normal; -fx-font-size: 18; -fx-text-fill: #A0A5B5;"); // Cinza claro discreto

        // 4. PAINEL CENTRAL DE INFORMAÇÕES (CARTÃO DE SUPORTE)
        VBox painelCentral = new VBox();
        painelCentral.setAlignment(Pos.CENTER);
        painelCentral.setSpacing(8);
        painelCentral.setPadding(new Insets(15));
        painelCentral.setStyle(
                "-fx-background-color: #1E222B; " + // Fundo levemente mais escuro que o geral
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #333742; " +     // Borda sutil para dar profundidade
                        "-fx-border-radius: 8;"
        );
        painelCentral.setPrefHeight(120);
        painelCentral.setMaxWidth(450);

        // Componentes internos do cartão de suporte
        Label lblEmailTitulo = new Label("E-mail para suporte:");
        lblEmailTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #FFFFFF;");

        Label lblEmailVal = new Label("suporte@gamesomega.com.br");
        lblEmailVal.setStyle("-fx-font-size: 14; -fx-text-fill: #E5A93C; -fx-cursor: hand;");

        Label lblTelTitulo = new Label("Telefone para suporte:");
        lblTelTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #FFFFFF;");

        Label lblTelVal = new Label("(11) 91234-5678");
        lblTelVal.setStyle("-fx-font-size: 14; -fx-text-fill: #E5A93C;");

        painelCentral.getChildren().addAll(lblEmailTitulo, lblEmailVal, lblTelTitulo, lblTelVal);

        // 5. RODAPÉ DA TELA (DIREITOS AUTORAIS)
        Label lblRodape = new Label("Desenvolvido por Games Omega© - 2026");
        lblRodape.setStyle("-fx-font-size: 12; -fx-text-fill: #626875; -fx-font-weight: bold;");
        VBox.setMargin(lblRodape, new Insets(15, 0, 0, 0)); // Margem superior para separar do bloco de suporte

        // 6. MONTAGEM FINAL DA ESTRUTURA
        painelPrincipal.getChildren().addAll(
                painelTitulo,
                ivLogo,
                lblNomeApp,
                lblDisc,
                painelCentral,
                lblRodape
        );

        return painelPrincipal;
    }
}