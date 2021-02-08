package com.rbj_games.idle_siege.horde.monsters;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rbj_games.idle_siege.horde.Horde;
import com.rbj_games.idle_siege.horde.mobs.Goblin;
import com.rbj_games.idle_siege.horde.mobs.IMob;

import java.util.ArrayList;

public class MonsterHorde extends Horde {
    public MonsterHorde(ShapeRenderer shapeRenderer) {
        super(shapeRenderer);

        name = "Monsters";
        fighterList = new ArrayList<>();
        fighterList.add(new Goblin(5, 5));
        fighterList.add(new Goblin(7, 7));
    }
}
