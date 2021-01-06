package com.rbj_games.idle_siege;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rbj_games.idle_siege.screens.GameScreen;

public class IdleSiege extends Game {
	public AssetManager assetManager;
	public SpriteBatch batch;
	Texture img;
	public OrthographicCamera camera;
	public Viewport viewport;
	public OrthographicCamera textCamera;
	private boolean loaded = false;
	
	static final int WORLD_WIDTH = 100;
	
	@Override
	public void create () {
		loadAssets();
		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
		float w = (float)Gdx.graphics.getWidth();
		float h = (float)Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		viewport = new FitViewport(WORLD_WIDTH, WORLD_WIDTH * (h/w), camera);
		viewport.apply();
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		
		textCamera = new OrthographicCamera(w, h);
		textCamera.position.set(textCamera.viewportWidth / 2f, textCamera.viewportHeight / 2f, 0);
		textCamera.update();
	}
	
	public void loadAssets() {
		assetManager = new AssetManager();
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		FreeTypeFontLoaderParameter size1Params = new FreeTypeFontLoaderParameter();
		size1Params.fontFileName = "fonts/open_sans/OpenSans-Light.ttf";
		size1Params.fontParameters.size = 14;
		size1Params.fontParameters.color = Color.BLACK;
		assetManager.load("OpenSans-Light_Small.ttf", BitmapFont.class, size1Params);
	}

	@Override
	public void render () {
		super.render();
		if (!loaded) {			
			if (assetManager.update()) {
				setScreen(new GameScreen(this));
				loaded = true;
			}
		}
		
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		viewport.apply();
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		
		textCamera.viewportWidth = viewport.getScreenWidth();
		textCamera.viewportHeight = viewport.getScreenHeight();
		textCamera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		textCamera.update();

		Screen currentScreen = getScreen();
		if (currentScreen != null) {			
			currentScreen.resize(width, height);
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
//		img.dispose();
	}
}
