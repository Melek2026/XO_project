package com.example.xo_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RadioGroup symbolRadioGroup;
    private Spinner gamesSpinner;
    private Button playButton, rulesButton, scoresButton;
    private TextView savedScoresText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupSpinner();
        setupButtonListeners();
    }

    private void initializeViews() {
        symbolRadioGroup = findViewById(R.id.symbolRadioGroup);
        gamesSpinner = findViewById(R.id.gamesSpinner);
        playButton = findViewById(R.id.playButton);
        rulesButton = findViewById(R.id.rulesButton);
        scoresButton = findViewById(R.id.scoresButton);
        savedScoresText = findViewById(R.id.savedScoresText);
    }

    private void setupSpinner() {
        String[] gamesOptions = {"5 parties", "10 parties", "15 parties"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gamesOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gamesSpinner.setAdapter(adapter);
    }

    private void setupButtonListeners() {
        playButton.setOnClickListener(v -> startGame());
        rulesButton.setOnClickListener(v -> showGameRules());
        scoresButton.setOnClickListener(v -> showSavedScores());
    }

    private void startGame() {
        int selectedId = symbolRadioGroup.getCheckedRadioButtonId();
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
        Intent intent = new Intent(MainActivity.this, RulesActivity.class);
        startActivity(intent);
    }

    private void showSavedScores() {
        savedScoresText.setVisibility(View.VISIBLE);
        // Logique de chargement des scores à implémenter
    }
}