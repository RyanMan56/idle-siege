package com.rbj_games.idle_siege.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.rbj_games.idle_siege.IDrawable;
import com.rbj_games.idle_siege.IdleSiege;
import com.rbj_games.idle_siege.graphs.GraphRenderer;
import com.rbj_games.idle_siege.utils.ScaleType;

import java.util.HashMap;
import java.util.Map;

public class GraphScreen extends ScreenAdapter {
    IdleSiege game;
    private Map<IDrawable, IDrawable> textDrawables;
    private GraphRenderer graphRenderer;

    public GraphScreen(IdleSiege game) {
        this.game = game;
        textDrawables = new HashMap<IDrawable, IDrawable>();
        Vector2 position = new Vector2(20f, 10f);
        Vector2 size = new Vector2(60f, 60f);
        Vector2 rangeX = new Vector2(0f, 1f);
        Vector2 rangeY = new Vector2(0f, 10f);
        Vector2 intervals = new Vector2(0.25f, 1f);
        graphRenderer = new GraphRenderer(game, textDrawables, position, size, rangeX, rangeY, intervals, ScaleType.LINEAR_MINUTES, ScaleType.LOGARITHM);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        addPointsFromQueue();
        graphRenderer.draw();

        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        // Draw all images in here
        game.batch.end();

        game.batch.setProjectionMatrix(game.textCamera.combined);
        game.batch.begin();
        for (IDrawable drawable : textDrawables.values()) {
            drawable.draw();
        }
        game.batch.end();
    }

    public void addPointsFromQueue() {
        if (game.queuedGraphPoints == null) {
            return;
        }

        while (game.queuedGraphPoints.size() > 0) {
            Vector2 point = game.queuedGraphPoints.remove();
            graphRenderer.addPoint(point);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        for (IDrawable drawable : textDrawables.values()) {
            drawable.resize();
        }
    }
}
