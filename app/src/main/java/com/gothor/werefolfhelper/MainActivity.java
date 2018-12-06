package com.gothor.werefolfhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button bt_cards, bt_players, bt_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ConstraintLayout layout = (ConstraintLayout) ConstraintLayout.inflate(this, R.layout.activity_main, null);*/

        bt_cards = findViewById(R.id.bt_cards);
        bt_players = findViewById(R.id.bt_players);
        bt_play = findViewById(R.id.bt_play);

        bt_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CardsActivity.class);
                startActivity(intent);
            }
        });

        bt_players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayersActivity.class);
                startActivity(intent);
            }
        });

        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* A FAIRE : tester si nb cartes et nb joueurs sont compatibles
                avant de lancer le jeu */
                final Game game = Game.getGame(MainActivity.this);

                if (game.players.size() < 8) {
                    Toast.makeText(MainActivity.this, "Il faut au moins 8 joueurs.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (game.players.size() > 18) {
                    Toast.makeText(MainActivity.this, "Il faut au plus 18 joueurs.", Toast.LENGTH_LONG).show();
                    return;
                }

                final Intent intent = new Intent(MainActivity.this, PlayActivity.class);

                if (!game.over) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.ongoing_game)
                            .setMessage(R.string.keep_going_question)
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    game.reset();
                                    startActivity(intent);
                                }
                            })
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(intent);
                                }
                            });
                    builder.show();
                } else {
                    game.reset();
                    startActivity(intent);
                }

            }
        });
    }
}
