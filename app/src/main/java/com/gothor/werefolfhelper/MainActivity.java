package com.gothor.werefolfhelper;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button bt_cards, bt_players, bt_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ConstraintLayout layout = (ConstraintLayout) ConstraintLayout.inflate(this, R.layout.activity_main, null);*/

        bt_cards = (Button) findViewById(R.id.bt_cards);
        bt_players = (Button) findViewById(R.id.bt_players);
        bt_play = (Button) findViewById(R.id.bt_play);

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
                Game game = Game.getGame(MainActivity.this);

                if (game.players.size() < 8) {
                    Toast.makeText(MainActivity.this, "Il faut au moins 8 joueurs.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (game.players.size() > 18) {
                    Toast.makeText(MainActivity.this, "Il faut au plus 18 joueurs.", Toast.LENGTH_LONG).show();
                    return;
                }

                game.reset();

                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
    }
}
