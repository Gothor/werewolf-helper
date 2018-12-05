package com.gothor.werefolfhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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

import java.util.Arrays;
import java.util.List;

import static com.gothor.werefolfhelper.Game.WEREWOLF_WIN;
import static com.gothor.werefolfhelper.Game.game;

public class PlayActivity extends PlayerAdapter.Listener {

    private Game game;

    PlayerAdapter adapter;

    private TextView textView;
    private RecyclerView recyclerView;
    private Button button;
    private PlayerAdapter.PlayerViewHolder currentViewHolder;

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
                nextTurn();
            }
        });
    }

    private void nextTurn() {
        final int result = game.isOver();
        game.sortPlayers();

        adapter.notifyDataSetChanged();

        switch (game.step) {
            case WEREWOLF:
                if (game.nbDays == 1 && game.countWerewolfPlayers() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
                    builder.setTitle(R.string.no_werewolf_selected)
                            .setMessage(R.string.continue_question)
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (result == Game.NO_WIN)
                                        if (game.clairvoyantEnabled) {
                                            clairvoyant();
                                        } else {
                                            villager();
                                        }
                                    else {
                                        endGame(result);
                                    }
                                }
                            });
                    builder.show();
                }
                else if (result == Game.NO_WIN)
                    if (game.clairvoyantEnabled) {
                        clairvoyant();
                    } else {
                        villager();
                    }
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

    public void werewolf() {
        game.nbDays++;
        game.step = Game.Step.WEREWOLF;
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
                .setMessage(result == WEREWOLF_WIN ? R.string.werewolf_win : R.string.villagers_win)
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
            String id = data.getStringExtra(RoleActivity.RESULT_ROLE_ID);
            Game.Role role = Game.Role.fromId(id);
            currentViewHolder.player.role = role;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClickRemove(PlayerAdapter.PlayerViewHolder viewHolder) { }

    @Override
    public void onClickEdit(PlayerAdapter.PlayerViewHolder viewHolder) { }

    @Override
    public void onClickRole(final PlayerAdapter.PlayerViewHolder viewHolder) {
        /*
        // If you prefer writing role names instead of showing pictures, use this

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_role)
                .setItems(Game.getGame(this).getAvailableRoleNames(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewHolder.player.role = Game.getGame(PlayActivity.this).getAvailableRoles()[i];
                        adapter.notifyDataSetChanged();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        */

        currentViewHolder = viewHolder;
        Intent intent = new Intent(this, RoleActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onClickStatus(PlayerAdapter.PlayerViewHolder viewHolder) {
        viewHolder.player.alive = !viewHolder.player.alive;
        adapter.notifyDataSetChanged();
    }
}
