package com.rbj_games.idle_siege.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.rbj_games.idle_siege.IDrawable;
import com.rbj_games.idle_siege.IdleSiege;
import com.rbj_games.idle_siege.TextDrawable;
import com.rbj_games.idle_siege.utils.Enums.Align;
import com.rbj_games.idle_siege.utils.Enums.Axis;

import com.rbj_games.idle_siege.geometry.Vector4;
import com.rbj_games.idle_siege.utils.ScaleType;
import com.rbj_games.idle_siege.utils.Utils;

// If ScaleType is LINEAR_MINUTES then all values are in seconds, but displayed in minutes on the graph
public class GraphRenderer {
	private IdleSiege game;
	private Map<IDrawable, IDrawable> textDrawables;
	private Vector2 position, size, rangeX, rangeY, intervals;
	private ScaleType scaleTypeX, scaleTypeY;
	private ShapeRenderer shapeRenderer;
	private List<Vector4> dashes;
	private TextDrawable[] labelsX, labelsY;
	private Color transparentBlack = new Color(0, 0, 0, 0.2f);
	private List<Point> points;
	
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
		dashes = new ArrayList<Vector4>();
		points = new ArrayList<Point>();
		
//		setupPoints();

		setupAxis(this.rangeX, this.intervals.x, this.scaleTypeX, Axis.X);
		setupAxis(this.rangeY, this.intervals.y, this.scaleTypeY, Axis.Y);
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

	private void setupAxis(Vector2 range, float interval, ScaleType scaleType, Axis axis) {
		int labelCount = (int) Math.round((range.y - range.x) / interval) + 1;
		for (int i = 0; i < labelCount; i++) {
			float labelValue = range.x + interval * i;
			float labelText = scaleValue(scaleType, labelValue);
			if (axis == Axis.X) {
				float posX = Utils.ConvertRanges(range.y, range.x, position.x + size.x, position.x, labelValue);
				labelsX = new TextDrawable[labelCount];
				labelsX[i] = new TextDrawable(game, new Vector2(posX, position.y - 3f), ""+labelText, Align.CENTER, Align.CENTER);
				setupDashedLines(Color.BLACK, new Vector2(1, 1), new Vector2(posX, position.y), new Vector2(posX, position.y + size.y));
				textDrawables.put(labelsX[i], labelsX[i]);
			} else {
				float posY = Utils.ConvertRanges(range.y, range.x, position.y + size.y, position.y, labelValue);
				labelsY = new TextDrawable[labelCount];
				labelsY[i] = new TextDrawable(game, new Vector2(position.x - 3f, posY), ""+labelText, Align.START, Align.CENTER);
				setupDashedLines(Color.BLACK, new Vector2(1, 1), new Vector2(position.x, posY), new Vector2(position.x + size.x, posY));
				textDrawables.put(labelsY[i], labelsY[i]);
			}
		}
	}
	
	public void setupDashedLines(Color color, Vector2 dashes, Vector2 start, Vector2 end) {
		if (dashes.x == 0) {
			return ;
		}
		float dirX = end.x - start.x;
		float dirY = end.y - start.y;
		  
		float length = Vector2.len(dirX, dirY);
		dirX /= length;
		dirY /= length;
		  
		float curLen = 0;
		float curX = 0;
		float curY = 0;
		  
		while (curLen <= length) {
			curX = (start.x+dirX*curLen);
			curY = (start.y+dirY*curLen);
			this.dashes.add(new Vector4(curX, curY, curX+dirX*dashes.x, curY+dirY*dashes.x));
			curLen += (dashes.x + dashes.y);
		}
   }

   private float plotPoint(ScaleType scaleType, float value, Vector2 range, float position, float size) {
		switch (scaleType) {
			case LOGARITHM:
				return Utils.ConvertRanges(range.y, range.x, position + size, position, (float) Math.log10(value));
			default:
				return Utils.ConvertRanges(range.y, range.x, position + size, position, value);
		}
   }

   public void addPoint(Vector2 value) {
		float posX = plotPoint(scaleTypeX, value.x, rangeX, position.x, size.x);
		float posY = plotPoint(scaleTypeY, value.y, rangeY, position.y, size.y);
	   	points.add(new Point(value, new Vector2(posX, posY)));
   }
	
//	private void setupPoints() {
//		ArrayList<Vector2> values = new ArrayList<Vector2>();
//		values.add(new Vector2(0, 0));
//		values.add(new Vector2(1, 10));
//		values.add(new Vector2(2, 100));
//		values.add(new Vector2(2.5f, 1000));
//		values.add(new Vector2(3, 10000));
//		values.add(new Vector2(4, 1000000));
//		values.add(new Vector2(5, 10000000));
//		System.out.println(scaleTypeX+" "+scaleTypeY);
//
//		for (Vector2 val : values) {
////			float posX = Utils.ConvertRanges(rangeX.y, rangeX.x, position.x + size.x, position.x, val.x);
////			float posY = Utils.ConvertRanges(rangeY.y, rangeY.x, position.y + size.y, position.y, val.y);
//			float posX = plotPoint(scaleTypeX, val.x, rangeX, position.x, size.x);
//			float posY = plotPoint(scaleTypeY, val.y, rangeY, position.y, size.y);
//			points.add(new Point(val, new Vector2(posX, posY)));
//		}
//	}
	
//	private PolynomialSplineFunction interpolate()
	
	public void draw() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setProjectionMatrix(game.camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.line(position.x, position.y, position.x + size.x, position.y);
		shapeRenderer.line(position.x, position.y, position.x, position.y + size.y);
		shapeRenderer.setColor(transparentBlack);
		for (Vector4 dash : dashes) {
			shapeRenderer.line(dash.x, dash.y, dash.z, dash.w);
		}
		shapeRenderer.setColor(Color.RED);
		for (int i = 0; i < points.size() - 1; i++) {
			shapeRenderer.line(points.get(i).position, points.get(i+1).position);
		}
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		
//		float posX = Utils.ConvertRanges(rangeX.y, rangeX.x, position.x + size.x, position.x, 1);
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(posX, position.y, 0.5f, 10);
		for (Point point : points) {
			shapeRenderer.circle(point.position.x, point.position.y, 0.5f, 10);
		}
		shapeRenderer.end();
	}
}
