package com.drkstrinc.pokemon.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.drkstrinc.pokemon.Pokemon;

public class SoundEffect {

	private static Music bump = Gdx.audio.newMusic(Gdx.files.internal("audio/se/Bump.mp3"));

	public SoundEffect() {

	}

	public static void bump() {
		if (!bump.isPlaying()) {
			if (Pokemon.getPlayer().getX() == Pokemon.getPlayer().getTargetX()
					&& Pokemon.getPlayer().getY() == Pokemon.getPlayer().getTargetY()) {
				bump.play();
			}
		}
	}

}
