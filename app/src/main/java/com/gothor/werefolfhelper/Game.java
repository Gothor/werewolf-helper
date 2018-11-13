package com.gothor.werefolfhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class Game {

    protected transient static Game game;

    private transient Context context;

    public int nbDays;

    public int nbWerewolves;
    public boolean clairvoyantEnabled;
    public boolean littleGirlEnabled;
    public Step step = Step.WEREWOLF;

    public ArrayList<Player> players = new ArrayList<>();

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

    public int isOver() {
        if (werewolfWin()) return 1;
        if (villagerWin()) return 2;
        return 0;
    }

    private boolean werewolfWin() {
        int nb = 0;
        for (Player p: players) {
            if (p.role != Role.WEREWOLF && p.alive)
                nb++;
        }
        return nb < 2;
    }

    private boolean villagerWin() {
        for (Player p: players) {
            if (p.role == Role.WEREWOLF && p.alive)
                return false;
        }
        return true;
    }

    public void sortPlayers() {
        Comparator c = new Comparator() {
            @Override
            public int compare(Object po, Object pt1) {
                Player o = (Player) po;
                Player t1 = (Player) pt1;
                if (o.role == Role.WEREWOLF) return -1;
                if (t1.role == Role.WEREWOLF) return 1;
                return 0;
            }
        };

        for (int i = 0; i < players.size(); i++) {
            for (int j = i; j > 1 && c.compare(players.get(j), players.get(j-1)) < 0; j--) {
                Player tmp = players.get(i);
                players.set(i, players.get(j));
                players.set(j, tmp);
            }
        }
    }

    public String[] getAvailableRoles() {
        ArrayList<String> roles = new ArrayList<>();

        roles.add(Role.WEREWOLF.toString());
        if (clairvoyantEnabled) roles.add(Role.CLAIRVOYANT.toString());
        if (littleGirlEnabled) roles.add(Role.LITTLE_GIRL.toString());
        roles.add(Role.VILLAGER.toString());

        String[] strRoles = new String[roles.size()];
        roles.toArray(strRoles);
        return strRoles;
    }

    public enum Role {
        WEREWOLF,
        CLAIRVOYANT,
        LITTLE_GIRL,
        VILLAGER;

        public static String[] getValues() {
            String[] strs = new String[Role.values().length];
            int i = 0;

            for (Role r: Role.values())
                strs[i++] = r.toString().toLowerCase();

            return strs;
        }
    }

    public enum Step {
        WEREWOLF,
        CLAIRVOYANT,
        DAY
    }

}
