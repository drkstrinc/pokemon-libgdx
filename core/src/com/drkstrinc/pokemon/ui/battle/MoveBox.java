package com.drkstrinc.pokemon.ui.battle;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import com.drkstrinc.pokemon.monster.Move;

/**
 * This selection box has four labels, enumerated like so: 0 1 2 3
 * 
 * @author hydrozoa
 */
public class MoveBox extends Table {

	private int selectorIndex = 0;

	private Label[] labels = new Label[4];
	private Image[] arrows = new Image[4];

	private Table uiContainer;

	public MoveBox(Skin skin) {
		super(skin);
		this.setBackground("choicebox");
		this.uiContainer = new Table();

		this.add(uiContainer).pad(1f);

		labels[0] = new Label(Move.EMPTY, skin);
		labels[1] = new Label(Move.EMPTY, skin);
		labels[2] = new Label(Move.EMPTY, skin);
		labels[3] = new Label(Move.EMPTY, skin);

		arrows[0] = new Image(skin, "arrow");
		arrows[0].setScaling(Scaling.none);
		arrows[1] = new Image(skin, "arrow");
		arrows[1].setScaling(Scaling.none);
		arrows[2] = new Image(skin, "arrow");
		arrows[2].setScaling(Scaling.none);
		arrows[3] = new Image(skin, "arrow");
		arrows[3].setScaling(Scaling.none);

		uiContainer.add(arrows[0]);
		uiContainer.add(labels[0]).align(Align.left);
		uiContainer.add(arrows[1]);
		uiContainer.add(labels[1]).align(Align.left).row();
		uiContainer.add(arrows[2]);
		uiContainer.add(labels[2]).align(Align.left);
		uiContainer.add(arrows[3]);
		uiContainer.add(labels[3]).align(Align.left);

		setSelection(0);
	}

	public void setLabel(int index, String text) {
		labels[index].setText(text);
	}

	public int getSelection() {
		return selectorIndex;
	}

	public void moveUp() {
		if (selectorIndex == 0) {
			return;
		}
		if (selectorIndex == 1) {
			return;
		}
		if (selectorIndex == 2) {
			setSelection(0);
			return;
		}
		if (selectorIndex == 3) {
			setSelection(1);
			return;
		}
	}

	public void moveDown() {
		if (selectorIndex == 0) {
			setSelection(2);
			return;
		}
		if (selectorIndex == 1) {
			setSelection(3);
			return;
		}
		if (selectorIndex == 2) {
			return;
		}
		if (selectorIndex == 3) {
			return;
		}
	}

	public void moveLeft() {
		if (selectorIndex == 0) {
			return;
		}
		if (selectorIndex == 1) {
			setSelection(0);
			return;
		}
		if (selectorIndex == 2) {
			return;
		}
		if (selectorIndex == 3) {
			setSelection(2);
			return;
		}
	}

	public void moveRight() {
		if (selectorIndex == 0) {
			setSelection(1);
			return;
		}
		if (selectorIndex == 1) {
			return;
		}
		if (selectorIndex == 2) {
			setSelection(3);
			return;
		}
		if (selectorIndex == 3) {
			return;
		}
	}

	private void setSelection(int index) {
		selectorIndex = index;
		for (int i = 0; i < labels.length; i++) {
			if (i == index) {
				arrows[i].setVisible(true);
			} else {
				arrows[i].setVisible(false);
			}
		}
	}
}
