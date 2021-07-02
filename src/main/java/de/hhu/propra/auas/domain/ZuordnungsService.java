package de.hhu.propra.auas.domain;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ZuordnungsService {

	public void abgabenZuordnen(Blatt blatt, List<Blatt> vorhergehendeBlaetter, List<Korrektor> korrektoren) {

		List<Abgabe> abgaben = blatt.getUnzugeordneteAbgaben();

		for(Abgabe abgabe : abgaben) {
			Korrektor zugewiesener_korrektor = suchePassendenKorrektor(korrektoren, blatt, vorhergehendeBlaetter);
			blatt.abgabeZuordnen(abgabe.getId(), zugewiesener_korrektor);
		}
	}

	private Korrektor suchePassendenKorrektor(List<Korrektor> korrektoren, Blatt blatt, List<Blatt> vorhergehendeBlaetter){
		Korrektor zugewiesener_korrektor = korrektoren.get(0);
		double groesste_differenz = 0.0;
		for (Korrektor korrektor : korrektoren) {
			double anteilAnAbgabenPlusUeberhang = getKorrektorsAnteilAnAbgaben(korrektor, blatt) + (getKorrektorsUeberhang(korrektor, vorhergehendeBlaetter, korrektoren)/ blatt.anzahlAllerAbgaben());
			double akt_differenz = getKorrektorsAnteilAnArbeitsstunden(korrektor, korrektoren) - anteilAnAbgabenPlusUeberhang;
			if (akt_differenz > groesste_differenz) {
				groesste_differenz = akt_differenz;
				zugewiesener_korrektor = korrektor;
			}
		}
		return(zugewiesener_korrektor);
	}

	int getGesamteArbeitsstunden(List<Korrektor> korrektoren){
		int gesamteArbeitsstunden = 0;
		for(Korrektor korrektor : korrektoren){
			gesamteArbeitsstunden += korrektor.getStunden();
		}
		return gesamteArbeitsstunden;
	}

	double getKorrektorsAnteilAnArbeitsstunden(Korrektor korrektor, List<Korrektor> korrektoren){
		return((double) korrektor.getStunden() / getGesamteArbeitsstunden(korrektoren));
	}

	int getKorrektorsAbgabenAnzahl(Korrektor korrektor, Blatt blatt){
		Map<Korrektor, List<Abgabe>> zugeordneteAbgaben = blatt.getZugeordneteAbgaben();
		List<Abgabe> abgaben = zugeordneteAbgaben.get(korrektor);
		if(abgaben == null){return(0);}
		return abgaben.size();
	}

	double getKorrektorsAnteilAnAbgaben(Korrektor korrektor, Blatt blatt){
		return((double) getKorrektorsAbgabenAnzahl(korrektor, blatt) / blatt.anzahlAllerAbgaben());
	}

	double getKorrektorsUeberhangInBlatt(Korrektor korrektor, Blatt blatt, List<Korrektor> korrektoren){
		double abgabenAnteil = getKorrektorsAnteilAnAbgaben(korrektor, blatt);
		double arbeitsstundenAnteil = getKorrektorsAnteilAnArbeitsstunden(korrektor, korrektoren);
		return((abgabenAnteil - arbeitsstundenAnteil) * blatt.anzahlZugeordneteAbgaben());
	}

	double getKorrektorsUeberhang(Korrektor korrektor, List<Blatt> vorhergehendeBlaetter, List<Korrektor> korrektoren){
		double ueberhang = 0.0;
		for(Blatt blatt : vorhergehendeBlaetter){
			ueberhang += getKorrektorsUeberhangInBlatt(korrektor, blatt, korrektoren);
		}
		return ueberhang;
	}

}
