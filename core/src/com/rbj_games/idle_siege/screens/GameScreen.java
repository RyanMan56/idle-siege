package com.rbj_games.idle_siege.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.rbj_games.idle_siege.IDrawable;
import com.rbj_games.idle_siege.IdleSiege;
import com.rbj_games.idle_siege.utils.GraphRenderer;
import com.rbj_games.idle_siege.utils.ScaleType;

public class GameScreen extends ScreenAdapter {
	IdleSiege game;
	private Map<IDrawable, IDrawable> textDrawables;
	private GraphRenderer graphRenderer;
	
	public GameScreen(IdleSiege game) {
		this.game = game;
		textDrawables = new HashMap<IDrawable, IDrawable>();
		graphRenderer = new GraphRenderer(game, textDrawables, new Vector2(10f, 10f), new Vector2(80f, 60f), new Vector2(0f, 5f), new Vector2(0f, 5f), new Vector2(1f, 1f), ScaleType.LINEAR, ScaleType.LOGARITHM);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
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
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		for (IDrawable drawable : textDrawables.values()) {
			drawable.resize();
		}
	}
}
