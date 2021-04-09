package com.drkstrinc.pokemon.ui.battle;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class PlayerStatusBox extends Table {

	private Label hpLabel;
	private Label nameLabel;
	private HPBar hpBar;

	public PlayerStatusBox(Skin skin) {
		super(skin);

		this.setBackground("battleplayerbox");
		uiContainer = new Table();
		this.add(uiContainer).pad(0).expand().fill();

		nameLabel = new Label("NAME", skin, "default");
		uiContainer.add(nameLabel).align(Align.left).padLeft(28f).padTop(4f).row();

		hpBar = new HPBar(skin, 0, -2, 0);
		uiContainer.add(hpBar).spaceTop(0f).expand().fill();

		hpLabel = new Label("NaN / NaN", skin, "default");
		uiContainer.row();
		uiContainer.add(hpLabel).expand().spaceLeft(32f);
	}

	protected Table uiContainer;

	public void setText(String newText) {
		nameLabel.setText(newText);
	}

	public HPBar getHPBar() {
		return hpBar;
	}

	public void setHPText(int hpLeft, int hpTotal) {
		hpLabel.setText(hpLeft + " / " + hpTotal);
	}

}
