package com.drkstrinc.pokemon.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CharacterSpriteSheet {

	private final int cols = 4;
	private final int rows = 4;

	private Texture spriteSheet;

	private TextureRegion[] downFrames;
	private TextureRegion[] leftFrames;
	private TextureRegion[] rightFrames;
	private TextureRegion[] upFrames;

	public CharacterSpriteSheet(String fileName) {
		spriteSheet = new Texture("image/characters/" + fileName);
		TextureRegion[][] tempFrames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / cols,
				spriteSheet.getHeight() / rows);

		loadFrames(tempFrames, cols, rows);
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
