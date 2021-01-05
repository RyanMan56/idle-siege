package com.rbj_games.idle_siege.utils;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.rbj_games.idle_siege.IDrawable;
import com.rbj_games.idle_siege.IdleSiege;
import com.rbj_games.idle_siege.TextDrawable;

class GraphLabel {
	Vector2 position;
	String name;
	
	public GraphLabel(Vector2 position, String name) {
		this.position = position;
		this.name = name;
	}
	
	// Override hashCode function if HashMap needs accessing from more than just the object instance
	// Default hashCode implementation converts memory address of object to an integer
}

public class GraphRenderer {
	private IdleSiege game;
	private Map<IDrawable, IDrawable> textDrawables;
	private Vector2 position, size, rangeX, rangeY;
	private ScaleType scaleTypeX, scaleTypeY;
	private ShapeRenderer shapeRenderer;
	private Vector2 labelGap;
	private GraphLabel[] labelsX;
	BitmapFont font;
	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open_sans/OpenSans-Light.ttf"));
	FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	
	// In the case of a log scale, the intervals are taken at every processed log.
	// e.g. interval = 1. 10 ln = 1, 100 ln = 2. 10, 100 get displayed on the axis.
	public GraphRenderer(IdleSiege game, Map<IDrawable, IDrawable> textDrawables, Vector2 position, Vector2 size, Vector2 rangeX, Vector2 rangeY, Vector2 intervals, ScaleType scaleTypeX, ScaleType scaleTypeY) {
		this.game = game;
		this.textDrawables = textDrawables;
		this.position = position;
		this.size = size;
		this.rangeX = rangeX;
		this.rangeY = rangeY;
		this.scaleTypeX = scaleTypeX;
		this.scaleTypeY = scaleTypeY;
		shapeRenderer = new ShapeRenderer();
		labelGap = new Vector2(3f, 3f);
		
		int labelCount = (int) Math.floor(size.x / labelGap.x);
		labelsX = new GraphLabel[labelCount];
		for (int i = 0; i < labelCount; i++) {
			labelsX[i] = new GraphLabel(new Vector2(position.x + (labelGap.x * i), position.y), ""+i); // TODO: calculate what the actual label value is. Or use the label value to calculate the correct offset
		}
		
		TextDrawable test = new TextDrawable(game, new Vector2(50, 50), "Hello!!");
		textDrawables.put(test, test);
		
		TextDrawable test2 = new TextDrawable(game, new Vector2(20, 20), "Test 2");
		textDrawables.put(test2, test2);
		
		textDrawables.get(test).setText("Resetting text!");
		
		textDrawables.get(test2).setText("Changing this text too!");
		
	}
	
	public void draw() {
		shapeRenderer.setProjectionMatrix(game.camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.line(position.x, position.y, position.x + size.x, position.y);
		shapeRenderer.line(position.x, position.y, position.x, position.y + size.y);
		shapeRenderer.circle(50, 50, 1);
		shapeRenderer.end();
		
		float posX = Utils.ConvertRanges(rangeX.y, rangeX.x, position.x + size.x, position.x, 2.5f);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(posX, position.y, 1f);
		shapeRenderer.end();
	}
}
