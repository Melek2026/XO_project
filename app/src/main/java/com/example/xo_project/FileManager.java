package com.example.xo_project;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "game_history.txt";
    private static final String TAG = "FileManager";

    // Sauvegarder une partie
    public static void saveGameHistory(Context context, GameHistory gameHistory) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            String line = gameHistory.getDate() + "|" +
                    gameHistory.getPlayer1Name() + "|" +
                    gameHistory.getPlayer2Name() + "|" +
                    gameHistory.getPlayer1Score() + "|" +
                    gameHistory.getPlayer2Score() + "|" +
                    gameHistory.getTotalRounds() + "|" +
                    gameHistory.getWinner() + "\n";
            fos.write(line.getBytes());
            fos.close();
            Log.d(TAG, "Partie sauvegardée: " + gameHistory.getDate());
        } catch (Exception e) {
            Log.e(TAG, "Erreur sauvegarde: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Charger l'historique
    public static List<GameHistory> loadGameHistory(Context context) {
        List<GameHistory> historyList = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length == 7) {
                        GameHistory history = new GameHistory(
                                parts[0], // date
                                parts[1], // player1Name
                                parts[2], // player2Name
                                Integer.parseInt(parts[3]), // player1Score
                                Integer.parseInt(parts[4]), // player2Score
                                Integer.parseInt(parts[5]), // totalRounds
                                parts[6]  // winner
                        );
                        historyList.add(history);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erreur ligne: " + line + " - " + e.getMessage());
                }
            }

            reader.close();
            fis.close();
            Log.d(TAG, "Historique chargé: " + historyList.size() + " parties");

        } catch (Exception e) {
            Log.d(TAG, "Aucun historique trouvé (fichier inexistant)");
        }

        return historyList;
    }

    // Effacer l'historique
    public static void clearGameHistory(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write("".getBytes());
            fos.close();
            Log.d(TAG, "Historique effacé");
        } catch (Exception e) {
            Log.e(TAG, "Erreur effacement: " + e.getMessage());
        }
    }
}