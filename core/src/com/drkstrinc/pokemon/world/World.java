package com.drkstrinc.pokemon.world;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.actor.Actor;
import com.drkstrinc.pokemon.datatype.BrainType;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.event.Event;
import com.drkstrinc.pokemon.sound.MidiPlayer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class World {

	private String name;
	private String mapFilePath;
	private String bgmFilePath;
	private boolean isOutdoors;
	private boolean hasEncounters;

	private static TiledMap currentMap;

	private static MidiPlayer bgm;

	public static boolean retroMap = true;
	public static int[] groundLayer = { 0, 1 };
	public static int[] aboveLayers = { 2 };

	private static ArrayList<Actor> actors;
	private static ArrayList<Event> events;

	public World(Pokemon game, String mapName) {
		initActors();
		readFromJSON(mapName);
		loadMap();
		loadBGM();
	}

	private void readFromJSON(String mapName) {
		String json = Gdx.files.local("data/" + mapName + ".json").readString();
		Gdx.app.log("TMX", "\"" + mapName + "\": " + json);

		JsonElement element = JsonParser.parseString(json);
		JsonObject rootObject = element.getAsJsonObject();

		name = rootObject.get("name").getAsString();
		mapFilePath = rootObject.get("map").getAsString();
		bgmFilePath = rootObject.get("bgm").getAsString();
		isOutdoors = rootObject.get("outdoors").getAsBoolean();
		hasEncounters = rootObject.get("encounters").getAsBoolean();

		for (JsonElement actorElement : rootObject.getAsJsonArray("actors")) {
			JsonObject actorObject = actorElement.getAsJsonObject();
			String actorName = actorObject.get("name").getAsString();
			String actorSprite = actorObject.get("sprite").getAsString();
			String actorBrainTypeString = actorObject.get("brain").getAsString();
			BrainType actorBrainType;
			if (actorBrainTypeString.equals("RANDOMMOVEMENT")) {
				actorBrainType = BrainType.RANDOMMOVEMENT;
			} else if (actorBrainTypeString.equals("STATIONARY")) {
				actorBrainType = BrainType.STATIONARY;
			} else {
				actorBrainType = BrainType.STATIONARY;
			}
			JsonObject actorPosition = actorObject.getAsJsonObject("position");
			int actorX = actorPosition.get("x").getAsInt();
			int actorY = actorPosition.get("y").getAsInt();
			String actorDirectionString = actorPosition.get("direction").getAsString();
			Direction actorDirection;
			if (actorDirectionString.equals("UP")) {
				actorDirection = Direction.UP;
			} else if (actorDirectionString.equals("DOWN")) {
				actorDirection = Direction.DOWN;
			} else if (actorDirectionString.equals("LEFT")) {
				actorDirection = Direction.LEFT;
			} else if (actorDirectionString.equals("RIGHT")) {
				actorDirection = Direction.RIGHT;
			} else {
				actorDirection = Direction.DOWN;
			}
			Actor tmpActor = new Actor(actorName, actorSprite, actorBrainType, actorX, actorY, actorDirection);
			actors.add(tmpActor);
		}
	}

	public void loadMap() {
		Gdx.app.log("TMX", "Loading Map: " + mapFilePath);
		currentMap = new TmxMapLoader().load(mapFilePath);
	}

	public void initActors() {
		actors = new ArrayList<Actor>();
		actors.add(Pokemon.getPlayer());
	}

	private void loadBGM() {
		bgm = new MidiPlayer(bgmFilePath);
	}

	public static void addActor(Actor actor) {
		actors.add(actor);
	}

	public static void removeActor(Actor actor) {
		actors.remove(actor);
	}

	public static ArrayList<Actor> getActors() {
		return actors;
	}

	public static ArrayList<Event> getEvents() {
		return events;
	}

	public static void setCurrentMap(TiledMap map) {
		currentMap = map;
	}

	public static TiledMap getCurrentMap() {
		return currentMap;
	}

	public static MidiPlayer getMidiPlayer() {
		return bgm;
	}

}
