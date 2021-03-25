package com.drkstrinc.pokemon.monster;

import com.drkstrinc.pokemon.datatype.DamageType;

public class Move {

	private int id;
	private String internalName;
	private String displayName;

	private int functionCode; //Additional effect. Needs to be converted from hex
	private int basePower;

	private ElementalType type;
	private DamageType damageType;
	private int accuracy;

	private int maxPP;
	private int currentPP;
	
	//target???
	private int priority;
	
	private String description;
}
