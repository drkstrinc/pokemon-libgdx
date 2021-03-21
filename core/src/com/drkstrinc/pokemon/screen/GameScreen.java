package com.drkstrinc.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.actor.Actor;
import com.drkstrinc.pokemon.world.World;

public class GameScreen extends ScreenAdapter {

	private Pokemon game;

	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;

	private SpriteBatch batch;
	private BitmapFont font;

	public GameScreen(Pokemon game) {
		this.game = game;
	}

	@Override
	public void show() {
		setupCamera();
		setupInput();
		setupOther();
		game.getPlayer().unlockMovement();
	}

	private void setupCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth() / Constants.GAME_SCALE,
				Gdx.graphics.getHeight() / Constants.GAME_SCALE);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(World.getCurrentMap());
	}

	private void setupInput() {
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Input.Keys.ENTER) {
					game.setScreen(new MenuScreen(game));
				} else if (keyCode == Input.Keys.B) {
					// This is just for testing Screens
					game.setScreen(new BattleScreen(game));
				}
				return true;
			}
		});
	}

	private void setupOther() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
	}

	@Override
	public void render(float delta) {
		// Set the Camera's position to the Player's position
		camera.position.x = game.getPlayer().getX() + Constants.TILE_WIDTH / 2;
		camera.position.y = game.getPlayer().getY();
		camera.update();

		// Render Map
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		// Render Actors
		for (Actor actor : World.getActors()) {
			actor.update();
			actor.render(batch, camera);
		}

		// Render Player
		game.getPlayer().update();
		game.getPlayer().render(batch, camera);

		// Render Debug Info
		if (Constants.DEBUG) {
			renderDebugInfo();
		}
	}

	private void renderDebugInfo() {
		batch.begin();
		font.draw(batch, game.getPlayer().getCoordX() + "," + game.getPlayer().getCoordY(), 10,
				Gdx.graphics.getHeight() - 10);
		batch.end();
	}

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		game.getPlayer().lockMovement();
	}

}
