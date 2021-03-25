package com.drkstrinc.pokemon.event;

import java.util.ArrayList;

import com.drkstrinc.pokemon.Pokemon;

public class Event {

	private int id;

	private ArrayList<String> messages;

	public Event(int id) {
		this.id = id;
		messages = new ArrayList<>();
	}

	public void addText(String text) {
		messages.add(text.replace("{PN}", Pokemon.getPlayer().getName()));
	}

	public ArrayList<String> getMessages() {
		return messages;
	}

	public int getID() {
		return id;
	}

}
