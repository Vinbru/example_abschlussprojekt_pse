package de.hhu.propra.auas.domain;

import java.util.UUID;

public class Abgabe {
	private String name;
	private final String id = UUID.randomUUID().toString();

	public Abgabe(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

}
