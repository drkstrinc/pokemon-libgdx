package com.drkstrinc.pokemon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.Pokemon;

public abstract class Character {

	public static enum Gender {
		MALE, FEMALE
	}

	public static enum Direction {
		DOWN, LEFT, RIGHT, UP
	}

	public static enum MovementState {
		IDLE(0), WALKING(2), RUNNING(3), BIKING(4), SURFING(2), FLYING(2);

		private final int speed;

		MovementState(int speed) {
			this.speed = speed;
		}

		public int getValue() {
			return speed;
		}
	}

	private String name;

	protected int currentX = 0;
	protected int currentY = 0;

	private int targetX = 0;
	private int targetY = 0;

	private float movementTimeout = 1;

	private boolean isMoving = false;
	private int movementSpeed = MovementState.WALKING.getValue();
	private MovementState movementState = MovementState.IDLE;
	private Direction direction = Direction.DOWN;

	protected ShapeRenderer actorBox;
	protected CharacterSpriteSheet characterSpriteSheet;
	protected TextureRegion currentSprite;
	protected int stepCount;

	public Character(String name, int startX, int startY, Direction initialDirection) {
		this.name = name;
		actorBox = new ShapeRenderer();

		stepCount = 0;
		currentX = startX * Constants.TILE_WIDTH;
		currentY = startY * Constants.TILE_HEIGHT;
		targetX = currentX;
		targetY = currentY;

		direction = initialDirection;
	}

	public void render(SpriteBatch batch, OrthographicCamera camera) {

	}

	public void update() {

	}

	protected void handleMovement() {
		if (isMoving) {
			if (movementState.equals(MovementState.WALKING)) {
				movementSpeed = MovementState.WALKING.getValue();
			} else if (movementState.equals(MovementState.RUNNING)) {
				movementSpeed = MovementState.RUNNING.getValue();
			}

			if (getX() < targetX) {
				currentX += movementSpeed;
			}

			if (getX() > targetX) {
				currentX -= movementSpeed;
			}

			if (getY() < targetY) {
				currentY += movementSpeed;
			}

			if (getY() > targetY) {
				currentY -= movementSpeed;
			}

			if (Math.abs(getX() - targetX) <= 1 && Math.abs(getY() - targetY) <= 1) {
				currentX = targetX;
				currentY = targetY;
				stepCount++;
				if (stepCount > 3) {
					stepCount = 0;
				}
				isMoving = false;
				Gdx.app.debug("TMX", "Position - X: " + getCoordX() + " Y: " + getCoordY());
			}
		} else {
			setState(MovementState.IDLE);
		}

		movementTimeout -= Gdx.graphics.getDeltaTime();
	}

	public void moveDown() {
		if (getDirection().equals(Direction.DOWN)) {
			if (movementTimeout < 0 && canMove(Direction.DOWN)) {
				if (!isMoving) {
					isMoving = true;
					targetY = getY() - Constants.TILE_HEIGHT;
					targetX = getX();
				}
			}
		} else {
			setDirection(Direction.DOWN);
			if (currentX == targetX && currentY == targetY)
				turnDown();
			movementTimeout = 0.2f;
		}
	}

	public void moveLeft() {
		if (getDirection().equals(Direction.LEFT)) {
			if (movementTimeout < 0 && canMove(Direction.LEFT)) {
				if (!isMoving) {
					isMoving = true;
					targetY = getY();
					targetX = getX() - Constants.TILE_WIDTH;
				}
			}
		} else {
			setDirection(Direction.LEFT);
			if (currentX == targetX && currentY == targetY)
				turnLeft();
			movementTimeout = 0.2f;
		}
	}

	public void moveRight() {
		if (getDirection().equals(Direction.RIGHT)) {
			if (movementTimeout < 0 && canMove(Direction.RIGHT)) {
				if (!isMoving) {
					isMoving = true;
					targetY = getY();
					targetX = getX() + Constants.TILE_WIDTH;
				}
			}
		} else {
			setDirection(Direction.RIGHT);
			if (currentX == targetX && currentY == targetY)
				turnRight();
			movementTimeout = 0.2f;
		}
	}

	public void moveUp() {
		if (getDirection().equals(Direction.UP)) {
			if (movementTimeout < 0 && canMove(Direction.UP)) {
				if (!isMoving) {
					isMoving = true;
					targetY = getY() + Constants.TILE_HEIGHT;
					targetX = getX();
				}
			}
		} else {
			setDirection(Direction.UP);
			if (currentX == targetX && currentY == targetY)
				turnUp();
			movementTimeout = 0.2f;
		}
	}

	public boolean canMove(Direction direction) {
		MapLayer collisionLayer = Pokemon.getCurrentMap().getLayers().get("Collision");
		MapObjects objects = collisionLayer.getObjects();

		for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

			Rectangle rectangle = rectangleObject.getRectangle();
			if (direction.equals(Direction.UP) && Intersector.overlaps(rectangle, new Rectangle(getX(),
					getY() + Constants.TILE_HEIGHT, Constants.TILE_WIDTH, Constants.TILE_HEIGHT))) {
				return false;
			} else if (direction.equals(Direction.DOWN) && Intersector.overlaps(rectangle, new Rectangle(getX(),
					getY() - Constants.TILE_HEIGHT, Constants.TILE_WIDTH, Constants.TILE_HEIGHT))) {
				return false;
			} else if (direction.equals(Direction.LEFT)
					&& Intersector.overlaps(rectangle, new Rectangle(getX() - Constants.TILE_WIDTH, getY(),
							Constants.TILE_WIDTH, Constants.TILE_HEIGHT))) {
				return false;
			} else if (direction.equals(Direction.RIGHT)
					&& Intersector.overlaps(rectangle, new Rectangle(getX() + Constants.TILE_WIDTH, getY(),
							Constants.TILE_WIDTH, Constants.TILE_HEIGHT))) {
				return false;
			}
		}

		return true;
	}

	protected void updateActorSprite() {
		if (isMoving) {
			if (movementState.equals(MovementState.WALKING) || movementState.equals(MovementState.RUNNING)) {
				if (direction.equals(Direction.DOWN)) {
					if (stepCount == 0) {
						currentSprite = characterSpriteSheet.getDownTexture(0);
					} else if (stepCount == 1) {
						currentSprite = characterSpriteSheet.getDownTexture(1);
					} else if (stepCount == 2) {
						currentSprite = characterSpriteSheet.getDownTexture(2);
					} else if (stepCount == 3) {
						currentSprite = characterSpriteSheet.getDownTexture(3);
					}
				} else if (direction.equals(Direction.LEFT)) {
					if (stepCount == 0) {
						currentSprite = characterSpriteSheet.getLeftTexture(0);
					} else if (stepCount == 1) {
						currentSprite = characterSpriteSheet.getLeftTexture(1);
					} else if (stepCount == 2) {
						currentSprite = characterSpriteSheet.getLeftTexture(2);
					} else if (stepCount == 3) {
						currentSprite = characterSpriteSheet.getLeftTexture(3);
					}
				} else if (direction.equals(Direction.RIGHT)) {
					if (stepCount == 0) {
						currentSprite = characterSpriteSheet.getRightTexture(0);
					} else if (stepCount == 1) {
						currentSprite = characterSpriteSheet.getRightTexture(1);
					} else if (stepCount == 2) {
						currentSprite = characterSpriteSheet.getRightTexture(2);
					} else if (stepCount == 3) {
						currentSprite = characterSpriteSheet.getRightTexture(3);
					}
				} else if (direction.equals(Direction.UP)) {
					if (stepCount == 0) {
						currentSprite = characterSpriteSheet.getUpTexture(0);
					} else if (stepCount == 1) {
						currentSprite = characterSpriteSheet.getUpTexture(1);
					} else if (stepCount == 2) {
						currentSprite = characterSpriteSheet.getUpTexture(2);
					} else if (stepCount == 3) {
						currentSprite = characterSpriteSheet.getUpTexture(3);
					}
				}
			} else if (movementState.equals(MovementState.BIKING)) {

			} else if (movementState.equals(MovementState.SURFING)) {

			} else if (movementState.equals(MovementState.FLYING)) {

			} else {

			}
		} else {
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
	}

	public void turnDown() {
		direction = Direction.DOWN;
		isMoving = false;
		stepCount = 0;
		updateActorSprite();
	}

	public void turnLeft() {
		direction = Direction.LEFT;
		isMoving = false;
		stepCount = 0;
		updateActorSprite();
	}

	public void turnRight() {
		direction = Direction.RIGHT;
		isMoving = false;
		stepCount = 0;
		updateActorSprite();
	}

	public void turnUp() {
		direction = Direction.UP;
		isMoving = false;
		stepCount = 0;
		updateActorSprite();
	}

	public void moveTo(int x, int y) {

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
