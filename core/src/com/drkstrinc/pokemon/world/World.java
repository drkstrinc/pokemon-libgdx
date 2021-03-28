package com.drkstrinc.pokemon.world;

import com.drkstrinc.pokemon.datatype.BrainType;
import com.drkstrinc.pokemon.datatype.Direction;

public class World {

	private String name;

	private String map;
	private String dayTileset;
	private String nightTileset;
	private int nightTimeTileOffset;
	private int indoorTileOffset;

	private String meta;

	private String bgm;

	private boolean retro;
	private boolean outdoors;
	private boolean encounters;

	private JConnection[] connections;

	private JActor[] actors;
	private JEvent[] events;

	public String getName() {
		return name;
	}

	public String getMapFileName() {
		return map;
	}

	public String getBGMFileName() {
		return bgm;
	}

	public String getDayTileset() {
		return dayTileset;
	}

	public String getNightTileset() {
		return nightTileset;
	}

	public int getNightTimeTileOffset() {
		return nightTimeTileOffset;
	}

	public int getIndoorTileOffset() {
		return indoorTileOffset;
	}

	public String getMapTileMetadata() {
		return meta;
	}

	public boolean getRetroFlag() {
		return retro;
	}

	public boolean isOutdoors() {
		return outdoors;
	}

	public boolean hasEncounters() {
		return encounters;
	}

	public JConnection[] getMapConnections() {
		return connections;
	}

	public JActor[] getActors() {
		return actors;
	}

	public JEvent[] getEvents() {
		return events;
	}

}

class JConnection {

	private int id;

	private String desc;

	private String target;

	private int enterX;
	private int enterY;
	private String enterDir;

	private int exitX;
	private int exitY;
	private String exitDir;

	public int getId() {
		return id;
	}

	public String getDescription() {
		return desc;
	}

	public String getTargetMap() {
		return target;
	}

	public int getEnterCoordX() {
		return enterX;
	}

	public int getEnterCoordY() {
		return enterY;
	}

	public Direction getEnterDirection() {
		return Direction.valueOf(enterDir);
	}

	public int getExitCoordX() {
		return exitX;
	}

	public int getExitCoordY() {
		return exitY;
	}

	public Direction getExitDirection() {
		return Direction.valueOf(exitDir);
	}
}

class JActor {

	private int id;
	private String type;

	private String name;
	private String trainername;

	private String sprite;
	private String brain;

	private JPosition position;

	private JEvent[] events;

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getTrainerName() {
		return trainername;
	}

	public String getSpriteName() {
		return sprite;
	}

	public BrainType getBrainType() {
		return BrainType.valueOf(brain);
	}

	public int getCoordX() {
		return position.getCoordX();
	}

	public int getCoordY() {
		return position.getCoordY();
	}

	public Direction getDirection() {
		return position.getDirection();
	}

	public JEvent[] getEvents() {
		return events;
	}
}

class JPosition {

	private int x;
	private int y;
	private String direction;

	public int getCoordX() {
		return x;
	}

	public int getCoordY() {
		return y;
	}

	public Direction getDirection() {
		return Direction.valueOf(direction);
	}
}

class JEvent {

	private int id;
	private JRequirement[] requirements;
	private String[] messages;
	private JAction[] actions;

	public int getId() {
		return id;
	}

	public JRequirement[] getRequirements() {
		return requirements;
	}

	public String[] getMessages() {
		return messages;
	}

	public JAction[] getActions() {
		return actions;
	}
}

class JRequirement {
	// TODO: Implement
}

class JAction {

	private JMove move;

	public JMove getMove() {
		return move;
	}
}

class JMove {

	private String target;
	private String[] sequence;

	public String getTarget() {
		return target;
	}

	public String[] getSequence() {
		return sequence;
	}
}
