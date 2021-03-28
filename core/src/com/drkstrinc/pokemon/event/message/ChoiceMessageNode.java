package com.drkstrinc.pokemon.event.message;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hydrozoa
 *
 */
public class ChoiceMessageNode implements MessageNode {

	private String text;
	private int id;

	private List<Integer> pointers = new ArrayList<>();
	private List<String> labels = new ArrayList<>();

	public ChoiceMessageNode(String text, int id) {
		this.text = text;
		this.id = id;
	}

	public void addChoice(String text, int targetId) {
		pointers.add(targetId);
		labels.add(text);
	}

	public String getText() {
		return text;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public List<Integer> getPointers() {
		return pointers;
	}

	public List<String> getLabels() {
		return labels;
	}

}
