package com.gothor.werefolfhelper;

import android.os.Parcel;
import android.os.Parcelable;

class Player {

    String name;
    Game.Role role;
    boolean alive;

    public Player(String name) {
        this.name = name;
        this.role = Game.Role.VILLAGER;
        this.alive = true;
    }

}