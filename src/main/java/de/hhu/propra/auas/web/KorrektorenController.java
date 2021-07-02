package de.hhu.propra.auas.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import de.hhu.propra.auas.domain.Abgabe;
import de.hhu.propra.auas.domain.Blatt;
import de.hhu.propra.auas.domain.Korrektor;
import de.hhu.propra.auas.domain.ZuordnungsService;

@Controller
public class KorrektorenController {

	private DatabaseSimulator repo;
	private ZuordnungsService zuordnung;

	public KorrektorenController(DatabaseSimulator repo, ZuordnungsService zuordnung) {
		this.repo = repo;
		this.zuordnung = zuordnung;
	}

	@PostMapping("/zuordnen")
	public String zuordnen(int id) {
		List<Korrektor> korrektoren = repo.getKorrektoren();
		List<Blatt> vorhergehendeBlaetter = repo.getBlaetterUpTo(id);
		Blatt aktuellesBlatt = repo.getBlatt(id);
		zuordnung.abgabenZuordnen(aktuellesBlatt, vorhergehendeBlaetter, korrektoren);
		return "redirect:/";
	}

	@PostMapping("/hinzufuegen")
	public String hinzufuegen(Model model, int id) {
		Blatt blatt = repo.getBlaetter().get(id);
		blatt.addAbgabe(repo.generateRandomAbgabe());
		return "redirect:/";
	}

	@GetMapping("/")
	public String main(Model model) {
		model.addAttribute("blaetter", repo.getBlaetter());
		model.addAttribute("korrektoren", repo.getKorrektoren());
		return "mainpage";
	}

	@GetMapping("/zuordnung")
	public String zuordnung(Model model, int id) {
		Blatt blatt = repo.getBlatt(id);
		Collection<Korrektor> korrektoren = repo.getKorrektoren();
		Map<Korrektor, List<Abgabe>> zugeordneteAbgaben = blatt.getZugeordneteAbgaben();
		model.addAttribute("zuordnung", zugeordneteAbgaben);
		model.addAttribute("korrektoren", korrektoren);
		model.addAttribute("unzugeordnet", blatt.anzahlAllerAbgaben()-blatt.anzahlZugeordneteAbgaben());
		return "zuordnung";
	}

}
