package com.drkstrinc.pokemon.monster;

public class Stats {

	private int hp;
	private int attack;
	private int defense;
	private int speed;
	private int specialAttack;
	private int specialDefense;

	public Stats(int hp, int attack, int defense, int speed, int specialAttack, int specialDefense) {
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		this.specialAttack = specialAttack;
		this.specialDefense = specialDefense;
	}

	public Stats(String[] statString) {
		this(Integer.valueOf(statString[0]), Integer.valueOf(statString[1]), Integer.valueOf(statString[2]),
				Integer.valueOf(statString[3]), Integer.valueOf(statString[4]), Integer.valueOf(statString[5]));
	}

	public int getHP() {
		return hp;
	}

	public void setHP(int hp) {
		this.hp = hp;
	}
}
