package com.rbj_games.idle_siege;

import com.rbj_games.idle_siege.horde.mobs.Goblin;
import com.rbj_games.idle_siege.horde.mobs.IMob;
import com.rbj_games.idle_siege.horde.mobs.Mob;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    public List<IMob> mobsInTile;
    public int index;

    public Tile(int index) {
        this.index = index;
//        mobsInTile = new ArrayList<>();
//        mobsInTile.add(new Goblin());
    }
}
