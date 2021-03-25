package com.drkstrinc.pokemon.monster;

import com.drkstrinc.pokemon.bag.Item;
import com.drkstrinc.pokemon.datatype.ElementalType;
import com.drkstrinc.pokemon.datatype.Gender;
import com.drkstrinc.pokemon.datatype.GrowthRate;

public class Monster {

	// Required attributes
	private int id;
	private String displayName;
	private String internalName;

	private ElementalType type1;
	private ElementalType type2;

	private Stats baseStats;
	private Gender gender;
	private GrowthRate growthRate;

	private int baseExp;
	private Stats effortPoints;

	private int catchRate; // Rareness
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

	// Optional attributes
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
	private int battlerAltitude; // typically unused
	private int battlerShadowSize;

	private String evolutions;
	private String FormName;
	private Item incense;
}
