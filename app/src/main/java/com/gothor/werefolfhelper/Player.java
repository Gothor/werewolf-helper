package com.gothor.werefolfhelper;

class Player {

    String name;
    Game.Role role;
    boolean alive;

    public Player(String name) {
        this.name = name;
        this.role = Game.Role.VILLAGER;
        this.alive = true;
    }

    public Player(Player p) {
        this.name = p.name;
        this.role = p.role;
        this.alive = p.alive;
    }
}
