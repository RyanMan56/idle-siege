package com.rbj_games.idle_siege;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TextDrawable implements IDrawable {
	IdleSiege game;
	Vector2 worldPos;
	public String text;
	BitmapFont font;
	GlyphLayout layout;
	Vector3 textPos;
	Align xAlign, yAlign;

	public enum Align {
		START, CENTER, END
	}

	public enum Axis {
		X, Y
	}
	
	public TextDrawable(IdleSiege game, Vector2 worldPos, String text, Align xAlign, Align yAlign) {
		this.game = game;
		this.worldPos = worldPos;
		this.text = text;
		this.xAlign = xAlign;
		this.yAlign = yAlign;
		
		font = game.assetManager.get("OpenSans-Light_Small.ttf", BitmapFont.class);
		layout = new GlyphLayout(font, text);
		
		Vector3 screenPos = game.camera.project(new Vector3(worldPos.x, game.camera.viewportHeight - worldPos.y, 0f)); // Because unproject and project have opposite facing Y-Axis 
		textPos = game.textCamera.unproject(screenPos);
	}

	public float calculatePosition(Align align, float pos, float size, Axis axis) {
		if (align == Align.CENTER) {
			return axis == Axis.X ? pos - size / 2 : pos + size / 2;
		}
		if (align == Align.END) {
			return pos;
		}
		return axis == Axis.X ? pos - size : pos + size / 2; // Align.START
	}

	@Override
	public void draw() {
//		font.draw(game.batch, layout, textPos.x - layout.width / 2, textPos.y + layout.height / 2);
		font.draw(game.batch, layout, calculatePosition(xAlign, textPos.x, layout.width, Axis.X), calculatePosition(yAlign, textPos.y, layout.height, Axis.Y));
	}
	
	public void resize() {
		Vector3 screenPos = game.camera.project(new Vector3(this.worldPos.x, game.camera.viewportHeight - this.worldPos.y, 0f)); // Because unproject and project have opposite facing Y-Axis 
		textPos = game.textCamera.unproject(screenPos);
	}
	
	@Override
	public void setText(String text) {
		this.text = text;
		layout.setText(font, text);
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public void setPosition(Vector2 pos) {
		this.worldPos = pos;
		this.resize();
	}
	
}
