package com.drkstrinc.pokemon.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.actor.Actor;
import com.drkstrinc.pokemon.datatype.BrainType;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.event.Event;
import com.drkstrinc.pokemon.sound.MidiPlayer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class World {

	private String name;
	private String mapFilePath;
	private String metadataFilePath;
	private String bgmFilePath;
	private boolean isOutdoors;
	private boolean hasEncounters;

	private static TiledMap currentMap;
	private static String dayTimeTileset;
	private static String nightTimeTileset;

	private static boolean isNightTime;
	private static final int nightTimeTileOffset = 9999;

	private static MidiPlayer bgm;

	public static boolean retroMap = true;
	public static int[] groundLayer = { 0, 1 };
	public static int[] aboveLayers = { 2 };

	private static List<Integer> impassibleTileList;

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
		Gdx.app.debug("TMX", "\"" + mapName + "\": " + json);

		JsonElement element = JsonParser.parseString(json);
		JsonObject rootObject = element.getAsJsonObject();

		// Map Metadata
		name = rootObject.get("name").getAsString();
		mapFilePath = rootObject.get("map").getAsString();
		dayTimeTileset = rootObject.get("dayTileset").getAsString();
		nightTimeTileset = rootObject.get("nightTileset").getAsString();
		metadataFilePath = rootObject.get("meta").getAsString();
		bgmFilePath = rootObject.get("bgm").getAsString();
		isOutdoors = rootObject.get("outdoors").getAsBoolean();
		hasEncounters = rootObject.get("encounters").getAsBoolean();
		isNightTime = false;

		impassibleTileList = new ArrayList<>();
		Arrays.asList(Gdx.files.local(metadataFilePath).readString().split(","))
				.forEach(tileId -> impassibleTileList.add(Integer.valueOf(tileId)));

		// Actors and Actor Events
		for (JsonElement actorElement : rootObject.getAsJsonArray("actors")) {
			JsonObject actorObject = actorElement.getAsJsonObject();
			int actorID = actorObject.get("id").getAsInt();
			String actorName = actorObject.get("name").getAsString();
			String actorSprite = actorObject.get("sprite").getAsString();
			BrainType actorBrainType = BrainType.valueOf(actorObject.get("brain").getAsString());

			JsonObject actorPosition = actorObject.getAsJsonObject("position");
			int actorX = actorPosition.get("x").getAsInt();
			int actorY = actorPosition.get("y").getAsInt();
			Direction actorDirection = Direction.valueOf(actorPosition.get("direction").getAsString());

			Actor tmpActor = new Actor(actorID, actorName, actorSprite, actorBrainType, actorX, actorY, actorDirection);

			for (JsonElement eventElement : actorObject.getAsJsonArray("events")) {
				JsonObject eventObject = eventElement.getAsJsonObject();
				int eventID = eventObject.get("id").getAsInt();
				Event actorEvent = new Event(eventID);

				JsonArray eventMessages = eventObject.getAsJsonArray("messages");
				eventMessages.forEach(messageText -> actorEvent.addText(messageText.getAsString()));

				tmpActor.addEvent(actorEvent);
			}

			actors.add(tmpActor);
		}

		// Map Events
		for (JsonElement eventElement : rootObject.getAsJsonArray("events")) {
			JsonObject eventObject = eventElement.getAsJsonObject();
			// TODO: Create Map level Events and interact with them
		}
	}

	public void loadMap() {
		Gdx.app.log("TMX", "Loading Map: " + name + " - " + mapFilePath);
		currentMap = new TmxMapLoader().load(mapFilePath);
		// loadNightTiles();
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

	public static boolean checkForCollisionAt(int x, int y) {
		for (int i = 0; i < currentMap.getLayers().size(); i++) {
			TiledMapTileLayer layer = (TiledMapTileLayer) currentMap.getLayers().get(i);
			Cell cell = layer.getCell(x, y);
			if (cell != null && cell.getTile() != null) {
				int tileId = cell.getTile().getId() - 1;
				if (isNightTime)
					tileId -= nightTimeTileOffset;
				if (impassibleTileList.contains(tileId)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void loadDayTiles() {
		if (isNightTime) {
			for (MapLayer layer : currentMap.getLayers()) {
				TiledMapTileLayer tiledLayer = (TiledMapTileLayer) layer;
				for (int x = 0; x < tiledLayer.getWidth(); x++) {
					for (int y = 0; y < tiledLayer.getHeight(); y++) {
						Cell cell = tiledLayer.getCell(x, y);
						if (cell != null && cell.getTile() != null) {
							int tileId = cell.getTile().getId() - 1;
							cell.setTile(currentMap.getTileSets().getTileSet(dayTimeTileset)
									.getTile(tileId - nightTimeTileOffset));
						}
					}
				}
			}
			isNightTime = false;
		}
	}

	public static void loadNightTiles() {
		if (!isNightTime) {
			for (MapLayer layer : currentMap.getLayers()) {
				TiledMapTileLayer tiledLayer = (TiledMapTileLayer) layer;
				for (int x = 0; x < tiledLayer.getWidth(); x++) {
					for (int y = 0; y < tiledLayer.getHeight(); y++) {
						Cell cell = tiledLayer.getCell(x, y);
						if (cell != null && cell.getTile() != null) {
							int tileId = cell.getTile().getId() - 1;
							cell.setTile(currentMap.getTileSets().getTileSet(nightTimeTileset)
									.getTile(tileId + nightTimeTileOffset));
						}
					}
				}
			}
			isNightTime = true;
		}
	}

	public static MidiPlayer getMidiPlayer() {
		return bgm;
	}

}
