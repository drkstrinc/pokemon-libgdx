package com.drkstrinc.pokemon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.drkstrinc.pokemon.actor.Player;

public class Pokemon extends ApplicationAdapter {

	private TiledMap tiledMap;

	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;
	private ShapeRenderer shape;

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
		player = new Player("Gold", startingCoordX, startingCoordY, "DOWN");
		shape = new ShapeRenderer();
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

	private void loadMap(String mapName) {
		tiledMap = new TmxMapLoader().load("map/" + mapName + ".tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Set the Camera's position to the Player's position
		camera.position.x = player.getX() + Constants.TILE_WIDTH/2;
		camera.position.y = player.getY();
		camera.update();

		// Render Map
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		// Render Player
		player.render(shape, camera);
		player.update();

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
