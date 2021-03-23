package com.drkstrinc.pokemon.datatype;

public enum MovementState {
	IDLE(0), WALKING(2), RUNNING(3), BIKING(4), SURFING(2), FLYING(2);

	private final int speed;

	MovementState(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}
}
