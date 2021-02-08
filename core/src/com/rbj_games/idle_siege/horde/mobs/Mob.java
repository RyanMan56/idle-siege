package com.rbj_games.idle_siege.horde.mobs;

import com.badlogic.gdx.graphics.Color;
import com.rbj_games.idle_siege.Tile;
import com.rbj_games.idle_siege.horde.mobs.IMob;

import java.util.List;

public class Mob implements IMob {
    public float x, y;
    public float width, height;
    public Color color;
    public List<Tile> currentTiles;

    @Override
    public void update() {
        move();
    }

    public void move() {
        checkTiles();
    }

    public void checkTiles() {

    }

    @Override
    public void render() {

    }
}
