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
	private Texture backgroundImage;
	private Texture logo;

	private float backgroundX = 0;

	public TitleScreen(Pokemon game) {
		this.game = game;
		batch = new SpriteBatch();

		backgroundImage = new Texture(Gdx.files.internal("image/titles/Background.png"));
		backgroundImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

		logo = new Texture(Gdx.files.internal("image/titles/Logo.png"));
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
		backgroundX += 2;
		backgroundX = (backgroundX + Gdx.graphics.getDeltaTime()) % backgroundImage.getWidth();

		batch.begin();

		batch.draw(backgroundImage, 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight(), (int) backgroundX, 0,
				backgroundImage.getWidth(), backgroundImage.getHeight(), false, false);
		batch.draw(logo, 0, 0);

		batch.end();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
}