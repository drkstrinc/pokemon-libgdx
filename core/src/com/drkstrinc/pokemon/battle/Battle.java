package com.drkstrinc.pokemon.battle;

import java.util.ArrayList;

import com.drkstrinc.pokemon.datatype.BattleState;
import com.drkstrinc.pokemon.datatype.BattleTurn;
import com.drkstrinc.pokemon.datatype.BattleType;
import com.drkstrinc.pokemon.monster.Monster;

public class Battle {

	private BattleTurn turn;

	private BattleState state;
	private BattleState previousState;

	private BattleType type;

	private ArrayList<Monster> playerMonsters;
	private ArrayList<Monster> enemyMonsters;

	private Monster playerActiveMonster;
	private Monster enemyActiveMonster;

	public Battle() {
		type = BattleType.WILD;
		turn = BattleTurn.PLAYER;
		state = BattleState.INIT;
		previousState = BattleState.INIT;

		// TODO: Change this
		playerActiveMonster = new Monster(25, 5);
		enemyActiveMonster = new Monster(133, 5);
	}

	public Monster getPlayerActivePokemon() {
		return playerActiveMonster;
	}

	public Monster getEnemyActivePokemon() {
		return enemyActiveMonster;
	}

	public void calculateDamage() {
		enemyActiveMonster.takeDamage(10);
	}

	public void win() {

	}

	public void lose() {

	}

	public void run() {

	}

	public BattleType getType() {
		return type;
	}

	public BattleTurn getTurn() {
		return turn;
	}

	public void setTurn(BattleTurn turn) {
		this.turn = turn;
	}

	public BattleState getState() {
		return state;
	}

	public void setState(BattleState state) {
		this.state = state;
	}

	public BattleState getPreivousState() {
		return previousState;
	}

	public void setPreviousState(BattleState state) {
		this.previousState = state;
	}

}
