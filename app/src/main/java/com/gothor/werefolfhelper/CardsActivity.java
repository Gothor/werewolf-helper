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

    private TextView werewolvesNumberText;
    private CheckBox clairvoyantCheckbox;
    private CheckBox littleGirlCheckbox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cards);

        game = Game.getGame(this);

        nbWerewolves = game.nbWerewolves;
        boolean clairvoyantEnabled = game.clairvoyantEnabled;
        boolean littleGirlEnabled = game.littleGirlEnabled;

        werewolvesNumberText = findViewById(R.id.werewolves_nb);
        Button werewolvesMinusButton = findViewById(R.id.werewolves_minus);
        Button werewolvesPlusButton = findViewById(R.id.werewolves_plus);
        clairvoyantCheckbox = findViewById(R.id.clairvoyant_enabled);
        littleGirlCheckbox = findViewById(R.id.little_girl_enabled);
        Button submitButton = findViewById(R.id.bt_submit);

        werewolvesNumberText.setText(String.valueOf(nbWerewolves));
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
        if (nbWerewolves > 2) {
            nbWerewolves--;
            werewolvesNumberText.setText(String.valueOf(nbWerewolves));
        }
    }

    private void onWerewolvesPlusButtonClick() {
        if (nbWerewolves < 4) {
            nbWerewolves++;
            werewolvesNumberText.setText(String.valueOf(nbWerewolves));
        }
    }

    private void onSubmitButtonClick() {
        game.nbWerewolves = nbWerewolves;
        game.clairvoyantEnabled = clairvoyantCheckbox.isChecked();
        game.littleGirlEnabled = littleGirlCheckbox.isChecked();
        game.save();
        onBackPressed();
    }
}
