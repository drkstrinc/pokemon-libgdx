package com.drkstrinc.pokemon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.Pokemon;

public class PokemonLauncher {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = Constants.GAME_TITLE;
		config.width = Constants.GAME_WIDTH;
		config.height = Constants.GAME_HEIGHT;
		config.resizable = false;
		config.vSyncEnabled = true;

		new LwjglApplication(new Pokemon(), config);
	}

}
