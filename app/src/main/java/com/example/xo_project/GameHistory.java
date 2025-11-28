package com.example.xo_project;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GameHistory implements Serializable {
    private String date;
    private String player1Name;
    private String player2Name;
    private int player1Score;
    private int player2Score;
    private int totalRounds;
    private String winner;

    // Constructeur pour nouvelles parties
    public GameHistory(String player1Name, String player2Name,
                       int player1Score, int player2Score, int totalRounds) {
        this.date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.totalRounds = totalRounds;
        this.winner = determineWinner();
    }

    // Constructeur pour chargement depuis fichier
    public GameHistory(String date, String player1Name, String player2Name,
                       int player1Score, int player2Score, int totalRounds, String winner) {
        this.date = date;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.totalRounds = totalRounds;
        this.winner = winner;
    }

    private String determineWinner() {
        if (player1Score > player2Score) {
            return player1Name;
        } else if (player2Score > player1Score) {
            return player2Name;
        } else {
            return "Égalité";
        }
    }

    // Getters
    public String getDate() { return date; }
    public String getPlayer1Name() { return player1Name; }
    public String getPlayer2Name() { return player2Name; }
    public int getPlayer1Score() { return player1Score; }
    public int getPlayer2Score() { return player2Score; }
    public int getTotalRounds() { return totalRounds; }
    public String getWinner() { return winner; }

    @Override
    public String toString() {
        return date + " - " + player1Name + " " + player1Score + " | " +
                player2Name + " " + player2Score + " | Vainqueur: " + winner;
    }
}