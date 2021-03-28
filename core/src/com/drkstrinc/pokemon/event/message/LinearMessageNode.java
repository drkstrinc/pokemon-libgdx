package com.drkstrinc.pokemon.event.message;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hydrozoa
 *
 */
public class LinearMessageNode implements MessageNode {

	private String text;
	private int id;
	private List<Integer> pointers = new ArrayList<>();

	public LinearMessageNode(String text, int id) {
		this.text = text;
		this.id = id;
	}

	public void setPointer(int id) {
		pointers.add(id);
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

}
