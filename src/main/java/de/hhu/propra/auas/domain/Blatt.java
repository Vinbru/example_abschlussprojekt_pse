package de.hhu.propra.auas.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blatt {

	private final String name;
	private final Map<String, Abgabe> unzugeordneteAbgaben = new HashMap<>();;
	private final Map<Korrektor, List<Abgabe>> zugeordneteAbgaben = new HashMap<>();
	private final int id;
	private int anzahlZugeordneteAbgaben;
	private int anzahlGesamtAbgaben;

	public Blatt(int id, List<Abgabe> abgaben) {
		anzahlZugeordneteAbgaben = 0;
		anzahlGesamtAbgaben = 0;
		this.id = id;
		this.name = "Blatt " + id;
		for (Abgabe abgabe : abgaben) {
			addAbgabe(abgabe);
		}
	}

	public void addAbgabe(Abgabe abgabe) {
		unzugeordneteAbgaben.put(abgabe.getId(), abgabe);
		anzahlGesamtAbgaben++;
	}

	public String getName() {
		return name;
	}

	public int anzahlAllerAbgaben() {
		return anzahlGesamtAbgaben;
	}

	public int anzahlZugeordneteAbgaben() {
		return anzahlZugeordneteAbgaben;
	}

	public void abgabeZuordnen(String abgabeId, Korrektor korrektor) {
		Abgabe abgabe = unzugeordneteAbgaben.get(abgabeId);
		unzugeordneteAbgaben.remove(abgabeId);

		List<Abgabe> abgaben = zugeordneteAbgaben.get(korrektor);
		if (abgaben == null)
			abgaben = new ArrayList<>();

		abgaben.add(abgabe);

		zugeordneteAbgaben.put(korrektor, abgaben);
		anzahlZugeordneteAbgaben++;
	}

	public List<Abgabe> getUnzugeordneteAbgaben() {
		return Collections.unmodifiableList(new ArrayList<>(unzugeordneteAbgaben.values()));
	}

	public Map<Korrektor, List<Abgabe>> getZugeordneteAbgaben() {
		return Collections.unmodifiableMap(zugeordneteAbgaben);
	}

	public int getId() {
		return id;
	}

}
