package com.drkstrinc.pokemon.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.drkstrinc.pokemon.screen.GameScreen;
import com.drkstrinc.pokemon.sound.SoundEffect;
import com.drkstrinc.pokemon.ui.MenuBox;

/**
 * 
 * @author hydrozoa
 *
 */
public class MenuController extends InputAdapter {

	private MenuBox menuBox;

	public MenuController(MenuBox menuBox) {
		this.menuBox = menuBox;
	}

	@Override
	public boolean keyDown(int keycode) {
		return menuBox.isVisible();
	}

	@Override
	public boolean keyUp(int keycode) {
		if (menuBox.isVisible()) {
			if (keycode == Keys.UP) {
				SoundEffect.select();
				menuBox.moveUp();
				return true;
			} else if (keycode == Keys.DOWN) {
				SoundEffect.select();
				menuBox.moveDown();
				return true;
			} else if (keycode == Keys.Z) {
				SoundEffect.select();
				menuBox.chooseOption();
				return true;
			} else if (keycode == Keys.X) {
				closeMenu();
				return true;
			}
		}

		return false;
	}

	public void update(float delta) {
		// TODO: Handle sub-menu updates here
	}

	public void openMenu() {
		SoundEffect.select();
		GameScreen.lockActors();
		menuBox.setVisible(true);
	}

	public void closeMenu() {
		SoundEffect.select();
		GameScreen.unlockActors();
		menuBox.setVisible(false);
	}

}
