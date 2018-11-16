package com.gothor.werefolfhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
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

                if (o.alive != t1.alive) {
                    return o.alive ? -1 : 1;
                }
                if (o.role == t1.role) return o.name.compareTo(t1.name);
                if (o.role.getOrder() < t1.role.getOrder()) return -1;
                if (o.role.getOrder() > t1.role.getOrder()) return 1;
                return 0;
            }
        };

        Collections.sort(players, c);
    }

    public Role[] getAvailableRoles() {
        ArrayList<Role> roles = new ArrayList<>();

        roles.add(Role.WEREWOLF);
        if (clairvoyantEnabled) roles.add(Role.CLAIRVOYANT);
        if (littleGirlEnabled) roles.add(Role.LITTLE_GIRL);
        roles.add(Role.VILLAGER);

        Role[] rolesArray = new Role[roles.size()];
        roles.toArray(rolesArray);

        return rolesArray;
    }

    public String[] getAvailableRoleNames() {
        Role[] roles = getAvailableRoles();
        String[] roleNames = new String[roles.length];

        for (int i = 0; i < roles.length; i++) {
            roleNames[i] = roles[i].toString();
        }
        return roleNames;
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

        public int getOrder() {
            if (this == WEREWOLF) return 0;
            if (this == CLAIRVOYANT) return 1;
            if (this == LITTLE_GIRL) return 2;
            return 3;
        }
    }

    public enum Step {
        WEREWOLF,
        CLAIRVOYANT,
        DAY
    }

}
