package com.drkstrinc.pokemon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.datatype.Gender;
import com.drkstrinc.pokemon.datatype.MovementState;

public class Player extends Actor {

	private boolean lockMovement = false;

	public Player(String name, Gender gender, int startX, int startY, Direction initialDirection) {
		super(name, startX, startY, initialDirection);

		if (gender.equals(Gender.MALE)) {
			actorSpriteSheet = new ActorSpriteSheet("gold.png");
		} else if (gender.equals(Gender.FEMALE)) {
			actorSpriteSheet = new ActorSpriteSheet("kris.png");
		}
		updateActorSprite();
	}

	@Override
	public void update() {
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {
			setState(MovementState.RUNNING);
		} else {
			setState(MovementState.WALKING);
		}

		handleMovement();

		if (!lockMovement) {
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				moveUp();
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				moveDown();
			} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				moveLeft();
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				moveRight();
			} else {
				stepCount = 1;
			}
		}
	}

}
