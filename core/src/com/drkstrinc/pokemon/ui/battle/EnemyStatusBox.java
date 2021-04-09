package com.drkstrinc.pokemon.ui.battle;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class EnemyStatusBox extends Table {

	private Label nameLabel;
	private HPBar hpBar;

	protected Table uiContainer;

	public EnemyStatusBox(Skin skin) {
		super(skin);

		this.setBackground("battleenemybox");
		uiContainer = new Table();
		this.add(uiContainer).pad(0f).expand().fill();

		nameLabel = new Label("NAME", skin, "default");
		uiContainer.add(nameLabel).align(Align.left).padLeft(12f).padTop(0f).row();

		hpBar = new HPBar(skin, -17, 19, -5);
		uiContainer.add(hpBar).spaceTop(0f).expand().fill();
	}

	public void setText(String newText) {
		nameLabel.setText(newText);
	}

	public HPBar getHPBar() {
		return hpBar;
	}

}
