package com.drkstrinc.pokemon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.drkstrinc.pokemon.Constants;

public class Player extends Character {

	private boolean lockMovement = false;

	public Player(String name, Gender gender, int startX, int startY, Direction initialDirection) {
		super(name, startX, startY, initialDirection);

		if (gender.equals(Gender.MALE)) {
			characterSpriteSheet = new CharacterSpriteSheet("gold.png");
		} else if (gender.equals(Gender.FEMALE)) {
			characterSpriteSheet = new CharacterSpriteSheet("kris.png");
		}
		updateActorSprite();
	}

	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		// Invisible Box around Player Sprite
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		actorBox.setProjectionMatrix(camera.combined);
		actorBox.begin(ShapeType.Line);
		actorBox.rect(currentX, currentY, Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
		actorBox.setColor(Color.CLEAR);
		actorBox.end();

		// Pick correct Player Sprite
		updateActorSprite();

		// Render Player Sprite
		batch.begin();
		batch.draw(currentSprite,
				Gdx.graphics.getWidth() / 2 - characterSpriteSheet.getDownTexture(0).getRegionWidth() / 2,
				Gdx.graphics.getHeight() / 2 - 1);
		batch.end();
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
