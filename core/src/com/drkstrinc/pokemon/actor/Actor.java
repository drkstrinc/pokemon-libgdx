package com.drkstrinc.pokemon.actor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.datatype.BrainType;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.datatype.MovementState;
import com.drkstrinc.pokemon.event.Event;
import com.drkstrinc.pokemon.sound.SoundEffect;
import com.drkstrinc.pokemon.world.WorldManager;

public class Actor {

	private String name;
	private int id;

	private boolean lockMovement = false;
	private float movementTimeout = 0.2f;

	private int movementSpeed;

	protected int currentX;
	protected int currentY;

	private int targetX;
	private int targetY;

	private MovementState previousMovementState;
	private MovementState movementState;
	protected Direction direction;

	protected ActorSpriteSheet actorSpriteSheet;
	protected TextureRegion currentSprite;
	protected int spriteIndex;

	private Brain brain;
	private BrainType brainType;

	private ArrayList<Event> events;

	public Actor(int id, String name, String spriteFileName, BrainType brainType, int startX, int startY,
			Direction initialDirection) {
		this(id, name, startX, startY, initialDirection);
		this.brainType = brainType;
		actorSpriteSheet = new ActorSpriteSheet(spriteFileName);

		// TODO: Create different type of NPCs that extend Actor with defined Brains
		if (this.getClass().equals(Actor.class)) {
			brain = new Brain(this);
		}
	}

	public Actor(int id, String name, int startX, int startY, Direction initialDirection) {
		Gdx.app.log("CHR", "Creating " + this.getClass().getSimpleName() + " " + name + " at " + startX + "," + startY
				+ " facing " + initialDirection.toString());
		this.id = id;
		this.name = name;

		spriteIndex = 0;
		currentX = startX * Constants.TILE_WIDTH;
		currentY = startY * Constants.TILE_HEIGHT;
		targetX = currentX;
		targetY = currentY;

		direction = initialDirection;
		movementSpeed = MovementState.WALKING.getSpeed();
		movementState = MovementState.IDLE;

		events = new ArrayList<>();
	}

	public void render(SpriteBatch batch, OrthographicCamera camera) {
		updateActorSprite();

		// Sprite Sheet Offsets
		int offsetX = (currentSprite.getRegionWidth() / 2) - (Constants.TILE_WIDTH / 2);
		int offsetY = 6;

		// Render Actor Sprite
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(currentSprite, currentX - offsetX, currentY - offsetY, currentSprite.getRegionWidth(),
				currentSprite.getRegionHeight());
		batch.end();
	}

	public void update() {
		if (!isMovementLocked()) {
			handleMovement();
		}
	}

	protected void handleMovement() {
		if (!movementState.equals(MovementState.IDLE)) {
			if (getX() < getTargetX()) {
				currentX += movementSpeed;
			}

			if (getX() > getTargetX()) {
				currentX -= movementSpeed;
			}

			if (getY() < getTargetY()) {
				currentY += movementSpeed;
			}

			if (getY() > getTargetY()) {
				currentY -= movementSpeed;
			}

			setPreviousMovementState(movementState);

			if (Math.abs(getX() - getTargetX()) <= (movementSpeed - 1)
					&& Math.abs(getY() - getTargetY()) <= (movementSpeed - 1)) {
				currentX = targetX;
				currentY = targetY;
				spriteIndex++;
				if (spriteIndex > actorSpriteSheet.getFrameCount()) {
					resetSpriteIndex();
				}
				setMovementState(MovementState.IDLE);
				Gdx.app.debug("CHR", this.getClass().getSimpleName() + " " + id + " Name: " + name + " - X: "
						+ getCoordX() + " Y: " + getCoordY());
			}
		} else {
			setMovementState(MovementState.IDLE);
			setPreviousMovementState(MovementState.IDLE);
		}

		movementTimeout -= Gdx.graphics.getDeltaTime();
	}

	public void turnDown() {
		setDirection(Direction.DOWN);
		setMovementState(MovementState.IDLE);
		resetSpriteIndex();
		updateActorSprite();
	}

	public void turnLeft() {
		setDirection(Direction.LEFT);
		setMovementState(MovementState.IDLE);
		resetSpriteIndex();
		updateActorSprite();
	}

	public void turnRight() {
		setDirection(Direction.RIGHT);
		setMovementState(MovementState.IDLE);
		resetSpriteIndex();
		updateActorSprite();
	}

	public void turnUp() {
		setDirection(Direction.UP);
		setMovementState(MovementState.IDLE);
		resetSpriteIndex();
		updateActorSprite();
	}

	public void moveDown() {
		if (getDirection().equals(Direction.DOWN)) {
			if (movementTimeout < 0 && canMove(Direction.DOWN) && movementState.equals(MovementState.IDLE)) {
				setMovementState(MovementState.WALKING);
				targetY = getY() - Constants.TILE_HEIGHT;
				targetX = getX();
			}
		} else if (movementState.equals(MovementState.IDLE)) {
			turnDown();
			checkForTimeout();
		}
	}

	public void moveLeft() {
		if (getDirection().equals(Direction.LEFT)) {
			if (movementTimeout < 0 && canMove(Direction.LEFT) && movementState.equals(MovementState.IDLE)) {
				setMovementState(MovementState.WALKING);
				targetY = getY();
				targetX = getX() - Constants.TILE_WIDTH;
			}
		} else if (movementState.equals(MovementState.IDLE)) {
			turnLeft();
			checkForTimeout();
		}
	}

	public void moveRight() {
		if (getDirection().equals(Direction.RIGHT)) {
			if (movementTimeout < 0 && canMove(Direction.RIGHT) && movementState.equals(MovementState.IDLE)) {
				setMovementState(MovementState.WALKING);
				targetY = getY();
				targetX = getX() + Constants.TILE_WIDTH;
			}
		} else if (movementState.equals(MovementState.IDLE)) {
			turnRight();
			checkForTimeout();
		}
	}

	public void moveUp() {
		if (getDirection().equals(Direction.UP)) {
			if (movementTimeout < 0 && canMove(Direction.UP) && movementState.equals(MovementState.IDLE)) {
				setMovementState(MovementState.WALKING);
				targetY = getY() + Constants.TILE_HEIGHT;
				targetX = getX();
			}
		} else if (movementState.equals(MovementState.IDLE)) {
			turnUp();
			checkForTimeout();
		}
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

	private void checkForTimeout() {
		if (previousMovementState.equals(MovementState.IDLE)) {
			movementTimeout = 0.2f;
			spriteIndex = 1;
		} else {
			spriteIndex = 1;
		}
	}

	public boolean canMove(Direction direction) {
		// Check Map Tiles
		if (direction.equals(Direction.UP) && WorldManager.checkForCollisionAt(getCoordX(), getCoordY() + 1)) {
			if (id == 0)
				SoundEffect.bump();
			return false;
		}
		if (direction.equals(Direction.DOWN) && WorldManager.checkForCollisionAt(getCoordX(), getCoordY() - 1)) {
			if (id == 0)
				SoundEffect.bump();
			return false;
		}
		if (direction.equals(Direction.LEFT) && WorldManager.checkForCollisionAt(getCoordX() - 1, getCoordY())) {
			if (id == 0)
				SoundEffect.bump();
			return false;
		}
		if (direction.equals(Direction.RIGHT) && WorldManager.checkForCollisionAt(getCoordX() + 1, getCoordY())) {
			if (id == 0)
				SoundEffect.bump();
			return false;
		}

		// Check Actors (Current, Actor Target, Other Actor Target)
		for (Actor actor : WorldManager.getActors()) {
			if (direction.equals(Direction.UP) && (getY() + Constants.TILE_WIDTH == actor.getY())) {
				if (getX() == actor.getX()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			} else if (direction.equals(Direction.DOWN) && (getY() - Constants.TILE_WIDTH == actor.getY())) {
				if (getX() == actor.getX()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			} else if (direction.equals(Direction.LEFT) && (getX() - Constants.TILE_WIDTH == actor.getX())) {
				if (getY() == actor.getY()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			} else if (direction.equals(Direction.RIGHT) && (getX() + Constants.TILE_WIDTH == actor.getX())) {
				if (getY() == actor.getY()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			}

			if (direction.equals(Direction.UP) && (getTargetY() + Constants.TILE_WIDTH == actor.getY())) {
				if (getX() == actor.getX()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			} else if (direction.equals(Direction.DOWN) && (getTargetY() - Constants.TILE_WIDTH == actor.getY())) {
				if (getX() == actor.getX()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			} else if (direction.equals(Direction.LEFT) && (getTargetX() - Constants.TILE_WIDTH == actor.getX())) {
				if (getY() == actor.getY()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			} else if (direction.equals(Direction.RIGHT) && (getTargetX() + Constants.TILE_WIDTH == actor.getX())) {
				if (getY() == actor.getY()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			}

			if (direction.equals(Direction.UP) && (getTargetY() + Constants.TILE_WIDTH == actor.getTargetY())) {
				if (getX() == actor.getX()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			} else if (direction.equals(Direction.DOWN)
					&& (getTargetY() - Constants.TILE_WIDTH == actor.getTargetY())) {
				if (getX() == actor.getX()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			} else if (direction.equals(Direction.LEFT)
					&& (getTargetX() - Constants.TILE_WIDTH == actor.getTargetX())) {
				if (getY() == actor.getY()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			} else if (direction.equals(Direction.RIGHT)
					&& (getTargetX() + Constants.TILE_WIDTH == actor.getTargetX())) {
				if (getY() == actor.getY()) {
					Gdx.app.debug("TMX", "Actor Collision " + direction.toString() + " for "
							+ this.getClass().getSimpleName() + " " + name + " and " + actor.getName());
					if (id == 0)
						SoundEffect.bump();
					return false;
				}
			}
		}

		return true;
	}

	protected void updateActorSprite() {
		if (movementState.equals(MovementState.WALKING)) {
			if (direction.equals(Direction.DOWN)) {
				if (spriteIndex == 0) {
					currentSprite = actorSpriteSheet.getDownTexture(0);
				} else if (spriteIndex == 1) {
					currentSprite = actorSpriteSheet.getDownTexture(1);
				} else if (spriteIndex == 2) {
					currentSprite = actorSpriteSheet.getDownTexture(2);
				} else if (spriteIndex == 3) {
					currentSprite = actorSpriteSheet.getDownTexture(3);
				}
			} else if (direction.equals(Direction.LEFT)) {
				if (spriteIndex == 0) {
					currentSprite = actorSpriteSheet.getLeftTexture(0);
				} else if (spriteIndex == 1) {
					currentSprite = actorSpriteSheet.getLeftTexture(1);
				} else if (spriteIndex == 2) {
					currentSprite = actorSpriteSheet.getLeftTexture(2);
				} else if (spriteIndex == 3) {
					currentSprite = actorSpriteSheet.getLeftTexture(3);
				}
			} else if (direction.equals(Direction.RIGHT)) {
				if (spriteIndex == 0) {
					currentSprite = actorSpriteSheet.getRightTexture(0);
				} else if (spriteIndex == 1) {
					currentSprite = actorSpriteSheet.getRightTexture(1);
				} else if (spriteIndex == 2) {
					currentSprite = actorSpriteSheet.getRightTexture(2);
				} else if (spriteIndex == 3) {
					currentSprite = actorSpriteSheet.getRightTexture(3);
				}
			} else if (direction.equals(Direction.UP)) {
				if (spriteIndex == 0) {
					currentSprite = actorSpriteSheet.getUpTexture(0);
				} else if (spriteIndex == 1) {
					currentSprite = actorSpriteSheet.getUpTexture(1);
				} else if (spriteIndex == 2) {
					currentSprite = actorSpriteSheet.getUpTexture(2);
				} else if (spriteIndex == 3) {
					currentSprite = actorSpriteSheet.getUpTexture(3);
				}
			}
		} else if (movementState.equals(MovementState.RUNNING)) {

		} else if (movementState.equals(MovementState.BIKING)) {

		} else if (movementState.equals(MovementState.SURFING)) {

		} else if (movementState.equals(MovementState.FLYING)) {

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

	public int getCoordX() {
		return currentX / Constants.TILE_WIDTH;
	}

	public int getX() {
		return currentX;
	}

	public void setX(int x) {
		currentX = x;
	}

	public int getCoordY() {
		return currentY / Constants.TILE_HEIGHT;
	}

	public int getY() {
		return currentY;
	}

	public void setY(int y) {
		currentY = y;
	}

	public int getTargetCoordX() {
		return targetX / Constants.TILE_WIDTH;
	}

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetCoordY() {
		return targetY / Constants.TILE_WIDTH;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setPreviousMovementState(MovementState movementState) {
		previousMovementState = movementState;
	}

	public void setMovementState(MovementState movementState) {
		this.movementState = movementState;
	}

	public void setMovementSpeed(int movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public ActorSpriteSheet getCharacterSpriteSheet() {
		return actorSpriteSheet;
	}

	public void setCharacterSpriteSheet(ActorSpriteSheet characterSpriteSheet) {
		this.actorSpriteSheet = characterSpriteSheet;
	}

	public TextureRegion getCurrentSprite() {
		return currentSprite;
	}

	private void resetSpriteIndex() {
		spriteIndex = 0;
	}

	public int getActorId() {
		return id;
	}

	public void setActorId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BrainType getBrainType() {
		return brainType;
	}

	public void setBrainType(BrainType brainType) {
		this.brainType = brainType;
	}

	public Brain getBrain() {
		return brain;
	}

	public boolean isMovementLocked() {
		return lockMovement;
	}

	public void lockMovement() {
		lockMovement = true;
	}

	public void unlockMovement() {
		lockMovement = false;
	}

	public void addEvent(Event event) {
		events.add(event);
	}

	public Event getEvent(int id) {
		return events.get(id);
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

}
