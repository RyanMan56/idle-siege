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
import com.rbj_games.idle_siege.TextDrawable.Align;

// If ScaleType is LINEAR_MINUTES then all values are in seconds, but displayed in minutes on the graph
public class GraphRenderer {
	private IdleSiege game;
	private Map<IDrawable, IDrawable> textDrawables;
	private Vector2 position, size, rangeX, rangeY, intervals;
	private ScaleType scaleTypeX, scaleTypeY;
	private ShapeRenderer shapeRenderer;
	private Vector2 labelGap;
	private TextDrawable[] labelsX, labelsY;
	BitmapFont font;
	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open_sans/OpenSans-Light.ttf"));
	FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	
	// In the case of a log scale, the intervals are taken at every processed log.
	// e.g. interval = 1. 10 ln = 1, 100 ln = 2. 10, 100 get displayed on the axis.
	// 10^1 = 10, 10^2 = 100
	public GraphRenderer(IdleSiege game, Map<IDrawable, IDrawable> textDrawables, Vector2 position, Vector2 size, Vector2 rangeX, Vector2 rangeY, Vector2 intervals, ScaleType scaleTypeX, ScaleType scaleTypeY) {
		this.game = game;
		this.textDrawables = textDrawables;
		this.position = position;
		this.size = size;
		this.rangeX = scaleTypeX == ScaleType.LINEAR_MINUTES ? rangeX.scl(60f) : rangeX;
		this.rangeY = rangeY;
		this.intervals = new Vector2(scaleIntervals(scaleTypeX, intervals.y), scaleIntervals(scaleTypeY, intervals.y));
		this.scaleTypeX = scaleTypeX;
		this.scaleTypeY = scaleTypeY;
		shapeRenderer = new ShapeRenderer();
		labelGap = new Vector2(10f, 3f);

		setupLabels(this.rangeX, this.intervals.x, this.size.x, this.scaleTypeX, "x");
		setupLabels(this.rangeY, this.intervals.y, this.size.y, this.scaleTypeY, "y");

		System.out.println(this.intervals);
	}

	private float scaleIntervals(ScaleType scaleType, float interval) {
		if (scaleType == ScaleType.LINEAR_MINUTES) {
			return interval * 60f;
		}
		return interval; // ScaleType.LINEAR
	}

	private float scaleValue(ScaleType scaleType, float value) {
		if (scaleType == ScaleType.LINEAR_MINUTES) {
			return value / 60f;
		}
		if (scaleType == ScaleType.LOGARITHM) {
			return (float) Math.pow(10f, value);
		}
		return value; // ScaleType.LINEAR
	}

	private void setupLabels(Vector2 range, float interval, float size, ScaleType scaleType, String axis) {
		int labelCount = (int) Math.round((range.y - range.x) / interval) + 1;
		for (int i = 0; i < labelCount; i++) {
			float labelValue = range.x + interval * i;
			float labelText = scaleValue(scaleType, labelValue);
			if (axis == "x") {
				float posX = Utils.ConvertRanges(range.y, range.x, position.x + size, position.x, labelValue);
				labelsX = new TextDrawable[labelCount];
				labelsX[i] = new TextDrawable(game, new Vector2(posX, position.y - 3f), ""+labelText, Align.CENTER, Align.CENTER);
				textDrawables.put(labelsX[i], labelsX[i]);
			} else {
				float posY = Utils.ConvertRanges(range.y, range.x, position.y + size, position.y, labelValue);
				labelsY = new TextDrawable[labelCount];
				labelsY[i] = new TextDrawable(game, new Vector2(position.x - 3f, posY), ""+labelText, Align.START, Align.CENTER);
				textDrawables.put(labelsY[i], labelsY[i]);
			}
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
