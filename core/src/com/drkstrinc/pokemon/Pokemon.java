package com.drkstrinc.pokemon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.drkstrinc.pokemon.actor.Player;

public class Pokemon extends Game {

	private static TiledMap currentMap;

	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;

	private Player player;

	private int startingCoordX = 170;
	private int startingCoordY = 40;

	private SpriteBatch batch;
	private BitmapFont font;

	@Override
	public void create() {
		setupPlayer();
		setupCamera();
		setupOther();

		loadMap("johto");
	}

	private void setupPlayer() {
		player = new Player("Kris", Player.Gender.FEMALE, startingCoordX, startingCoordY, Player.Direction.DOWN);
	}

	private void setupCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void setupOther() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
	}

	public void loadMap(String mapName) {
		if (currentMap != null) {
			Gdx.app.log("TMX", "Unloading Current Map");
			currentMap.dispose();
		}
		Gdx.app.log("TMX", "Loading Map: " + mapName);
		currentMap = new TmxMapLoader().load("map/" + mapName + ".tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(currentMap);
		player.moveTo(startingCoordX, startingCoordY);
	}

	public static void setCurrentMap(TiledMap map) {
		currentMap = map;
	}

	public static TiledMap getCurrentMap() {
		return currentMap;
	}

	@Override
	public void render() {
		// Set the Camera's position to the Player's position
		camera.position.x = player.getX() + Constants.TILE_WIDTH / 2;
		camera.position.y = player.getY();
		camera.update();

		// Render Map
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		// Render Player
		player.update();
		player.render(batch, camera);

		// Render Debug Info
		if (Constants.DEBUG) {
			renderDebugInfo();
		}
	}

	private void renderDebugInfo() {
		batch.begin();
		font.draw(batch, player.getCoordX() + "," + player.getCoordY(), 10, Gdx.graphics.getHeight() - 10);
		batch.end();
	}

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
	}

}
