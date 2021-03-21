package com.drkstrinc.pokemon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.datatype.MovementState;
import com.drkstrinc.pokemon.world.World;

public class Actor {

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
	protected ActorSpriteSheet actorSpriteSheet;
	protected TextureRegion currentSprite;
	protected int stepCount;

	public Actor(String name, String spriteFileName, int startX, int startY, Direction initialDirection) {
		this(name, startX, startY, initialDirection);
		actorSpriteSheet = new ActorSpriteSheet(spriteFileName);
	}

	public Actor(String name, int startX, int startY, Direction initialDirection) {
		Gdx.app.log("CHR", "Creating " + this.getClass().getSimpleName() + " " + name + " at " + startX + "," + startY
				+ " facing " + initialDirection.toString());
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
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

		// Invisible Box around Actor Sprite for Collision
		actorBox.setProjectionMatrix(camera.combined);
		actorBox.begin(ShapeType.Line);
		actorBox.rect(currentX, currentY, Constants.TILE_WIDTH * Constants.GAME_SCALE,
				Constants.TILE_HEIGHT * Constants.GAME_SCALE);
		actorBox.setColor(Color.CLEAR);
		actorBox.end();

		// Update Actor Sprite
		updateActorSprite();

		int offsetX = (currentSprite.getRegionWidth() / 2 * Constants.GAME_SCALE)
				- (Constants.TILE_WIDTH * Constants.GAME_SCALE) / 2;

		// Render Actor Sprite
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(currentSprite, currentX - offsetX, currentY, currentSprite.getRegionWidth() * Constants.GAME_SCALE,
				currentSprite.getRegionHeight() * Constants.GAME_SCALE);
		batch.end();
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
				Gdx.app.log("CHR", "Actor: " + name + " - X: " + getCoordX() + " Y: " + getCoordY());
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
		MapLayer collisionLayer = World.getCurrentMap().getLayers().get("Collision");
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
						currentSprite = actorSpriteSheet.getDownTexture(0);
					} else if (stepCount == 1) {
						currentSprite = actorSpriteSheet.getDownTexture(1);
					} else if (stepCount == 2) {
						currentSprite = actorSpriteSheet.getDownTexture(2);
					} else if (stepCount == 3) {
						currentSprite = actorSpriteSheet.getDownTexture(3);
					}
				} else if (direction.equals(Direction.LEFT)) {
					if (stepCount == 0) {
						currentSprite = actorSpriteSheet.getLeftTexture(0);
					} else if (stepCount == 1) {
						currentSprite = actorSpriteSheet.getLeftTexture(1);
					} else if (stepCount == 2) {
						currentSprite = actorSpriteSheet.getLeftTexture(2);
					} else if (stepCount == 3) {
						currentSprite = actorSpriteSheet.getLeftTexture(3);
					}
				} else if (direction.equals(Direction.RIGHT)) {
					if (stepCount == 0) {
						currentSprite = actorSpriteSheet.getRightTexture(0);
					} else if (stepCount == 1) {
						currentSprite = actorSpriteSheet.getRightTexture(1);
					} else if (stepCount == 2) {
						currentSprite = actorSpriteSheet.getRightTexture(2);
					} else if (stepCount == 3) {
						currentSprite = actorSpriteSheet.getRightTexture(3);
					}
				} else if (direction.equals(Direction.UP)) {
					if (stepCount == 0) {
						currentSprite = actorSpriteSheet.getUpTexture(0);
					} else if (stepCount == 1) {
						currentSprite = actorSpriteSheet.getUpTexture(1);
					} else if (stepCount == 2) {
						currentSprite = actorSpriteSheet.getUpTexture(2);
					} else if (stepCount == 3) {
						currentSprite = actorSpriteSheet.getUpTexture(3);
					}
				}
			} else if (movementState.equals(MovementState.BIKING)) {

			} else if (movementState.equals(MovementState.SURFING)) {

			} else if (movementState.equals(MovementState.FLYING)) {

			} else {

			}
		} else {
			if (direction.equals(Direction.DOWN)) {
				currentSprite = actorSpriteSheet.getDownTexture(0);
			} else if (direction.equals(Direction.LEFT)) {
				currentSprite = actorSpriteSheet.getLeftTexture(0);
			} else if (direction.equals(Direction.RIGHT)) {
				currentSprite = actorSpriteSheet.getRightTexture(0);
			} else if (direction.equals(Direction.UP)) {
				currentSprite = actorSpriteSheet.getUpTexture(0);
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

	public void moveTo(int x, int y, Direction direction) {
		Gdx.app.log("CHR", "Moving " + this.getClass().getSimpleName() + " " + name + " to " + x + "," + y + " facing "
				+ direction.toString());
		currentX = x * Constants.TILE_WIDTH;
		currentY = y * Constants.TILE_HEIGHT;
		targetX = currentX;
		targetY = currentY;
		if (direction.equals(Direction.UP))
			turnUp();
		else if (direction.equals(Direction.DOWN))
			turnDown();
		else if (direction.equals(Direction.LEFT))
			turnLeft();
		else if (direction.equals(Direction.RIGHT))
			turnRight();
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

	public ActorSpriteSheet getCharacterSpriteSheet() {
		return actorSpriteSheet;
	}

	public void setCharacterSpriteSheet(ActorSpriteSheet characterSpriteSheet) {
		this.actorSpriteSheet = characterSpriteSheet;
	}

	public String getName() {
		return name;
	}

}
