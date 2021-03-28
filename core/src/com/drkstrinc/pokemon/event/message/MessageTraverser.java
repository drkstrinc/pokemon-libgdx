package com.drkstrinc.pokemon.event.message;

/**
 * 
 * @author hydrozoa
 *
 */
public class MessageTraverser {

	private Message dialogue;
	private MessageNode currentNode;

	public MessageTraverser(Message dialogue) {
		this.dialogue = dialogue;
		currentNode = dialogue.getNode(dialogue.getStart());
	}

	public MessageNode getNextNode(int pointerIndex) {
		if (currentNode.getPointers().isEmpty()) {
			return null;
		}
		MessageNode nextNode = dialogue.getNode(currentNode.getPointers().get(pointerIndex));
		currentNode = nextNode;
		return nextNode;
	}

	public MessageNode getNode() {
		return currentNode;
	}
}
