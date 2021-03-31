package com.drkstrinc.pokemon.monster;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.bag.Item;
import com.drkstrinc.pokemon.datatype.Element;
import com.drkstrinc.pokemon.datatype.Gender;
import com.drkstrinc.pokemon.datatype.GrowthRate;

public class Monster implements Serializable {

	// Required Fields
	private int id;

	private String displayName;
	private String internalName;

	private Element type1;
	private Element type2;

	private Stats baseStats;
	private Gender gender;
	private GrowthRate growthRate;

	private int baseExp;
	private Stats effortPoints;

	private int catchRate;
	private int happiness;
	private Move[] moves;

	private String compatibility;
	private int stepsToHatch;

	private float height;
	private float weight;
	private String color;
	private int shape;
	private String kind;
	private String pokedexEntry;

	// Optional Attributes
	private Ability[] abilities;
	private Ability hiddenAbility;
	private Move[] eggMoves;

	private String habitat;
	private int[] regionalNumbers;

	private Item wildItemCommon;
	private Item wildItemUncommon;
	private Item wildItemRare;

	private int battlerPlayerX;
	private int battlerPlayerY;
	private int battlerEnemyX;
	private int battlerEnemyY;
	private int battlerAltitude;
	private int battlerShadowSize;

	private String evolutions;
	private String formName;

	private Item incense;

	// Unique Values
	private int level;
	private boolean shiny;
	private Move[] currentMoves;
	private Stats individualValues;

	private int otId;
	private String otName;

	private AtlasRegion frontSprite;
	private AtlasRegion backSprite;

	public Monster() {
		this(0, 1); // Missingno
	}

	public Monster(int id, int level) {
		this.id = id;
		this.level = level;

		currentMoves = new Move[4];
		currentMoves[0] = new Move("THUNDERSHOCK");
		currentMoves[1] = new Move("GROWL");
		currentMoves[2] = new Move();
		currentMoves[3] = new Move();

		loadBaseStats();
		calculateStats();
		initSprites();
	}

	public Monster(int id) {
		this(id, 1);
	}

	public void loadBaseStats() {
		Scanner input;
		InputStream in;
		in = Gdx.files.local("data/pbs/Pokemon.txt").read();
		input = new Scanner(in);
		boolean found = false;
		String regex = "\\[\\d+\\]$";
		Pattern regPat = Pattern.compile(regex);

		while (input.hasNext()) {
			String n = input.nextLine();
			if (!found && !n.equals("[" + id + "]")) {
				// no-op
			} else if (n.equals("[" + id + "]")) {
				found = true;
			} else {
				Matcher matcher = regPat.matcher(n);
				if (matcher.find()) {
					break;
				}
				Gdx.app.debug("MST", n);
				if (n.startsWith("Name=")) {
					displayName = n.substring(n.indexOf("=") + 1);
				} else if (n.startsWith("InternalName=")) {
					internalName = n.substring(n.indexOf("=") + 1);
				} else if (n.startsWith("Type1=")) {
					type1 = Element.valueOf(n.substring(n.indexOf("=") + 1));
				} else if (n.startsWith("Type2=")) {
					type2 = Element.valueOf(n.substring(n.indexOf("=") + 1));
				} else if (n.startsWith("BaseStats=")) {
					String[] baseStatString = n.substring(n.indexOf("=") + 1).split(",");
					baseStats = new Stats(baseStatString);
				}
				// TODO: Complete this
			}
		}
		input.close();
	}

	private void calculateStats() {
		// TODO: Use unique values to calculate final Stat values
	}

	private void initSprites() {
		String padId = String.format("%03d", id);
		TextureAtlas battlerAtlas = Pokemon.getAssetManager().get("image/battlers/battlers.atlas");
		if (shiny) {
			frontSprite = battlerAtlas.findRegion(padId + "s");
			backSprite = battlerAtlas.findRegion(padId + "sb");
		} else {
			frontSprite = battlerAtlas.findRegion(padId);
			backSprite = battlerAtlas.findRegion(padId + "b");
		}
	}

	public AtlasRegion getFrontSprite() {
		return frontSprite;
	}

	public AtlasRegion getBackSprite() {
		return backSprite;
	}

	public Move[] getMoves() {
		return currentMoves;
	}

}
