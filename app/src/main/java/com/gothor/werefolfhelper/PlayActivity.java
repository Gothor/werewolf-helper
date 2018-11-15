package com.gothor.werefolfhelper;

import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.gothor.werefolfhelper.Game.game;

public class PlayActivity extends AppCompatActivity {

    private Game game;

    PlayerAdapter adapter;

    private TextView textView;
    private RecyclerView recyclerView;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);

        game = Game.getGame(this);
        game.sortPlayers();

        textView = (TextView) findViewById(R.id.turn_description);
        recyclerView = (RecyclerView) findViewById(R.id.list_players);
        button = (Button) findViewById(R.id.bt_next_turn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayerAdapter(this, game.players, PlayerAdapter.IN_GAME_VIEW);
        recyclerView.setAdapter(adapter);

        werewolf();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = game.isOver();
                game.sortPlayers();
                adapter.notifyDataSetChanged();

                switch (game.step) {
                    case WEREWOLF:
                        if (result == 0)
                            clairvoyant();
                        else {
                            endGame(result);
                        }
                        break;
                    case CLAIRVOYANT:
                        villageois();
                        break;
                    case DAY:
                        werewolf();
                        break;
                }
            }
        });
    }

    public void werewolf() {
        game.step = Game.Step.WEREWOLF;
        game.nbDays++;
        textView.setText(getString(R.string.werewolves_turn_description));
    }

    public void clairvoyant() {
        game.step = Game.Step.CLAIRVOYANT;
        textView.setText(getString(R.string.clairvoyant_turn_description));
    }

    public void villageois() {
        game.step = Game.Step.DAY;
        textView.setText(getString(R.string.villagers_turn_description));
    }

    public void endGame(int result) {
        // @TODO Afficher le message dans une AlertDialog puis retour à l'accueil
        if (result == 1) {
            textView.setText("Les loups-garous ont gagné.");
        } else {
            textView.setText("Les villageois ont gagné.");
        }
    }
}
