package com.rbj_games.idle_siege.horde.mobs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Goblin extends Mob {

    public Goblin() {
        width = 1;
        height = 2;
        color = Color.GREEN;
    }

    public Goblin(float x, float y) {
        this();
        this.x = x;
        this.y = y;
    }
}
