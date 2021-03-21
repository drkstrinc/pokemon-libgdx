package com.drkstrinc.pokemon.world;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import com.drkstrinc.pokemon.actor.Actor;
import com.drkstrinc.pokemon.datatype.Direction;

public class World {

	private static TiledMap currentMap;
	private static ArrayList<Actor> actors;

	public World(String mapName) {
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
		actors.add(new Actor("NPC", "kris.png", 167, 41, Direction.DOWN));
	}

	public static ArrayList<Actor> getActors() {
		return actors;
	}

	public static void setCurrentMap(TiledMap map) {
		currentMap = map;
	}

	public static TiledMap getCurrentMap() {
		return currentMap;
	}

}
