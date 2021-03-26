package com.drkstrinc.pokemon.world;

import java.time.LocalDateTime;
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
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.datatype.Time;
import com.drkstrinc.pokemon.event.Event;
import com.drkstrinc.pokemon.sound.MidiPlayer;

import com.google.gson.Gson;

public class WorldManager {

	private static World currentWorld;
	private static TiledMap currentMap;
	private static MidiPlayer bgm;

	public static boolean retroMap;
	public static boolean hasEncounters;

	public static Time timeOfDay;

	public static final int[] belowLayers = { 0, 1 };
	public static final int[] aboveLayers = { 2 };

	private static int nightTimeTileOffset;
	private static List<Integer> impassibleTileList;
	private static List<Integer> autoTileList;

	private static ArrayList<Actor> actors;
	private static ArrayList<Event> events;

	public static void setWorld(String worldName, int playerX, int playerY, Direction playerDirection) {
		initActors();
		loadWorld(worldName);
		initLists();
		loadMap();
		movePlayer(playerX, playerY, playerDirection);
		loadBGM();
	}

	private static void loadWorld(String worldName) {
		Gson gson = new Gson();
		String worldJSON = Gdx.files.local("data/world/" + worldName + ".json").readString();

		Gdx.app.debug("TMX", "\"" + worldName + "\": " + worldJSON);

		currentWorld = gson.fromJson(worldJSON, World.class);

		// World Flags
		nightTimeTileOffset = currentWorld.getNightTimeTileOffset();
		retroMap = currentWorld.getRetroFlag();
		hasEncounters = currentWorld.hasEncounters();

		if (LocalDateTime.now().getHour() >= 6 && LocalDateTime.now().getHour() <= 18) {
			timeOfDay = Time.DAY;
		} else {
			timeOfDay = Time.NIGHT;
		}

		// Actors and Actor Events
		for (JActor jActor : currentWorld.getActors()) {
			Actor actor = new Actor(jActor.getId(), jActor.getName(), jActor.getSpriteName(), jActor.getBrainType(),
					jActor.getCoordX(), jActor.getCoordY(), jActor.getDirection());
			for (JEvent jEvent : jActor.getEvents()) {
				Event event = new Event(jEvent.getId());
				for (String message : jEvent.getMessages()) {
					event.addText(message);
				}
				actor.addEvent(event);
			}
			actors.add(actor);
		}

		// Map Events
		for (JEvent event : currentWorld.getEvents()) {
			Event tempEvent = new Event(event.getId());
			events.add(tempEvent);
		}

	}

	private static void movePlayer(int x, int y, Direction direction) {
		Pokemon.getPlayer().moveTo(x, y, direction);
	}

	private static void loadMap() {
		Gdx.app.log("TMX", "Loading Map: " + currentWorld.getName() + " - " + currentWorld.getMapFileName());
		currentMap = new TmxMapLoader().load(currentWorld.getMapFileName());

		if (timeOfDay == Time.NIGHT) {
			loadNightTiles();
		}
	}

	private static void initLists() {
		impassibleTileList = new ArrayList<>();
		Arrays.asList(Gdx.files.local(currentWorld.getMapTileMetadata()).readString().split(","))
				.forEach(tileId -> impassibleTileList.add(Integer.valueOf(tileId)));

		autoTileList = new ArrayList<>();
		autoTileList.add(2848);
		autoTileList.add(2856);
	}

	private static void initActors() {
		actors = new ArrayList<Actor>();
		actors.add(Pokemon.getPlayer());
	}

	private static void loadBGM() {
		if (bgm != null) {
			bgm.stop();
			bgm.dispose();
		}
		bgm = new MidiPlayer(currentWorld.getBGMFileName());
		bgm.play();
	}

	public static MidiPlayer getMidiPlayer() {
		return bgm;
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
				if (timeOfDay == Time.NIGHT) {
					tileId -= nightTimeTileOffset;
					tileId += 1;
				}
				if (impassibleTileList.contains(tileId)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void loadDayTiles() {
		for (MapLayer layer : currentMap.getLayers()) {
			TiledMapTileLayer tiledLayer = (TiledMapTileLayer) layer;
			for (int x = 0; x < tiledLayer.getWidth(); x++) {
				for (int y = 0; y < tiledLayer.getHeight(); y++) {
					Cell cell = tiledLayer.getCell(x, y);
					if (cell != null && cell.getTile() != null) {
						int tileId = cell.getTile().getId() - 1;
						cell.setTile(currentMap.getTileSets().getTileSet(currentWorld.getDayTileset())
								.getTile(tileId - nightTimeTileOffset));
					}
				}
			}
		}
	}

	public static void loadNightTiles() {
		for (MapLayer layer : currentMap.getLayers()) {
			TiledMapTileLayer tiledLayer = (TiledMapTileLayer) layer;
			for (int x = 0; x < tiledLayer.getWidth(); x++) {
				for (int y = 0; y < tiledLayer.getHeight(); y++) {
					Cell cell = tiledLayer.getCell(x, y);
					if (cell != null && cell.getTile() != null) {
						int tileId = cell.getTile().getId() - 1;
						if (!autoTileList.contains(tileId)) {
							cell.setTile(currentMap.getTileSets().getTileSet(currentWorld.getNightTileset())
									.getTile(tileId + nightTimeTileOffset));
						}
					}
				}
			}
		}
	}

}
