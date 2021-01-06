package com.rbj_games.idle_siege.utils;

import java.text.DecimalFormat;
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

//class GraphLabel {
//	Vector2 position;
//	TextDrawable textDrawable;
//	
//	public GraphLabel(Vector2 position, TextDrawable textDrawable) {
//		this.position = position;
//		this.textDrawable = textDrawable;
//	}
//	
//	// Override hashCode function if HashMap needs accessing from more than just the object instance
//	// Default hashCode implementation converts memory address of object to an integer
//}

// If ScaleType is LINEAR_MINUTES then all values are in seconds, but displayed in minutes on the graph
public class GraphRenderer {
	private IdleSiege game;
	private Map<IDrawable, IDrawable> textDrawables;
	private Vector2 position, size, rangeX, rangeY, intervals;
	private ScaleType scaleTypeX, scaleTypeY;
	private ShapeRenderer shapeRenderer;
	private Vector2 labelGap;
	private TextDrawable[] labelsX;
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
		this.rangeX = scaleTypeX == ScaleType.LINEAR_MINUTES ? rangeX.scl(60f) : rangeX;
		this.rangeY = rangeY;
		this.intervals = new Vector2(scaleTypeX == ScaleType.LINEAR_MINUTES ? intervals.x * 60f : intervals.x, intervals.y);
		this.scaleTypeX = scaleTypeX;
		this.scaleTypeY = scaleTypeY;
		shapeRenderer = new ShapeRenderer();
		labelGap = new Vector2(10f, 3f);
		
		setupXAxisLabels();
	}
	
	private void setupXAxisLabels() {
		// range - x: from, y: to
		int labelCount = (int) Math.round((rangeX.y - rangeX.x) / intervals.x) + 1;
		float ratio = 1f / (labelCount - 1);
		labelGap.x = size.x * ratio;
		labelsX = new TextDrawable[labelCount];
		for (int i = 0; i < labelCount; i++) {
			float labelValue = rangeX.x + intervals.x * i;
			float labelText = scaleTypeX == ScaleType.LINEAR_MINUTES ? labelValue / 60f : labelValue;
			float posX = Utils.ConvertRanges(rangeX.y, rangeX.x, position.x + size.x, position.x, labelValue);
			labelsX[i] = new TextDrawable(game, new Vector2(posX, position.y - 2f), ""+labelText);
//			labelsX[i] = new GraphLabel(new Vector2(position.x + (labelGap.x * i), position.y), ""+i); // TODO: calculate what the actual label value is. Or use the label value to calculate the correct offset
			textDrawables.put(labelsX[i], labelsX[i]);
		}
	}
	
	public void draw() {
		shapeRenderer.setProjectionMatrix(game.camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.line(position.x, position.y, position.x + size.x, position.y);
		shapeRenderer.line(position.x, position.y, position.x, position.y + size.y);
		shapeRenderer.circle(50, 50, 1);
		shapeRenderer.end();
		
		float posX = Utils.ConvertRanges(rangeX.y, rangeX.x, position.x + size.x, position.x, 60f);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(posX, position.y, 1f);
		shapeRenderer.end();
	}
}
