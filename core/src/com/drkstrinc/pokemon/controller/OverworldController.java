package com.drkstrinc.pokemon.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.screen.BattleScreen;

public class OverworldController extends InputAdapter {

	Pokemon game;

	public OverworldController(Pokemon game) {
		this.game = game;
	}

	@Override
	public boolean keyDown(int keyCode) {
		if (keyCode == Input.Keys.Z) {
			Pokemon.getPlayer().doInteract();
		} else if (keyCode == Input.Keys.ENTER) {
			Pokemon.getGameScreen().getMenuController().openMenu();
			// game.setScreen(new MenuScreen(game));
		} else if (keyCode == Input.Keys.B) {
			// TODO: Remove this later. This is just for testing Screens
			game.setScreen(new BattleScreen(game));
		}

		return true;
	}
}
