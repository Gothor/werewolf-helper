package com.gothor.werefolfhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class Game {

    protected transient static Game game;

    private transient Context context;

    private int nbDays;

    private int nbWerewolves;
    private boolean clairvoyantEnabled;
    private boolean littleGirlEnabled;

    private ArrayList<Player> players = new ArrayList<>();

    public Game() { }

    public static Game getGame(Context c) {
        if (game == null) {
            SharedPreferences sp = c.getSharedPreferences("whConfig", Context.MODE_PRIVATE);
            String config = sp.getString("config", "");

            if (config == "") {
                game = new Game();
            } else {
                Gson gson = new Gson();
                game = gson.fromJson(config, Game.class);
            }
            game.setContext(c);
        }

        return game;
    }

    /* Methods */

    private void setContext(Context c) {
        context = c;
    }

    protected void addPlayer(Player player) {
        players.add(player);
    }

    public void save() {
        SharedPreferences.Editor e = context.getSharedPreferences("whConfig", Context.MODE_PRIVATE).edit();

        Gson gson = new Gson();
        String config = gson.toJson(this, Game.class);

        e.putString("config", config);

        e.commit();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public enum Role {
        WEREWOLF,
        CLAIRVOYANT,
        LITTLE_GIRL,
        VILLAGER
    }

    public enum Step {
        WEREWOLF,
        CLAIRVOYANT,
        DAY
    }

}
