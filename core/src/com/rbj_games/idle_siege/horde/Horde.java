package com.rbj_games.idle_siege.horde;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rbj_games.idle_siege.horde.mobs.IMob;
import com.rbj_games.idle_siege.horde.mobs.Mob;

import java.util.List;

public class Horde {
    protected String name = null;
    protected Stronghold stronghold = null;
    protected List<Mob> fighterList = null;
    protected List<Mob> fighterTypes = null;
    ShapeRenderer shapeRenderer;

    public Horde(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public void update() {
        for (int i = 0; i < fighterList.size(); i++) {
            fighterList.get(i).update();
        }
    }

    public void renderShapes() {
        for (int i = 0; i < fighterList.size(); i++) {
            Mob fighter = fighterList.get(i);
            shapeRenderer.setColor(fighter.color);
            shapeRenderer.rect(fighter.x, fighter.y, fighter.width, fighter.height);
        }
    }
}
