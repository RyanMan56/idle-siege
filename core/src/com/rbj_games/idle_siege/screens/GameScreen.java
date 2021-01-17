package com.rbj_games.idle_siege.screens;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.rbj_games.idle_siege.Battlefield;
import com.rbj_games.idle_siege.IDrawable;
import com.rbj_games.idle_siege.IdleSiege;
import com.rbj_games.idle_siege.TextDrawable;
import com.rbj_games.idle_siege.files.FileHandler;
import com.rbj_games.idle_siege.graphs.GraphRenderer;
import com.rbj_games.idle_siege.horde.Horde;
import com.rbj_games.idle_siege.utils.ScaleType;

public class GameScreen extends ScreenAdapter {
	private IdleSiege game;
	private Map<IDrawable, IDrawable> textDrawables;
	private FileHandler fileHandler;
	private long startTime = System.currentTimeMillis();
	private int lastX = 0;
	private Battlefield battlefield;
	
	public GameScreen(IdleSiege game) {
		this.game = game;
		textDrawables = new HashMap<IDrawable, IDrawable>();
		fileHandler = new FileHandler();
		String filename = "process_comms";
		fileHandler.createFile(filename, true);
		fileHandler.appendLine("Line 1");
		fileHandler.appendLine("Line 2!");
		battlefield = new Battlefield(this.game, new ArrayList<Horde>());
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		processNewPoints();
		battlefield.render();
		
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

	private void processNewPoints() {
		long totalTime = (System.currentTimeMillis() - startTime) / 1000;
		if (totalTime > 1) {
			float x = lastX++;
//			graphRenderer.addPoint(new Vector2(x, graphFn(x)));
			game.server.sendMessage(new Vector2(x, graphFn(x)));
			startTime = System.currentTimeMillis();
		}
	}

	private float graphFn(float x) {
		float y = (float) Math.pow(x, 3);
		return y;
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		for (IDrawable drawable : textDrawables.values()) {
			drawable.resize();
		}
//		battlefield.resize();
	}

	@Override
	public void dispose() {
		super.dispose();
		fileHandler.dispose();
	}
}
