package com.drkstrinc.pokemon.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.world.WorldManager;

public class SoundEffect {

	private static Music select = Gdx.audio.newMusic(Gdx.files.internal("audio/se/Select.wav"));
	private static Music save = Gdx.audio.newMusic(Gdx.files.internal("audio/se/Save.wav"));

	private static Music bump = Gdx.audio.newMusic(Gdx.files.internal("audio/se/Bump.mp3"));
	private static Music doorEnter = Gdx.audio.newMusic(Gdx.files.internal("audio/se/Door_Enter.wav"));
	private static Music doorExit = Gdx.audio.newMusic(Gdx.files.internal("audio/se/Door_Exit.wav"));
	private static Music jump = Gdx.audio.newMusic(Gdx.files.internal("audio/se/Jump.wav"));

	private SoundEffect() {

	}

	public static void checkForTileNoises(int id) {
		if (Pokemon.getPlayer().getX() == Pokemon.getPlayer().getTargetX()
				&& Pokemon.getPlayer().getY() == Pokemon.getPlayer().getTargetY()) {
			if (id == 69) {
				doorEnter();
			} else if (id == 25 || id == 39 || id == 40 || id == 159) {
				if (!WorldManager.getCurrentWorld().isOutdoors()) {
					doorExit();
				}
			} else if (id == 1 || id == 3 || id == 10 || id == 4 || id == 6 || id == 14) {
				jump();
			}
		}
	}

	public static void select() {
		if (!select.isPlaying()) {
			select.play();
		}
	}

	public static void save() {
		if (!save.isPlaying()) {
			save.play();
		}
	}

	public static void bump() {
		if (!bump.isPlaying()) {
			if (Pokemon.getPlayer().getX() == Pokemon.getPlayer().getTargetX()
					&& Pokemon.getPlayer().getY() == Pokemon.getPlayer().getTargetY()) {
				bump.play();
			}
		}
	}

	public static void doorEnter() {
		if (!doorEnter.isPlaying()) {
			doorEnter.play();
		}
	}

	public static void doorExit() {
		if (!doorExit.isPlaying()) {
			doorExit.play();
		}
	}

	public static void jump() {
		if (!jump.isPlaying()) {
			if (Pokemon.getPlayer().getDirection() != Direction.UP) {
				jump.play();
			}
		}
	}

}
