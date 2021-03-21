package com.drkstrinc.pokemon;

import com.badlogic.gdx.Game;

import com.drkstrinc.pokemon.actor.Player;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.datatype.Gender;
import com.drkstrinc.pokemon.screen.TitleScreen;
import com.drkstrinc.pokemon.world.World;

public class Pokemon extends Game {

	private World currentWorld;
	private Player player;

	private int startingCoordX = 170;
	private int startingCoordY = 40;

	@Override
	public void create() {
		currentWorld = new World("johto");
		setupPlayer();

		setScreen(new TitleScreen(this));
	}

	private void setupPlayer() {
		player = new Player("Gold", Gender.MALE, startingCoordX, startingCoordY, Direction.DOWN);
	}

	public Player getPlayer() {
		return player;
	}

	public World getWorld() {
		return currentWorld;
	}

}
