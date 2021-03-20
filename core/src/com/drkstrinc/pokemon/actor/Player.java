package com.drkstrinc.pokemon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.drkstrinc.pokemon.Constants;

public class Player {

	public static enum Gender {
		MALE, FEMALE
	}

	public static enum Direction {
		DOWN, LEFT, RIGHT, UP
	}

	public static enum MovementState {
		IDLE, WALKING, RUNNING, BIKING, SURFING, FLYING
	}

	private String name;

	private int currentX = 0;
	private int currentY = 0;

	private boolean lockMovement = false;
	private boolean isMoving = false;
	private float moveTimeout = 1;
	private float moveSpeed = 1;

	private int targetX = 0;
	private int targetY = 0;

	private Direction direction = Direction.DOWN;
	private MovementState movementState = MovementState.IDLE;

	private ShapeRenderer playerBox;
	private CharacterSpriteSheet characterSpriteSheet;
	private TextureRegion currentSprite;

	public Player(String name, Gender gender, int startX, int startY, Direction initialDirection) {
		this.name = name;

		playerBox = new ShapeRenderer();

		if (gender.equals(Gender.MALE)) {
			characterSpriteSheet = new CharacterSpriteSheet("gold.png");
		} else {
			characterSpriteSheet = new CharacterSpriteSheet("kris.png");
		}
		turnDown();

		currentX = startX * Constants.TILE_WIDTH;
		currentY = startY * Constants.TILE_HEIGHT;
		targetX = currentX;
		targetY = currentY;

		direction = initialDirection;
	}

	public void render(SpriteBatch batch, OrthographicCamera camera) {
		// Invisible Box around Player Sprite
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		playerBox.setProjectionMatrix(camera.combined);
		playerBox.begin(ShapeType.Line);
		playerBox.rect(currentX, currentY, Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
		playerBox.setColor(Color.CLEAR);
		playerBox.end();

		// Player Sprite
		batch.begin();
		batch.draw(currentSprite,
				Gdx.graphics.getWidth() / 2 - characterSpriteSheet.getDownTexture(0).getRegionWidth() / 2,
				Gdx.graphics.getHeight() / 2 - 1);
		batch.end();
	}

	public void update() {
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {
			moveSpeed = 2;
		} else {
			moveSpeed = 1;
		}

		if (isMoving) {
			if (moveSpeed == 1) {
				setState(MovementState.WALKING);
			} else if (moveSpeed == 2) {
				setState(MovementState.RUNNING);
			}

			if (getX() < targetX) {
				currentX += moveSpeed;
			}

			if (getX() > targetX) {
				currentX -= moveSpeed;
			}

			if (getY() < targetY) {
				currentY += moveSpeed;
			}

			if (getY() > targetY) {
				currentY -= moveSpeed;
			}

			if (Math.abs(getX() - targetX) <= 1 && Math.abs(getY() - targetY) <= 1) {
				updatePlayerSprite();
				currentX = targetX;
				currentY = targetY;
				isMoving = false;
			}
		} else {
			setState(MovementState.IDLE);
		}

		if (!lockMovement) {
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				if (getDirection().equals(Direction.UP)) {
					if (moveTimeout < 0 && canMove(Direction.UP)) {
						if (!isMoving) {
							updatePlayerSprite();
							isMoving = true;
							targetY = getY() + Constants.TILE_HEIGHT;
							targetX = getX();
						}
					}
				} else {
					setDirection(Direction.UP);
					if (currentX == targetX && currentY == targetY)
						updatePlayerSprite();
					moveTimeout = 0.2f;
				}
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				if (getDirection().equals(Direction.DOWN)) {
					if (moveTimeout < 0 && canMove(Direction.DOWN)) {
						if (!isMoving) {
							updatePlayerSprite();
							isMoving = true;
							targetY = getY() - Constants.TILE_HEIGHT;
							targetX = getX();
						}
					}
				} else {
					setDirection(Direction.DOWN);
					if (currentX == targetX && currentY == targetY)
						updatePlayerSprite();
					moveTimeout = 0.2f;
				}
			} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				if (getDirection().equals(Direction.LEFT)) {
					if (moveTimeout < 0 && canMove(Direction.LEFT)) {
						if (!isMoving) {
							updatePlayerSprite();
							isMoving = true;
							targetY = getY();
							targetX = getX() - Constants.TILE_WIDTH;
						}
					}
				} else {
					setDirection(Direction.LEFT);
					if (currentX == targetX && currentY == targetY)
						updatePlayerSprite();
					moveTimeout = 0.2f;
				}
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				if (getDirection().equals(Direction.RIGHT)) {
					if (moveTimeout < 0 && canMove(Direction.RIGHT)) {
						if (!isMoving) {
							updatePlayerSprite();
							isMoving = true;
							targetY = getY();
							targetX = getX() + Constants.TILE_WIDTH;
						}
					}
				} else {
					setDirection(Direction.RIGHT);
					if (currentX == targetX && currentY == targetY)
						updatePlayerSprite();
					moveTimeout = 0.2f;
				}
			}

			moveTimeout -= Gdx.graphics.getDeltaTime();
		}
	}

	public boolean canMove(Direction diretion) {
		return true;
	}

	private void updatePlayerSprite() {
		if (direction.equals(Direction.DOWN)) {
			currentSprite = characterSpriteSheet.getDownTexture(0);
		} else if (direction.equals(Direction.LEFT)) {
			currentSprite = characterSpriteSheet.getLeftTexture(0);
		} else if (direction.equals(Direction.RIGHT)) {
			currentSprite = characterSpriteSheet.getRightTexture(0);
		} else if (direction.equals(Direction.UP)) {
			currentSprite = characterSpriteSheet.getUpTexture(0);
		}
	}

	public void turnDown() {
		direction = Direction.DOWN;
		updatePlayerSprite();
	}

	public void turnLeft() {
		direction = Direction.LEFT;
		updatePlayerSprite();
	}

	public void turnRight() {
		direction = Direction.RIGHT;
		updatePlayerSprite();
	}

	public void turnUp() {
		direction = Direction.UP;
		updatePlayerSprite();
	}

	public int getX() {
		return currentX;
	}

	public int getY() {
		return currentY;
	}

	public int getCoordX() {
		return currentX / Constants.TILE_WIDTH;
	}

	public int getCoordY() {
		return currentY / Constants.TILE_HEIGHT;
	}

	public void setX(int x) {
		currentX = x;
	}

	public void setY(int y) {
		currentY = y;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public MovementState getState() {
		return movementState;
	}

	public void setState(MovementState movementState) {
		this.movementState = movementState;
	}

	public CharacterSpriteSheet getCharacterSpriteSheet() {
		return characterSpriteSheet;
	}

	public void setCharacterSpriteSheet(CharacterSpriteSheet characterSpriteSheet) {
		this.characterSpriteSheet = characterSpriteSheet;

	}

	public String getName() {
		return name;
	}
}
