package com.drkstrinc.pokemon.bag;

import com.drkstrinc.pokemon.datatype.BagPocket;
import com.drkstrinc.pokemon.datatype.BattleItemUsability;
import com.drkstrinc.pokemon.datatype.OverworldItemUsability;
import com.drkstrinc.pokemon.datatype.SpecialItemType;
import com.drkstrinc.pokemon.monster.Move;

public class Item {

	private int id;

	private String internalName;
	private String displayName;
	private String pluralDisplayName;

	private BagPocket pocket;

	private int price;

	private String description;

	private OverworldItemUsability overworldItemUsability;
	private BattleItemUsability battleItemUsability;
	private SpecialItemType specialItemType;

	private Move teachMove;

	public Item() {

	}

}
