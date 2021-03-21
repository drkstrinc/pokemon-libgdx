package com.drkstrinc.pokemon.world;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.actor.Actor;
import com.drkstrinc.pokemon.actor.Player;
import com.drkstrinc.pokemon.datatype.BrainType;
import com.drkstrinc.pokemon.datatype.Direction;
import com.drkstrinc.pokemon.event.Event;

public class World {

	private static Pokemon game;

	private static TiledMap currentMap;

	public static int[] groundLayer = { 0, 1 };
	public static int[] aboveLayers = { 2 };

	private static ArrayList<Actor> actors;
	private static ArrayList<Event> events;

	public World(String mapName, Pokemon game) {
		loadMap(mapName);
		loadActors(mapName);
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
		actors.add(new Actor("NPC1", "Silver.png", BrainType.STATIONARY, 15, 72, Direction.RIGHT));
		actors.add(new Actor("NPC2", "Lass.png", BrainType.RANDOMMOVEMENT, 20, 65, Direction.LEFT));
		actors.add(new Actor("NPC3", "Man_1.png", BrainType.RANDOMMOVEMENT, 25, 63, Direction.UP));

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

	public static Player getPlayer() {
		return game.getPlayer();
	}

}
