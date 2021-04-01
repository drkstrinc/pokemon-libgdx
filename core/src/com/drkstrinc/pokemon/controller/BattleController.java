package com.drkstrinc.pokemon.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import com.drkstrinc.pokemon.battle.Battle;
import com.drkstrinc.pokemon.datatype.BattleState;
import com.drkstrinc.pokemon.monster.Move;
import com.drkstrinc.pokemon.sound.SoundEffect;
import com.drkstrinc.pokemon.ui.BattleActionBox;
import com.drkstrinc.pokemon.ui.BattleMoveBox;

public class BattleController extends InputAdapter {

	private Battle battle;

	private BattleActionBox actionBox;
	private BattleMoveBox moveBox;

	public BattleController(Battle battle, BattleActionBox actionBox, BattleMoveBox moveBox) {
		this.battle = battle;
		this.actionBox = actionBox;
		this.moveBox = moveBox;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (battle.getState() == BattleState.SELECT_ACTION) {
			if (keycode == Keys.Z) {
				SoundEffect.select();
				handleAction();
				return true;
			} else if (keycode == Keys.X) {
				SoundEffect.select();
				battle.setState(BattleState.SELECT_ACTION);
				return true;
			} else if (keycode == Keys.UP) {
				SoundEffect.select();
				actionBox.moveUp();
				return true;
			} else if (keycode == Keys.DOWN) {
				SoundEffect.select();
				actionBox.moveDown();
				return true;
			} else if (keycode == Keys.LEFT) {
				SoundEffect.select();
				actionBox.moveLeft();
				return true;
			} else if (keycode == Keys.RIGHT) {
				SoundEffect.select();
				actionBox.moveRight();
				return true;
			}
		} else if (battle.getState() == BattleState.SELECT_MOVE) {
			if (keycode == Keys.Z) {
				Gdx.app.debug("BTL", "Selected Move: "
						+ battle.getPlayerActivePokemon().getMoves()[moveBox.getSelection()].getDisplayName());
				SoundEffect.select();
				handleMove();
				return true;
			} else if (keycode == Keys.X) {
				SoundEffect.select();
				battle.setState(BattleState.SELECT_ACTION);
				return true;
			} else if (keycode == Keys.UP) {
				SoundEffect.select();
				moveBox.moveUp();
				return true;
			} else if (keycode == Keys.DOWN) {
				SoundEffect.select();
				moveBox.moveDown();
				return true;
			} else if (keycode == Keys.LEFT) {
				SoundEffect.select();
				moveBox.moveLeft();
				return true;
			} else if (keycode == Keys.RIGHT) {
				SoundEffect.select();
				moveBox.moveRight();
				return true;
			}
		} else if (battle.getState() == BattleState.SELECT_PKMN) {

		}

		return false;
	}

	private void handleAction() {
		int sel = actionBox.getSelection();
		if (sel == 0) {
			battle.setState(BattleState.SELECT_MOVE);
		} else if (sel == 1) {
			Gdx.app.debug("BTL", "Not Implemented");
			// battle.setState(BattleState.SELECT_PKMN);
		} else if (sel == 2) {
			Gdx.app.debug("BTL", "Not Implemented");
			// battle.setState(BattleState.SELECT_ITEM);
		} else if (sel == 3) {
			battle.setState(BattleState.RUN);
		}
	}

	private void handleMove() {
		int sel = moveBox.getSelection();
		if (battle.getPlayerActivePokemon().getMoves()[sel].getDisplayName() != Move.EMPTY) {
			if (battle.getPlayerActivePokemon().getMoves()[sel].canUseMove()) {
				battle.getPlayerActivePokemon().getMoves()[sel].useMove();
				battle.setState(BattleState.SELECT_ACTION);
				// TODO: Handle Move logic
			} else {
				// Not enough PP or some other reason
			}
		}
	}

}
