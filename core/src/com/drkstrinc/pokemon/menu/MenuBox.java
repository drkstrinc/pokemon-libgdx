package com.drkstrinc.pokemon.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import com.drkstrinc.pokemon.Pokemon;

public class MenuBox extends ChoiceBox {

	public MenuBox(Skin skin) {
		super(skin);
		this.setBackground("choicebox");
		uiContainer = new Table();
		this.add(uiContainer).pad(5f);

		addOption("Pokedex");
		addOption("Pokemon");
		addOption("Pokegear");
		addOption("Bag");
		addOption(Pokemon.getPlayer().getName());
		addOption("Save");
		addOption("Exit");
	}

	@Override
	public void addOption(String option) {
		Label optionLabel = new Label(option, this.getSkin());
		options.add(optionLabel);
		Image selectorLabel = new Image(this.getSkin(), "arrow");
		selectorLabel.setScaling(Scaling.none);
		arrows.add(selectorLabel);
		selectorLabel.setVisible(false);

		if (selectorLabel == arrows.get(selectorIndex)) {
			selectorLabel.setVisible(true);
		}

		uiContainer.add(selectorLabel).expand().align(Align.left).padRight(10f);
		uiContainer.add(optionLabel).expand().align(Align.right);
		uiContainer.row();
	}

	public void chooseOption() {
		Gdx.app.debug("MNU", "Selected " + getCurrentSelection());
		if ("Exit".equals(getCurrentSelection())) {
			Pokemon.getGameScreen().getMenuController().closeMenu();
		}
	}

	@Override
	public float getPrefWidth() {
		return 90f;
	}

}
