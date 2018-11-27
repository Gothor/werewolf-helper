package com.gothor.werefolfhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import static com.gothor.werefolfhelper.Game.game;

public class PlayActivity extends AppCompatActivity {

    private Game game;

    PlayerAdapter adapter;

    private TextView textView;
    private RecyclerView recyclerView;
    private Button button;

    Player pwet;

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
                        villager();
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

    public void villager() {
        game.step = Game.Step.DAY;
        textView.setText(getString(R.string.villagers_turn_description));
    }

    public void endGame(int result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.end_game)
                .setMessage(result == 1 ? R.string.werewolf_win : R.string.villagers_win)
                .setPositiveButton(R.string.home, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String role = data.getStringExtra(RoleActivity.RESULT_CONTRYCODE);
            List<Game.Role> roles = Arrays.asList(Game.Role.values());
            for (Game.Role r : roles) {
                if (role == r.toString().toLowerCase()) {
                    player
                }
            }
        }
    }
}
