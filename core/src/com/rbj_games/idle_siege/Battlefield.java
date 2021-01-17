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
    public Tile[][] tiles;
    private Vector2 interval = new Vector2(10, 10);
    private boolean drawGrid = true;
    private List<Vector4> gridLines;
    private ShapeRenderer shapeRenderer;

    public Battlefield(IdleSiege game, List<Horde> hordeList) {
        this.game = game;
        this.hordeList = hordeList;

        shapeRenderer = new ShapeRenderer();
        setupGrid();
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

        Vector3 touchWorldPos = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
//        System.out.println(touchWorldPos.x + " : " + touchWorldPos.y);
        System.out.println(tileFromPosition(touchWorldPos.x, touchWorldPos.y));
    }

    public void setupGrid() { // TODO: Also call this on resize? No, I don't think so
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        Vector2 tileCount = new Vector2((float) Math.ceil(IdleSiege.WORLD_WIDTH / interval.x), (float) Math.ceil((IdleSiege.WORLD_WIDTH * (h/w)) / interval.y));
        tiles = new Tile[(int) tileCount.y][(int) tileCount.x];

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

    private Vector2 tileFromPosition(float w, float h) {
        return new Vector2((float) Math.floor(w / interval.x), (float) Math.floor(h / interval.y));
    }

    public void resize() {
        setupGrid();
    }
}
