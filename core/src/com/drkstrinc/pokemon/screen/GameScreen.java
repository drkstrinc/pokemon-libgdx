package com.drkstrinc.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.actor.Actor;
import com.drkstrinc.pokemon.controller.InteractionController;
import com.drkstrinc.pokemon.controller.OverworldController;
import com.drkstrinc.pokemon.menu.MessageBox;
import com.drkstrinc.pokemon.menu.ChoiceBox;
import com.drkstrinc.pokemon.world.WorldManager;

public class GameScreen extends ScreenAdapter {

	private InputMultiplexer inputMultiplexer;
	private OverworldController overworldController;
	private InteractionController interactionController;

	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;

	private Stage uiStage;

	private MessageBox speechBox;
	private ChoiceBox choiceBox;

	private SpriteBatch batch;
	private BitmapFont font;

	public GameScreen(Pokemon game) {
		initUI();

		inputMultiplexer = new InputMultiplexer();
		interactionController = new InteractionController(speechBox, choiceBox);
		overworldController = new OverworldController(game);
		inputMultiplexer.addProcessor(interactionController);
		inputMultiplexer.addProcessor(overworldController);
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
		Gdx.input.setInputProcessor(inputMultiplexer);
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

		// Update UI
		uiStage.act(delta);
		interactionController.update(delta);

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

		// Render UI
		uiStage.draw();

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

	private void initUI() {
		uiStage = new Stage(new ScreenViewport());
		uiStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		Table speechRoot = new Table();
		speechRoot.setFillParent(true);

		speechBox = new MessageBox(Pokemon.getSkin());
		speechBox.setVisible(false);

		choiceBox = new ChoiceBox(Pokemon.getSkin());
		choiceBox.setVisible(false);

		Table messageTable = new Table();
		messageTable.add(choiceBox).expand().align(Align.right).space(8f).row();
		messageTable.add(speechBox).expand().align(Align.bottom).space(8f).row();

		speechRoot.add(messageTable).expand().align(Align.bottom);

		uiStage.addActor(speechRoot);
	}

	public InteractionController getDialogueController() {
		return interactionController;
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
