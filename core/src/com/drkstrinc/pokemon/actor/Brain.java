package com.drkstrinc.pokemon.actor;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.drkstrinc.pokemon.datatype.BrainType;

public class Brain {

	private Actor actor;
	Random r;

	public Brain(Actor actor) {
		this.actor = actor;

		r = new Random();

		if (actor.getBrainType().equals(BrainType.RANDOMMOVEMENT)) {
			setupRandomMovement();
		} else if (actor.getBrainType().equals(BrainType.STATIONARY)) {
			// NO-OP
		}
	}

	private void setupRandomMovement() {
		// TODO: Make sure the Actor cannot exit a certain amount of space beyond their
		// initial starting position (in terms of x and y offsets)
		int interval = r.nextInt(5000) + 3000;
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!actor.isMovementLocked()) {
					Gdx.app.debug("CHR",
							actor.getClass().getSimpleName() + " " + actor.getName() + " - Execute Movement");
					int rnd = r.nextInt(4);
					if (rnd == 0) {
						actor.turnUp();
						actor.moveUp();
					} else if (rnd == 1) {
						actor.turnDown();
						actor.moveDown();
					} else if (rnd == 2) {
						actor.turnLeft();
						actor.moveLeft();
					} else if (rnd == 3) {
						actor.turnRight();
						actor.moveRight();
					}
				}
			}
		}, interval, interval);
	}

}
