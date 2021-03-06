package com.gothor.werefolfhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Game {

    public static final int NO_WIN = 0;
    public static final int WEREWOLF_WIN = 1;
    public static final int VILLAGER_WIN = 2;
    protected transient static Game game;

    private transient Context context;

    public int nbDays;

    public int nbWerewolves;
    public boolean clairvoyantEnabled;
    public boolean littleGirlEnabled;
    public Step step = Step.WEREWOLF;
    public boolean over = false;

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

    public int getState() {
        int res = NO_WIN;
        if (werewolfWin()) {
            over = true;
            res = WEREWOLF_WIN;
        };
        if (villagerWin()) {
            over = true;
            res = VILLAGER_WIN;
        }
        return res;
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
            roleNames[i] = roles[i].getLabel(context);
        }
        return roleNames;
    }

    public void reset() {
        nbDays = 1;
        game.step = Step.WEREWOLF;
        for (Player p : players) {
            p.alive = true;
            p.role = Game.Role.VILLAGER;
        }
        over = false;
    }

    public int countRole(Role role) {
        int count = 0;
        for (Player p: players)
            if (p.role == role)
                count++;
        return count;
    }

    public boolean clairvoyantIsAlive() {
        for (Player p: players) {
            if (p.role == Role.CLAIRVOYANT && p.alive)
                return true;
        }
        return false;
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

        public static Role fromId(String id) {
            for (Role r: Role.values())
                if (r.getId().equals(id))
                    return r;
            return null;
        }

        public String getId() {
            return "role_" + this.name().toLowerCase();
        }

        public String getLabel(Context ctx) {
            Resources res = ctx.getResources();
            int id = res.getIdentifier(this.getId(), "string", ctx.getPackageName());
            if (id != 0) {
                return ctx.getString(id);
            }
            return name();
        }

        public int getPictureId(Context ctx) {
            Resources res = ctx.getResources();
            return res.getIdentifier(this.getId(), "drawable", ctx.getPackageName());
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
