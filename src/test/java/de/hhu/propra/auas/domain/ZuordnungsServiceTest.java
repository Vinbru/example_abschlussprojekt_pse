package de.hhu.propra.auas.domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ZuordnungsServiceTest {

	public List<Abgabe> erstelleAbgabenListe(int anzahl){
		List<Abgabe> output = new ArrayList<>();
		for(int i = 0; i < anzahl; i++){
			output.add(new Abgabe("Abgabe Nr " + i ));
		}
		return output;
	}

	public List<Korrektor> erstelleKorrektorenListe(int anzahlKorrektoren, int anzahlStunden){
		List<Korrektor> output = new ArrayList<>();
		for(int i = 0; i < anzahlKorrektoren; i++){
			output.add(new Korrektor("Korrektor Nr " + i, anzahlStunden));
		}
		return(output);
	}

	public Blatt erstelleBlattMitKorrektorAbgabe(Korrektor korrektor, int anzahlAbgaben){
		List<Abgabe> korrektorsAbgaben = erstelleAbgabenListe(anzahlAbgaben);
		Blatt output = new Blatt(1,korrektorsAbgaben);
		for(Abgabe abgabe : korrektorsAbgaben){
			output.abgabeZuordnen(abgabe.getId(),korrektor);
		}
		return(output);
	}



	ZuordnungsService ZOS = new ZuordnungsService();

	@Test
	public void korrektorsAbgabenAnzahlTest() {
		int[] test = {4,6,20,0,1};
		List<Korrektor> korrektorenListe1 = erstelleKorrektorenListe(5,0);
		for(int i = 0; i < test.length; i++){
			Blatt blatt = erstelleBlattMitKorrektorAbgabe(korrektorenListe1.get(i), test[i]);
			Assert.assertEquals(ZOS.getKorrektorsAbgabenAnzahl(korrektorenListe1.get(i), blatt),test[i]);
		}

		//Zusätzlicher RandomTest

		Random generator = new Random();
		for(int i = 0; i < 5; i++){
			int anzahl = generator.nextInt(20);
			List<Korrektor> korrektorenListe2 = erstelleKorrektorenListe(anzahl,0);
			for(int j = 0; j < anzahl; j++){
				int korrektorsKorrekturen = generator.nextInt(30);
				Blatt blatt2 = erstelleBlattMitKorrektorAbgabe(korrektorenListe2.get(j), korrektorsKorrekturen);
				Assert.assertEquals(ZOS.getKorrektorsAbgabenAnzahl(korrektorenListe2.get(j), blatt2),korrektorsKorrekturen);
			}
		}
	}

	@Test
	public void gesamteArbeitsStundenTest() {
		int korrektorenAnzahl1 = 5;
		int stundenAnzahl1 = 17;
		List<Korrektor> korrektorenListe1 = erstelleKorrektorenListe(korrektorenAnzahl1,stundenAnzahl1);
		Assert.assertEquals(ZOS.getGesamteArbeitsstunden(korrektorenListe1),korrektorenAnzahl1 * stundenAnzahl1);

		//Zusätzlicher RandomTest

		Random generator = new Random();
		for(int i = 0; i < 5; i++){
			int anzahl = generator.nextInt(20);
			int arbeitsstunden = 0;
			List<Korrektor> korrektorenListe2 = new ArrayList<>();
			for(int j = 0; j < anzahl; j++){
				int korrektorsStunden = generator.nextInt(17);
				korrektorenListe2.add(new Korrektor("korrektor1", korrektorsStunden));
				arbeitsstunden += korrektorsStunden;
			}
			Assert.assertEquals(ZOS.getGesamteArbeitsstunden(korrektorenListe2),arbeitsstunden);
		}
	}

	@Test
	public void getKorrektorsUeberhang_InBlatt_Test(){
		List<Abgabe> abgabenListe20 = erstelleAbgabenListe(20);
		Blatt blatt20 = new Blatt(20, abgabenListe20);
		List<Korrektor> korrektorList = erstelleKorrektorenListe(4,4);
		List<Blatt> vorherige = new ArrayList<>();
		ZOS.abgabenZuordnen(blatt20, vorherige,korrektorList);
		korrektorList.add(new Korrektor("Korrektor4", 4));
		Assert.assertEquals((int)ZOS.getKorrektorsUeberhangInBlatt(korrektorList.get(4), blatt20, korrektorList), -4);
		for(int i = 0; i < korrektorList.size() - 1; i++){
			Assert.assertEquals(ZOS.getKorrektorsUeberhangInBlatt(korrektorList.get(i), blatt20, korrektorList), 1, 0.0000001);
		}


		List<Abgabe> abgabenListe30 = erstelleAbgabenListe(20);
		Blatt blatt30 = new Blatt(30, abgabenListe30);
		vorherige.add(blatt20);
		ZOS.abgabenZuordnen(blatt30, vorherige ,korrektorList);
		Assert.assertEquals((int)ZOS.getKorrektorsUeberhangInBlatt(korrektorList.get(4), blatt30, korrektorList), 4);
		for(int i = 0; i < korrektorList.size() - 1; i++){
			Assert.assertEquals(ZOS.getKorrektorsUeberhangInBlatt(korrektorList.get(i), blatt30, korrektorList), -1, 0.0000001);
		}

		vorherige.add(blatt30);
		for(int i = 0; i < korrektorList.size(); i++){
			Assert.assertEquals(ZOS.getKorrektorsUeberhang(korrektorList.get(i), vorherige, korrektorList), 0, 0.0000001);
		}
	}

	@Test
	public void maximalEineAbgabeDifferenzTest(){
		Random generator = new Random();


		int anzahl_korrektoren = 3 + generator.nextInt(5);
		List<Korrektor> korrektoren = new ArrayList<>();
		for(int i = 0; i < anzahl_korrektoren; i++){
			int stunden = 5 + generator.nextInt(15);
			Korrektor korrektor = new Korrektor("Korrektor" + i, stunden);
			korrektoren.add(korrektor);
		}

		int anzahl_blaetter = 3 + generator.nextInt(7);
		List<Blatt> blattListe = new ArrayList<>();
		for(int i = 0; i < anzahl_blaetter; i++){
			int anzahl_abgaben = 100 + generator.nextInt(100);
			List<Abgabe> abgaben = new ArrayList<>();
			for(int j = 0; j < anzahl_abgaben; j++){
				Abgabe abgabe = new Abgabe("Abgabe" + i);
				abgaben.add(abgabe);
			}
			Blatt blatt = new Blatt(i, abgaben);
			blattListe.add(blatt);
		}

		List<Blatt> vorherige = new ArrayList<>();
		for(Blatt blatt : blattListe){
			ZOS.abgabenZuordnen(blatt, vorherige, korrektoren);
			vorherige.add(blatt);
			for(Korrektor korrektor : korrektoren){
				double anteilAnAbgabenPlusUeberhang = ZOS.getKorrektorsAnteilAnAbgaben(korrektor, blatt) + (ZOS.getKorrektorsUeberhang(korrektor, vorherige, korrektoren)/ blatt.anzahlAllerAbgaben());
				double akt_differenz = ZOS.getKorrektorsAnteilAnArbeitsstunden(korrektor, korrektoren) - anteilAnAbgabenPlusUeberhang;
				Assert.assertTrue(akt_differenz * blatt.anzahlAllerAbgaben() > - 2 && akt_differenz * blatt.anzahlAllerAbgaben() < 2);
			}
		}
	}
}
