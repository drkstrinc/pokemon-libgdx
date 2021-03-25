package com.drkstrinc.pokemon;

import com.badlogic.gdx.Game;

import com.drkstrinc.pokemon.actor.Player;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.datatype.Gender;
import com.drkstrinc.pokemon.screen.GameScreen;
import com.drkstrinc.pokemon.screen.TitleScreen;
import com.drkstrinc.pokemon.world.WorldManager;

public class Pokemon extends Game {

	private static GameScreen gs;

	private static Player player;

	// Outside Player's House
	private int startingCoordX = 24;
	private int startingCoordY = 25;

	@Override
	public void create() {
		newPlayer();
		new WorldManager(this, "NewBarkTown");
		gs = new GameScreen(this);

		setScreen(new TitleScreen(this));
	}

	private void newPlayer() {
		player = new Player("Kris", Gender.FEMALE, startingCoordX, startingCoordY, Direction.DOWN);
	}

	public static Player getPlayer() {
		return player;
	}

	public static GameScreen getGameScreen() {
		return gs;
	}

}
