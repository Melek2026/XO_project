package com.example.xo_project;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    // Variables du jeu
    private Button[][] cells = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount = 0;
    private int player1Score = 0;
    private int player2Score = 0;
    private int currentRound = 1;
    private int totalRounds = 5;
    private int totalMatches = 1; // Compteur de matches

    // Données des joueurs
    private String playerSymbol;
    private String player1Name = "Joueur 1";
    private String player2Name = "Joueur 2";
    private String player1Symbol = "X";
    private String player2Symbol = "O";

    // Views
    private TextView roundText;
    private TextView player1ScoreText;
    private TextView player2ScoreText;
    private TextView turnIndicator;
    private TextView matchesText; // TextView pour les matches
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Récupérer les données passées
        playerSymbol = getIntent().getStringExtra("PLAYER_SYMBOL");
        totalRounds = getIntent().getIntExtra("NUMBER_OF_GAMES", 5);

        initializeViews();
        setupPlayerSymbols();
        setupCellListeners();
        updateUI();
    }

    private void initializeViews() {
        roundText = findViewById(R.id.roundText);
        player1ScoreText = findViewById(R.id.player1Score);
        player2ScoreText = findViewById(R.id.player2Score);
        turnIndicator = findViewById(R.id.turnIndicator);
        backButton = findViewById(R.id.backButton);

        // CORRECTION : Initialiser le TextView pour les matches
        matchesText = findViewById(R.id.matchesText);

        // DEBUG : Vérifier si le TextView est trouvé
        if (matchesText == null) {
            // Créer un TextView temporaire si l'ID n'existe pas
            matchesText = new TextView(this);
            matchesText.setText("Matches DEBUG: " + totalMatches);
        }

        backButton.setOnClickListener(v -> finish());
    }

    private void setupPlayerSymbols() {
        if (playerSymbol != null) {
            if (playerSymbol.equals("X")) {
                player1Symbol = "X";
                player2Symbol = "O";
                player1Name = "Joueur 1 (X)";
                player2Name = "Joueur 2 (O)";
            } else {
                player1Symbol = "O";
                player2Symbol = "X";
                player1Name = "Joueur 1 (O)";
                player2Name = "Joueur 2 (X)";
            }
        }
    }

    private void setupCellListeners() {
        cells[0][0] = findViewById(R.id.cell00);
        cells[0][1] = findViewById(R.id.cell01);
        cells[0][2] = findViewById(R.id.cell02);
        cells[1][0] = findViewById(R.id.cell10);
        cells[1][1] = findViewById(R.id.cell11);
        cells[1][2] = findViewById(R.id.cell12);
        cells[2][0] = findViewById(R.id.cell20);
        cells[2][1] = findViewById(R.id.cell21);
        cells[2][2] = findViewById(R.id.cell22);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setOnClickListener(this);
                updateCellDescription(cells[i][j], "");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        playMove((Button) v);
    }

    private void playMove(Button cell) {
        String currentSymbol;
        int textColor;

        if (player1Turn) {
            currentSymbol = player1Symbol;
            textColor = player1Symbol.equals("X") ?
                    getResources().getColor(android.R.color.holo_blue_dark) :
                    getResources().getColor(android.R.color.holo_red_dark);
        } else {
            currentSymbol = player2Symbol;
            textColor = player2Symbol.equals("X") ?
                    getResources().getColor(android.R.color.holo_blue_dark) :
                    getResources().getColor(android.R.color.holo_red_dark);
        }

        cell.setText(currentSymbol);
        cell.setTextColor(textColor);
        updateCellDescription(cell, currentSymbol);

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
            updateTurnIndicator();
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = cells[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1Wins() {
        player1Score++;
        totalMatches++; // INCREMENTATION
        updateScores();
        updateMatchesText(); // METTRE À JOUR IMMÉDIATEMENT
        showWinnerDialog(player1Name + " gagne cette manche !");
        prepareNextRound();
    }

    private void player2Wins() {
        player2Score++;
        totalMatches++; // INCREMENTATION
        updateScores();
        updateMatchesText(); // METTRE À JOUR IMMÉDIATEMENT
        showWinnerDialog(player2Name + " gagne cette manche !");
        prepareNextRound();
    }

    private void draw() {
        totalMatches++; // INCREMENTATION
        updateMatchesText(); // METTRE À JOUR IMMÉDIATEMENT
        showWinnerDialog("Match nul !");
        prepareNextRound();
    }

    private void showWinnerDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Continuer", (dialog, which) -> {
                    // S'assurer que l'UI est mise à jour après la fermeture du dialog
                    updateUI();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void prepareNextRound() {
        currentRound++;

        if (currentRound > totalRounds) {
            endGame();
        } else {
            new android.os.Handler().postDelayed(this::resetBoard, 1000);
        }
    }

    private void endGame() {
        String winnerMessage;
        if (player1Score > player2Score) {
            winnerMessage = player1Name + " gagne le tournoi !";
        } else if (player2Score > player1Score) {
            winnerMessage = player2Name + " gagne le tournoi !";
        } else {
            winnerMessage = "Le tournoi se termine par une égalité !";
        }

        // SAUVEGARDE SÉCURISÉE
        try {
            GameHistory gameHistory = new GameHistory(
                    player1Name,
                    player2Name,
                    player1Score,
                    player2Score,
                    totalRounds
            );
            FileManager.saveGameHistory(this, gameHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tournoi Terminé")
                .setMessage(winnerMessage + "\n\nScore Final:\n" +
                        player1Name + ": " + player1Score + "\n" +
                        player2Name + ": " + player2Score + "\n\n" +
                        "Historique sauvegardé!")
                .setCancelable(false)
                .setPositiveButton("Nouveau Tournoi", (dialog, which) -> newGame())
                .setNegativeButton("Quitter", (dialog, which) -> finish());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void newGame() {
        player1Score = 0;
        player2Score = 0;
        currentRound = 1;
        // Ne pas réinitialiser totalMatches pour garder le compteur global
        resetBoard();
        updateUI();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setText("");
                cells[i][j].setBackgroundResource(R.drawable.cell_background);
                updateCellDescription(cells[i][j], "");
            }
        }

        roundCount = 0;
        player1Turn = true;
        updateTurnIndicator();
    }

    private void updateUI() {
        roundText.setText(currentRound + "/" + totalRounds + " Manches");
        updateScores();
        updateTurnIndicator();
        updateMatchesText(); // METTRE À JOUR L'AFFICHAGE DES MATCHES
    }

    private void updateScores() {
        player1ScoreText.setText(String.valueOf(player1Score));
        player2ScoreText.setText(String.valueOf(player2Score));
    }

    // CORRECTION : Méthode pour mettre à jour l'affichage des matches
    private void updateMatchesText() {
        if (matchesText != null) {
            matchesText.setText("Matches " + totalMatches);
        } else {
            // Debug : afficher dans les logs
            System.out.println("DEBUG: totalMatches = " + totalMatches);
        }
    }

    private void updateTurnIndicator() {
        if (player1Turn) {
            turnIndicator.setText(player1Name + " joue");
            turnIndicator.setTextColor(player1Symbol.equals("X") ?
                    getResources().getColor(android.R.color.holo_blue_dark) :
                    getResources().getColor(android.R.color.holo_red_dark));
        } else {
            turnIndicator.setText(player2Name + " joue");
            turnIndicator.setTextColor(player2Symbol.equals("X") ?
                    getResources().getColor(android.R.color.holo_blue_dark) :
                    getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    private void updateCellDescription(Button cell, String symbol) {
        String description = "";
        String cellId = "";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cell == cells[i][j]) {
                    cellId = "Ligne " + (i + 1) + " Colonne " + (j + 1);
                    break;
                }
            }
        }

        if (symbol.equals("")) {
            description = cellId + ", vide";
        } else {
            description = cellId + ", contient " + symbol;
        }

        cell.setContentDescription(description);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("player1Turn", player1Turn);
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Score", player1Score);
        outState.putInt("player2Score", player2Score);
        outState.putInt("currentRound", currentRound);
        outState.putString("player1Symbol", player1Symbol);
        outState.putString("player2Symbol", player2Symbol);
        outState.putInt("totalMatches", totalMatches);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        player1Turn = savedInstanceState.getBoolean("player1Turn");
        roundCount = savedInstanceState.getInt("roundCount");
        player1Score = savedInstanceState.getInt("player1Score");
        player2Score = savedInstanceState.getInt("player2Score");
        currentRound = savedInstanceState.getInt("currentRound");
        player1Symbol = savedInstanceState.getString("player1Symbol");
        player2Symbol = savedInstanceState.getString("player2Symbol");
        totalMatches = savedInstanceState.getInt("totalMatches");

        updateUI();
    }
    private void showSavedScores() {
        try {
            List<GameHistory> gameHistory = FileManager.loadGameHistory(this);

            if (gameHistory.isEmpty()) {
                showNoHistoryDialog();
            } else {
                showHistoryDialog(gameHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog();
        }
    }

    private void showNoHistoryDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Historique des Jeux")
                .setMessage("Aucun historique de jeu.\n\nJouez votre premier tournoi!")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Erreur")
                .setMessage("Impossible de charger l'historique.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showHistoryDialog(List<GameHistory> gameHistory) {
        StringBuilder historyText = new StringBuilder();
        historyText.append("Total des parties: ").append(gameHistory.size()).append("\n\n");

        for (int i = 0; i < gameHistory.size(); i++) {
            GameHistory history = gameHistory.get(i);
            historyText.append("Partie ").append(i + 1).append(":\n")
                    .append("Date: ").append(history.getDate()).append("\n")
                    .append(history.getPlayer1Name()).append(": ").append(history.getPlayer1Score()).append("\n")
                    .append(history.getPlayer2Name()).append(": ").append(history.getPlayer2Score()).append("\n")
                    .append("Vainqueur: ").append(history.getWinner()).append("\n\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Historique des Tournois")
                .setMessage(historyText.toString())
                .setPositiveButton("OK", null)
                .setNegativeButton("Effacer", (dialog, which) -> clearHistory())
                .show();
    }

    private void clearHistory() {
        new AlertDialog.Builder(this)
                .setTitle("Effacer l'historique")
                .setMessage("Êtes-vous sûr de vouloir effacer tout l'historique?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    FileManager.clearGameHistory(this);
                    Toast.makeText(this, "Historique effacé!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", null)
                .show();
    }

}