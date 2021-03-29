package com.drkstrinc.pokemon;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.drkstrinc.pokemon.actor.Player;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.datatype.Gender;
import com.drkstrinc.pokemon.graphics.SkinGenerator;
import com.drkstrinc.pokemon.screen.GameScreen;
import com.drkstrinc.pokemon.screen.TitleScreen;

public class Pokemon extends Game {

	private static Skin skin;

	private static GameScreen gs;

	private static Player player;

	// Outside Player's House
	private int startingCoordX = 24;
	private int startingCoordY = 25;

	@Override
	public void create() {
		if (Constants.DEBUG) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		} else {
			Gdx.app.setLogLevel(Application.LOG_INFO);
		}

		initAssetManager();
		newPlayer();

		gs = new GameScreen(this);
		setScreen(new TitleScreen(this));
	}

	private void newPlayer() {
		player = new Player("Kris", Gender.FEMALE, startingCoordX, startingCoordY, Direction.DOWN);
	}

	private void initAssetManager() {
		AssetManager assetManager = new AssetManager();
		assetManager.load("image/ui/gs_ui.atlas", TextureAtlas.class);
		assetManager.load("image/battlers/battlers.atlas", TextureAtlas.class);
		assetManager.finishLoading();

		skin = SkinGenerator.generateSkin(assetManager);
	}

	public static Skin getSkin() {
		return skin;
	}

	public static Player getPlayer() {
		return player;
	}

	public static GameScreen getGameScreen() {
		return gs;
	}

}
