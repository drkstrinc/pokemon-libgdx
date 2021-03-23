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

public class World {

	private static TiledMap currentMap;

	private static MidiPlayer bgm;

	public static boolean retroMap = true;
	public static int[] groundLayer = { 0, 1 };
	public static int[] aboveLayers = { 2 };

	private static ArrayList<Actor> actors;
	private static ArrayList<Event> events;

	public World(Pokemon game, String mapName, boolean isRetro) {
		retroMap = isRetro;
		loadMap(mapName);
		loadActors(mapName);
		loadBGM(mapName);
	}

	public void loadMap(String mapName) {
		if (currentMap != null) {
			Gdx.app.log("TMX", "Unloading Current Map");
			currentMap.dispose();
		}
		Gdx.app.log("TMX", "Loading Map: " + mapName);
		currentMap = new TmxMapLoader().load("map/" + mapName + ".tmx");
	}

	public void loadActors(String mapName) {
		// TODO: Load actors using mapName parameter from somewhere
		actors = new ArrayList<Actor>();

		actors.add(Pokemon.getPlayer());

		if (retroMap) {
			actors.add(new Actor("Silver", "Silver.png", BrainType.STATIONARY, 160, 45, Direction.RIGHT));
			actors.add(new Actor("Lass", "Lass.png", BrainType.RANDOMMOVEMENT, 162, 40, Direction.LEFT));
			actors.add(new Actor("Man", "Man_1.png", BrainType.RANDOMMOVEMENT, 168, 38, Direction.UP));
		} else {
			actors.add(new Actor("Silver", "Silver.png", BrainType.STATIONARY, 15, 72, Direction.RIGHT));
			actors.add(new Actor("Lass", "Lass.png", BrainType.RANDOMMOVEMENT, 20, 65, Direction.LEFT));
			actors.add(new Actor("Man", "Man_1.png", BrainType.RANDOMMOVEMENT, 25, 63, Direction.UP));
		}
	}

	private void loadBGM(String mapName) {
		// TODO: Load Midi based off of mapName
		bgm = new MidiPlayer("audio/bgm/" + "newbarktown" + ".mid");
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
