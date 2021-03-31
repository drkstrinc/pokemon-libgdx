package com.drkstrinc.pokemon.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import com.drkstrinc.pokemon.battle.Battle;
import com.drkstrinc.pokemon.datatype.BattleState;
import com.drkstrinc.pokemon.ui.ChoiceBox;
import com.drkstrinc.pokemon.ui.MessageBox;
import com.drkstrinc.pokemon.ui.MoveSelectBox;

public class BattleController extends InputAdapter {

	private Battle battle;

	private MessageBox messageBox;
	private MoveSelectBox moveSelectBox;
	private ChoiceBox choiceBox;

	public BattleController(Battle battle, MessageBox messageBox, MoveSelectBox moveSelectBoxBox, ChoiceBox choiceBox) {
		this.battle = battle;
		this.messageBox = messageBox;
		this.moveSelectBox = moveSelectBoxBox;
		this.choiceBox = choiceBox;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (battle.getState() == BattleState.SELECT_ACTION) {
			if (keycode == Keys.Z) {
				return true;
			} else if (keycode == Keys.X) {
				battle.setState(BattleState.RUN);
				return true;
			} else if (keycode == Keys.UP) {
				choiceBox.moveUp();
				return true;
			} else if (keycode == Keys.DOWN) {
				choiceBox.moveDown();
				return true;
			} else if (keycode == Keys.LEFT) {
				return true;
			} else if (keycode == Keys.RIGHT) {
				return true;
			}
		} else if (battle.getState() == BattleState.SELECT_MOVE) {
			if (keycode == Keys.Z) {
				Gdx.app.debug("BTL", "Selected Move: " + moveSelectBox.getSelection());
				return true;
			} else if (keycode == Keys.UP) {
				moveSelectBox.moveUp();
				return true;
			} else if (keycode == Keys.DOWN) {
				moveSelectBox.moveDown();
				return true;
			} else if (keycode == Keys.LEFT) {
				moveSelectBox.moveLeft();
				return true;
			} else if (keycode == Keys.RIGHT) {
				moveSelectBox.moveRight();
				return true;
			}
		} else if (battle.getState() == BattleState.CHOOSE_NEXT) {

		}

		return false;
	}

}
