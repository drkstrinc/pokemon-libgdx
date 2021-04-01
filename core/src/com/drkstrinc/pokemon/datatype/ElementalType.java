package com.drkstrinc.pokemon.datatype;

import java.util.Arrays;
import java.util.List;

public class ElementalType {

	private int id;
	private String displayName;
	private Element type;
	private boolean isSpecialType;
	private Element[] weaknesses;
	private Element[] resistances;
	private Element[] immunities;
	private boolean isPseudoType;

	public ElementalType() {

	}

	public ElementalType(Element type) {
		this.type = type;
		loadTypeData();
	}

	private void loadTypeData() {

	}

	public List<Element> getWeaknesses() {
		return Arrays.asList(weaknesses);
	}

	public List<Element> getResissncees() {
		return Arrays.asList(resistances);
	}

	public List<Element> getImmunities() {
		return Arrays.asList(immunities);
	}

}
