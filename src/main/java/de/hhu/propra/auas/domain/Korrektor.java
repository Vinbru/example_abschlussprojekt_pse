package de.hhu.propra.auas.domain;

import java.util.UUID;

public class Korrektor {

	private final String id = UUID.randomUUID().toString();
	private final String name;
	private int stunden;

	public Korrektor(String name, int stunden) {
		this.name = name;
		this.stunden = stunden;
	}

	public String getName() {
		return name;
	}

	public int getStunden() {
		return stunden;
	}

	public String getId() {
		return id;
	}

	public void setStunden(int stunden) {
		this.stunden = stunden;
	}

}
