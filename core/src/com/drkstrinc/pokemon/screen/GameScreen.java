package com.drkstrinc.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.actor.Actor;
import com.drkstrinc.pokemon.world.WorldManager;

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
		setupMapRenderer(WorldManager.getCurrentMap());
		setupInput();
		setupOther();
		unlockActors();
	}

	private void setupCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void setupMapRenderer(TiledMap map) {
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
	}

	private void setupInput() {
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Input.Keys.ENTER) {
					game.setScreen(new MenuScreen(game));
				} else if (keyCode == Input.Keys.B) {
					// TODO: Remove this later. This is just for testing Screens
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

	public static void unlockActors() {
		for (Actor actor : WorldManager.getActors()) {
			actor.unlockMovement();
		}
	}

	public static void lockActors() {
		for (Actor actor : WorldManager.getActors()) {
			actor.lockMovement();
		}
	}

	@Override
	public void render(float delta) {
		// Set the Camera's position to the Player's position
		camera.position.x = Pokemon.getPlayer().getX() + Constants.TILE_WIDTH / 2;
		camera.position.y = Pokemon.getPlayer().getY();
		camera.update();

		tiledMapRenderer.setView(camera);

		if (!WorldManager.isRetroMap()) {
			// Render Map Layers below Actors' Z-Axis
			tiledMapRenderer.render(WorldManager.belowLayers);
		} else {
			// Retro Worlds don't utilize Z axis, render all Layers below Actors
			tiledMapRenderer.render();

		}

		// Render Actors (including Player)
		for (Actor actor : WorldManager.getActors()) {
			actor.update();
			actor.render(batch, camera);
		}

		if (!WorldManager.isRetroMap()) {
			// Render Map Layers above Actors' Z-Axis
			tiledMapRenderer.render(WorldManager.aboveLayers);
		}

		// Render Debug Info
		renderDebugInfo();
	}

	private void renderDebugInfo() {
		if (Constants.DEBUG) {
			batch.begin();
			String debugString = Pokemon.getPlayer().getCoordX() + " Y: " + Pokemon.getPlayer().getCoordY() + " Tile: "
					+ WorldManager.getCurrentTileId();
			font.draw(batch, "X: " + debugString, Pokemon.getPlayer().getX() - 130, Pokemon.getPlayer().getY() + 130);
			batch.end();
		}
	}

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		lockActors();
	}

}
