package com.drkstrinc.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.sound.MidiPlayer;
import com.drkstrinc.pokemon.world.WorldManager;

public class TitleScreen extends ScreenAdapter {

	private Pokemon game;

	private MidiPlayer bgm;

	private SpriteBatch batch;
	private Texture titleImage;

	public TitleScreen(Pokemon game) {
		this.game = game;
		batch = new SpriteBatch();
		titleImage = new Texture(Gdx.files.internal("image/titles/Title.png"));
	}

	@Override
	public void show() {
		loadBGM();
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Input.Keys.ENTER) {
					bgm.stop();
					bgm.dispose();
					WorldManager.setWorld("NewBarkTown", Pokemon.getPlayer().getCoordX(),
							Pokemon.getPlayer().getCoordY(), Pokemon.getPlayer().getDirection());
					game.setScreen(Pokemon.getGameScreen());
				}
				return true;
			}
		});
	}

	private void loadBGM() {
		bgm = new MidiPlayer("audio/bgm/Titlescreen.mid");
		bgm.play();
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(titleImage, 0, 0);
		batch.end();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
}