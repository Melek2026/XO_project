package com.example.xo_project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupérer les données passées
        String playerSymbol = getIntent().getStringExtra("PLAYER_SYMBOL");
        int numberOfGames = getIntent().getIntExtra("NUMBER_OF_GAMES", 5);

        // Implémenter la logique du jeu ici
    }
}