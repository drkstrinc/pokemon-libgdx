package com.drkstrinc.pokemon.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ActorSpriteSheet {

	private final int cols = 4;
	private final int rows = 4;

	private Texture spriteSheet;

	private TextureRegion[] downFrames;
	private TextureRegion[] leftFrames;
	private TextureRegion[] rightFrames;
	private TextureRegion[] upFrames;

	public ActorSpriteSheet(String fileName) {
		Gdx.app.log("GFX", "Loading Sprite Sheet: " + fileName);
		spriteSheet = new Texture("image/characters/" + fileName);
		TextureRegion[][] tempFrames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / cols,
				spriteSheet.getHeight() / rows);

		loadFrames(tempFrames);
	}

	private void loadFrames(TextureRegion[][] tempFrames) {
		downFrames = new TextureRegion[cols];
		leftFrames = new TextureRegion[cols];
		rightFrames = new TextureRegion[cols];
		upFrames = new TextureRegion[cols];

		String dir = "";
		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < rows; row++) {
				if (col == 0) {
					downFrames[row] = tempFrames[0][row];
					dir = "Down";
				} else if (col == 1) {
					leftFrames[row] = tempFrames[1][row];
					dir = "Left";
				} else if (col == 2) {
					rightFrames[row] = tempFrames[2][row];
					dir = "Right";
				} else if (col == 3) {
					upFrames[row] = tempFrames[3][row];
					dir = "Up";
				}

				Gdx.app.debug("GFX",
						"Adding " + dir + " Frame: " + row + "\tRegion X: " + tempFrames[col][row].getRegionX()
								+ "\tRegion Y: " + tempFrames[col][row].getRegionY() + "\tWidth: "
								+ tempFrames[col][row].getRegionWidth() + "\tHeight: "
								+ tempFrames[col][row].getRegionHeight());
			}
		}
	}

	public Texture getSpriteSheet() {
		return spriteSheet;
	}

	public int getFrameCount() {
		return downFrames.length - 1;
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
