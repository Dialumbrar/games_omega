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
        // Container principal que sustenta ttodo o conteúdo da Home
        VBox painelPrincipal = new VBox();
        painelPrincipal.setAlignment(Pos.TOP_CENTER);
        painelPrincipal.setPadding(new Insets(15, 20, 20, 20));
        painelPrincipal.setSpacing(10); // Espaçamento entre os elementos
        painelPrincipal.setStyle("-fx-background-color: #2F3336;");

        VBox painelTitulo = new VBox();
        painelTitulo.setSpacing(10);
        painelTitulo.setPadding(new Insets(20));
        painelTitulo.setStyle("-fx-background-color: #2F3336");

        Label lblTitulo = new Label("Seja Bem-Vindo!");
        lblTitulo.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: #1563cd;");

        painelTitulo.getChildren().addAll(lblTitulo, new Separator());

        // Carregamento da imagem central (Joystick)
        Image imgLogo = new Image(getClass().getResourceAsStream("/imagens/joystick.png"));
        ImageView ivLogo = new ImageView(imgLogo);
        ivLogo.setFitHeight(200);
        ivLogo.setPreserveRatio(true);

        // Identificação da Aplicação (Admin removido)
        Label lblNomeApp = new Label("Games Omega");
        lblNomeApp.setStyle("-fx-font-weight: bold; -fx-font-size: 36; -fx-text-fill: #C2B98A;");

        Label lblDisc = new Label("Gerenciamento de Jogos");
        lblDisc.setStyle("-fx-font-weight: normal; -fx-font-size: 18; -fx-text-fill: #C2B98A;");

        // Painel informativo inferior
        VBox painelCentral = new VBox();
        painelCentral.setStyle("-fx-background-color: #C2B98A; -fx-background-radius: 10;");
        painelCentral.setPrefHeight(100);
        painelCentral.setMaxWidth(500);

        // Montagem final do layout
        painelPrincipal.getChildren().addAll(
                painelTitulo,
                ivLogo,
                lblNomeApp,
                lblDisc,
                painelCentral
        );

        return painelPrincipal;
    }
}