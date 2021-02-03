package com.rbj_games.idle_siege;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.rbj_games.idle_siege.geometry.Vector4;
import com.rbj_games.idle_siege.horde.Horde;
import com.rbj_games.idle_siege.utils.Colors;

import java.util.ArrayList;
import java.util.List;

public class Battlefield {
    private IdleSiege game;
    public List<Horde> hordeList;
    public Tile[] tiles;
    private Vector2 interval = new Vector2(10, 10);
    private Vector2 tileCount;
    private boolean drawGrid = true;
    private List<Vector4> gridLines;
    private ShapeRenderer shapeRenderer;

    public Battlefield(IdleSiege game, List<Horde> hordeList) {
        this.game = game;
        this.hordeList = hordeList;

        shapeRenderer = new ShapeRenderer();
        setupGrid();
        System.out.println(tileCount);
    }

    public void setupGrid() { // TODO: Also call this on resize? No, I don't think so
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        tileCount = new Vector2((float) Math.ceil(IdleSiege.WORLD_WIDTH / interval.x), (float) Math.ceil((IdleSiege.WORLD_WIDTH * (h/w)) / interval.y));
        tiles = new Tile[(int) tileCount.y * (int) tileCount.x];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(i);
        }

        gridLines = new ArrayList<>();
        for (int y = 0; y < tileCount.y; y++) {
            float yPos = 0 + interval.y * y;
            gridLines.add(new Vector4(0f, yPos, w, yPos));
        }
        for (int x = 0; x < tileCount.x; x++) {
            float xPos = 0 + interval.x * x;
            gridLines.add(new Vector4(xPos, 0, xPos, h));
        }
    }

    public void render() {
        if (drawGrid) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(game.camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Colors.transparentBlack);
            for (Vector4 gridLine : gridLines) {
                shapeRenderer.line(gridLine.x, gridLine.y, gridLine.z, gridLine.w);
            }
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        for (Horde horde : hordeList) {
            horde.render();
        }

        // TODO: Remember, this only works because we're using viewport.unproject instead of camera.unproject
        Vector2 touchWorldPos = game.viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
//        System.out.println(touchWorldPos.x + " : " + touchWorldPos.y);
        System.out.println(tileIndexFromPosition(touchWorldPos.x, touchWorldPos.y));

    }

    private Vector2 tileFromPosition(float w, float h) {
        return new Vector2((float) Math.floor(w / interval.x), (float) Math.floor(h / interval.y));
    }

    // Grid coord is (0 -> tileCount.x, 0 -> tileCount.y)
    private int tileIndexFromGridCoord(float x, float y) {
        return (int) (y * tileCount.x + x);
    }

    private int tileIndexFromPosition(float w, float h) {
        Vector2 tileCoord = tileFromPosition(w, h);
        return tileIndexFromGridCoord(tileCoord.x, tileCoord.y);
    }

//    private int[]

    public void resize() {
        setupGrid();
    }
}
