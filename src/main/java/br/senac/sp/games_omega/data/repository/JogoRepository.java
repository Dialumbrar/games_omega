package br.senac.sp.games_omega.data.repository;

import br.senac.sp.games_omega.model.Jogo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class JogoRepository {

    public ObservableList<Jogo> getJogos() {
        // 1. Objeto: The Legend of Zelda: Tears of the Kingdom
        Jogo jogo1 = new Jogo(
                1,
                "The Legend of Zelda: Tears of the Kingdom",
                "Nintendo Switch",
                "Ação/Aventura",
                "Nintendo",
                299.90,
                LocalDate.of(2023, 5, 12),
                true
        );

        // 2. Objeto: God of War Ragnarök
        Jogo jogo2 = new Jogo(
                2,
                "God of War Ragnarök",
                "PlayStation 5",
                "Ação/Aventura",
                "Santa Monica Studio",
                349.90,
                LocalDate.of(2022, 11, 9),
                false
        );

        // 3. Objeto: Hollow Knight
        Jogo jogo3 = new Jogo(
                3,
                "Hollow Knight",
                "PC",
                "Metroidvania",
                "Team Cherry",
                46.99,
                LocalDate.of(2017, 2, 24),
                true
        );

        // Criando a lista observável do JavaFX
        ObservableList<Jogo> listaJogos = FXCollections.observableArrayList();

        // ADIÇÃO: Adicionando os objetos à lista
        listaJogos.addAll(jogo1, jogo2, jogo3);

        // Agora retornamos a lista preenchida em vez de null
        return listaJogos;
    }

    public void salvar(Jogo jogo){

    }
}