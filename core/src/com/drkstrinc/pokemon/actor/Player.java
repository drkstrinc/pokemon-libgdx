package com.drkstrinc.pokemon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.datatype.Gender;
import com.drkstrinc.pokemon.datatype.MovementState;
import com.drkstrinc.pokemon.screen.GameScreen;
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
			if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
				doInteract();
			}

			// TODO: Remove this Map Change/Teleport Test
			if (Gdx.input.isKeyPressed(Input.Keys.T)) {
				WorldManager.setWorld("NewBarkTown", 15, 25, Direction.LEFT);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				setMovementSpeed(MovementState.BIKING.getSpeed());
			} else if (Gdx.input.isKeyPressed(Input.Keys.X)) {
				setMovementSpeed(MovementState.RUNNING.getSpeed());
			} else {
				setMovementSpeed(MovementState.WALKING.getSpeed());
			}

			handleMovement();

			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				moveUp();
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				moveDown();
			} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				moveLeft();
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				moveRight();
			} else {
				spriteIndex = 1;
			}
		}
	}

	private void doInteract() {
		// TODO: Work on Message window and having text render there
		for (Actor actor : WorldManager.getActors()) {
			if (direction.equals(Direction.UP) && (getY() + Constants.TILE_WIDTH == actor.getY())) {
				if (getX() == actor.getX()) {
					GameScreen.lockActors();
					actor.turnDown();
					Gdx.app.log("CHR", actor.getEvent(0).getMessages().get(0));
				}
			} else if (direction.equals(Direction.DOWN) && (getY() - Constants.TILE_WIDTH == actor.getY())) {
				if (getX() == actor.getX()) {
					GameScreen.lockActors();
					actor.turnUp();
					Gdx.app.log("CHR", actor.getEvent(0).getMessages().get(0));
				}
			} else if (direction.equals(Direction.LEFT) && (getX() - Constants.TILE_WIDTH == actor.getX())) {
				if (getY() == actor.getY()) {
					GameScreen.lockActors();
					actor.turnRight();
					Gdx.app.log("CHR", actor.getEvent(0).getMessages().get(0));
				}
			} else if (direction.equals(Direction.RIGHT) && (getX() + Constants.TILE_WIDTH == actor.getX())) {
				if (getY() == actor.getY()) {
					GameScreen.lockActors();
					actor.turnLeft();
					Gdx.app.log("CHR", actor.getEvent(0).getMessages().get(0));
				}
			}
		}

		GameScreen.unlockActors();
	}

}
