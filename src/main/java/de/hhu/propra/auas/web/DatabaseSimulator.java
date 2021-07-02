package de.hhu.propra.auas.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.hhu.propra.auas.domain.Abgabe;
import de.hhu.propra.auas.domain.Blatt;
import de.hhu.propra.auas.domain.Korrektor;

@Component
public class DatabaseSimulator {

	DatabaseSimulator() {
		korrektoren = generateKorrektoren();
		blaetter = generateBlaetter();
	}

	List<Blatt> getBlaetter() {
		return getBlaetterUpTo(blaetter.size());
	}

	List<Blatt> getBlaetterUpTo(int upto) {
		return blaetter
				.keySet()
				.stream()
				.filter(id -> id < upto)
				.sorted()
				.map(key -> blaetter.get(key))
				.collect(Collectors.toList());
	}

	Blatt getBlatt(int id) {
		return blaetter.get(id);
	}

	List<Korrektor> getKorrektoren() {
		return Collections.unmodifiableList(new ArrayList<>(korrektoren.values()));
	}

	Abgabe generateRandomAbgabe() {
		return new Abgabe(generateName());
	}

	private List<Abgabe> generateAbgaben() {
		ArrayList<Abgabe> abgaben = new ArrayList<>();
		int anzahl = random.nextInt(100) + 100;
		for (int i = 0; i < anzahl; i++) {
			abgaben.add(generateRandomAbgabe());
		}
		return abgaben;
	}

	private Map<Integer, Blatt> generateBlaetter() {
		int number = random.nextInt(5) + 2;
		Map<Integer, Blatt> blaetter = new HashMap<>();
		for (int i = 0; i < number; i++) {
			List<Abgabe> abgaben = generateAbgaben();
			Blatt blatt = new Blatt(i, abgaben);
			blaetter.put(i, blatt);
		}
		return blaetter;
	}

	private Korrektor generateKorrektor() {
		String name = generateName();
		int stunden = generateStunden();
		return new Korrektor(name, stunden);
	}

	private Map<String, Korrektor> generateKorrektoren() {
		Map<String, Korrektor> korrektoren = new HashMap<>();
		int anzahl = random.nextInt(10) + 10;
		for (int i = 0; i < anzahl; i++) {
			Korrektor korrektor = generateKorrektor();
			korrektoren.put(korrektor.getId(), korrektor);
		}
		return korrektoren;
	}

	private String generateName() {
		String vorname = selectRandom(VORNAMEN);
		String nachname = selectRandom(NACHNAMEN);
		return vorname + " " + nachname;
	}

	private int generateStunden() {
		int select = random.nextInt(100);
		if (select < 10)
			return 17;
		if (select < 20)
			return 7;
		return 10;
	}

	private String selectRandom(String[] array) {
		int index = random.nextInt(array.length);
		return array[index];
	}

	private final Random random = new Random();

	private final String[] VORNAMEN = { "Sarah", "Anna", "Lea", "Laura", "Julia", "Lisa", "Lena", "Marie", "Hannah",
			"Lara", "Vanessa", "Annika", "Johanna", "Antonia", "Katharina", "Jan", "Lukas", "Niklas", "Tim", "Finn",
			"Jonas", "Leon", "Felix", "Florian", "Philipp", "Tom", "Jannik", "Luca", "Jakob", "Lennard" };

	private final String[] NACHNAMEN = { "Müller", "Schmidt", "Schneider", "Fischer", "Meyer", "Weber", "Wagner",
			"Becker", "Schulz", "Hoffmann", "Schäfer", "Koch", "Bauer", "Richter", "Klein", "Schröder", "Wolf",
			"Neumann", "Schwarz", "Zimmermann" };

	private final Map<String, Korrektor> korrektoren;
	private final Map<Integer, Blatt> blaetter;

}
