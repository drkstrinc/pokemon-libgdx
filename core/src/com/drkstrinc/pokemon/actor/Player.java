package com.drkstrinc.pokemon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.datatype.Gender;
import com.drkstrinc.pokemon.datatype.MovementState;
import com.drkstrinc.pokemon.world.WorldManager;

public class Player extends Actor {

	public Player(String name, Gender gender, int startX, int startY, Direction initialDirection) {
		super(0, name, startX, startY, initialDirection);

		if (gender.equals(Gender.MALE)) {
			actorSpriteSheet = new ActorSpriteSheet("Gold.png");
		} else if (gender.equals(Gender.FEMALE)) {
			actorSpriteSheet = new ActorSpriteSheet("Kris.png");
		}
		updateActorSprite();
	}

	@Override
	public void update() {
		if (!isMovementLocked()) {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				setMovementSpeed(MovementState.BIKING.getSpeed());
			} else if (Gdx.input.isKeyPressed(Input.Keys.X)) {
				setMovementSpeed(MovementState.RUNNING.getSpeed());
			} else {
				setMovementSpeed(MovementState.WALKING.getSpeed());
			}

			handleMovement();
			checkInputs();
		}
	}

	private void checkInputs() {
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			moveUp(1);
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if (WorldManager.getTilesAt(getCoordX(), getCoordY() - 1).contains(10)) {
				moveDown(2);
			} else {
				moveDown(1);
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if (WorldManager.getTilesAt(getCoordX() - 1, getCoordY()).contains(1)) {
				moveLeft(2);
			} else {
				moveLeft(1);
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if (WorldManager.getTilesAt(getCoordX() + 1, getCoordY()).contains(3)) {
				moveRight(2);
			} else {
				moveRight(1);
			}
		} else {
			spriteIndex = 1;
		}
	}

	public void doInteract() {
		for (Actor actor : WorldManager.getActors()) {
			if (direction.equals(Direction.UP) && (getY() + Constants.TILE_WIDTH == actor.getY())) {
				if (getX() == actor.getX()) {
					actor.turnDown();
					Pokemon.getGameScreen().getMessageController().startDialogue(actor);
					return;
				}
			} else if (direction.equals(Direction.DOWN) && (getY() - Constants.TILE_WIDTH == actor.getY())) {
				if (getX() == actor.getX()) {
					actor.turnUp();
					Pokemon.getGameScreen().getMessageController().startDialogue(actor);
					return;
				}
			} else if (direction.equals(Direction.LEFT) && (getX() - Constants.TILE_WIDTH == actor.getX())) {
				if (getY() == actor.getY()) {
					actor.turnRight();
					Pokemon.getGameScreen().getMessageController().startDialogue(actor);
					return;
				}
			} else if (direction.equals(Direction.RIGHT) && (getX() + Constants.TILE_WIDTH == actor.getX())) {
				if (getY() == actor.getY()) {
					actor.turnLeft();
					Pokemon.getGameScreen().getMessageController().startDialogue(actor);
					return;
				}
			}
		}
	}

}
