package com.gothor.werefolfhelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class CardsActivity extends AppCompatActivity {

    private Game game;

    private int nbWerewolves;
    private boolean clairvoyantEnabled;
    private boolean littleGirlEnabled;

    private TextView werewolvesNumberText;
    private Button werewolvesMinusButton;
    private Button werewolvesPlusButton;
    private CheckBox clairvoyantCheckbox;
    private CheckBox littleGirlCheckbox;
    private Button submitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cards);

        game = Game.getGame(this);

        nbWerewolves = game.nbWerewolves;
        clairvoyantEnabled = game.clairvoyantEnabled;
        littleGirlEnabled = game.littleGirlEnabled;

        werewolvesNumberText = findViewById(R.id.werewolves_nb);
        werewolvesMinusButton = findViewById(R.id.werewolves_minus);
        werewolvesPlusButton = findViewById(R.id.werewolves_plus);
        clairvoyantCheckbox = findViewById(R.id.clairvoyant_enabled);
        littleGirlCheckbox = findViewById(R.id.little_girl_enabled);
        submitButton = findViewById(R.id.bt_submit);

        werewolvesNumberText.setText(nbWerewolves + "");
        clairvoyantCheckbox.setChecked(clairvoyantEnabled);
        littleGirlCheckbox.setChecked(littleGirlEnabled);

        werewolvesMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWerewolvesMinusButtonClick();
            }
        });

        werewolvesPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWerewolvesPlusButtonClick();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitButtonClick();
            }
        });
    }

    private void onWerewolvesMinusButtonClick() {
        if (nbWerewolves > 1) {
            nbWerewolves--;
        }
        werewolvesNumberText.setText(nbWerewolves + "");
    }

    private void onWerewolvesPlusButtonClick() {
        nbWerewolves++;
        werewolvesNumberText.setText(nbWerewolves + "");
    }

    private void onSubmitButtonClick() {
        game.nbWerewolves = nbWerewolves;
        game.clairvoyantEnabled = clairvoyantCheckbox.isChecked();
        game.littleGirlEnabled = littleGirlCheckbox.isChecked();
        game.save();
        onBackPressed();
    }
}
