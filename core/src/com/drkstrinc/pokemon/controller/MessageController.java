package com.drkstrinc.pokemon.controller;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import com.drkstrinc.pokemon.actor.Actor;
import com.drkstrinc.pokemon.event.Event;
import com.drkstrinc.pokemon.event.message.ChoiceMessageNode;
import com.drkstrinc.pokemon.event.message.LinearMessageNode;
import com.drkstrinc.pokemon.event.message.Message;
import com.drkstrinc.pokemon.event.message.MessageNode;
import com.drkstrinc.pokemon.event.message.MessageTraverser;
import com.drkstrinc.pokemon.screen.GameScreen;
import com.drkstrinc.pokemon.sound.SoundEffect;
import com.drkstrinc.pokemon.ui.ChoiceBox;
import com.drkstrinc.pokemon.ui.MessageBox;

/**
 * 
 * @author hydrozoa
 *
 */
public class MessageController extends InputAdapter {

	private MessageTraverser traverser;

	private MessageBox speechBox;
	private ChoiceBox choiceBox;

	public MessageController(MessageBox speechBox, ChoiceBox choiceBox) {
		this.speechBox = speechBox;
		this.choiceBox = choiceBox;
	}

	@Override
	public boolean keyDown(int keycode) {
		return speechBox.isVisible();
	}

	@Override
	public boolean keyUp(int keycode) {
		if (choiceBox.isVisible()) {
			if (keycode == Keys.UP) {
				choiceBox.moveUp();
				return true;
			} else if (keycode == Keys.DOWN) {
				choiceBox.moveDown();
				return true;
			}
		}
		if (speechBox.isVisible() && !speechBox.isFinished()) {
			return false;
		}
		if (traverser != null && keycode == Keys.Z) {
			SoundEffect.select();
			MessageNode thisNode = traverser.getNode();

			if (thisNode instanceof LinearMessageNode) {
				LinearMessageNode node = (LinearMessageNode) thisNode;
				if (node.getPointers().isEmpty()) {
					traverser = null;
					speechBox.setVisible(false);
					GameScreen.unlockActors();
				} else {
					progress(0);
				}
			}
			if (thisNode instanceof ChoiceMessageNode) {
				progress(choiceBox.getIndex());
			}

			return true;
		}

		return speechBox.isVisible();
	}

	public void update(float delta) {
		if (speechBox.isFinished() && traverser != null) {
			MessageNode nextNode = traverser.getNode();
			if (nextNode instanceof ChoiceMessageNode) {
				choiceBox.setVisible(true);
			}
		}
	}

	public void startDialogue(Actor actor) {
		SoundEffect.select();
		GameScreen.lockActors();

		// TODO: Rework Actor Event/Message in JSON to include choices and target Index
		Message dialogue = new Message();
		for (Event event : actor.getEvents()) {
			ArrayList<String> messages = event.getMessages();
			for (int i = 0; i < messages.size(); i++) {
				int target = -1;
				String text = messages.get(i);
				if (i + 1 < messages.size()) {
					target = i + 1;
				}
				LinearMessageNode linearNode = new LinearMessageNode(text, i);
				if (target > -1) {
					linearNode.setPointer(target);
				}
				dialogue.addNode(linearNode);
			}
		}

		traverser = new MessageTraverser(dialogue);
		speechBox.setVisible(true);

		MessageNode nextNode = traverser.getNode();
		if (nextNode instanceof LinearMessageNode) {
			LinearMessageNode node = (LinearMessageNode) nextNode;
			speechBox.animateText(node.getText());
		}
		if (nextNode instanceof ChoiceMessageNode) {
			ChoiceMessageNode node = (ChoiceMessageNode) nextNode;
			speechBox.animateText(node.getText());
			choiceBox.clear();
			for (String s : node.getLabels()) {
				choiceBox.addOption(s);
			}
		}
	}

	private void progress(int index) {
		choiceBox.setVisible(false);
		MessageNode nextNode = traverser.getNextNode(index);

		if (nextNode instanceof LinearMessageNode) {
			LinearMessageNode node = (LinearMessageNode) nextNode;
			speechBox.animateText(node.getText());
		}
		if (nextNode instanceof ChoiceMessageNode) {
			ChoiceMessageNode node = (ChoiceMessageNode) nextNode;
			speechBox.animateText(node.getText());
			choiceBox.clearChoices();
			for (String s : node.getLabels()) {
				choiceBox.addOption(s);
			}
		}
	}

	public boolean isDialogueShowing() {
		return speechBox.isVisible();
	}
}
