package com.drkstrinc.pokemon.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class CharacterSpriteSheet {

	private final int cols = 4;
	private final int rows = 4;

	private Texture spriteSheet;

	private TextureRegion[] downFrames;
	private TextureRegion[] leftFrames;
	private TextureRegion[] rightFrames;
	private TextureRegion[] upFrames;

	private AnimatedSprite downAnimatedSprite;
	private AnimatedSprite leftAnimatedSprite;
	private AnimatedSprite rightAnimatedSprite;
	private AnimatedSprite upAnimatedSprite;

	public CharacterSpriteSheet(String fileName) {
		spriteSheet = new Texture("image/characters/" + fileName);
		TextureRegion[][] tempFrames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / cols,
				spriteSheet.getHeight() / rows);

		loadFrames(tempFrames, cols, rows);
		// loadSprites();
	}

	private void loadFrames(TextureRegion[][] tempFrames, int cols, int rows) {
		downFrames = new TextureRegion[cols];
		leftFrames = new TextureRegion[cols];
		rightFrames = new TextureRegion[cols];
		upFrames = new TextureRegion[cols];

		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < rows; row++) {
				if (col == 0) {
					downFrames[row] = tempFrames[0][row];
					// System.out.print("Added Down Frame: ");
				} else if (col == 1) {
					leftFrames[row] = tempFrames[1][row];
					// System.out.print("Added Left Frame: ");
				} else if (col == 2) {
					rightFrames[row] = tempFrames[2][row];
					// System.out.print("Added Right Frame: ");
				} else if (col == 3) {
					upFrames[row] = tempFrames[3][row];
					// System.out.print("Added Up Frame: ");
				}
				/*
				 * System.out.print(row + "\tRegion X: " + tempFrames[col][row].getRegionX() +
				 * "\tRegion Y: " + tempFrames[col][row].getRegionY() + "\tWidth: " +
				 * tempFrames[col][row].getRegionWidth() + "\tHeight: " +
				 * tempFrames[col][row].getRegionHeight() + System.lineSeparator());
				 */
			}
		}
	}

	private void loadSprites() {
		Animation<TextureRegion> downAnimation = new Animation<TextureRegion>(1f / 4f, downFrames);
		Animation<TextureRegion> leftAnimation = new Animation<TextureRegion>(1f / 4f, leftFrames);
		Animation<TextureRegion> rightAnimation = new Animation<TextureRegion>(1f / 4f, rightFrames);
		Animation<TextureRegion> upAnimation = new Animation<TextureRegion>(1f / 4f, upFrames);

		downAnimation.setPlayMode(PlayMode.LOOP);
		downAnimatedSprite = new AnimatedSprite(downAnimation);

		leftAnimation.setPlayMode(PlayMode.LOOP);
		leftAnimatedSprite = new AnimatedSprite(leftAnimation);

		rightAnimation.setPlayMode(PlayMode.LOOP);
		rightAnimatedSprite = new AnimatedSprite(rightAnimation);

		upAnimation.setPlayMode(PlayMode.LOOP);
		upAnimatedSprite = new AnimatedSprite(upAnimation);

		pause();
	}

	public void pause() {
		downAnimatedSprite.pause();
		leftAnimatedSprite.pause();
		rightAnimatedSprite.pause();
		upAnimatedSprite.pause();
	}

	public Texture getSpriteSheet() {
		return spriteSheet;
	}

	public TextureRegion getDownTexture(int pos) {
		return downFrames[pos];
	}

	public TextureRegion getLeftTexture(int pos) {
		return leftFrames[pos];
	}

	public TextureRegion getRightTexture(int pos) {
		return rightFrames[pos];
	}

	public TextureRegion getUpTexture(int pos) {
		return upFrames[pos];
	}

}
