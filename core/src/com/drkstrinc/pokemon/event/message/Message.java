package com.drkstrinc.pokemon.event.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author hydrozoa
 *
 */
public class Message {

	private Map<Integer, MessageNode> nodes = new HashMap<>();

	private static final int START_INDEX = 0;

	public MessageNode getNode(int id) {
		return nodes.get(id);
	}

	public void addNode(MessageNode node) {
		this.nodes.put(node.getID(), node);
	}

	public int getStart() {
		return START_INDEX;
	}

	public int size() {
		return nodes.size();
	}
}
