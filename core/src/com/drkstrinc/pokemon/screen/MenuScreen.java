package com.drkstrinc.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.drkstrinc.pokemon.Pokemon;

public class MenuScreen extends ScreenAdapter {

	private Pokemon game;

	private SpriteBatch batch;
	private BitmapFont font;

	public MenuScreen(Pokemon game) {
		this.game = game;
		batch = new SpriteBatch();
		font = new BitmapFont();
	}

	@Override
	public void show() {
		Gdx.app.log("MNU", "Menu Opened");
		setupInput();
	}

	private void setupInput() {
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Input.Keys.X) {

					game.setScreen(new GameScreen(game));
				}
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, .25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.draw(batch, "Menu Screen", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .75f);
		font.draw(batch, "Press [X] to Exit", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .25f);
		batch.end();
	}

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
	}

	@Override
	public void hide() {
		Gdx.app.log("MNU", "Menu Closed");
		Gdx.input.setInputProcessor(null);
		this.dispose();
	}
}