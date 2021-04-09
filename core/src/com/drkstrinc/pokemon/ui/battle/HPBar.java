package com.drkstrinc.pokemon.ui.battle;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class HPBar extends Widget {

	private int maxWidth = 81;
	private int height = 4;

	private int offsetX = 54;
	private int offsetY = 2;

	private float hpAmount = 1f;

	private Drawable green;
	private Drawable yellow;
	private Drawable red;

	public HPBar(Skin skin, int offsetX, int offsetY, int offsetWidth) {
		super();

		this.maxWidth += offsetWidth;
		this.offsetX += offsetX;
		this.offsetY += offsetY;

		green = skin.getDrawable("green");
		yellow = skin.getDrawable("yellow");
		red = skin.getDrawable("red");
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		float curWidth = hpAmount * maxWidth;

		Drawable hpColor = null;
		if (hpAmount <= 0.1) {
			hpColor = red;
		} else if (hpAmount <= 0.5) {
			hpColor = yellow;
		} else {
			hpColor = green;
		}

		hpColor.draw(batch, this.getX() + offsetX, this.getY() + offsetY, curWidth, height);
	}

	public void displayHPLeft(float hp) {
		this.hpAmount = hp;
		hpAmount = MathUtils.clamp(hpAmount, 0f, 1f);
	}
}
