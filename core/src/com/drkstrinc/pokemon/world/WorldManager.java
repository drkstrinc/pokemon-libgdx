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
import com.drkstrinc.pokemon.event.Event;
import com.drkstrinc.pokemon.sound.MidiPlayer;

import com.google.gson.Gson;

public class WorldManager {

	private static World currentWorld;
	private static TiledMap currentMap;
	private static MidiPlayer bgm;

	public static boolean retroMap;
	public static boolean hasEncounters;
	public static boolean isNightTime;

	public static final int[] belowLayers = { 0, 1 };
	public static final int[] aboveLayers = { 2 };

	private static final int nightTimeTileOffset = 9999;
	private static List<Integer> impassibleTileList;

	private static ArrayList<Actor> actors;
	private static ArrayList<Event> events;

	public WorldManager(Pokemon game, String mapName) {
		initActors();
		loadWorld(mapName);
		loadMap();
		loadBGM();
	}

	private static void loadWorld(String mapName) {
		String json = Gdx.files.local("data/" + mapName + ".json").readString();
		Gdx.app.debug("TMX", "\"" + mapName + "\": " + json);

		Gson gson = new Gson();
		currentWorld = gson.fromJson(json, World.class);

		// Initialize Tileset Collision
		impassibleTileList = new ArrayList<>();
		Arrays.asList(Gdx.files.local(currentWorld.getMapTileMetadata()).readString().split(","))
				.forEach(tileId -> impassibleTileList.add(Integer.valueOf(tileId)));

		retroMap = currentWorld.getRetroFlag();
		hasEncounters = currentWorld.hasEncounters();

		// Actors and Actor Events
		for (JActor actor : currentWorld.getActors()) {
			Actor tempActor = new Actor(actor.getId(), actor.getName(), actor.getSpriteName(), actor.getBrainType(),
					actor.getCoordX(), actor.getCoordY(), actor.getDirection());
			for (JEvent event : actor.getEvents()) {
				Event tempEvent = new Event(event.getId());
				for (String message : event.getMessages()) {
					tempEvent.addText(message);
				}
				tempActor.addEvent(tempEvent);
			}
			actors.add(tempActor);

		}

		// Map Events
		for (JEvent event : currentWorld.getEvents()) {
			Event tempEvent = new Event(event.getId());
			events.add(tempEvent);
		}

	}

	public void loadMap() {
		Gdx.app.log("TMX", "Loading Map: " + currentWorld.getName() + " - " + currentWorld.getMapFileName());
		currentMap = new TmxMapLoader().load(currentWorld.getMapFileName());
		// loadNightTiles();
	}

	public void initActors() {
		actors = new ArrayList<Actor>();
		actors.add(Pokemon.getPlayer());
	}

	private void loadBGM() {
		bgm = new MidiPlayer(currentWorld.getBGMFileName());
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
							cell.setTile(currentMap.getTileSets().getTileSet(currentWorld.getDayTileset())
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
							cell.setTile(currentMap.getTileSets().getTileSet(currentWorld.getNightTileset())
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
