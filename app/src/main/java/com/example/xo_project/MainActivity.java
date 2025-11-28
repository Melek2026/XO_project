package com.example.xo_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> calculationHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSpinner();
        setupButtonListeners();
    }

    private void setupSpinner() {
        Spinner gamesSpinner = findViewById(R.id.gamesSpinner);
        String[] gamesOptions = {"5 parties", "10 parties", "15 parties"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gamesOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gamesSpinner.setAdapter(adapter);
    }

    private void setupButtonListeners() {
        Button playButton = findViewById(R.id.playButton);
        Button rulesButton = findViewById(R.id.rulesButton);
        Button scoresButton = findViewById(R.id.scoresButton);

        playButton.setOnClickListener(v -> startGame());
        rulesButton.setOnClickListener(v -> showGameRules());
        scoresButton.setOnClickListener(v -> showSavedScores());
    }

    private void startGame() {
        RadioGroup symbolRadioGroup = findViewById(R.id.symbolRadioGroup);
        Spinner gamesSpinner = findViewById(R.id.gamesSpinner);

        int selectedId = symbolRadioGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            Toast.makeText(this, "Symbole X sélectionné par défaut", Toast.LENGTH_SHORT).show();
            selectedId = R.id.radioX;
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        String playerSymbol = selectedRadioButton.getText().toString();

        String gamesSelected = gamesSpinner.getSelectedItem().toString();
        int numberOfGames = Integer.parseInt(gamesSelected.split(" ")[0]);

        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("PLAYER_SYMBOL", playerSymbol);
        intent.putExtra("NUMBER_OF_GAMES", numberOfGames);
        startActivity(intent);
    }

    private void showGameRules() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_game_rules, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        ImageButton closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void showSavedScores() {
        try {
            Log.d("DEBUG", "showSavedScores called");

            List<GameHistory> gameHistory = FileManager.loadGameHistory(this);
            Log.d("DEBUG", "History loaded: " + gameHistory.size() + " items");

            if (gameHistory.isEmpty()) {
                Log.d("DEBUG", "Showing no history dialog");
                showNoHistoryDialog();
            } else {
                Log.d("DEBUG", "Showing history dialog");
                showHistoryDialog(gameHistory);
            }
        } catch (Exception e) {
            Log.e("DEBUG", "CRASH in showSavedScores: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showNoHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aucun Historique")
                .setMessage("Aucune partie enregistrée pour le moment.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showHistoryDialog(List<GameHistory> gameHistory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Historique des Parties");

        if (gameHistory.isEmpty()) {
            builder.setMessage("Aucune partie enregistrée pour le moment.");
        } else {
            // Créer un tableau avec les éléments de l'historique des parties
            String[] historyArray = new String[gameHistory.size()];
            for (int i = 0; i < gameHistory.size(); i++) {
                GameHistory history = gameHistory.get(i);
                historyArray[i] = formatGameHistory(history);
            }

            builder.setItems(historyArray, (dialog, which) -> {
                // Quand on clique sur un élément de l'historique
                GameHistory selectedGame = gameHistory.get(which);
                showGameDetails(selectedGame);
            });
        }

        builder.setPositiveButton("Fermer", null)
                .setNeutralButton("Effacer l'historique", (dialog, which) -> {
                    clearGameHistory();
                })
                .show();
    }

    private String formatGameHistory(GameHistory history) {
        // Formater l'affichage d'une partie dans la liste
        // Utilisation des vrais getters de GameHistory
        return String.format("%s - %s %d:%d %s",
                history.getDate(),
                history.getPlayer1Name(),
                history.getPlayer1Score(),
                history.getPlayer2Score(),
                history.getPlayer2Name());
    }

    private void showGameDetails(GameHistory game) {
        // Afficher les détails d'une partie spécifique
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Détails de la Partie")
                .setMessage(formatGameDetails(game))
                .setPositiveButton("OK", null)
                .show();
    }

    private String formatGameDetails(GameHistory game) {
        // Formater les détails complets d'une partie avec les vrais getters
        return String.format(
                "Date: %s\n" +
                        "Joueur 1: %s\n" +
                        "Joueur 2: %s\n" +
                        "Score: %d - %d\n" +
                        "Nombre de rounds: %d\n" +
                        "Gagnant: %s",
                game.getDate(),
                game.getPlayer1Name(),
                game.getPlayer2Name(),
                game.getPlayer1Score(),
                game.getPlayer2Score(),
                game.getTotalRounds(),
                game.getWinner()
        );
    }

    private void clearGameHistory() {
        try {
            FileManager.clearGameHistory(this);
            Toast.makeText(this, "Historique effacé", Toast.LENGTH_SHORT).show();

            // Recharger l'activité pour mettre à jour l'affichage
            recreate();
        } catch (Exception e) {
            Log.e("DEBUG", "Error clearing history: " + e.getMessage());
            Toast.makeText(this, "Erreur lors de l'effacement", Toast.LENGTH_SHORT).show();
        }
    }

    // Méthodes pour l'historique des calculs (si vous en avez besoin pour autre chose)
    private void addToHistory(String calculation) {
        calculationHistory.add(0, calculation);
        if (calculationHistory.size() > 50) {
            calculationHistory.remove(calculationHistory.size() - 1);
        }
    }

    private void clearCalculationHistory() {
        calculationHistory.clear();
        Toast.makeText(this, "Historique des calculs effacé", Toast.LENGTH_SHORT).show();
    }

    private void useCalculationFromHistory(String calculation) {
        Toast.makeText(this, "Sélectionné: " + calculation, Toast.LENGTH_SHORT).show();
    }
}