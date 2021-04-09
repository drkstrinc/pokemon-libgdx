package com.drkstrinc.pokemon.ui.battle;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.datatype.MessageState;

/**
 * 
 * @author hydrozoa
 *
 */
public class MessageBox extends Table {

	private String targetText = "";
	private int textviewWidth = 40;

	private float animTimer = 0f;
	private float animationTotalTime = 0f;
	private float timePerCharacter = 0.05f;

	private MessageState state = MessageState.IDLE;

	private Label textLabel;

	public MessageBox(Skin skin) {
		super(skin);
		setBackground("speechbox");
		textLabel = new Label("\n", skin);
		setClip(true);
		add(textLabel).expand().align(Align.topLeft).pad(8);
	}

	public void animateText(String text) {
		targetText = wrapText(text);
		animationTotalTime = text.length() * timePerCharacter;
		state = MessageState.ANIMATING;
		animTimer = 0f;
	}

	public boolean isFinished() {
		return state == MessageState.IDLE;
	}

	private void setText(String text) {
		if (!text.contains("\n")) {
			text += "\n";
		}
		this.textLabel.setText(text);
	}

	@Override
	public void act(float delta) {
		if (state == MessageState.ANIMATING) {
			animTimer += delta;
			if (animTimer > animationTotalTime) {
				state = MessageState.IDLE;
				animTimer = animationTotalTime;
			}
			String displayedText = "";
			int charactersToDisplay = (int) ((animTimer / animationTotalTime) * targetText.length());
			for (int i = 0; i < charactersToDisplay; i++) {
				displayedText += targetText.charAt(i);
			}
			if (!displayedText.equals(textLabel.getText().toString())) {
				setText(displayedText);
			}
		}
	}

	private String wrapText(String text) {
		String temp = "";
		String sentence = "";
		String[] array = text.split(" ");
		for (String word : array) {
			if ((temp.length() + word.length()) < textviewWidth) {
				temp += " " + word;
			} else {
				sentence += temp + "\n";
				temp = word;
			}
		}
		return (sentence.replaceFirst(" ", "") + temp);
	}

	@Override
	public float getPrefWidth() {
		return Constants.GAME_WIDTH;
	}

	@Override
	public float getPrefHeight() {
		return 80f;
	}
}
