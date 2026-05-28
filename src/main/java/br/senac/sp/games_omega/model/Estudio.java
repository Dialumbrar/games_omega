package br.senac.sp.games_omega.model;

public class Estudio {
    private int id;
    private String nome;
    private String fundador;
    private int anoFundacao;
    private String paisOrigem;

    public Estudio(int id, String nome, String fundador, int anoFundacao, String paisOrigem) {
        this.id = id;
        this.nome = nome;
        this.fundador = fundador;
        this.anoFundacao = anoFundacao;
        this.paisOrigem = paisOrigem;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getFundador() { return fundador; }
    public void setFundador(String fundador) { this.fundador = fundador; }

    public int getAnoFundacao() { return anoFundacao; }
    public void setAnoFundacao(int anoFundacao) { this.anoFundacao = anoFundacao; }

    public String getPaisOrigem() { return paisOrigem; }
    public void setPaisOrigem(String paisOrigem) { this.paisOrigem = paisOrigem; }
}