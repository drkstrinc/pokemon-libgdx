package com.drkstrinc.pokemon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.drkstrinc.pokemon.Constants;

public class Player {

	private String name;

	private int currentX = 0;
	private int currentY = 0;

	private boolean lockMovement = false;
	private boolean isMoving = false;
	private float moveTimeout = 1;
	private float moveSpeed = 1;

	private int targetX = 0;
	private int targetY = 0;

	private String direction = "DOWN";
	private String movementState = "IDLE";

	public Player(String name, int startX, int startY, String initialDirection) {
		this.name = name;
		currentX = startX * Constants.TILE_WIDTH;
		currentY = startY * Constants.TILE_HEIGHT;
		targetX = currentX;
		targetY = currentY;
		direction = initialDirection;
	}

	public void render(ShapeRenderer shape, OrthographicCamera camera) {
		shape.setProjectionMatrix(camera.combined);
		shape.begin(ShapeType.Filled);
		shape.rect(currentX, currentY, Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
		shape.setColor(Color.BLACK);
		shape.end();
	}

	public void update() {
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {
			moveSpeed = 2;
		} else {
			moveSpeed = 1;
		}

		if (isMoving) {
			if (moveSpeed == 1) {
				setState("WALKING");
			} else if (moveSpeed == 2) {
				setState("RUNNING");
			}

			if (getX() < targetX) {
				// getSprite().translateX(moveSpeed);
				currentX += moveSpeed;
			}

			if (getX() > targetX) {
				// getSprite().translateX(-moveSpeed);
				currentX -= moveSpeed;
			}

			if (getY() < targetY) {
				// getSprite().translateY(moveSpeed);
				currentY += moveSpeed;
			}

			if (getY() > targetY) {
				// getSprite().translateY(-moveSpeed);
				currentY -= moveSpeed;
			}

			if (Math.abs(getX() - targetX) <= 1 && Math.abs(getY() - targetY) <= 1) {
				currentX = targetX;
				currentY = targetY;
				isMoving = false;
			}
		} else {
			setState("IDLE");
		}

		if (!lockMovement) {
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				if (getDirection() == "UP") {
					if (moveTimeout < 0 && canMove("UP")) {
						if (!isMoving) {
							isMoving = true;
							targetY = getY() + Constants.TILE_HEIGHT;
							targetX = getX();
						}
					}
				} else {
					setDirection("UP");
					moveTimeout = 0.25f;
				}
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				if (getDirection() == "DOWN") {
					if (moveTimeout < 0 && canMove("DOWN")) {
						if (!isMoving) {
							isMoving = true;
							targetY = getY() - Constants.TILE_HEIGHT;
							targetX = getX();
						}
					}
				} else {
					setDirection("DOWN");
					moveTimeout = 0.25f;
				}
			} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				if (getDirection() == "LEFT") {
					if (moveTimeout < 0 && canMove("LEFT")) {
						if (!isMoving) {
							isMoving = true;
							targetY = getY();
							targetX = getX() - Constants.TILE_WIDTH;
						}
					}
				} else {
					setDirection("LEFT");
					moveTimeout = 0.25f;
				}
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				if (getDirection() == "RIGHT") {
					if (moveTimeout < 0 && canMove("RIGHT")) {
						if (!isMoving) {
							isMoving = true;
							targetY = getY();
							targetX = getX() + Constants.TILE_WIDTH;
						}
					}
				} else {
					setDirection("RIGHT");
					moveTimeout = 0.25f;
				}
			}

			moveTimeout -= Gdx.graphics.getDeltaTime();
		}
	}

	private boolean canMove(String diretion) {
		return true;
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getState() {
		return movementState;
	}

	public void setState(String movementState) {
		this.movementState = movementState;
	}

	public String getName() {
		return name;
	}
}
