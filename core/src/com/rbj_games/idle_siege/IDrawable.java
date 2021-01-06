package com.rbj_games.idle_siege;

import com.badlogic.gdx.math.Vector2;

public interface IDrawable {
	public void draw();
	public void resize();
	public void setText(String text);
	public String getText();
	public void setPosition(Vector2 pos);
}
