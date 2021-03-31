package com.drkstrinc.pokemon.monster;

import com.drkstrinc.pokemon.datatype.DamageType;
import com.drkstrinc.pokemon.datatype.ElementalType;

public class Move {

	public static String EMPTY = "-----";

	private int id;

	private String internalName;
	private String displayName;

	private int functionCode; // Additional effect. Needs to be converted from hex
	private int basePower;

	private ElementalType type;
	private DamageType damageType;
	private int accuracy;

	private int maxPP;
	private int currentPP;

	// target???
	private int priority;

	private String description;

	public Move() {
		this(EMPTY);
	}

	public Move(String name) {
		this(name, 1);
	}

	public Move(String name, int pp) {
		displayName = name;
		currentPP = pp;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getCurrentPP() {
		return currentPP;
	}

	public boolean canUseMove() {
		if (currentPP >= 1) {
			return true;
		}
		return false;
	}

	public void useMove() {
		currentPP -= 1;
	}
}
